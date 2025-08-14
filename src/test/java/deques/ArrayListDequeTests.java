package deques;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

/**
 * Tests for the {@link ArrayListDeque} class.
 */
public class ArrayListDequeTests extends DequeTests {
    @Override
    public <E> Deque<E> createDeque() {
        return new ArrayListDeque<>();
    }

    @Test
    @Disabled
    void browserHistoryRuntimeSimulation() {
        Deque<String> deque = createDeque();

        // Simulate visiting uw.edu many times
        for (int i = 0; i < 100000; i += 1) {
            deque.addLast("uw.edu");
        }

        // Simulate removing the last hour of history
        for (int i = 0; i < 100; i += 1) {
            deque.removeLast();
        }

        // Simulate visiting uw.edu many more times
        for (int i = 0; i < 1000000; i += 1) {
            deque.addLast("uw.edu");
        }

        // Simulate removing history from over 3 months ago
        for (int i = 0; i < 100000; i += 1) {
            deque.removeFirst();
        }
    }
}
