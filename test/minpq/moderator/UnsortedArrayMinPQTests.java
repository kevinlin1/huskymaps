package minpq.moderator;

import minpq.ExtrinsicMinPQ;
import minpq.UnsortedArrayMinPQ;

/**
 * Tests for the {@link UnsortedArrayMinPQ} class.
 *
 * @see UnsortedArrayMinPQ
 */
public class UnsortedArrayMinPQTests extends ExtrinsicMinPQTests {
    @Override
    public ExtrinsicMinPQ<String> createExtrinsicMinPQ() {
        return new UnsortedArrayMinPQ<>();
    }
}
