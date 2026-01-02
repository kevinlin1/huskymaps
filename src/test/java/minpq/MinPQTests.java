package minpq;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract class providing test cases for all {@link MinPQ} implementations.
 *
 * @see MinPQ
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class MinPQTests {
    /**
     * Returns an empty {@link MinPQ}.
     *
     * @return an empty {@link MinPQ}
     */
    public abstract <E> MinPQ<E> createMinPQ();

    @Test
    public void wcagIndexAsPriority() throws FileNotFoundException {
        File inputFile = new File("data/wcag.tsv");
        MinPQ<String> reference = new DoubleMapMinPQ<>();
        MinPQ<String> testing = createMinPQ();
        try (Scanner scanner = new Scanner(inputFile)) {
            while (scanner.hasNextLine()) {
                String[] line = scanner.nextLine().split("\t", 2);
                int index = Integer.parseInt(line[0].replace(".", ""));
                String title = line[1];
                reference.add(title, index);
                testing.add(title, index);
            }
        }
        while (!reference.isEmpty()) {
            assertEquals(reference.removeMin(), testing.removeMin());
        }
        assertTrue(testing.isEmpty());
    }

    @Test
    public void randomPriorities() {
        int[] elements = new int[1000];
        for (int i = 0; i < elements.length; i = i + 1) {
            elements[i] = i;
        }
        Random random = new Random(373);
        int[] priorities = new int[elements.length];
        for (int i = 0; i < priorities.length; i = i + 1) {
            priorities[i] = random.nextInt(priorities.length);
        }

        MinPQ<Integer> reference = new DoubleMapMinPQ<>();
        MinPQ<Integer> testing = createMinPQ();
        for (int i = 0; i < elements.length; i = i + 1) {
            reference.add(elements[i], priorities[i]);
            testing.add(elements[i], priorities[i]);
        }

        for (int i = 0; i < elements.length; i = i+1) {
            int expected = reference.removeMin();
            int actual = testing.removeMin();

            if (expected != actual) {
                int expectedPriority = priorities[expected];
                int actualPriority = priorities[actual];
                assertEquals(expectedPriority, actualPriority);
            }
        }
    }

    @Test
    public void randomIntegersRandomPriorities() {
        MinPQ<Integer> reference = new DoubleMapMinPQ<>();
        MinPQ<Integer> testing = createMinPQ();

        int iterations = 10000;
        int maxElement = 1000;
        Random random = new Random();
        for (int i = 0; i < iterations; i += 1) {
            int element = random.nextInt(maxElement);
            double priority = random.nextDouble();
            reference.addOrChangePriority(element, priority);
            testing.addOrChangePriority(element, priority);
            assertEquals(reference.peekMin(), testing.peekMin());
            assertEquals(reference.size(), testing.size());
            for (int e = 0; e < maxElement; e += 1) {
                if (reference.contains(e)) {
                    assertTrue(testing.contains(e));
                    assertEquals(reference.getPriority(e), testing.getPriority(e));
                } else {
                    assertFalse(testing.contains(e));
                }
            }
        }
        for (int i = 0; i < iterations; i += 1) {
            boolean shouldRemoveMin = random.nextBoolean();
            if (shouldRemoveMin && !reference.isEmpty()) {
                assertEquals(reference.removeMin(), testing.removeMin());
            } else {
                int element = random.nextInt(maxElement);
                double priority = random.nextDouble();
                reference.addOrChangePriority(element, priority);
                testing.addOrChangePriority(element, priority);
            }
            if (!reference.isEmpty()) {
                assertEquals(reference.peekMin(), testing.peekMin());
                assertEquals(reference.size(), testing.size());
                for (int e = 0; e < maxElement; e += 1) {
                    if (reference.contains(e)) {
                        assertTrue(testing.contains(e));
                        assertEquals(reference.getPriority(e), testing.getPriority(e));
                    } else {
                        assertFalse(testing.contains(e));
                    }
                }
            } else {
                assertTrue(testing.isEmpty());
            }
        }
    }
}
