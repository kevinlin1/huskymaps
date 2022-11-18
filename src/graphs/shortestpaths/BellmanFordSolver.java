package graphs.shortestpaths;

import graphs.Edge;
import graphs.Graph;

import java.util.*;

/**
 * Bellman-Ford algorithm implementation of the {@link ShortestPathSolver} interface.
 *
 * @param <V> the type of vertices.
 * @see ShortestPathSolver
 */
public class BellmanFordSolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;

    /**
     * Constructs a new instance by executing Bellman-Ford algorithm on the graph from the start.
     *
     * @param graph the input graph.
     * @param start the start vertex.
     */
    public BellmanFordSolver(Graph<V> graph, V start) {
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();
        edgeTo.put(start, null);
        distTo.put(start, 0.0);
        List<V> vertices = vertices(graph, start);
        for (int i = 1; i < vertices.size(); i += 1) {
            for (V from : vertices) {
                for (Edge<V> e : graph.neighbors(from)) {
                    V to = e.to;
                    double oldDist = distTo.getOrDefault(to, Double.POSITIVE_INFINITY);
                    double newDist = distTo.get(from) + e.weight;
                    if (newDist < oldDist) {
                        edgeTo.put(to, e);
                        distTo.put(to, newDist);
                    }
                }
            }
        }
    }

    @Override
    public List<V> solution(V goal) {
        List<V> path = new ArrayList<>();
        V curr = goal;
        path.add(curr);
        while (edgeTo.get(curr) != null) {
            curr = edgeTo.get(curr).from;
            path.add(curr);
        }
        Collections.reverse(path);
        return path;
    }

    private List<V> vertices(Graph<V> graph, V start) {
        List<V> result = new ArrayList<>();
        Queue<V> queue = new ArrayDeque<>();
        Set<V> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()) {
            V from = queue.remove();
            result.add(from);
            for (Edge<V> e : graph.neighbors(from)) {
                V to = e.to;
                if (!visited.contains(to)) {
                    queue.add(to);
                    visited.add(to);
                }
            }
        }
        return result;
    }
}
