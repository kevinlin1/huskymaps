package huskymaps;

import org.apache.commons.math3.util.Precision;

import java.util.Objects;

/**
 * A physical place in the world represented as longitude, latitude, and (optionally) a name.
 *
 * @see Builder
 * @see MapGraph
 */
public class Location {
    private static final int DECIMAL_PLACES = 5;
    /**
     * Error tolerance for latitudes and longitudes.
     */
    private static final double EPSILON = 0.000001;
    /**
     * Radius of the Earth in miles.
     */
    private static final int R = 3963;

    /**
     * The longitude of this location.
     */
    public final double lon;
    /**
     * The latitude of this location.
     */
    public final double lat;
    /**
     * The name of this location (or null).
     */
    public final String name;

    /**
     * Constructs a new location from the given longitude, latitude, and name.
     *
     * @param lon  the longitude.
     * @param lat  the latitude.
     * @param name the name.
     */
    public Location(double lon, double lat, String name) {
        this.lon = lon;
        this.lat = lat;
        this.name = name;
    }

    /**
     * Returns a new location from parsing the first two elements of the given array of strings.
     *
     * @param lonLat an array of strings where the first two elements represent the longitude and latitude.
     * @return a new location from parsing the string longitude and string latitude.
     */
    public static Location parse(String... lonLat) {
        if (lonLat == null || lonLat.length < 2) {
            return null;
        }
        return new Location(Double.parseDouble(lonLat[0]), Double.parseDouble(lonLat[1]), null);
    }

    /**
     * Returns the great-circle (haversine) distance between geographic coordinates.
     *
     * @param other The other location.
     * @return The great-circle distance between the two vertices.
     * @see <a href="https://www.movable-type.co.uk/scripts/latlong.html">https://www.movable-type.co.uk/scripts/latlong.html</a>
     */
    public double distance(Location other) {
        double phi1 = Math.toRadians(this.lat);
        double phi2 = Math.toRadians(other.lat);
        double dphi = Math.toRadians(other.lat - this.lat);
        double dlambda = Math.toRadians(other.lon - this.lon);
        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return Precision.equals(location.lon, lon, EPSILON) &&
                Precision.equals(location.lat, lat, EPSILON) &&
                Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Precision.round(lon, DECIMAL_PLACES),
                Precision.round(lat, DECIMAL_PLACES),
                name
        );
    }

    @Override
    public String toString() {
        return "Location{" +
                "lon=" + lon +
                ", lat=" + lat +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Interactively constructs a new location when the fields are not all immediately available.
     * <pre>
     *     Location.Builder builder = new Location.Builder()
     *             .setLon(...)
     *             .setLat(...);
     *     ...
     *     builder.setName(...);
     *     Location result = builder.build();
     * </pre>
     *
     * @see Location
     */
    public static class Builder {
        private double lon;
        private double lat;
        private String name;

        /**
         * Sets the longitude for this builder.
         *
         * @param lon the longitude.
         * @return this builder.
         */
        public Builder setLon(double lon) {
            this.lon = lon;
            return this;
        }

        /**
         * Sets the latitude for this builder.
         *
         * @param lat the latitude.
         * @return this builder.
         */
        public Builder setLat(double lat) {
            this.lat = lat;
            return this;
        }

        /**
         * Sets the name for this builder.
         *
         * @param name the name.
         * @return this builder.
         */
        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        /**
         * Returns a new {@link Location} from the fields of this builder.
         *
         * @return Returns a new {@link Location} from the fields of this builder.
         */
        public Location build() {
            return new Location(lon, lat, name);
        }
    }
}
