# Priority Queues

**Priority queues** are a fundamental building block for many real-world algorithms. For instance, if we only need to show the first 10 autocomplete matches in a specific order, a priority queue can be more efficient than sorting the entire list of all matches. Or, if we're implementing a map navigation algorithm, a priority queue allows dynamic updates to the next-shortest path as the algorithm discovers new roads. Or, if we're designing a worldwide web analytics dashboard, a priority queue can track statistics like the accessibility of the most commonly-visited web pages.

## Purpose

In this project, we will compare 4 implementations of priority queues to build a web analytics dashboard for tracking the occurrence of [Web Content Accessibility Guideline][WCAG] recommendations. By the end of this project, students will be able to:

[WCAG]: https://www.w3.org/WAI/standards-guidelines/wcag/

- **Design and implement** multiple data structures to solve complex problems.
- **Analyze and compare** implementation runtimes and optimizations.

In the next project, we'll also use your priority queues as a building block for shortest paths.

## Priority queue interface

Review [`MinPQ.java`](MinPQ.java) to see the interface and its methods. Note that priority values are extrinsic to each element: they need to be specified as arguments to the `add` and `changePriority` methods. Furthermore, a `MinPQ` cannot contain duplicate elements. However, different elements can have the same priority value. In the case of duplicate priority values, `peekMin` and `removeMin` may return _any_ element with the minimum priority value.

<details>
<summary><strong>Why not implement our own <code>MinPQ</code> instead of Java's <code>PriorityQueue</code> interface?</strong></summary>

The `java.util` standard library includes a binary heap [`PriorityQueue`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/PriorityQueue.html) class. Here are three reasons why we can't implement Java's `PriorityQueue` class in this project.

1. We can't implement `PriorityQueue` because it's a _class_, not an _interface_. Although the Java developers designed `Set`, `Map`, and `List` as interfaces, priority queues are so commonly associated with binary heaps that the Java developers decided to define `PriorityQueue` as a class instead.
1. By default, `PriorityQueue` uses elements' `compareTo` method to define the priority of an element. For example, the priority of the string "A" is less than "B" because "A" precedes "B" in the alphabet. This makes `PriorityQueue` great for sorting objects, but not great for other applications where the priority value is not intrinsic to the object.
1. The fact that `PriorityQueue` uses elements' `compareTo` methods reveals a hidden _disaffordance_: to change the priority of an object in a `PriorityQueue`, we need to remove the element from the priority queue and then re-insert it. This workaround is not only inconvenient, but also inefficient and potentially tricky to debug if used wrongly.
</details>

### Example usage

After adding the numbers 1 to 6 (inclusive) to a `MinPQ`, we can change the priority of the element 3 to 0 and the priority of the element 1 to 7. If we then repeatedly call `removeMin`, the order of elements that appear will be [3, 2, 4, 5, 6, 1].

```java
@Test
void simpleTest() {
    MinPQ<String> reference = new DoubleMapMinPQ<>();
    MinPQ<String> testing = createMinPQ();
    for (int i = 1; i < 7; i += 1) {
        reference.add("" + i, i);
        testing.add("" + i, i);
    }

    reference.changePriority("3", 0.);
    testing.changePriority("3", 0.);

    reference.changePriority("1", 7.);
    testing.changePriority("1", 7.);

    while (!reference.isEmpty()) {
        assertEquals(reference.removeMin(), testing.removeMin());
    }
    assertTrue(testing.isEmpty());
}
```

> [!important]
> Add this `simpleTest` example to your `MinPQTests` as you will need to use it later. Write additional test cases to assist in debugging.

### Priority nodes

Java collections typically only specify a single data type, such as `ArrayList<String>`. But a `MinPQ` needs to keep track of each element as well as its associated priority value. We could do this by creating two lists: an `ArrayList<T>` for elements and an `ArrayList<Double>` for each element's priority value. However, this approach requires us to synchronize the state of both lists, which introduces additional complexity that makes the code harder to maintain and more susceptible to bugs.

The `PriorityNode` class addresses this problem by defining a data type that holds an `element` alongside its `priority` value.

```java
List<PriorityNode<String>> elements = new ArrayList<>();
elements.add(new PriorityNode<>("example", 0));
```

> [!caution]
> Two `PriorityNode` objects are considered equal if and only if their elements are equal. Priority values are not checked for equality.

<details>
<summary><strong>How will this property of <code>PriorityNode</code> equality help you implement <code>MinPQ</code>?</strong></summary>

`MinPQ` does not allow duplicate elements, but does allow duplicate priority values. When using Java collections such as a `List`, methods like `List.contains` or `List.remove` will call the objects' `equals` method to check for equality.

The following `contains` call will return `true`, and the `remove` call will successfully remove the priority node even though their priority values are different.

```java
elements.contains(new PriorityNode<>("example", 1));
elements.remove(new PriorityNode<>("example", 2));
```
</details>

### Reference implementation

The project code includes a working `DoubleMapMinPQ`. This implementation is called "double map" because it combines the runtime benefits of two maps to solve the problem efficiently. The two maps work together to help achieve **sublinear** (logarithmic or better) runtime for every operation.

1. The `NavigableMap<Double, Set<E>> priorityToElement` associates each unique priority value to all the elements with that priority value. Returning a minimum-priority element involves finding the set of minimum-priority elements and picking any element from that set.
1. The `Map<E, Double> elementToPriority` associates each element with its priority value in order to speed-up `contains` and `changePriority`.

