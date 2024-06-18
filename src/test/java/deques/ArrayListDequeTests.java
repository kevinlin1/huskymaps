package deques;

/**
 * Tests for the {@link ArrayListDeque} class.
 */
public class ArrayListDequeTests extends DequeTests {
    @Override
    public <E> Deque<E> createDeque() {
        return new ArrayListDeque<>();
    }
}
