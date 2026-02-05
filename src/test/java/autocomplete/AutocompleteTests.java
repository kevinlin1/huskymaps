package autocomplete;

import net.jqwik.api.*;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.StringLength;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract class providing test cases for all {@link Autocomplete} implementations.
 *
 * @see Autocomplete
 */
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
    private static final List<String> CITIES = new ArrayList<>(MAX_CITIES);
    static {
        try (Scanner input = new Scanner(new FileInputStream(PATH))) {
            while (input.hasNextLine() && CITIES.size() < MAX_CITIES) {
                try (Scanner line = new Scanner(input.nextLine()).useDelimiter("\t")) {
                    CITIES.add(line.next());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reference implementation of the {@link Autocomplete} interface for comparison.
     */
    private final Autocomplete reference = new TreeSetAutocomplete(CITIES);
    /**
     * Testing implementation of the {@link Autocomplete} interface for comparison.
     */
    private final Autocomplete testing = createAutocomplete(CITIES);

    /**
     * Returns an {@link Autocomplete} instance containing the given terms.
     *
     * @param terms to add to the new {@link Autocomplete} instance
     * @return an {@link Autocomplete} instance containing the given terms
     */
    public abstract Autocomplete createAutocomplete(Collection<? extends CharSequence> terms);

    @Property
    void compareArbitraryPrefix(@ForAll @AlphaChars @StringLength(max = 10) String prefix) {
        assertAllMatches(prefix);
    }

    @Property
    void compareValidPrefix(@ForAll("validPrefix") String prefix) {
        assertAllMatches(prefix);
    }

    @Provide
    Arbitrary<String> validPrefix() {
        Arbitrary<String> city = Arbitraries.of(CITIES);
        return city.flatMap(s -> Arbitraries.integers().between(1, s.length()).map(i -> s.substring(0, i)));
    }

    @Example
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

    static void runtimeExperiments(Supplier<Autocomplete> constructor) {
        for (int size = STEP; size <= MAX_SIZE; size += STEP) {
            System.out.print(size);
            System.out.print(',');

            // Make a new test input dataset containing the first size cities
            List<String> dataset = CITIES.subList(0, size);

            // Record the total runtimes accumulated across all trials
            long totalAddAllTime = 0;

            Autocomplete autocomplete = null;
            for (int i = 0; i < NUM_TRIALS; i += 1) {
                autocomplete = constructor.get();
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
