package graphs;

import java.util.Objects;

/**
 * Weighted, directed edge.
 *
 * @param <V> the type of vertices.
 * @see Graph
 * @see AStarGraph
 */
public class Edge<V> {
    /**
     * The originating vertex.
     */
    public final V from;
    /**
     * The destination vertex.
     */
    public final V to;
    /**
     * The weight of the edge.
     */
    public final double weight;

    /**
     * Constructs an edge from and to the given vertices and with the given weight.
     *
     * @param from   the originating vertex.
     * @param to     the destination vertex.
     * @param weight the weight of the edge.
     */
    public Edge(V from, V to, double weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Edge)) {
            return false;
        }
        Edge other = (Edge) o;
        return Objects.equals(this.from, other.from) && Objects.equals(this.to, other.to)
                && Double.compare(this.weight, other.weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, weight);
    }
}
