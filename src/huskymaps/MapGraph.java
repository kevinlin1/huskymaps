package huskymaps;

import autocomplete.Autocomplete;
import autocomplete.TreeSetAutocomplete;
import graphs.AStarGraph;
import graphs.Edge;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * {@link AStarGraph} of places as {@link Location} vertices and streets edges weighted by physical distance. Estimated
 * distance is defined by {@link Location#distance(Location)}.
 *
 * @see AStarGraph
 * @see Location
 * @see MapServer
 */
public class MapGraph implements AStarGraph<Location> {
    private final String osmPath;
    private final String placesPath;
    private final Map<Location, Set<Edge<Location>>> neighbors;
    private final Map<String, List<Location>> locations;
    private final Autocomplete autocomplete;
    private final Map<CharSequence, Integer> importance;

    /**
     * Constructs a new street map graph from the path to an OSM GZ file and a places TSV.
     *
     * @param osmPath    The path to a gzipped OSM (XML) file.
     * @param placesPath The path to a TSV file representing places and importance.
     * @throws ParserConfigurationException if a parser cannot be created.
     * @throws SAXException                 for SAX errors.
     * @throws IOException                  if a file is not found or if the file is not gzipped.
     */
    public MapGraph(String osmPath, Collection<String> allowedHighwayTypes, String placesPath)
            throws ParserConfigurationException, SAXException, IOException {
        this.osmPath = osmPath;
        this.placesPath = placesPath;

        // Parse the OpenStreetMap (OSM) data using the SAXParser XML tree walker.
        neighbors = new HashMap<>();
        Handler handler = new Handler(allowedHighwayTypes);
        SAXParser saxParser = SAXParserFactory.newInstance().newSAXParser();
        saxParser.parse(new GZIPInputStream(fileStream(osmPath)), handler);

        // Add reachable locations to the Autocomplete engine.
        locations = handler.locations();
        autocomplete = new TreeSetAutocomplete();
        autocomplete.addAll(locations.keySet());

        // Parse the place-importance data.
        importance = new HashMap<>();
        try (Scanner input = new Scanner(fileStream(placesPath))) {
            while (input.hasNextLine()) {
                Scanner line = new Scanner(input.nextLine()).useDelimiter("\t");
                importance.put(line.next(), line.nextInt());
            }
        }
    }

    /**
     * Returns an input stream from the contents of the file at the given path.
     *
     * @param path a file path.
     * @return an input stream with the contents of the specified file.
     * @throws FileNotFoundException if there is no file at the specified path.
     */
    private static InputStream fileStream(String path) throws FileNotFoundException {
        if (System.getenv("PORT") == null) {
            return new FileInputStream(path);
        }
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    /**
     * Returns the location closest to the given target location.
     *
     * @param target the target location.
     * @return the id of the location closest to the target.
     */
    public Location closest(Location target) {
        return Collections.min(neighbors.keySet(), Comparator.comparingDouble(target::distance));
    }

    /**
     * Return the names of all locations that prefix-match the query string.
     *
     * @param prefix prefix string that could be any case with or without punctuation.
     * @return a list of full names of locations matching the prefix.
     */
    public List<CharSequence> getLocationsByPrefix(String prefix) {
        List<CharSequence> result = autocomplete.allMatches(prefix);
        result.sort(Comparator.comparingInt(importance::get));
        return result;
    }

    /**
     * Return all locations that match a valid location name.
     *
     * @param locationName a full name of a valid location.
     * @return a list of locations whose name matches the location name.
     */
    public List<Location> getLocations(String locationName, Location center) {
        if (locationName == null || !locations.containsKey(locationName)) {
            return List.of();
        }
        List<Location> result = new ArrayList<>(locations.get(locationName));
        result.sort(Comparator.comparingDouble(center::distance));
        return result;
    }

    @Override
    public List<Edge<Location>> neighbors(Location v) {
        return new ArrayList<>(neighbors.getOrDefault(v, Set.of()));
    }

    @Override
    public double estimatedDistance(Location start, Location end) {
        return start.distance(end);
    }

    @Override
    public String toString() {
        return "MapGraph{" +
                "osmPath='" + osmPath + '\'' +
                ", placesPath='" + placesPath + '\'' +
                '}';
    }

    /**
     * Adds an edge to this graph if it doesn't already exist, using distance as the weight.
     */
    private void addEdge(Location from, Location to) {
        if (!neighbors.containsKey(from)) {
            neighbors.put(from, new HashSet<>());
        }
        neighbors.get(from).add(new Edge<>(from, to, from.distance(to)));
    }

    /**
     * Parses OSM XML files to construct a StreetMapGraph.
     */
    private class Handler extends DefaultHandler {
        private final Collection<String> allowedHighwayTypes;
        private final Map<Long, Location> nodes;
        private String state;
        private long id;
        private boolean validWay;
        private Location.Builder builder;
        private Queue<Location> path;

        Handler(Collection<String> allowedHighwayTypes) {
            this.allowedHighwayTypes = allowedHighwayTypes;
            this.nodes = new HashMap<>();
            reset();
        }

        Map<String, List<Location>> locations() {
            Map<String, List<Location>> result = new HashMap<>();
            for (Location location : nodes.values()) {
                String name = location.name;
                if (name != null) {
                    if (!result.containsKey(name)) {
                        result.put(name, new ArrayList<>());
                    }
                    result.get(name).add(location);
                }
            }
            return result;
        }

        /**
         * Reset the handler state before processing a new way or node.
         */
        private void reset() {
            state = "";
            id = Long.MIN_VALUE;
            validWay = false;
            builder = new Location.Builder();
            path = new ArrayDeque<>();
        }

        /**
         * Called at the beginning of an element.
         *
         * @param uri        The Namespace URI, or the empty string if the element has no Namespace URI or
         *                   if Namespace processing is not being performed.
         * @param localName  The local name (without prefix), or the empty string if Namespace
         *                   processing is not being performed.
         * @param qName      The qualified name (with prefix), or the empty string if qualified names are
         *                   not available. This tells us which element we're looking at.
         * @param attributes The attributes attached to the element. If there are no attributes, it
         *                   shall be an empty Attributes object.
         * @see Attributes
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equals("node")) {
                state = "node";
                id = Long.parseLong(attributes.getValue("id"));
                double lat = Double.parseDouble(attributes.getValue("lat"));
                double lon = Double.parseDouble(attributes.getValue("lon"));
                builder.setLat(lat).setLon(lon);
            } else if (qName.equals("way")) {
                state = "way";
            } else if (state.equals("way") && qName.equals("nd")) {
                long ref = Long.parseLong(attributes.getValue("ref"));
                path.add(nodes.get(ref));
            } else if (state.equals("way") && qName.equals("tag")) {
                String k = attributes.getValue("k");
                String v = attributes.getValue("v");
                if (k.equals("highway")) {
                    validWay = allowedHighwayTypes.contains(v);
                }
            } else if (state.equals("node") && qName.equals("tag") && attributes.getValue("k").equals("name")) {
                String name = attributes.getValue("v").strip()
                        .replace('“', '"')
                        .replace('”', '"')
                        .replace('‘', '\'')
                        .replace('’', '\'');
                builder.setName(name);
            }
        }

        /**
         * Called at the end of an element.
         *
         * @param uri       The Namespace URI, or the empty string if the element has no Namespace URI or
         *                  if Namespace processing is not being performed.
         * @param localName The local name (without prefix), or the empty string if Namespace
         *                  processing is not being performed.
         * @param qName     The qualified name (with prefix), or the empty string if qualified names are
         *                  not available.
         */
        @Override
        public void endElement(String uri, String localName, String qName) {
            if (qName.equals("way")) {
                if (validWay && !path.isEmpty()) {
                    Location from = path.remove();
                    while (!path.isEmpty()) {
                        Location to = path.remove();
                        addEdge(from, to);
                        addEdge(to, from);
                        from = to;
                    }
                }
                reset();
            } else if (qName.equals("node")) {
                nodes.put(id, builder.build());
                reset();
            }
        }
    }
}
