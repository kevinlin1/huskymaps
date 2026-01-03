package minpq;

import net.jqwik.api.*;
import net.jqwik.api.footnotes.EnableFootnotes;
import net.jqwik.api.footnotes.Footnotes;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract class providing test cases for all {@link MinPQ} implementations.
 *
 * @see MinPQ
 */
@EnableFootnotes
public abstract class MinPQTests {
    /**
     * Returns an empty {@link MinPQ}.
     *
     * @return an empty {@link MinPQ}
     */
    public abstract <E> MinPQ<E> createMinPQ();

    @Example
    void wcagIndexAsPriority() throws FileNotFoundException {
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

    @Property
    void addChangeAndRemove(@ForAll List<@From("operations") String> operations, Footnotes footnotes) {
        MinPQ<Integer> reference = new DoubleMapMinPQ<>();
        MinPQ<Integer> testing = createMinPQ();

        for (String operation : operations) {
            footnotes.addFootnote(testing.toString());

            String[] parts = operation.split("[()]");
            switch (parts[0]) {
                case "addOrChangePriority" -> {
                    String[] arguments = parts[1].split(", ");
                    int element = Integer.parseInt(arguments[0]);
                    double priority = Double.parseDouble(arguments[1]);
                    reference.addOrChangePriority(element, priority);
                    testing.addOrChangePriority(element, priority);
                }
                case "removeMin" -> {
                    if (!reference.isEmpty()) {
                        // Not checked since peekMin is tested instead
                        reference.removeMin();
                        testing.removeMin();
                    }
                }
            }
            assertEquals(reference.size(), testing.size());
            if (!reference.isEmpty()) {
                int referenceElement = reference.peekMin();
                int testingElement = testing.peekMin();
                if (referenceElement != testingElement) {
                    double referencePriority = reference.getPriority(referenceElement);
                    double testingPriority = testing.getPriority(testingElement);
                    assertEquals(referencePriority, testingPriority);
                }
            }
        }
    }

    @Provide
    Arbitrary<String> operations() {
        return Arbitraries.oneOf(
            Combinators.combine(Arbitraries.integers(), Arbitraries.doubles().between(-1000, 1000)).as(
                (element, priority) -> String.format("addOrChangePriority(%d, %.2f)", element, priority)
            ),
            Arbitraries.just("removeMin()")
        );
    }
}
