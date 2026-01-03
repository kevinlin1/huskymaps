package seamfinding;

import graphs.shortestpaths.DijkstraSolver;

/**
 * Tests for the {@link GenerativeSeamFinder} class using the {@link DijkstraSolver}.
 *
 * @see GenerativeSeamFinder
 * @see DijkstraSolver
 */
public class GenerativeSeamFinderDijkstraSolverTests extends SeamFinderTests {
    @Override
    public SeamFinder createSeamFinder() {
        return new GenerativeSeamFinder(DijkstraSolver::new);
    }

    public static void main(String[] args) {
        runtimeExperiments(() -> new GenerativeSeamFinder(DijkstraSolver::new));
    }
}