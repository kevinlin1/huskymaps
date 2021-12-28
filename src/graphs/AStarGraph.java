package graphs;

import graphs.shortestpaths.AStarSolver;

/**
 * Directed, edge-weighted graph with a heuristic function to estimate distances between vertices.
 *
 * @param <V> the type of vertices.
 * @see Graph
 * @see AStarSolver
 */
public interface AStarGraph<V> extends Graph<V> {
    /**
     * Returns an estimated distance from start to end.
     *
     * @param start the beginning vertex.
     * @param end   the destination vertex.
     * @return an estimated distance from start to end.
     */
    double estimatedDistance(V start, V end);
}
