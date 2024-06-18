package graphs.shortestpaths;

import graphs.Edge;
import graphs.Graph;

import java.util.*;

/**
 * Shortest Path Faster Algorithm implementation of the {@link ShortestPathSolver} interface.
 *
 * @param <V> the type of vertices.
 * @see ShortestPathSolver
 */
public class SPFASolver<V> implements ShortestPathSolver<V> {
    private final Map<V, Edge<V>> edgeTo;
    private final Map<V, Double> distTo;

    /**
     * Constructs a new instance by executing SPFA on the graph from the start.
     *
     * @param graph the input graph.
     * @param start the start vertex.
     */
    public SPFASolver(Graph<V> graph, V start) {
        edgeTo = new HashMap<>();
        distTo = new HashMap<>();
        Queue<V> perimeter = new OptimizedArrayQueueSet<>();
        perimeter.add(start);
        edgeTo.put(start, null);
        distTo.put(start, 0.0);
        while (!perimeter.isEmpty()) {
            V from = perimeter.remove();
            for (Edge<V> e : graph.neighbors(from)) {
                V to = e.to;
                double oldDist = distTo.getOrDefault(to, Double.POSITIVE_INFINITY);
                double newDist = distTo.get(from) + e.weight;
                if (newDist < oldDist) {
                    edgeTo.put(to, e);
                    distTo.put(to, newDist);
                    perimeter.add(to);
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

    /**
     * Optimized array queue implementation of the {@link Queue} interface.
     *
     * @param <E> the type of elements in this queue.
     */
    private static class OptimizedArrayQueueSet<E> extends AbstractQueue<E> {
        /**
         * {@link Queue} of elements.
         */
        private final Queue<E> queue;
        /**
         * {@link Set} of elements in this queue.
         */
        private final Set<E> set;

        public OptimizedArrayQueueSet() {
            queue = new ArrayDeque<>();
            set = new HashSet<>();
        }

        @Override
        public boolean contains(Object o) {
            return set.contains(o);
        }

        @Override
        public Iterator<E> iterator() {
            return queue.iterator();
        }

        @Override
        public boolean offer(E e) {
            if (set.contains(e)) {
                return false;
            }
            boolean result = queue.offer(e);
            set.add(e);
            return result;
        }

        @Override
        public E peek() {
            return queue.peek();
        }

        @Override
        public E poll() {
            E element = queue.poll();
            set.remove(element);
            return element;
        }

        @Override
        public int size() {
            return queue.size();
        }
    }
}
