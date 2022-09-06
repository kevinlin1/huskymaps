package minpq.moderator;

import minpq.ExtrinsicMinPQ;
import minpq.HeapMinPQ;

/**
 * Tests for the {@link HeapMinPQ} class.
 *
 * @see HeapMinPQ
 */
public class HeapMinPQTests extends ExtrinsicMinPQTests {
    @Override
    public ExtrinsicMinPQ<String> createExtrinsicMinPQ() {
        return new HeapMinPQ<>();
    }
}
