package autocomplete.cities;

import autocomplete.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Compare different {@link Autocomplete} implementations to check that they compute the same values.
 *
 * @see Autocomplete
 * @see TreeSetAutocomplete
 * @see SequentialSearchAutocomplete
 * @see BinarySearchAutocomplete
 * @see TernarySearchTreeAutocomplete
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AutocompleteTests {
    /**
     * Maximum number of cities to parse.
     */
    private static final int MAX_CITIES = 43187;
    /**
     * Path to the cities dataset.
     */
    private static final String PATH = "data/cities.tsv";
    /**
     * Associating each city name to the importance weight of that city.
     */
    private final Set<String> cities = new HashSet<>(MAX_CITIES, 1.0f);
    /**
     * Reference implementation of the {@link Autocomplete} interface for comparison.
     */
    private final Autocomplete reference = new TreeSetAutocomplete();
    /**
     * Testing implementation of the {@link Autocomplete} interface for comparison.
     */
    private final Autocomplete testing = createAutocomplete();

    /**
     * Returns an empty {@link Autocomplete} instance.
     *
     * @return an empty {@link Autocomplete} instance
     */
    public abstract Autocomplete createAutocomplete();

    @BeforeAll
    void setup() throws IOException {
        Scanner input = new Scanner(new FileInputStream(PATH));
        while (input.hasNextLine() && cities.size() < MAX_CITIES) {
            Scanner line = new Scanner(input.nextLine()).useDelimiter("\t");
            String city = line.next();
            // int weight = line.nextInt();
            cities.add(city);
        }
        reference.addAll(cities);
        testing.addAll(cities);
    }

    @Test
    void comparePrefixSea() {
        assertAllMatches("Sea");
    }

    @Test
    void compareRandomPrefixes() {
        Random random = new Random(373);
        double samplingProportion = 0.0001;
        for (String city : cities) {
            if (random.nextDouble() <= samplingProportion) {
                String prefix = city;
                if (prefix.length() >= 4) {
                    int length = random.nextInt(prefix.length() - 2) + 2;
                    prefix = prefix.substring(0, length);
                }
                assertAllMatches(prefix);
            }
        }
    }

    /**
     * Asserts that the reference and testing implementations' {@code allMatches} methods produce
     * the same results ignoring order.
     *
     * @param prefix the prefix string to pass to {@code allMatches}
     */
    void assertAllMatches(String prefix) {
        List<CharSequence> expected = reference.allMatches(prefix);
        List<CharSequence> actual = testing.allMatches(prefix);
        assertEquals(expected.size(), actual.size());
        assertTrue(expected.containsAll(actual));
        assertTrue(actual.containsAll(expected));
    }
}
