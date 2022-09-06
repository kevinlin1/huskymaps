package seamcarving.seamfinding;

import graphs.shortestpaths.DijkstraSolver;

/**
 * Tests for the {@link GenerativeSeamFinder} class.
 *
 * @see GenerativeSeamFinder
 */
public class GenerativeSeamFinderTests extends SeamFinderTests {
    @Override
    public SeamFinder createSeamFinder() {
        return new GenerativeSeamFinder(DijkstraSolver::new);
    }
}
