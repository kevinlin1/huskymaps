package minpq.moderator;

import minpq.MinPQ;
import minpq.OptimizedHeapMinPQ;

/**
 * Tests for the {@link OptimizedHeapMinPQ} class.
 *
 * @see OptimizedHeapMinPQ
 */
public class OptimizedHeapMinPQTests extends MinPQTests {
    @Override
    public MinPQ<String> createMinPQ() {
        return new OptimizedHeapMinPQ<>();
    }
}
