package minpq.moderator;

import minpq.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * Compare different {@link ExtrinsicMinPQ} implementations to check that they compute the same values.
 *
 * @see ExtrinsicMinPQ
 * @see DoubleMapMinPQ
 * @see UnsortedArrayMinPQ
 * @see HeapMinPQ
 * @see OptimizedHeapMinPQ
 */
class ModeratorMultiTests {
    /**
     * Path to the toxic content.
     */
    private static final String PATH = "data/toxic.tsv";

    /**
     * Add all comments and toxicities to the given priority queue.
     *
     * @param toxic {@link Map} of toxicity values to comments (since comments can share the same toxicity value).
     * @param pq    destination priority queue.
     */
    private static void addAllComments(Map<Double, String> toxic, ExtrinsicMinPQ<String> pq) {
        for (double toxicity : toxic.keySet()) {
            String comment = toxic.get(toxicity);
            pq.add(comment, -toxicity);
        }
    }

    /**
     * Remove all elements from {@code ref} and {@code test} and returns true if the order of removals are the same.
     *
     * @param ref  the reference priority queue.
     * @param test the testing priority queue.
     * @return true if the order of removals are the same.
     */
    private static boolean sameRemoveOrder(ExtrinsicMinPQ<String> ref, ExtrinsicMinPQ<String> test) {
        int numItems = ref.size();
        List<String> refOutput = new ArrayList<>();
        List<String> testOutput = new ArrayList<>();
        for (int i = 0; i < numItems; i++) {
            refOutput.add(ref.removeMin());
            testOutput.add(test.removeMin());
        }
        return refOutput.equals(testOutput);
    }

    public static void main(String[] args) throws IOException {
        // We go toxicity -> comment because all toxicity values used for testing need to be unique.
        Map<Double, String> toxic = new LinkedHashMap<>();

        Scanner scanner = new Scanner(new FileInputStream(PATH));
        scanner.nextLine(); // Skip header
        while (scanner.hasNextLine()) {
            Scanner line = new Scanner(scanner.nextLine()).useDelimiter("\t");
            double toxicity = line.nextDouble();
            String comment = line.next();
            toxic.put(toxicity, comment);
        }

        System.out.println(toxic.size() + " comments loaded");

        // Testing implementations.
        Map<String, ExtrinsicMinPQ<String>> implementations = Map.of(
                "UnsortedArrayMinPQ", new UnsortedArrayMinPQ<>(),
                "HeapMinPQ", new HeapMinPQ<>(),
                "OptimizedHeapMinPQ", new OptimizedHeapMinPQ<>()
        );

        // Check each implementation against the reference matches.
        for (String name : implementations.keySet()) {
            System.out.println();

            ExtrinsicMinPQ<String> referencePQ = new DoubleMapMinPQ<>();
            ExtrinsicMinPQ<String> testPQ = implementations.get(name);

            // Add same elements to both reference and student-written PQ.
            addAllComments(toxic, referencePQ);
            addAllComments(toxic, testPQ);

            // Test 1: Basic test that `contains` is working.
            boolean containsPassed = true;
            for (double toxicity : toxic.keySet()) {
                String comment = toxic.get(toxicity);

                if (!testPQ.contains(comment)) {
                    System.out.println(name + " contains() FAIL [after insertion]!");
                    containsPassed = false;
                    break;
                }
            }

            if (containsPassed)
                System.out.println(name + " contains() PASS [after insertion]!");

            // Test 2: See if size() is working.
            if (referencePQ.size() == testPQ.size())
                System.out.println(name + " size() PASS [after insertion]!");
            else
                System.out.println(name + " size() FAIL [after insertion]!");

            // Test 3: See if peek() is working.
            if (referencePQ.peekMin().equals(testPQ.peekMin()))
                System.out.println(name + " peek() PASS!");
            else
                System.out.println(name + " peek() FAIL!");

            // Test 4: Remove all comments from both the reference and test PQ, make sure
            // the order matches.
            if (sameRemoveOrder(referencePQ, testPQ))
                System.out.println(name + " removeMin() PASS!");
            else
                System.out.println(name + " removeMin() FAIL!");

            // Test 5: Make sure contains works after removing everything.
            containsPassed = true;
            for (double toxicity : toxic.keySet()) {
                String comment = toxic.get(toxicity);

                // This should not happen on an emptied-out PQ.
                if (testPQ.contains(comment)) {
                    System.out.println(name + " contains() FAIL [after removeMin]!");
                    containsPassed = false;
                    break;
                }
            }

            if (containsPassed)
                System.out.println(name + " contains() PASS [after removeMin]!");

            // Test 6: Make sure size works after removing everything.
            if (referencePQ.size() == testPQ.size())
                System.out.println(name + " size() PASS [after removeMin]!");
            else
                System.out.println(name + " size() FAIL [after removeMin]!");

            // Test 7: Re-populate, change priorities around, test new remove order.
            addAllComments(toxic, referencePQ);
            addAllComments(toxic, testPQ);

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
                referencePQ.changePriority(shuffledComments.get(i), shuffledPriorities.get(i));
                testPQ.changePriority(shuffledComments.get(i), shuffledPriorities.get(i));
            }

            // Make sure order matches after `changePriority`.
            if (sameRemoveOrder(referencePQ, testPQ))
                System.out.println(name + " changePriority() PASS!");
            else
                System.out.println(name + " changePriority() FAIL!");

            System.out.println();
        }
    }
}
