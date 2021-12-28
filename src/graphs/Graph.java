package graphs;

import graphs.shortestpaths.ShortestPathSolver;

import java.util.List;

/**
 * Directed, edge-weighted graph.
 *
 * @param <V> the type of vertices.
 * @see Edge
 * @see ShortestPathSolver
 * @see AStarGraph
 */
@FunctionalInterface
public interface Graph<V> {

    /**
     * Returns a list of the outgoing edges from the given vertex.
     *
     * @param vertex the node of interest.
     * @return a list of the outgoing edges from the given vertex.
     */
    List<Edge<V>> neighbors(V vertex);
}
