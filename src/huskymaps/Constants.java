package huskymaps;

import java.awt.*;
import java.util.Set;

/**
 * All the constant values used in {@code huskymaps}.
 */
public class Constants {
    /**
     * Radius of the Earth in miles.
     */
    public static final int R = 3963;
    /**
     * Error tolerance for latitudes and longitudes.
     */
    public static final double EPSILON = 0.000001;
    public static final int DECIMAL_PLACES = 5;
    /**
     * Only allow for non-service roads. This prevents going on pedestrian streets.
     */
    public static final Set<String> ALLOWED_HIGHWAY_TYPES = Set.of(
            "motorway", "trunk", "primary", "secondary", "tertiary", "unclassified", "residential",
            "living_street", "motorway_link", "trunk_link", "primary_link", "secondary_link",
            "tertiary_link"
    );
    /**
     * String template for the <a href="https://docs.mapbox.com/api/maps/static-images/">MapBox Static Images API</a>.
     */
    public static final String STATIC_IMAGES_API = "https://api.mapbox.com/"
            // {username}/{style_id} and {overlay} (must include trailing slash)
            + "styles/v1/%s/%s/static/%s"
            // {bbox}/{width}x{height}{@2x}
            + "[%f,%f,%f,%f]/%dx%d%s"
            // Access token and optional parameters
            + "?access_token=%s&logo=false&attribution=false";
    public static final String MARKER_OVERLAY = "pin-s(%f,%f)";
    /**
     * Route stroke information.
     */
    public static final Color ROUTE_STROKE_COLOR = new Color(108, 181, 230);
    public static final float ROUTE_STROKE_WIDTH_PX = 5.0f;
    /**
     * Maximum number of autocomplete search results.
     */
    public static final int MAX_MATCHES = 10;
    /**
     * Default port for serving the application locally.
     */
    public static final int PORT = 8080;
    /**
     * The OSM XML file path. Downloaded from <a href="http://download.bbbike.org/osm/">BBBike</a>
     * using custom region selection.
     */
    public static final String OSM_DB_PATH = "data/huskymaps/seattle-small.osm.gz";
    public static final String PLACES_PATH = "data/huskymaps/places.tsv";
}
