package autocomplete;

import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract class providing test cases for all {@link Autocomplete} implementations.
 *
 * @see Autocomplete
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
    private final List<String> cities = new ArrayList<>(MAX_CITIES);
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

    @Test
    void allMatchesMutation() {
        List<CharSequence> results = testing.allMatches("Sea");
        List<CharSequence> expected = new ArrayList<>(results);
        results.clear();
        assertEquals(expected, testing.allMatches("Sea"));
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

    @Nested
    @Disabled
    class RuntimeExperiments {
        /**
         * Number of trials per implementation run. Making this smaller means experiments run faster.
         */
        private static final int NUM_TRIALS = 1000;
        /**
         * Maximum number of elements to add.
         */
        public static final int MAX_SIZE = 20000;
        /**
         * Step size increment. Making this smaller means experiments run slower.
         */
        private static final int STEP = 1000;

        @Test
        void addAllAllMatches() {
            for (int size = STEP; size <= MAX_SIZE; size += STEP) {
                System.out.print(size);
                System.out.print(',');

                // Make a new test input dataset containing the first size cities
                List<String> dataset = cities.subList(0, size);

                // Record the total runtimes accumulated across all trials
                long totalAddAllTime = 0;

                Autocomplete autocomplete = null;
                for (int i = 0; i < NUM_TRIALS; i += 1) {
                    autocomplete = createAutocomplete();
                    // Measure the time to add all cities
                    long addStart = System.nanoTime();
                    autocomplete.addAll(dataset);
                    long addTime = System.nanoTime() - addStart;
                    totalAddAllTime += addTime;
                }
                // Output the average rounded to the closest integer.
                System.out.printf("%.0f", totalAddAllTime / (double) NUM_TRIALS);

                for (String prefix : new String[]{"Sea"}) {
                    long totalMatchesTime = 0;
                    for (int i = 0; i < NUM_TRIALS; i += 1) {
                        // Measure the time to find all matches
                        long matchesStart = System.nanoTime();
                        autocomplete.allMatches(prefix);
                        long matchesTime = System.nanoTime() - matchesStart;
                        totalMatchesTime += matchesTime;
                    }
                    // Output the average rounded to the closest integer.
                    System.out.print(',');
                    System.out.printf("%.0f", totalMatchesTime / (double) NUM_TRIALS);
                }
                System.out.println();
            }
        }
    }
}
