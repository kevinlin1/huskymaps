package seamcarving.seamfinding;

import graphs.shortestpaths.ToposortDAGSolver;

/**
 * Tests for the {@link AdjacencyListSeamFinder} class.
 *
 * @see AdjacencyListSeamFinder
 */
public class AdjacencyListSeamFinderTests extends SeamFinderTests {
    @Override
    public SeamFinder createSeamFinder() {
        // Test the ToposortDAGSolver shortest paths solver
        return new AdjacencyListSeamFinder(ToposortDAGSolver::new);
    }
}
