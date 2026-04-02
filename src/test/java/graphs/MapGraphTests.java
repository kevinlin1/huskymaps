package graphs;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MapGraphTests {

    private MapGraph graph;
    private SpatialContext context;

    @BeforeEach
    public void setUp() throws ParserConfigurationException, SAXException, IOException {
        context = SpatialContext.GEO;
        // MapGraph uses fileStream which looks at ClassLoader getResourceAsStream,
        // so loading from classpath requires "test.osm.gz" from src/test/resources/
        graph = new MapGraph("test.osm.gz", "test.places.tsv", context);
    }

    @Test
    public void testClosestExactMatch() {
        Point p1 = context.getShapeFactory().pointLatLon(47.60, -122.33);
        Point closest = graph.closest(p1);
        assertEquals(p1, closest);
    }

    @Test
    public void testClosestNearestMatch() {
        // Point closer to p1 (47.60, -122.33)
        Point target = context.getShapeFactory().pointLatLon(47.601, -122.331);
        Point closest = graph.closest(target);

        Point expected = context.getShapeFactory().pointLatLon(47.60, -122.33);
        assertEquals(expected, closest);

        // Point closer to p2 (47.61, -122.33)
        Point target2 = context.getShapeFactory().pointLatLon(47.609, -122.331);
        Point closest2 = graph.closest(target2);

        Point expected2 = context.getShapeFactory().pointLatLon(47.61, -122.33);
        assertEquals(expected2, closest2);
    }

    @Test
    public void testClosestEmptyGraph() throws Exception {
        // Create an empty graph (we can mock empty OSM and places)
        // Since test.osm.gz and test.places.tsv are used in setUp(),
        // we can create empty ones here if we need, or use an empty file.
        // Actually, we can just check if an empty graph throws NoSuchElementException.
        MapGraph emptyGraph = new MapGraph("empty.osm.gz", "empty.places.tsv", context);
        Point target = context.getShapeFactory().pointLatLon(47.60, -122.33);
        assertThrows(NoSuchElementException.class, () -> {
            emptyGraph.closest(target);
        });
    }
}
