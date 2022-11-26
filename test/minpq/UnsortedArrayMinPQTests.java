package minpq;

/**
 * Tests for the {@link UnsortedArrayMinPQ} class.
 *
 * @see UnsortedArrayMinPQ
 */
public class UnsortedArrayMinPQTests extends MinPQTests {
    @Override
    public MinPQ<String> createMinPQ() {
        return new UnsortedArrayMinPQ<>();
    }
}
