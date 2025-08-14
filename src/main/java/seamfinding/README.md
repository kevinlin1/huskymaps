# Shortest Paths

In addition to implementing navigation directions in Husky Maps, **shortest paths** is also useful for **seam carving**: a technique for _image resizing_ where the size of an image is reduced by one pixel in height (by removing a horizontal seam) or width (by removing a vertical seam) at a time. Rather than cropping pixels from the edges or scaling the entire image, seam carving is considered _content-aware image resizing_ because it attempts to identify and preserve the most important content in an image.

[![Seam Carving for Content-Aware Image Resizing](https://faculty.runi.ac.il/arik/scweb/imret/teaser.jpg)](https://faculty.runi.ac.il/arik/scweb/imret/)

## Purpose

In this project, we will compare 2 graph representations, 2 graph algorithms, and 1 dynamic programming algorithm for seam carving. By the end of this project, students will be able to:

- **Design and implement** graph representations and algorithms for seam finding.
- **Analyze and compare** implementation runtime and adaptibility to new metrics.

## Seam finding interfaces

<video controls src="https://faculty.runi.ac.il/arik/scweb/imret/IMRet-All.mov" title="Seam Carving for Content-Aware Image Resizing"></video>

The `SeamCarver` class depends on algorithms that can find a least-noticeable horizontal or vertical seam. The interfaces for **seam finding** are defined in the [`src/main/java/seamfinding`](../seamfinding/) folder.

<dl>
<dt><code>SeamFinder</code></dt><dd>

An interface specifying a single method, `findHorizontal`, for finding a least-noticeable horizontal seam in a given `Picture` according to the `EnergyFunction`. The horizontal seam is returned as a list of integer indices representing the _y_-value (vertical) pixel indices for each pixel in the width of the picture.
</dd>

<dt><code>Picture</code></dt><dd>

A class representing a digital image where the color of each pixel is an `int`. In image processing, pixel _(x, y)_ refers to the pixel in **column _x_ and row _y_** where pixel _(0, 0)_ is the upper-left corner and the lower-right corner is the pixel with the largest coordinates.

> [!caution]
> This is opposite to linear algebra, where _(i, j)_ is row _i_ column _j_ and _(0, 0)_ is the lower-left corner.
</dd>

<dt><code>EnergyFunction</code></dt><dd>

An interface specifying a single method, `apply`, for computing the importance of a given pixel in the picture. The higher the energy of a pixel, the more noticeable it is in the picture.
</dd>
</dl>

Seam finder implementations work by applying the `EnergyFunction` to each pixel in the given `Picture`. Then, we can use a shortest paths algorithm to find the least-noticeable horizontal seam from the left side of the picture to the right side of the picture.

## Graph interfaces

The **graph** interfaces and algorithms are defined in the [`src/main/java/graphs`](../graphs/) folder.

<dl>
<dt><code>Graph</code></dt><dd>

An interface representing a directed weighted graph with a single method that returns a list of the `neighbors` of a given vertex. The directed `Edge` class provides 3 fields: the origin vertex `from`, the destination vertex `to`, and the edge `weight`.
</dd>

<dt><code>ShortestPathSolver</code></dt><dd>

An interface for finding a shortest paths tree in a `Graph`. Implementations of this interface must provide a public constructor that accepts two parameters: a `Graph` and a start vertex. The `solution` method returns the list of vertices representing a shortest path from the start vertex to the given `goal` vertex.

> [!note]
> The generic type `V` is used throughout the `graphs` package to indicate the vertex data type. For seam carving, all vertices will be of the interface type `Node` (described below).
</dd>

## Reference implementation

[`AdjacencyListSeamFinder.java`](../seamfinding/AdjacencyListSeamFinder.java) implements `SeamFinder` by building an _adjacency list graph representation_ of the picture and then running a _single-source shortest paths algorithm_ to find a lowest-cost horizontal seam.

```java
public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
    PixelGraph graph = new PixelGraph(picture, f);
    List<Node> seam = sps.run(graph, graph.source).solution(graph.sink);
    seam = seam.subList(1, seam.size() - 1); // Skip the source and sink
    List<Integer> result = new ArrayList<>(seam.size());
    for (Node node : seam) {
        // All remaining nodes must be Pixels
        PixelGraph.Pixel pixel = (PixelGraph.Pixel) node;
        result.add(pixel.y);
    }
    return result;
}
```

Given a `Picture` and an `EnergyFunction` that defines the way that we want to measure the importance of each pixel:

1. `new PixelGraph(picture, f)` creates a `PixelGraph` where each vertex represents a pixel and each edge represents the energy cost for the neighboring pixel. The `PixelGraph` constructor creates a new `Pixel` (node) for each pixel in the image. It also creates a `source` node and a `sink` node.

1. `sps.run(graph, graph.source).solution(graph.sink)` calls the `ShortestPathSolver` (such as `DijkstraSolver`) to find the shortest path in the `PixelGraph` from the `source` and immediately asks for a shortest path to the `sink`. The resulting solution is assigned to the variable `seam`.

1. `seam = seam.subList(1, seam.size() - 1)` reassigns the `seam` to exclude the `source` and `sink`, which we don't need in our final solution.

1. Finally, the `for` loop iterates through each remaining `Node` (which must represent a `Pixel`) and add its `y` index to the `result` list before returning the final `result`.

### Node interface

[`Node.java`](../graphs/Node.java) is an interface that adapts the `Graph.neighbors` method for use with different types of nodes in the `AdjacencyListSeamFinder`. This is helpful because not all nodes are the same. Although most nodes represent pixels that have `x` and `y` coordinates, the graph also contains `source` and `sink` nodes that don't have `x` or `y` coordinates! The `Node` interface allows these different types of nodes to co-exist in a single collection.

Inside the [`AdjacencyListSeamFinder.java`](../seamfinding/AdjacencyListSeamFinder.java) file is the `PixelGraph` class, which defines three types of nodes.

<dl>
<dt><code>source</code></dt><dd>

A field that implements `Node.neighbors` by returning a list of `picture.height()` outgoing edges to each `Pixel` in the first column of the picture. The weight for each outgoing edge represents the energy of the corresponding pixel in the leftmost column.
</dd>

<dt><code>Pixel</code></dt><dd>

An inner class representing an _(x, y)_ pixel in the picture with directed edges to its _right-up_, _right-middle_, and _right-down_ neighbors. Most pixels have 3 adjacent neighbors except for pixels at the boundary of the picture that only have 2 adjacent neighbors. The weight of an edge represents the energy of the neighboring `to`-side pixel.
</dd>

<dt><code>sink</code></dt><dd>

A field that implements `Node.neighbors` by returning an empty list representing a node with no outgoing edges. Each `Pixel` in the rightmost column of the picture has an outgoing edge to the `sink` with weight 0.
</dd>
</dl>

> [!note]
> The `source` and `sink` are defined using a feature in Java called [anonymous classes](https://docs.oracle.com/javase/tutorial/java/javaOO/anonymousclasses.html). Anonymous classes are great for objects that implement an interface but only need to be instantiated once.

## Design and implement

Design and implement 1 graph representation, 1 graph algorithm, and 1 dynamic programming algorithm for seam finding.

### [`seamfnding.GenerativeSeamFinder.java`](../seamfinding/GenerativeSeamFinder.java)

A **graph representation** that implements `SeamFinder` much like `AdjacencyListSeamFinder`, but rather than creating the neighbors for every node in the `PixelGraph` constructor this approach only creates vertices and edges when `Pixel.neighbors` is called.

1. Study the `AdjacencyListSeamFinder.PixelGraph` constructor, which constructs the entire graph and represents it as a `Pixel[][]`. Identify how the logic connects a given `Pixel` to its right-up, right-middle, and right-down neighbors.

1. Adapt the ideas to implement `GenerativeSeamFinder.PixelGraph.Pixel.neighbors`, which should return the list of neighbors for a given `Pixel`. Create a new `Pixel` for each neighbor.

1. Then, define the `source` and `sink` nodes.

> [!important]
> Walk through how you adapted the code from `AdjacencyListSeamFinder.PixelGraph` to implement `GenerativeSeamFinder.PixelGraph.Pixel.neighbors`. What are the key similarities and differences between these two implementations?

### [`graphs.shortestpaths.ToposortDAGSolver.java`](../graphs/shortestpaths/ToposortDAGSolver.java)

A **graph algorithm** that implements `ShortestPathSolver` using the topological sorting _directed acyclic graph_ (DAG) shortest paths algorithm.

1. Initialize `edgeTo` and `distTo` data structures just as in `DijkstraSolver`.

1. List all reachable vertices in **depth-first search postorder**. Then, `Collections.reverse` the list.

1. For each node in the reverse DFS postorder, relax each neighboring edge by updating the best-known `edgeTo` and `distTo` entries accordingly.

### [`seamfinding.DynamicProgrammingSeamFinder.java`](../seamfinding/DynamicProgrammingSeamFinder.java)

A **dynamic programming algorithm** that implements `SeamFinder`. The dynamic programming approach processes pixels in a topological order: start from the leftmost column and work rightward using the previous columns to help identify the best shortest paths. The difference is that dynamic programming does not create a graph representation (vertices and edges) nor does it use a graph algorithm.

How does dynamic programming solve the seam finding problem? We need to first generate a dynamic programming table storing the accumulated path costs, where each entry represents the total energy cost of the least-noticeable path from the left edge to the given pixel.

1. Initialize a 2-d `double[picture.width()][picture.height()]` array.

1. Fill out the leftmost column in the 2-d array with the energy for each pixel.

1. For each pixel in each of the remaining columns, determine the lowest-energy predecessor to the pixel: the minimum of its _left-up_, _left-middle_, and _left-down_ neighbors. Compute the cost to reach the current pixel by taking the cost to reach the lowest-energy predecessor and then adding on the energy for the current pixel.

After generating this table, we can use it to find the shortest path, or the seam with the lowest total energy cost.

1. Add the _y_ value of the least-noticeable pixel in the rightmost column to the result.

1. Follow the path back to the left by adding the _y_-value of each lowest-energy predecessor to the result.

1. Finally, to return the coordinates ordered from left to right, `Collections.reverse` the result.

> [!important]
> Walk through your implementation for `DynamicProgrammingSeamFinder`.
> 1. Discuss the code for filling-out the table with energy costs. How does it select the minimum from its neighbors? How does the code handle pixels at the edges of the image?
> 1. Discuss the code for finding the seam with the lowest total energy cost. How is the first minimum found in the rightmost column? Afterwards, how do we find the lowest-energy predecessors? What is ultimately returned from your `findHorizontal` method?

## Analyze and compare

### Experimental analysis

Run the provided `RuntimeExperiments` to compare the real-world runtime of each implementation. For each implementation, `RuntimeExperiments` constructs an empty instance and records the number of seconds to `findHorizontal` through randomly-generated square-sized pictures of increasing resolution.

- The first column denotes _N_, the image dimensions (resolution) in pixels.
- The second column denotes the average runtime for `findHorizontal` in seconds.

Copy-paste the text into plotting software such as [Desmos](https://www.desmos.com/calculator). Plot the runtimes of all 5 approaches on the same graph.

- `AdjacencyListSeamFinder(DijkstraSolver::new)`
- `AdjacencyListSeamFinder(ToposortDAGSolver::new)`
- `GenerativeSeamFinder(DijkstraSolver::new)`
- `GenerativeSeamFinder(ToposortDAGSolver::new)`
- `DynamicProgrammingSeamFinder()`

> [!important]
> Display a plot comparing runtimes across all 5 approaches. Which implementation is the fastest? Then, explain the patterns between the runtimes of all 4 approaches except for `DynamicProgrammingSeamFinder`. How do the two `SeamFinders` differ in computing `neighbors`? How do the two `ShortestPathSolvers` differ in the number of times they traverse the `PixelGraph`? Based on these properties, how might combinations of `SeamFinders` and `ShortestPathSolvers` cause certain pairings to be faster than others?

### Algorithm engineering

In our study of affordance analysis, we walked through how to implement a redesign for the Husky Maps search: instead of displaying results in order of [importance](https://nominatim.org/release-docs/latest/customize/Importance/), we learned how to redesign the app to display results in order of distance from the center of the map. All of the asymptotic, experimental, and affordance analysis we've done in this class are different forms of **algorithm engineering**.

Algorithm engineering can also include studying the architecture of a software system. Let's study the architecture behind shortest paths for navigation directions in Husky Maps.

> [!important]
> Starting from the call to `map.shortestPath(start, goal)` in `src/main/java/MapServer.java`, walk through the project code to show how the algorithm finds the shortest path from the `start` to the `goal` using `AStarSolver`, which is an optimization of Dijkstra's  algorithm for certain problems. In `MapGraph`, why do we need to pass `this` as an argument to `AStarSolver`? Why does the code call `closest`, and how long does it take to find the closest node? In `AStarSolver`, why does the `solution` method call `Collections.reverse(path)`?

How might we redesign Husky Maps to provide **accessible routing**, or navigation directions that prefer more accessible routes? [Project Sidewalk](https://sidewalk-sea.cs.washington.edu/api) has assigned almost all sidewalks in Seattle a decimal **access score** ranging between 0 (inaccessible) and 1 (accessible).

If each edge in the `MapGraph` stores its own access score, then we could define the edge's distance by dividing the real distance in the world by the access score so that access scores closer to 0 will incur larger distance penalties. This penalty could represent the difficulty of traversing that edge.

> [!important]
> Explain an alternative mathematical formula that could be used to implement accessible routing. Your formula may operate on individual edges in `MapGraph` and/or the routing formulas defined by `AStarSolver`. Then, explain what motivated you to define your formula that way. Finally, explain how you would handle the situation where a sidewalk does not have an access score.

### Optional: Implement accessible routing

Optionally, implement your ideas into the project using the [`src/main/resources/access.tsv`](../../resources/access.tsv) file, which associates each OSM ID (roadway identifier) with its access score.