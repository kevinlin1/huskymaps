# Autocomplete

**Autocomplete** is a feature that helps a user select valid search results by showing possible inputs as they type. For example, in a map app, autocomplete might allow the user to enter a prefix such as _Sea_ and automatically suggest city names such as _Seattle_.

In addition to autocompleting names, places, or things, autocomplete can also be a useful abstraction for implementing DNA subsequence search. Instead of indexing a list of all the city names or places, a DNA data structure can index all the suffixes of a very long DNA sequence. For example, given the DNA string ATTGCAGTCCG, the sorted _suffix array_ data structure is given by the following table.

| Index | Suffix      |
|------:|-------------|
| 5     | AGTCCG      |
| 0     | ATTGCAGTCCG |
| 4     | CAGTCCG     |
| 8     | CCG         |
| 9     | CG          |
| 10    | G           |
| 3     | GCAGTCCG    |
| 6     | GTCCG       |
| 7     | TCCG        |
| 2     | TGCAGTCCG   |
| 1     | TTGCAGTCCG  |

Autocompleting with this suffix array enables efficient search across the entire DNA string!

## Purpose

In this project, we'll compare 4 implementations and 2 applications of autocomplete. By the end of this project, students will be able to:

- **Design and implement** tree-based and array-based search data structures.
- **Analyze and compare** runtimes using asymptotic and experimental analysis.

## Autocomplete interface

Review [`Autocomplete.java`](Autocomplete.java) to see the interface and its methods.

<details>
<summary><strong>What can implementations do when behavior is not defined?</strong></summary>

When behavior is not defined by the interface, implementations can do anything, including throwing exceptions or otherwise causing an error.
</details>

### Collections and generic types

[`Collection`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Collection.html) is the parent interface to lists and sets in Java. Using `Collection` rather than `List` lets clients use _any_ list or set or other collection that they've already created in their program.

<details>
<summary><strong>Why does the <code>addAll</code> method take a <code>Collection&lt;? extends CharSequence&gt;</code> rather than a <code>Collection&lt;String&gt;</code>?</strong></summary>

`Collection<? extends CharSequence>` reads, "a `Collection` of any type of elements that extend `CharSequence`." The `? extends` lets clients call the method with a `Collection<String>` or a `Collection<SuffixSequence>` instead of having to strictly use a `Collection<CharSequence>`.
</details>

<details>
<summary><strong>Why does the <code>allMatches</code> method take a <code>CharSequence</code> rather than a <code>? extends CharSequence</code>?</strong></summary>

`addAll` takes a `Collection<? extends CharSequence>` so that clients can call the method with a `Collection<String>`, for example. `allMatches` takes a `CharSequence`, but since `String` extends `CharSequence`, there is no need for the `? extends` keyword. This difference is more of a limitation with how Java inheritance works with generics.
</details>

### Example usage

Given the terms [alpha, delta, do, cats, dodgy, pilot, dog], `allMatches("do")` should return [do, dodgy, dog] in any order.

```java
@Test
void simpleTest() {
    List<CharSequence> terms = List.of(
        "alpha", "delta", "do", "cats", "dodgy", "pilot", "dog"
    );
    CharSequence prefix = "do";
    List<CharSequence> expected = List.of("do", "dodgy", "dog");

    Autocomplete testing = createAutocomplete();
    testing.addAll(terms);
    List<CharSequence> actual = testing.allMatches(prefix);
    assertEquals(expected.size(), actual.size());
    assertTrue(expected.containsAll(actual));
    assertTrue(actual.containsAll(expected));
}
```

> [!important]
> Add this `simpleTest` example to your [`AutocompleteTests.java`](../../../test/java/autocomplete/AutocompleteTests.java) as you will need to use it later. Write additional test cases to assist in debugging.

<details>
<summary><strong>Why does this test make three separate assertions rather than <code>assertEquals(expected, actual)</code>?</strong></summary>

Since `expected` and `actual` are `List` types, their order matters. However, our definition for the `Autocomplete` interface allows elements to be returned in any order. Therefore, our tests need to manually ensure that the returned elements are the same without considering order. When `assertEquals` is called on `List` objects, the `List.equals` method will be used, which is sensitive to element order.
</details>

### Reference implementation

The project code includes a fully functional `TreeSetAutocomplete` implementation that stores all the terms in a `TreeSet`. The class contains a single field for storing the underlying `TreeSet` of terms. Rather than declare the field as a `Set`, we've chosen to use the more specialized subtype `NavigableSet` because it includes helpful methods that can be used to find the first term that matches the prefix.

```java
private final NavigableSet<CharSequence> elements;
```

The constructor assigns a new `TreeSet` collection to this field. In Java, `TreeSet` is implemented using a red-black tree, a type of balanced search tree where access to individual elements are worst-case logarithmic time with respect to the size of the set. `CharSequence::compare` tells the `TreeSet` to use the natural dictionary order when comparing any two elements.[^1]

