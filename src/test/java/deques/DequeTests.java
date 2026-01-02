package deques;

import net.jqwik.api.*;
import net.jqwik.api.footnotes.EnableFootnotes;
import net.jqwik.api.footnotes.Footnotes;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract class providing test cases for all implementations of the {@link Deque} interface.
 *
 * @see Deque
 */
@EnableFootnotes
public abstract class DequeTests {
    /**
     * Returns an empty {@link Deque}.
     *
     * @param <E> the type of elements in the returned deque
     * @return an empty deque
     */
    public abstract <E> Deque<E> createDeque();

    @Property(seed = "1")
    void addAndRemove(@ForAll List<@From("operations") String> operations, Footnotes footnotes) {
        java.util.Deque<Integer> reference = new java.util.ArrayDeque<>();
        Deque<Integer> testing = createDeque();

        for (String operation : operations) {
            footnotes.addFootnote(testing.toString());

            String[] parts = operation.split("[()]");
            switch (parts[0]) {
                case "addFirst" -> {
                    int element = Integer.parseInt(parts[1]);
                    reference.addFirst(element);
                    testing.addFirst(element);
                }
                case "addLast" -> {
                    int element = Integer.parseInt(parts[1]);
                    reference.addLast(element);
                    testing.addLast(element);
                }
                case "removeFirst" -> {
                    if (!reference.isEmpty()) {
                        assertEquals(reference.removeFirst(), testing.removeFirst());
                    } else {
                        assertNull(testing.removeFirst());
                    }
                }
                case "removeLast" -> {
                    if (!reference.isEmpty()) {
                        assertEquals(reference.removeLast(), testing.removeLast());
                    } else {
                        assertNull(testing.removeLast());
                    }
                }
            }
            assertEquals(reference.size(), testing.size());
        }
    }

    @Provide
    Arbitrary<String> operations() {
        return Arbitraries.oneOf(
            Arbitraries.integers().map(i -> "addFirst(" + i + ")"),
            Arbitraries.integers().map(i -> "addLast(" + i + ")"),
            Arbitraries.just("removeFirst()"),
            Arbitraries.just("removeLast()")
        );
    }

    /**
     * Number of trials per implementation run. Making this smaller means experiments run faster.
     */
    public static final int NUM_TRIALS = 1000;
    /**
     * Maximum number of elements to add.
     */
    public static final int MAX_SIZE = 20000;
    /**
     * Step size increment. Making this smaller means experiments run slower.
     */
    public static final int STEP = 100;

    /**
     * Print the time in seconds that it takes to add elements to an increasingly-large deque.
     * The output is comma-separated with columns for deque size and add time (nanoseconds).
     */
    static void runtimeExperiments(Supplier<Deque<Integer>> constructor) {
        for (int size = STEP; size <= MAX_SIZE; size += STEP) {
            System.out.print(size);
            System.out.print(',');

            // Create a new deque and add size-number of integers
            Deque<Integer> deque = constructor.get();
            for (int j = 0; j < size; j += 1) {
                deque.addLast(j);
            }

            // Record the total runtimes accumulated across all trials
            long totalAddTime = 0;

            for (int i = 0; i < NUM_TRIALS; i += 1) {
                // Measure the time to add one more integer
                long addStart = System.nanoTime();
                deque.addLast(size);
                long addTime = System.nanoTime() - addStart;
                // Add to total time
                totalAddTime += addTime;
                // Remove the just-added integer
                deque.removeLast();
            }

            // Output the average rounded to the closest integer.
            System.out.printf("%.0f", totalAddTime / (double) NUM_TRIALS);
            System.out.println();
        }
    }
}
