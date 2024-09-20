package minpq;

/**
 * Tests for the {@link DoubleMapMinPQ} class.
 *
 * @see DoubleMapMinPQ
 */
public class DoubleMapMinPQTests extends MinPQTests {
    @Override
    public <E> MinPQ<E> createMinPQ() {
        return new DoubleMapMinPQ<>();
    }
}
