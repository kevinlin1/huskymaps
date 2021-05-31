package huskymaps;

import org.apache.commons.math3.util.Precision;

import java.util.Objects;

import static huskymaps.Constants.*;

/**
 * A physical place in the world represented as latitude, longitude, and (optionally) a name.
 *
 * @see Builder
 * @see MapGraph
 */
public class Location {
    /**
     * The latitude of this location.
     */
    public final double lat;
    /**
     * The longitude of this location.
     */
    public final double lon;
    /**
     * The name of this location (or null).
     */
    public final String name;

    /**
     * Constructs a new location from the given latitude, longitude, and name.
     *
     * @param lat  the latitude.
     * @param lon  the longitude.
     * @param name the name.
     */
    public Location(double lat, double lon, String name) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    /**
     * Returns a new location from parsing the string latitude and string longitude.
     *
     * @param lat the numeric {@link String} representing the latitude.
     * @param lon the numeric {@link String} representing the longitude.
     * @return a new location from parsing the string latitude and string longitude.
     */
    public static Location parse(String lat, String lon) {
        return new Location(Double.parseDouble(lat), Double.parseDouble(lon), null);
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
        return Precision.equals(location.lat, lat, EPSILON) &&
                Precision.equals(location.lon, lon, EPSILON) &&
                Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                Precision.round(lat, DECIMAL_PLACES),
                Precision.round(lon, DECIMAL_PLACES),
                name
        );
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", name='" + name + '\'' +
                '}';
    }

    /**
     * Interactively constructs a new location when the fields are not all immediately available.
     * <pre>
     *     Location.Builder builder = new Location.Builder()
     *             .setLat(...)
     *             .setLon(...);
     *     ...
     *     builder.setName(...);
     *     Location result = builder.build();
     * </pre>
     *
     * @see Location
     */
    public static class Builder {
        private double lat;
        private double lon;
        private String name;

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
            return new Location(lat, lon, name);
        }
    }
}
