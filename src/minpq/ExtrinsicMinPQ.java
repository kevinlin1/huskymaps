package minpq;

/**
 * Priority queue where objects have <b>extrinsic priority</b>. While {@link java.util.PriorityQueue} relies on objects'
 * {@link Comparable} (or a {@link java.util.Comparator} object), this interface requires priority values represented as
 * {@code double}. Cannot contain duplicate or null items.
 *
 * @param <T> the type of elements in this priority queue.
 * @see DoubleMapMinPQ
 * @see UnsortedArrayMinPQ
 * @see HeapMinPQ
 * @see OptimizedHeapMinPQ
 */
public interface ExtrinsicMinPQ<T> {

    /**
     * Adds an item with the given priority value.
     *
     * @param item     the element to add.
     * @param priority the priority value for the item.
     * @throws IllegalArgumentException if item is null or already present.
     */
    void add(T item, double priority);

    /**
     * Returns true if the given item is in this priority queue.
     *
     * @param item element to be checked for containment.
     * @return true if the given item is in this priority queue.
     */
    boolean contains(T item);

    /**
     * Returns the item with the minimum priority value.
     *
     * @return the item with the minimum priority value.
     * @throws java.util.NoSuchElementException if this priority queue is empty.
     */
    T peekMin();

    /**
     * Returns and removes the item with the minimum priority value.
     *
     * @return the item with the minimum priority value.
     * @throws java.util.NoSuchElementException if this priority queue is empty.
     */
    T removeMin();

    /**
     * Updates the given items' associated priority value.
     *
     * @param item     the element whose associated priority value should be modified.
     * @param priority the updated priority value.
     * @throws java.util.NoSuchElementException if the item is not present.
     */
    void changePriority(T item, double priority);

    /**
     * Returns the number of items in this priority queue.
     *
     * @return the number of elements in this priority queue.
     */
    int size();

    /**
     * Returns true if this priority queue contains no items.
     *
     * @return true if this priority queue contains no items.
     */
    default boolean isEmpty() {
        return size() == 0;
    }
}
