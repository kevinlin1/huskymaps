package huskymaps;

import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.ShapeFactory;

import java.util.Objects;

/**
 * A physical place in the world represented as longitude, latitude, and (optionally) a name.
 *
 * @see Builder
 * @see MapGraph
 */
public class Location {
    private static final ShapeFactory factory = SpatialContext.GEO.getShapeFactory();
    /**
     * The geospatial point for this location.
     */
    private final Point point;
    /**
     * The name of this location (or null).
     */
    private final String name;

    /**
     * Constructs a new location from the given longitude, latitude, and name.
     *
     * @param lon  the longitude.
     * @param lat  the latitude.
     * @param name the name.
     */
    public Location(Point point, String name) {
        this.point = point;
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
        double lon = Double.parseDouble(lonLat[0]);
        double lat = Double.parseDouble(lonLat[1]);
        return new Location(factory.pointLatLon(lat, lon), null);
    }

    public double getLon() {
        return point.getLon();
    }

    public double getLat() {
        return point.getLat();
    }

    public String getName() {
        return name;
    }

    /**
     * Returns the great-circle (haversine) distance between geographic coordinates.
     *
     * @param other The other location.
     * @return The great-circle distance between the two vertices.
     */
    public double distance(Location other) {
        return SpatialContext.GEO.calcDistance(this.point, other.point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location other = (Location) o;
        return point.equals(other.point) && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point, name);
    }

    @Override
    public String toString() {
        return "Location{" +
                "point=" + point +
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
            return new Location(factory.pointLatLon(lat, lon), name);
        }
    }
}
