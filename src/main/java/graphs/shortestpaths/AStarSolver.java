package graphs.shortestpaths;

import graphs.AStarGraph;
import graphs.Edge;
import minpq.DoubleMapMinPQ;
import minpq.MinPQ;

import java.util.*;

/**
 * A* search implementation for single-pair shortest paths in an {@link AStarGraph}.
 *
 * @param <V> the type of vertices.
 * @see AStarGraph
 */
public class AStarSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;
    private final V goal;

    /**
     * Constructs a new instance by executing A* search on the graph from the start to the goal.
     *
     * @param graph the input graph.
     * @param start the start vertex.
     * @param goal  the goal vertex.
     */
    public AStarSolver(AStarGraph<V> graph, V start, V goal) {
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();
        this.goal = goal;
        MinPQ<V> perimeter = new DoubleMapMinPQ<>();
        perimeter.add(start, 0.0);
        edgeTo.put(start, null);
        distTo.put(start, 0.0);
        while (!perimeter.isEmpty()) {
            V from = perimeter.removeMin();
            for (Edge<V> e : graph.neighbors(from)) {
                V to = e.to;
                double oldDist = distTo.getOrDefault(to, Double.POSITIVE_INFINITY);
                double newDist = distTo.get(from) + e.weight;
                if (newDist < oldDist) {
                    edgeTo.put(to, e);
                    distTo.put(to, newDist);
                    double priority = newDist + graph.estimatedDistance(to, goal);
                    perimeter.addOrChangePriority(to, priority);
                }
            }
        }
    }

    /**
     * Returns the single-pair shortest path from the stored start to the stored goal.
     *
     * @return a list of vertices representing the shortest path.
     */
    public List<V> solution() {
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
