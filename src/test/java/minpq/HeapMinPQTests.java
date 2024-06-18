package minpq;

/**
 * Tests for the {@link HeapMinPQ} class.
 *
 * @see HeapMinPQ
 */
public class HeapMinPQTests extends MinPQTests {
    @Override
    public <E> MinPQ<E> createMinPQ() {
        return new HeapMinPQ<>();
    }
}
