import io.javalin.Javalin;
import io.javalin.validation.ValidationException;
import org.apache.commons.codec.binary.Base64InputStream;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Run the Husky Maps server.
 *
 * @see MapGraph
 */
public class MapServer {
    /**
     * Default port for serving the application locally.
     */
    private static final int PORT = 8080;
    /**
     * The OpenStreetMap XML file path.
     *
     * @see <a href="https://download.bbbike.org/osm/">BBBike</a>
     */
    private static final String OSM_DB_PATH = "seattle.osm.gz";
    /**
     * The place-importance TSV data file path from OpenStreetMap.
     */
    private static final String PLACES_PATH = "places.tsv";
    /**
     * Maximum number of autocomplete search results.
     */
    private static final int MAX_MATCHES = 10;

    public static void main(String[] args) throws Exception {
        SpatialContext context = SpatialContext.GEO;
        ShapeFactory factory = context.getShapeFactory();
        MapGraph map = new MapGraph(OSM_DB_PATH, PLACES_PATH, context);
        Javalin app = Javalin.create(config -> {
            config.spaRoot.addFile("/", "index.html");
        }).start(port());
        app.get("/map/{lon},{lat},{zoom}/{width}x{height}", ctx -> {
            double lon = ctx.pathParamAsClass("lon", Double.class).get();
            double lat = ctx.pathParamAsClass("lat", Double.class).get();
            int zoom = ctx.pathParamAsClass("zoom", Integer.class).get();
            int width = ctx.pathParamAsClass("width", Integer.class).get();
            int height = ctx.pathParamAsClass("height", Integer.class).get();
            String term = ctx.queryParam("term");

            Point center = factory.pointLatLon(lat, lon);
            List<Point> route;
            try {
                double startLon = ctx.queryParamAsClass("startLon", Double.class).get();
                double startLat = ctx.queryParamAsClass("startLat", Double.class).get();
                double goalLon = ctx.queryParamAsClass("goalLon", Double.class).get();
                double goalLat = ctx.queryParamAsClass("goalLat", Double.class).get();
                Point start = factory.pointLatLon(startLat, startLon);
                Point goal = factory.pointLatLon(goalLat, goalLon);
                route = map.shortestPath(start, goal);
            } catch (ValidationException e) {
                route = List.of();
            }
            List<Point> locations = map.getLocations(term);
            URL staticImageURL = url(center, zoom, width, height, route, locations);
            ctx.result(new Base64InputStream(staticImageURL.openStream(), true));
        });
        app.get("/search", ctx -> {
            ctx.json(map.getLocationsByPrefix(ctx.queryParam("term"), MAX_MATCHES));
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
     * Return the API URL for retrieving the map image.
     *
     * @param center    the center of the map image.
     * @param width     the width of the window.
     * @param height    the height of the window.
     * @param route     the list of route points (or null).
     * @param locations the list of locations (or null).
     * @return the URL for retrieving the map image.
     * @throws MalformedURLException if the URL is invalid.
     */
    private static URL url(Point center, int zoom, int width, int height, List<Point> route, List<Point> locations)
            throws MalformedURLException {
        StringBuilder overlay = new StringBuilder();
        if (route != null && !route.isEmpty()) {
            overlay.append("path-4+6cb5e6-1(");
            overlay.append(URLEncoder.encode(encode(route), StandardCharsets.UTF_8));
            overlay.append("),");
        }
        if (locations != null && !locations.isEmpty()) {
            for (Point location : locations) {
                overlay.append("pin-s(");
                overlay.append(location.getLon());
                overlay.append(',');
                overlay.append(location.getLat());
                overlay.append("),");
            }
        }
        if (!overlay.isEmpty()) {
            // Replace the trailing comma with a forward slash
            overlay.setCharAt(overlay.length() - 1, '/');
        }
        return new URL(String.format(
                "https://api.mapbox.com/"
                        // {username}/{style_id} and {overlay} (must include trailing slash)
                        + "styles/v1/%s/%s/static/%s"
                        // {lon},{lat},{zoom}/{width}x{height}{@2x}
                        + "%f,%f,%d/%dx%d%s"
                        // Access token and optional parameters
                        + "?access_token=%s&logo=false&attribution=false",
                "mapbox",
                "cj7t3i5yj0unt2rmt3y4b5e32",
                overlay,
                center.getLon(), center.getLat(), zoom,
                (int) Math.ceil(width / 2.), (int) Math.ceil(height / 2.), "@2x",
                System.getenv("TOKEN")
        ));
    }

    /**
     * Returns an encoded route string.
     *
     * @param route list of points representing the route to encode.
     * @return an encoded route string.
     * @see <a href="https://github.com/mapbox/mapbox-java">MapBox PolylineUtils</a>
     */
    private static String encode(List<Point> route) {
        StringBuilder result = new StringBuilder();
        long lastLat = 0;
        long lastLon = 0;
        for (Point point : route) {
            long lat = Math.round(point.getLat() * 1e5);
            long diffLat = lat - lastLat;
            diffLat = diffLat < 0 ? ~(diffLat << 1) : diffLat << 1;
            while (diffLat >= 0x20) {
                result.append(Character.toChars((int) ((0x20 | (diffLat & 0x1f)) + 63)));
                diffLat >>= 5;
            }
            result.append(Character.toChars((int) (diffLat + 63)));
            lastLat = lat;

            long lon = Math.round(point.getLon() * 1e5);
            long diffLon = lon - lastLon;
            diffLon = diffLon < 0 ? ~(diffLon << 1) : diffLon << 1;
            while (diffLon >= 0x20) {
                result.append(Character.toChars((int) ((0x20 | (diffLon & 0x1f)) + 63)));
                diffLon >>= 5;
            }
            result.append(Character.toChars((int) (diffLon + 63)));
            lastLon = lon;
        }
        return result.toString();
    }
}
