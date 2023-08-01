package minpq;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract class providing test cases for all {@link MinPQ} implementations.
 *
 * @see MinPQ
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class MinPQTests {
    /**
     * Path to the toxic content.
     */
    private static final String PATH = "data/toxic.tsv";
    /**
     * Associates toxicity value to comment because all toxicity values need to be unique.
     */
    private final Map<Double, String> toxic = new LinkedHashMap<>();

    /**
     * Returns an empty {@link MinPQ}.
     *
     * @return an empty {@link MinPQ}
     */
    public abstract MinPQ<String> createMinPQ();

    @BeforeAll
    void setup() throws IOException {
        Scanner scanner = new Scanner(new FileInputStream(PATH));
        scanner.nextLine(); // Skip header
        while (scanner.hasNextLine()) {
            Scanner line = new Scanner(scanner.nextLine()).useDelimiter("\t");
            double toxicity = line.nextDouble();
            String comment = line.next().replaceAll("\\w", "*");
            toxic.put(toxicity, comment);
        }
    }

    @Test
    void complicatedTest() {
        MinPQ<String> reference = new DoubleMapMinPQ<>();
        addAllComments(reference, toxic);
        MinPQ<String> testing = createMinPQ();
        addAllComments(testing, toxic);

        // Test 1: Basic test that contains is working.
        for (double toxicity : toxic.keySet()) {
            String comment = toxic.get(toxicity);
            assertTrue(testing.contains(comment));
        }
        // Test 2: See if size() is working.
        assertEquals(reference.size(), testing.size());
        // Test 3: See if peek() is working.
        assertEquals(reference.peekMin(), testing.peekMin());
        // Test 4: Remove all comments from both the reference and test PQ, ensuring order matches.
        assertTrue(sameRemoveOrder(reference, testing));
        // Test 5: Make sure contains works after removing everything.
        for (double toxicity : toxic.keySet()) {
            String comment = toxic.get(toxicity);
            assertFalse(testing.contains(comment));
        }
        // Test 6: Make sure size works after removing everything.
        assertEquals(reference.size(), testing.size());

        // Test 7: Re-populate, change priorities around, test new remove order.
        addAllComments(reference, toxic);
        addAllComments(testing, toxic);
        // Mix up all the priority values.
        List<String> shuffledComments = new ArrayList<>();
        List<Double> shuffledPriorities = new ArrayList<>();
        for (double toxicity : toxic.keySet()) {
            String comment = toxic.get(toxicity);
            shuffledComments.add(comment);
            shuffledPriorities.add(toxicity);
        }
        Collections.shuffle(shuffledPriorities);
        // Change priority.
        for (int i = 0; i < shuffledComments.size(); i++) {
            reference.changePriority(shuffledComments.get(i), shuffledPriorities.get(i));
            testing.changePriority(shuffledComments.get(i), shuffledPriorities.get(i));
        }
        // Make sure order matches after changePriority.
        assertTrue(sameRemoveOrder(reference, testing));
    }

    /**
     * Add all comments and toxicities to the given priority queue.
     *
     * @param pq    destination priority queue.
     * @param toxic {@link Map} of toxicity values to comments (since comments can share the same toxicity value).
     */
    private static void addAllComments(MinPQ<String> pq, Map<Double, String> toxic) {
        Set<String> seen = new HashSet<>();
        for (double toxicity : toxic.keySet()) {
            String comment = toxic.get(toxicity);
            if (!seen.contains(comment)) {
                pq.add(comment, -toxicity);
                seen.add(comment);
            }
        }
    }

    /**
     * Remove all elements from {@code ref} and {@code test} and returns true if the order of removals are the same.
     *
     * @param ref  the reference priority queue.
     * @param test the testing priority queue.
     * @return true if the order of removals are the same.
     */
    private static boolean sameRemoveOrder(MinPQ<String> ref, MinPQ<String> test) {
        int numElements = ref.size();
        List<String> refOutput = new ArrayList<>();
        List<String> testOutput = new ArrayList<>();
        for (int i = 0; i < numElements; i++) {
            refOutput.add(ref.removeMin());
            testOutput.add(test.removeMin());
        }
        return refOutput.equals(testOutput);
    }
}
