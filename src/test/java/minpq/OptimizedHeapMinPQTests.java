package minpq;

/**
 * Tests for the {@link OptimizedHeapMinPQ} class.
 *
 * @see OptimizedHeapMinPQ
 */
public class OptimizedHeapMinPQTests extends MinPQTests {
    @Override
    public <E> MinPQ<E> createMinPQ() {
        return new OptimizedHeapMinPQ<>();
    }
}
