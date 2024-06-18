package minpq;

/**
 * Tests for the {@link UnsortedArrayMinPQ} class.
 *
 * @see UnsortedArrayMinPQ
 */
public class UnsortedArrayMinPQTests extends MinPQTests {
    @Override
    public <E> MinPQ<E> createMinPQ() {
        return new UnsortedArrayMinPQ<>();
    }
}
