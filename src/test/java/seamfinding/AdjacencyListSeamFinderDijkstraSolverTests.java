package seamfinding;

import graphs.shortestpaths.DijkstraSolver;

/**
 * Tests for the {@link AdjacencyListSeamFinder} class using the {@link DijkstraSolver}.
 *
 * @see AdjacencyListSeamFinder
 * @see DijkstraSolver
 */
public class AdjacencyListSeamFinderDijkstraSolverTests extends SeamFinderTests {
    @Override
    public SeamFinder createSeamFinder() {
        return new AdjacencyListSeamFinder(DijkstraSolver::new);
    }

    public static void main(String[] args) {
        runtimeExperiments(() -> new AdjacencyListSeamFinder(DijkstraSolver::new));
    }
}