package graphs.shortestpaths;

import graphs.Graph;

import java.util.List;

/**
 * Single-source shortest paths from a start vertex to all reachable vertices.
 *
 * @param <V> the type of vertices.
 * @see Constructor
 * @see Graph
 * @see DijkstraSolver
 * @see ToposortDAGSolver
 */
public interface ShortestPathSolver<V> {
    /**
     * Returns the single-pair shortest path from a start vertex to the goal.
     *
     * @param goal the goal vertex.
     * @return a list of vertices representing the shortest path.
     */
    List<V> solution(V goal);

    /**
     * Constructor for {@link ShortestPathSolver}.
     *
     * @param <V> the type of vertices.
     * @see ShortestPathSolver
     */
    @FunctionalInterface
    interface Constructor<V> {
        /**
         * Functional interface for running the constructor. Given an implementation of {@link ShortestPathSolver},
         * refer to its constructor as (for example) {@code ShortestPathSolver::new}.
         *
         * @param graph the input graph.
         * @param start the start vertex.
         * @return an instance of {@link ShortestPathSolver}.
         */
        ShortestPathSolver<V> run(Graph<V> graph, V start);
    }
}