[^1]: https://bugs.openjdk.org/browse/JDK-8137326?focusedCommentId=13889936&page=com.atlassian.jira.plugin.system.issuetabpanels%3Acomment-tabpanel#comment-13889936

```java
public TreeSetAutocomplete() {
    elements = new TreeSet<>(CharSequence::compare);
}
```

The `addAll` method calls [`TreeSet.addAll`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/TreeSet.html#addAll(java.util.Collection)) to add all the terms to the underlying `TreeSet`.

```java
@Override
public void addAll(Collection<? extends CharSequence> terms) {
    elements.addAll(terms);
}
```

The `allMatches` method is more complicated:

```java
@Override
public List<CharSequence> allMatches(CharSequence prefix) {
    List<CharSequence> result = new ArrayList<>();
    if (prefix == null || prefix.length() == 0) {
        return result;
    }
    CharSequence start = elements.ceiling(prefix);
    if (start == null) {
        return result;
    }
    for (CharSequence term : elements.tailSet(start)) {
        if (Autocomplete.isPrefixOf(prefix, term)) {
            result.add(term);
        } else {
            return result;
        }
    }
    return result;
}
```

1. Ensures the prefix is valid: if the prefix is `null` or empty, return an empty list.

1. Finds the first matching term by calling [`TreeSet.ceiling`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/TreeSet.html#ceiling(E)), which returns "the least element in this set greater than or equal to the given element, or `null` if there is no such element."

1. Collects the remaining matching terms by iterating over the [`TreeSet.tailSet`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/TreeSet.html#tailSet(E)), which is "a view of the portion of this set whose elements are greater than or equal to `fromElement`." If a term no longer matches the `prefix`, return the results.

> [!tip]
> In Java, a **view** is a clever way of working with a part of a data structure without making a copy of it. For example, the `ArrayList` class has a [`subList`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/ArrayList.html#subList(int,int)) method with the following method signature.
>
> ```java
> public List<E> subList(int fromIndex, int toIndex)
> ```
>
> `subList` returns another `List`. But instead of constructing a new `ArrayList` and copying over all the elements from the `fromIndex` to the `toIndex`, the Java developers defined a `SubList` class that provides a _view_ of the data structure using the following fields (some details omitted).
>
> ```java
> private static class SubList<E> implements List<E> {
>     private final ArrayList<E> root;
>     private final int offset;
>     private int size;
> }
> ```
>
> The `SubList` class keeps track of its `ArrayList root` (which contains the actual data array), an `int offset` representing the start of the sublist, and the `int size` of the sublist. The sublist implements `get(index)` by checking that the index is in the sublist before returning the offset index.
>
> ```java
> public E get(int index) {
>     if (index < 0 || index >= size) {
>         throw new IndexOutOfBoundsException();
>     }
>     return root.elementData.get(offset + index);
> }
> ```

<details>
<summary><strong>Where is the <code>isPrefixOf</code> method defined?</strong></summary>

The `isPrefixOf` method is defined in the `Autocomplete` interface.
</details>

## Design and implement

Design and implement 3 implementations of the `Autocomplete` interface.

### [`SequentialSearchAutocomplete.java`](SequentialSearchAutocomplete.java)

Terms are added to an `ArrayList` in any order.

Since terms are not stored in any particular location, the `allMatches` method must scan across the entire list and check every term to see if it matches the `prefix`.

> [!note]
> Remember to stage, commit, and push your code to GitLab!

### [`BinarySearchAutocomplete.java`](BinarySearchAutocomplete.java)

Terms are added to a sorted `ArrayList`. When additional terms are added, the entire list is re-sorted using [`Collections.sort`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Collections.html#sort(java.util.List,java.util.Comparator)).

Since terms are stored according to their natural dictionary order, all matches must be located in a contiguous sublist. [`Collections.binarySearch`](https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Collections.html#binarySearch(java.util.List,T,java.util.Comparator)) can find the start index for the first match. After the first match is found, all remaining matching terms can be collected by iterating through the list until the term no longer matches the prefix.

The `binarySearch` method can return negative numbers to indicate that there is not an exact match for a given search term.

> **Returns:** the index of the search key, if it is contained in the list; otherwise, `(-(insertion point) - 1)`. The _insertion point_ is defined as the point at which the key would be inserted into the list: the index of the first element greater than the key, or `list.size()` if all elements in the list are less than the specified key. Note that this guarantees that the return value will be `>= 0` if and only if the key is found.

For example, calling `binarySearch("bay")` on the sorted list [alpha, cats, delta, do] will return -2 to indicate that the desired _insertion point_ for "bay" is index 1.

```java
List<CharSequence> elements = new ArrayList<>();
elements.add("alpha");
elements.add("delta");
elements.add("do");
elements.add("cats");
Collections.sort(elements, CharSequence::compare);
System.out.println("sorted: " + elements);

CharSequence prefix = "bay";
System.out.println("prefix: " + prefix);
int i = Collections.binarySearch(elements, prefix, CharSequence::compare);
System.out.println("     i: " + i);
```

> [!important]
> Trace through the `simpleTest` with your `BinarySearchAutocomplete`.
> 1. First, without walking through the code, draw a diagram of the `ArrayList` representation _after_ calling `testing.addAll(terms)`.
> 1. Then, using the diagram, explain how your `allMatches` method collects terms into the `actual` (result) list.
> 1. Finally, explain how the `actual` and `expected` lists compare for each of the 3 assertion tests.

### [`TernarySearchTreeAutocomplete.java`](TernarySearchTreeAutocomplete.java)

Terms are added to a ternary search tree.

1. Skim [TST.java](https://github.com/kevin-wayne/algs4/blob/master/src/main/java/edu/princeton/cs/algs4/TST.java). How does this align with your conceptual understanding of TSTs? How is data stored? What do you notice will work for `Autocomplete`? What needs to change?

1. Identify methods in the `TST` class that are most similar to `Autocomplete`.

1. Adapt the code to implement the `Autocomplete` interface. Descriptive variable names are crucial! The reference class can be difficult to parse from its shorthand variable names: you should choose better names for your own implementation.

> [!tip]
> Don't copy and paste code! Most of the time, changes need to be made and subtle bugs could be introduced when code is copied directly. Instead, rewrite the code after making sense of its logic and adapting it so that it is more suitable for our problem.

It's okay if your `TernarySearchTreeAutocomplete` throws a `StackOverflowError` when running the `DNASearch` class. This is caused by Java's built-in limit on recursive depth and won't be tested.

> [!important]
> Trace through the `simpleTest` with your `TernarySearchTreeAutocomplete`.
> 1. First, without walking through the code, draw a diagram of the `ArrayList` representation _after_ calling `testing.addAll(terms)`.
> 1. Then, using the diagram, explain how your `allMatches` method collects terms into the `actual` (result) list.
> 1. Finally, explain how the `actual` and `expected` lists compare for each of the 3 assertion tests.

### Optional: `TrieAutocomplete`

Optionally, create your own implementation and test for a `TrieAutocomplete` class based on [TrieST.java](https://github.com/kevin-wayne/algs4/blob/master/src/main/java/edu/princeton/cs/algs4/TrieST.java).

## Analyze and compare

### Asymptotic analysis

> [!important]
> Give a big-theta bound for the worst-case runtime of the `addAll` and `allMatches` methods for each of the 4 implementations, including `TreeSetAutocomplete`, with respect to _N_, the total number of terms already stored in the data structure. Explain the runtime of each implementation in a couple sentences while referencing the code.

For `addAll`, assume a constant number of terms are added to a dataset that already contains _N_ terms and that arrays can accommodate all the new terms without resizing. `Collections.sort` uses Timsort, an optimized version of merge sort with runtime in [O(_N_ log _N_)](https://drops.dagstuhl.de/opus/volltexte/2018/9467/) where _N_ is the size of the collection.

For `allMatches`, consider how the relationship between the added terms and the prefix can affect your worst case analysis. Assume all strings have constant length. `TreeSet` is implemented using a red-black tree, which has the same asymptotic runtime as a left-leaning red-black tree or a 2-3 tree.

### Experimental analysis

Now that you've predicted runtimes across the 4 implementations, let's compare results for the `cities.tsv` dataset. `RuntimeExperiments` are disabled by default: remove the `@Disabled` line above the `RuntimeExperiments` class header to enable it.

Run the provided `RuntimeExperiments` in [`AutocompleteTests.java`](../../../test/java/autocomplete/AutocompleteTests.java) to compare the real-world runtime of each implementation. For each implementation, `RuntimeExperiments` constructs an empty instance and records the number of seconds to add _N_ terms to the dataset before computing all matches for the given `prefix`. The output is printed-out in comma-separated values format representing a table with three columns:

- The first column denotes _N_, the total number of terms.
- The second column denotes the average runtime for `addAll` in seconds.
- The third column denotes the average runtime for `allMatches` in seconds.

Copy-paste the text into plotting software such as [Desmos](https://www.desmos.com/calculator). Create one plot containing the runtimes for all 4 implementations on `addAll`, and then create a second plot containing the runtimes for all 4 implementations on `allMatches`. Ensure that your plots are legible by adding a label or legend to your plots.

> [!important]
> Display both plots for `addAll` and `allMatches` to compare runtimes across all 4 implementations. Which implementation is the fastest for `addAll`, and why? Which implementation is the slowest for `allMatches`, and why? What are the benefits and drawbacks for tree-based data structures?
>
> There are apparent disagreements between the runtimes you hypothesized in asymptotic analysis and the runtimes you observed in your experimental analysis. Explain how the settings for the `RuntimeExperiments` differs from the asymptotic analysis assumptions.