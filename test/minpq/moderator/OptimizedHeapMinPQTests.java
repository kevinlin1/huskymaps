package minpq.moderator;

import minpq.ExtrinsicMinPQ;
import minpq.OptimizedHeapMinPQ;

/**
 * Tests for the {@link OptimizedHeapMinPQ} class.
 *
 * @see OptimizedHeapMinPQ
 */
public class OptimizedHeapMinPQTests extends ExtrinsicMinPQTests {
    @Override
    public ExtrinsicMinPQ<String> createExtrinsicMinPQ() {
        return new OptimizedHeapMinPQ<>();
    }
}
