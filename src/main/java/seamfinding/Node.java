package seamfinding;

import graphs.Edge;
import seamfinding.energy.EnergyFunction;

import java.util.List;

/**
 * A node in a graph representation of a picture. Most nodes will represent real pixels in the picture, but some nodes
 * can also represent concepts such as source nodes and sink nodes that don't represent real pixels.
 *
 * @see AdjacencyListSeamFinder
 * @see GenerativeSeamFinder
 */
public interface Node {
    /**
     * Returns the {@link List} of right-up, right-middle, and right-down neighbors (if they exist) for this node.
     *
     * @param picture the input picture.
     * @param f       the input energy function.
     * @return the {@link List} of right-up, right-middle, and right-down neighbors (if they exist) for this node.
     */
    List<Edge<Node>> neighbors(Picture picture, EnergyFunction f);
}
