package huskymaps;

import graphs.AStarSolver;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.apache.commons.math3.util.Precision;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static huskymaps.Constants.*;

/**
 * Run the {@code huskymaps} server.
 *
 * @see MapGraph
 */
public class MapServer {
    public static void main(String[] args) throws Exception {
        MapGraph map = new MapGraph(OSM_DB_PATH, PLACES_PATH);
        Javalin app = Javalin.create(config -> {
            config.addSinglePageRoot("/", "huskymaps/index.html");
        }).start(port());
        app.get("/raster", ctx -> {
            BoundingBox bbox = BoundingBox.from(ctx);
            int width = ctx.queryParam("width", Integer.class).get();
            int height = ctx.queryParam("height", Integer.class).get();
            String term = ctx.sessionAttribute("term");
            List<Location> locations = term == null ? List.of() : map.getLocations(term, bbox.center());
            Location src = ctx.sessionAttribute("src");
            Location dest = ctx.sessionAttribute("dest");
            BufferedImage image = ctx.sessionAttribute("image");
            // Only make an API call if the image is not clean, the bboxes don't match, or locations are requested.
            if (image == null || !bbox.equals(ctx.sessionAttribute("bbox")) || !locations.isEmpty()) {
                image = ImageIO.read(url(bbox, width, height, locations));
            }
            // Overlay the route if there were no locations and the route source and destination are defined.
            if (locations.isEmpty() && src != null && dest != null) {
                overlay(image, new AStarSolver<>(map, map.closest(src), map.closest(dest)).solution(), bbox);
            }
            // Cache the bbox and image if this request was served without modification.
            if (locations.isEmpty() && src == null && dest == null) {
                ctx.sessionAttribute("bbox", bbox);
                ctx.sessionAttribute("image", image);
            } else {
                ctx.sessionAttribute("bbox", null);
                ctx.sessionAttribute("image", null);
            }
            ctx.json(bbox.with(image));
        });
        app.get("/route", ctx -> {
            Location src = Location.parse(ctx.queryParam("startLat"), ctx.queryParam("startLon"));
            Location dest = Location.parse(ctx.queryParam("endLat"), ctx.queryParam("endLon"));
            ctx.sessionAttribute("src", src);
            ctx.sessionAttribute("dest", dest);
            ctx.sessionAttribute("term", null);
            ctx.json(true);
        });
        app.get("/clear", ctx -> {
            ctx.sessionAttribute("src", null);
            ctx.sessionAttribute("dest", null);
            ctx.sessionAttribute("term", null);
            ctx.json(true);
        });
        app.get("/search", ctx -> {
            List<CharSequence> result = map.getLocationsByPrefix(ctx.queryParam("term"));
            if (result.size() > MAX_MATCHES) {
                ctx.json(result.subList(0, MAX_MATCHES));
            } else {
                ctx.json(result);
            }
        });
        app.get("/locations", ctx -> ctx.sessionAttribute("term", ctx.queryParam("term")));
    }

