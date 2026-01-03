package seamfinding;

import graphs.shortestpaths.ToposortDAGSolver;

/**
 * Tests for the {@link AdjacencyListSeamFinder} class using the {@link ToposortDAGSolver}.
 *
 * @see AdjacencyListSeamFinder
 * @see ToposortDAGSolver
 */
public class AdjacencyListSeamFinderToposortDAGSolverTests extends SeamFinderTests {
    @Override
    public SeamFinder createSeamFinder() {
        return new AdjacencyListSeamFinder(ToposortDAGSolver::new);
    }

    public static void main(String[] args) {
        runtimeExperiments(() -> new AdjacencyListSeamFinder(ToposortDAGSolver::new));
    }
}