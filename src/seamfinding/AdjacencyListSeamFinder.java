package seamfinding;

import graphs.Edge;
import graphs.Graph;
import graphs.shortestpaths.ShortestPathSolver;
import seamfinding.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Adjacency list graph single-source {@link ShortestPathSolver} implementation of the {@link SeamFinder} interface.
 *
 * @see Graph
 * @see ShortestPathSolver
 * @see SeamFinder
 */
public class AdjacencyListSeamFinder implements SeamFinder {
    /**
     * The constructor for the {@link ShortestPathSolver} implementation.
     */
    private final ShortestPathSolver.Constructor<Node> sps;

    /**
     * Constructs an instance with the given {@link ShortestPathSolver} implementation.
     *
     * @param sps the {@link ShortestPathSolver} implementation.
     */
    public AdjacencyListSeamFinder(ShortestPathSolver.Constructor<Node> sps) {
        this.sps = sps;
    }

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        PixelGraph graph = new PixelGraph(picture, f);
        List<Node> seam = sps.run(graph, graph.source).solution(graph.sink);
        seam = seam.subList(1, seam.size() - 1); // Skip the source and sink nodes
        List<Integer> result = new ArrayList<>(seam.size());
        for (Node node : seam) {
            // All remaining nodes must be Pixels
            PixelGraph.Pixel pixel = (PixelGraph.Pixel) node;
            result.add(pixel.y);
        }
        return result;
    }

    /**
     * Adjacency list graph of {@link Pixel} vertices and {@link EnergyFunction}-weighted edges.
     *
     * @see Pixel
     * @see EnergyFunction
     */
    private static class PixelGraph implements Graph<Node> {
        /**
         * The {@link Pixel} vertices in the {@link Picture}.
         */
        private final Pixel[][] pixels;
        /**
         * The {@link Picture} for {@link #neighbors(Node)}.
         */
        private final Picture picture;
        /**
         * The {@link Picture} for {@link #neighbors(Node)}.
         */
        private final EnergyFunction f;
        /**
         * Source {@link Node} for the adjacency list graph.
         */
        private final Node source = new Node() {
            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                List<Edge<Node>> result = new ArrayList<>(picture.height());
                for (int j = 0; j < picture.height(); j += 1) {
                    Pixel to = pixels[0][j];
                    result.add(new Edge<>(this, to, f.apply(picture, 0, j)));
                }
                return result;
            }
        };
        /**
         * Sink {@link Node} for the adjacency list graph.
         */
        private final Node sink = new Node() {
            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                return List.of(); // Sink has no neighbors
            }
        };

        /**
         * Constructs an adjacency list graph by materializing all vertices and edges.
         *
         * @param picture the input picture.
         * @param f       the input energy function.
         */
        private PixelGraph(Picture picture, EnergyFunction f) {
            this.pixels = new Pixel[picture.width()][picture.height()];
            // Starting from the rightmost column, each pixel has only a single edge to the sink (with 0 weight).
            for (int y = 0; y < picture.height(); y += 1) {
                Pixel from = new Pixel(picture.width() - 1, y);
                pixels[picture.width() - 1][y] = from;
                from.neighbors.add(new Edge<>(from, sink, 0));
            }
            // Starting from the next-rightmost column...
            for (int x = picture.width() - 2; x >= 0; x -= 1) {
                // Consider each pixel in the column...
                for (int y = 0; y < picture.height(); y += 1) {
                    Pixel from = new Pixel(x, y);
                    pixels[x][y] = from;
                    // Connect the pixel to its right-up, right-middle, and right-down neighbors...
                    for (int z = y - 1; z <= y + 1; z += 1) {
                        // Only if the neighbor is in the bounds of the picture.
                        if (0 <= z && z < picture.height()) {
                            Pixel to = pixels[x + 1][z];
                            from.neighbors.add(new Edge<>(from, to, f.apply(picture, x + 1, z)));
                        }
                    }
                }
            }
            this.picture = picture;
            this.f = f;
        }

        @Override
        public List<Edge<Node>> neighbors(Node node) {
            return node.neighbors(picture, f);
        }

        /**
         * A pixel in the {@link PixelGraph} representation of the {@link Picture} with {@link EnergyFunction}-weighted
         * edges to neighbors.
         *
         * @see PixelGraph
         * @see Picture
         * @see EnergyFunction
         */
        public class Pixel implements Node {
            private final int x;
            private final int y;
            private final List<Edge<Node>> neighbors;

            /**
             * Constructs a pixel representing the (<i>x</i>, <i>y</i>) indices in the picture with no neighbors.
             *
             * @param x horizontal index into the picture.
             * @param y vertical index into the picture.
             */
            public Pixel(int x, int y) {
                this.x = x;
                this.y = y;
                this.neighbors = new ArrayList<>(3);
            }

            @Override
            public List<Edge<Node>> neighbors(Picture picture, EnergyFunction f) {
                return neighbors;
            }

            @Override
            public String toString() {
                return "(" + x + ", " + y + ")";
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                } else if (!(o instanceof Pixel)) {
                    return false;
                }
                Pixel other = (Pixel) o;
                return this.x == other.x && this.y == other.y;
            }

            @Override
            public int hashCode() {
                return Objects.hash(x, y);
            }
        }
    }
}
