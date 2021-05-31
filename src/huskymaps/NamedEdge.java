package huskymaps;

import graphs.Edge;

/**
 * Named, weighted, directed edge, typically used to represent roadway names.
 *
 * @param <V> the type of vertices.
 * @see Edge
 * @see MapGraph
 */
public class NamedEdge<V> extends Edge<V> {
    /**
     * The name of the edge.
     */
    public final String name;

    /**
     * Constructs an edge from and to the given vertices with the given weight and name.
     *
     * @param from   the originating vertex.
     * @param to     the destination vertex.
     * @param weight the weight of the edge.
     * @param name   the name of the edge.
     */
    public NamedEdge(V from, V to, double weight, String name) {
        super(from, to, weight);
        this.name = name;
    }

    @Override
    public String toString() {
        return "NamedEdge{" +
                "from=" + from +
                ", to=" + to +
                ", weight=" + weight +
                ", name='" + name + '\'' +
                '}';
    }
}
