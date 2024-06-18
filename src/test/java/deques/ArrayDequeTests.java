package deques;

/**
 * Tests for the {@link ArrayDeque} class.
 *
 * @see ArrayDeque
 */
public class ArrayDequeTests extends DequeTests {
    @Override
    public <E> Deque<E> createDeque() {
        return new ArrayDeque<>();
    }

    // You may write additional tests here if you only want them to run for ArrayDeque
}
