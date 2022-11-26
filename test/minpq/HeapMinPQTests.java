package minpq;

/**
 * Tests for the {@link HeapMinPQ} class.
 *
 * @see HeapMinPQ
 */
public class HeapMinPQTests extends MinPQTests {
    @Override
    public MinPQ<String> createMinPQ() {
        return new HeapMinPQ<>();
    }
}
