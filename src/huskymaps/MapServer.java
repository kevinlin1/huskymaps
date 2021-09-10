package huskymaps;

import graphs.AStarSolver;
import io.javalin.Javalin;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Run the {@code huskymaps} server.
 *
 * @see MapGraph
 */
public class MapServer {
    /**
     * Default port for serving the application locally.
     */
    private static final int PORT = 8080;
    /**
     * The OpenStreetMap XML file path. Downloaded from <a href="http://download.bbbike.org/osm/">BBBike</a>
     * using custom region selection.
     */
    private static final String OSM_DB_PATH = "data/huskymaps/seattle-small.osm.gz";
    /**
     * The place-importance TSV data file path from OpenStreetMap.
     */
    private static final String PLACES_PATH = "data/huskymaps/places.tsv";
    /**
     * Maximum number of autocomplete search results.
     */
    private static final int MAX_MATCHES = 10;
    /**
     * The longitudinal distance per pixel when the map is centered on Seattle.
     */
    private static final double SEATTLE_ROOT_LONDPP = 0.3515625;
    /**
     * The latitudinal distance per pixel when the map is centered on Seattle.
     */
    private static final double SEATTLE_ROOT_LATDPP = 0.23689728184;

    public static void main(String[] args) throws Exception {
        SpatialContext context = SpatialContext.GEO;
        MapGraph map = new MapGraph(OSM_DB_PATH, PLACES_PATH, context);
        Javalin app = Javalin.create(config -> {
            config.addSinglePageRoot("/", "huskymaps/index.html");
        }).start(port());
        ConcurrentHashMap<String, BufferedImage> cache = new ConcurrentHashMap<>();
        app.get("/map/{coordinates}/{dimensions}", ctx -> {
            String[] coordinates = ctx.pathParam("coordinates").split(",");
            Point center = pointLonLat(context, coordinates);
            int zoom = Integer.parseInt(coordinates[2]);
            String[] dimensions = ctx.pathParam("dimensions").split("x");
            int width = Integer.parseInt(dimensions[0]);
            int height = Integer.parseInt(dimensions[1]);
            BufferedImage image = cache.get(ctx.path());
            List<Point> locations = map.getLocations(ctx.queryParam("term"), center);
            if (image == null || !locations.isEmpty()) {
                // Only make an API call if the cached image is not available/matches or locations are requested.
                image = ImageIO.read(url(center, zoom, width, height, locations));
                if (locations.isEmpty()) {
                    cache.putIfAbsent(ctx.path(), image);
                }
            }
            String start = ctx.queryParam("start");
            String goal = ctx.queryParam("goal");
            if (start != null && goal != null) {
                // Overlay route if the route start and goal are defined.
                double lonDPP = SEATTLE_ROOT_LONDPP / Math.pow(2, zoom);
                double latDPP = SEATTLE_ROOT_LATDPP / Math.pow(2, zoom);
                List<Point> route = new AStarSolver<>(
                        map,
                        map.closest(pointLonLat(context, start.split(","))),
                        map.closest(pointLonLat(context, goal.split(",")))
                ).solution();
                int[] xPoints = new int[route.size()];
                int[] yPoints = new int[route.size()];
                int i = 0;
                for (Point location : route) {
                    xPoints[i] = (int) ((location.getLon() - center.getLon()) * (1 / lonDPP)) + (width / 2);
                    yPoints[i] = (int) ((center.getLat() - location.getLat()) * (1 / latDPP)) + (height / 2);
                    i += 1;
                }
                image = withPolyline(image, xPoints, yPoints);
            }
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "png", os);
            ctx.result(Base64.getEncoder().encode(os.toByteArray()));
        });
        app.get("/search", ctx -> {
            List<CharSequence> result = map.getLocationsByPrefix(ctx.queryParam("term"));
            if (result.size() > MAX_MATCHES) {
                ctx.json(result.subList(0, MAX_MATCHES));
            } else {
                ctx.json(result);
            }
        });
    }

    /**
     * Returns the port for communicating with the server.
     *
     * @return the port for communicating with the server.
     */
    private static int port() {
        String port = System.getenv("PORT");
        if (port != null) {
            return Integer.parseInt(port);
        }
        return PORT;
    }

    /**
     * Returns a new {@link Point} from parsing the given longitude and latitude strings.
     *
     * @param context the spatial context.
     * @param lonLat  the strings representing the longitude and latitude.
     * @return a new {@link Point} from parsing the given longitude and latitude strings.
     */
    private static Point pointLonLat(SpatialContext context, String... lonLat) {
        if (lonLat == null || lonLat.length < 2) {
            return null;
        }
        double lon = Double.parseDouble(lonLat[0]);
        double lat = Double.parseDouble(lonLat[1]);
        return context.getShapeFactory().pointLatLon(lat, lon);
    }

    /**
     * Return the API URL for retrieving the map image.
     *
     * @param center    the center of the map image.
     * @param width     the width of the window.
     * @param height    the height of the window.
     * @param locations the list of locations (or null).
     * @return the URL for retrieving the map image.
     * @throws MalformedURLException if the URL is invalid.
     */
    private static URL url(Point center, int zoom, int width, int height, List<Point> locations)
            throws MalformedURLException {
        String markers = "";
        if (locations != null && !locations.isEmpty()) {
            markers = locations.stream().map(location -> String.format(
                    "pin-s(%f,%f)", location.getLon(), location.getLat()
            )).collect(Collectors.joining(","));
            markers += "/";
        }
        return new URL(String.format(
                "https://api.mapbox.com/"
                        // {username}/{style_id} and {overlay} (must include trailing slash)
                        + "styles/v1/%s/%s/static/%s"
                        // {lon},{lat},{zoom}/{width}x{height}{@2x}
                        + "%f,%f,%d/%dx%d%s"
                        // Access token and optional parameters
                        + "?access_token=%s&logo=false&attribution=false",
                System.getenv().getOrDefault("USERNAME", "mapbox"),
                System.getenv().getOrDefault("STYLE_ID", "streets-v11"),
                markers,
                center.getLon(), center.getLat(), zoom,
                (int) Math.ceil(width / 2.), (int) Math.ceil(height / 2.), "@2x",
                System.getenv("TOKEN")
        ));
    }

    /**
     * Returns a new image identical to the given image except for an additional polyline defined by the points.
     *
     * @param image   the input image.
     * @param xPoints the x-coordinates for the points on the polyline.
     * @param yPoints the y-coordinates for the points on the polyline.
     * @return a new image identical to the given image except for an additional polyline defined by the points.
     */
    private static BufferedImage withPolyline(BufferedImage image, int[] xPoints, int[] yPoints) {
        BufferedImage clone = new BufferedImage(
                image.getColorModel(),
                image.copyData(null),
                image.isAlphaPremultiplied(),
                null
        );
        Graphics2D g2d = clone.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(new Color(255, 255, 255));
        g2d.setStroke(new BasicStroke(10.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawPolyline(xPoints, yPoints, xPoints.length);
        g2d.setColor(new Color(108, 181, 230));
        g2d.setStroke(new BasicStroke(5.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.drawPolyline(xPoints, yPoints, xPoints.length);
        g2d.dispose();
        return clone;
    }
}
