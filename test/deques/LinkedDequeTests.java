package deques;

/**
 * Tests for the {@link LinkedDeque} class.
 *
 * @see LinkedDeque
 */
public class LinkedDequeTests extends DequeTests {
    @Override
    public <E> Deque<E> createDeque() {
        return new LinkedDeque<>();
    }

    // You may write additional tests here if you only want them to run for LinkedDeque
}