The state of both maps is synchronized across all methods. Any change to one data structure also requires a change to the other data structure.

## Complete the report analyzer

In `src/main/java/ReportAnalyzer.java`, complete the `main` method to display the top 3 most commonly-reported [Web Content Accessibility Guideline][WCAG] (WCAG) tags by using a `DoubleMapMinPQ` to count the unique `wcagTags`. When displaying the most commonly-reported tags, use the `wcagDefinitions` to convert the tag identifiers to their descriptions.

> [!important]
> Which WCAG descriptions were the top 3 most commonly-reported for the given `data/reports`? Explain how you turned a `MaxPQ` problem into a `MinPQ` problem.

## Design and implement

Design and implement 3 implementations of the `MinPQ` interface.

### UnsortedArrayMinPQ

Elements are added to an `ArrayList` in any order. Many operations will need to scan over the entire list.

### HeapMinPQ

A standard binary heap priority queue that delegates all method calls to an instance of [`java.util.PriorityQueue`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/PriorityQueue.html). Each `MinPQ` operation is implemented by calling the underlying `PriorityQueue`. Our solution only adds 1–3 lines of code for most methods.

> [!important]
> Trace through the `simpleTest` with your `HeapMinPQ`.
> 1. First, without walking through the code, draw a diagram of the `PriorityQueue` representation _after_ adding all 6 elements.
> 1. Then, using the diagram, explain how your `changePriority` method changes the representation in response to elements 3 and 1. Justify why your approach works.
> 1. Finally, explain how the `reference` and `testing` implementations produce the same removal order.

### OptimizedHeapMinPQ

A optimized binary heap priority queue supported by a `HashMap` that associates each element with its array index to speed-up `contains` and `changePriority`. A standard binary heap array holds priority nodes organized by priority value. The `HashMap` acts as an address book for the indices of each priority node, helping us locate nodes within the heap.

1. Skim [MinPQ.java](https://github.com/kevin-wayne/algs4/blob/master/src/main/java/edu/princeton/cs/algs4/MinPQ.java)—unfortunately, this class shares the same name as our `MinPQ` interface. What do you notice will work for our `MinPQ` interface? What needs to change? At what index is the first element found? Accordingly, what indexing calculations are being used? How does this connect back to your understanding of binary min-heaps?
1. Identify methods in the `MinPQ` class that are most similar to our `MinPQ` interface. For each corresponding method, pay close attention to the helper methods.
1. Adapt the code to implement our interface without the `HashMap` optimization. **Make sure all tests pass before proceeding.** Descriptive variable names are crucial! The reference class can be difficult to parse from its shorthand variable-naming: you should choose better names for your own implementation.
1. Optimize the implementation by adding a `HashMap` synchronized to the state of the elements in the array and use it to speed-up `contains` and `changePriority`. Any operation that changes `elements` necessitates a corresponding change to `elementsToIndex`.

> [!important]
> Trace through the `simpleTest` with your `OptimizedHeapMinPQ`.
> 1. First, without walking through the code, draw a diagram of the `PriorityQueue` representation _after_ adding all 6 elements.
> 1. Then, using the diagram, explain how your `changePriority` method changes the representation in response to elements 3 and 1. Justify why your approach works.
> 1. Finally, explain how the `reference` and `testing` implementations produce the same removal order.

### Optional: Randomized testing

Optionally, write a randomized test in `MinPQTests` that simulates a large-scale version of the `ReportAnalyzer`. Read the `data/wcag.tsv` file and use randomness to sample WCAG tags until about 10000 tags have been counted by the reference and testing implementations. Then, remove all the tags from both implementations and check that the remove orders are consistent. Use the provided `randomIntegersRandomPriorities` method and the `ReportAnalyzer` that you implemented earlier to aid in writing your random test with WCAG tags. Your random test will likely be simpler than the sample `randomIntegersRandomPriorities` method.

After you have a functioning random test, modify it to better simulate our `data/reports` sample by upweighting the occurrence of the top 3 most commonly-reported tags.

## Analyze and compare

### Asymptotic analysis

> [!important]
> Give a big-theta bound for the worst-case runtime of the `removeMin` and `changePriority` methods for each of the 4 implementations, including `DoubleMapMinPQ`, with respect to _N_, the size of the priority queue. Explain the worst case and the runtime of each implementation in a couple sentences while referencing the code.

For all array-backed data structures, ignore the time it would take to resize the array.

The [`java.util.PriorityQueue`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/PriorityQueue.html) documentation includes an implementation note:
> this implementation provides O(log(n)) time for the enqueuing and dequeuing methods (`offer`, `poll`, `remove()` and `add`); linear time for the `remove(Object)` and `contains(Object)` methods; and constant time for the retrieval methods (`peek`, `element`, and `size`).

`HashSet` and `HashMap` are implemented using resizing separate-chaining hash tables where the number of buckets is always similar to the size. Assume the hash function evenly distributes elements across the buckets in the underlying array.

### Tree bucket optimization

`HashSet` and `HashMap` contain a further optimization. When the size of a bucket exceeds 8 elements, the separate chain is automatically converted from a linked list to a red-black tree ordered primarily by `hashCode`.[^1]

[^1]: https://github.com/adoptium/jdk17u/blob/jdk-17%2B35/src/java.base/share/classes/java/util/HashMap.java#L147-L181

> [!important]
> Explain the impact of tree bucket optimization assuming an even distribution of elements across the underlying array. Does the tree bucket optimization help, hurt, or not affect the asymptotic analysis given our prior assumptions?