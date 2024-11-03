package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class OptimizedHeapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of element-priority pairs.
     */
    private final List<PriorityNode<E>> elements;
    /**
     * {@link Map} of each element to its associated index in the {@code elements} heap.
     */
    private final Map<E, Integer> elementsToIndex;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        elements = new ArrayList<>();
        elementsToIndex = new HashMap<>();
        elements.add(null);
    }

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public OptimizedHeapMinPQ(Map<E, Double> elementsAndPriorities) {
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public boolean contains(E element) {
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public double getPriority(E element) {
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int size() {
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
