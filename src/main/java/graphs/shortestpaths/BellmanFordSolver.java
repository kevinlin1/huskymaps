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

        List<Edge<V>> allEdges = new ArrayList<>();
        int numVertices = 0;

        Queue<V> queue = new ArrayDeque<>();
        Set<V> visited = new HashSet<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            V from = queue.remove();
            numVertices++;
            for (Edge<V> e : graph.neighbors(from)) {
                allEdges.add(e);
                V to = e.to;
                if (visited.add(to)) {
                    queue.add(to);
                }
            }
        }

        for (int i = 1; i < numVertices; i += 1) {
            boolean changed = false;
            for (Edge<V> e : allEdges) {
                V from = e.from;
                V to = e.to;
                double oldDist = distTo.getOrDefault(to, Double.POSITIVE_INFINITY);
                double newDist = distTo.get(from) + e.weight;
                if (newDist < oldDist) {
                    edgeTo.put(to, e);
                    distTo.put(to, newDist);
                    changed = true;
                }
            }
            if (!changed) {
                break;
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
}