    /**
     * Overlay the route on the image using the bounding box parameters.
     *
     * @param image the input image.
     * @param route the input route.
     * @param bbox  the bounding box.
     */
    private static void overlay(BufferedImage image, List<Location> route, BoundingBox bbox) {
        double widthDPP = (bbox.lrlon - bbox.ullon) / image.getWidth();
        double heightDPP = (bbox.ullat - bbox.lrlat) / image.getHeight();
        Graphics2D g2d = (Graphics2D) image.getGraphics();
        g2d.setColor(ROUTE_STROKE_COLOR);
        g2d.setStroke(new BasicStroke(ROUTE_STROKE_WIDTH_PX, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        route.stream().reduce((v, w) -> {
            g2d.drawLine(
                    (int) ((v.lon - bbox.ullon) * (1 / widthDPP)),
                    (int) ((bbox.ullat - v.lat) * (1 / heightDPP)),
                    (int) ((w.lon - bbox.ullon) * (1 / widthDPP)),
                    (int) ((bbox.ullat - w.lat) * (1 / heightDPP))
            );
            return w;
        });
    }

    /**
     * Returns the port for communicating with the server.
     *
     * @return the port for communicating with the server.
     */
    private static int port() {
        if (System.getenv("HEROKU") != null) {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (processBuilder.environment().get("PORT") != null) {
                return Integer.parseInt(processBuilder.environment().get("PORT"));
            }
        }
        return PORT;
    }

    /**
     * Return the API URL for retrieving the map image.
     *
     * @param bbox      the bounding box.
     * @param width     the width of the window.
     * @param height    the height of the window.
     * @param locations the list of locations (or null).
     * @return the URL for retrieving the map image.
     * @throws MalformedURLException if the URL is invalid.
     */
    private static URL url(BoundingBox bbox, int width, int height, List<Location> locations)
            throws MalformedURLException {
        String scale;
        if (width > 1280 || height > 1280) {
            width = (int) Math.ceil(width / 2.);
            height = (int) Math.ceil(height / 2.);
            scale = "@2x";
        } else {
            scale = "";
        }
        String markers = "";
        if (locations != null && !locations.isEmpty()) {
            markers = locations.stream().map(location -> String.format(
                    MARKER_OVERLAY, location.lon, location.lat
            )).collect(Collectors.joining(","));
            markers += "/";
        }
        return new URL(String.format(
                STATIC_IMAGES_API,
                System.getenv().getOrDefault("USERNAME", "mapbox"),
                System.getenv().getOrDefault("STYLE_ID", "streets-v11"),
                markers,
                bbox.ullon, bbox.lrlat, // minLon, minLat
                bbox.lrlon, bbox.ullat, // maxLon, maxLat
                width, height, scale,
                System.getenv("TOKEN")
        ));
    }

    /**
     * A bounding box represented by the upper-left and lower-right corners' latitudes and longitudes.
     */
    private static class BoundingBox {
        private final double ullat;
        private final double ullon;
        private final double lrlat;
        private final double lrlon;

        /**
         * Constructs a new bounding box from the given parameters.
         *
         * @param ullat upper-left latitude.
         * @param ullon upper-left longitude.
         * @param lrlat lower-right latitude.
         * @param lrlon lower-right longitude.
         */
        private BoundingBox(double ullat, double ullon, double lrlat, double lrlon) {
            this.ullat = ullat;
            this.ullon = ullon;
            this.lrlat = lrlat;
            this.lrlon = lrlon;
        }

        /**
         * Returns a new bounding box from the given {@link Context}.
         *
         * @param ctx the {@link Context}.
         * @return a new bounding box with the corresponding context parameters.
         */
        private static BoundingBox from(Context ctx) {
            return new BoundingBox(
                    ctx.queryParam("ullat", Double.class).get(),
                    ctx.queryParam("ullon", Double.class).get(),
                    ctx.queryParam("lrlat", Double.class).get(),
                    ctx.queryParam("lrlon", Double.class).get()
            );
        }

        /**
         * Returns a new JSON-serializable {@link Map} with the base64-encoded image and all of the fields.
         *
         * @param image the input image to include in the result.
         * @return a new JSON-serializable {@link Map} with the base64-encoded image and all of the fields.
         */
        private Map<String, Object> with(BufferedImage image) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Map.of(
                    "image", Base64.getEncoder().encodeToString(os.toByteArray()),
                    "ullat", ullat,
                    "ullon", ullon,
                    "lrlat", lrlat,
                    "lrlon", lrlon
            );
        }

        /**
         * Returns the location representing the center of this bounding box.
         *
         * @return the location representing the center of this bounding box.
         */
        private Location center() {
            return new Location(ullat / 2 + lrlat / 2, ullon / 2 + lrlon / 2, null);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            BoundingBox that = (BoundingBox) o;
            return Precision.equals(this.ullat, that.ullat, EPSILON)
                    && Precision.equals(this.ullon, that.ullon, EPSILON)
                    && Precision.equals(this.lrlat, that.lrlat, EPSILON)
                    && Precision.equals(this.lrlon, that.lrlon, EPSILON);
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                    Precision.round(ullat, DECIMAL_PLACES),
                    Precision.round(ullon, DECIMAL_PLACES),
                    Precision.round(lrlat, DECIMAL_PLACES),
                    Precision.round(lrlon, DECIMAL_PLACES)
            );
        }
    }
}
