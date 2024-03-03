package minpq;

import java.util.ArrayList;
import java.util.List;

/**
 * Priority queue where objects have <b>extrinsic priority</b>. Whereas {@link java.util.PriorityQueue} relies on
 * {@link Comparable} objects (or a {@link java.util.Comparator}), this interface requires priority values represented
 * using {@code double} values. Elements must be unique, but priority values do not need to be unique.
 *
 * @param <E> the type of elements in this priority queue.
 * @see DoubleMapMinPQ
 * @see UnsortedArrayMinPQ
 * @see HeapMinPQ
 * @see OptimizedHeapMinPQ
 */
public interface MinPQ<E> {

    /**
     * Adds an element with the given priority value.
     *
     * @param element  the element to add.
     * @param priority the priority value for the element.
     * @throws IllegalArgumentException if element is null or already present.
     */
    void add(E element, double priority);

    /**
     * Adds an element with the given priority value if it is not already present. Otherwise, updates the priority value
     * of the existing element.
     *
     * @param element  the element to add or update.
     * @param priority the priority value for the element.
     */
    default void addOrChangePriority(E element, double priority) {
        if (!contains(element)) {
            add(element, priority);
        } else {
            changePriority(element, priority);
        }
    }

    /**
     * Returns true if the given element is in this priority queue.
     *
     * @param element element to be checked for containment.
     * @return true if the given element is in this priority queue.
     */
    boolean contains(E element);

    /** Returns the priority value for the given element if it is present.
     *
     * @param element element to query.
     * @return the priority value for the given element.
     * @throws java.util.NoSuchElementException if element is not present.
     */
    double getPriority(E element);

    /**
     * Returns the element with the minimum priority value.
     *
     * @return the element with the minimum priority value.
     * @throws java.util.NoSuchElementException if this priority queue is empty.
     */
    E peekMin();

    /**
     * Returns and removes the element with the minimum priority value.
     *
     * @return the element with the minimum priority value.
     * @throws java.util.NoSuchElementException if this priority queue is empty.
     */
    E removeMin();

    /**
     * Returns and removes up to the given number of lowest-priority elements.
     *
     * @param numElements the desired number of lowest-priority elements to remove.
     * @return a list containing up to the given number of lowest-priority elements.
     */
    default List<E> removeMin(int numElements) {
        numElements = Math.min(numElements, size());
        List<E> result = new ArrayList<>(numElements);
        for (int i = 0; i < numElements; i += 1) {
            result.add(removeMin());
        }
        return result;
    }

    /**
     * Updates the given elements' associated priority value.
     *
     * @param element  the element whose associated priority value should be modified.
     * @param priority the updated priority value.
     * @throws java.util.NoSuchElementException if the element is not present.
     */
    void changePriority(E element, double priority);

    /**
     * Returns the number of elements in this priority queue.
     *
     * @return the number of elements in this priority queue.
     */
    int size();

    /**
     * Returns true if this priority queue contains no elements.
     *
     * @return true if this priority queue contains no elements.
     */
    default boolean isEmpty() {
        return size() == 0;
    }
}
