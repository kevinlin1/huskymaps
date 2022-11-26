package seamfinding;

import graphs.shortestpaths.DijkstraSolver;
import graphs.shortestpaths.ToposortDAGSolver;
import org.junit.jupiter.api.Nested;

/**
 * Tests for the {@link AdjacencyListSeamFinder} class.
 *
 * @see AdjacencyListSeamFinder
 */
public class AdjacencyListSeamFinderTests {

    /**
     * Tests using the {@link DijkstraSolver} implementation.
     */
    @Nested
    public class UsingDijkstraSolver extends SeamFinderTests {
        @Override
        public SeamFinder createSeamFinder() {
            return new AdjacencyListSeamFinder(DijkstraSolver::new);
        }
    }

    /**
     * Tests using the {@link ToposortDAGSolver} implementation.
     */
    @Nested
    public class UsingToposortDAGSolver extends SeamFinderTests {
        @Override
        public SeamFinder createSeamFinder() {
            return new AdjacencyListSeamFinder(ToposortDAGSolver::new);
        }
    }
}
