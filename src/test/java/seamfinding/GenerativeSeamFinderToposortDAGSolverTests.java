package seamfinding;

import graphs.shortestpaths.ToposortDAGSolver;

/**
 * Tests for the {@link GenerativeSeamFinder} class using the {@link ToposortDAGSolver}.
 *
 * @see GenerativeSeamFinder
 * @see ToposortDAGSolver
 */
public class GenerativeSeamFinderToposortDAGSolverTests extends SeamFinderTests {
    @Override
    public SeamFinder createSeamFinder() {
        return new GenerativeSeamFinder(ToposortDAGSolver::new);
    }

    public static void main(String[] args) {
        runtimeExperiments(() -> new GenerativeSeamFinder(ToposortDAGSolver::new));
    }
}