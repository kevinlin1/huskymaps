package minpq;

import java.util.*;

/**
 * {@link TreeMap} and {@link HashMap} implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class DoubleMapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link NavigableMap} of priority values to all elements that share the same priority values.
     */
    private final NavigableMap<Double, Set<E>> priorityToElement;
    /**
     * {@link Map} of elements to their associated priority values.
     */
    private final Map<E, Double> elementToPriority;

    /**
     * Constructs an empty instance.
     */
    public DoubleMapMinPQ() {
        priorityToElement = new TreeMap<>();
        elementToPriority = new HashMap<>();
    }

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public DoubleMapMinPQ(Map<E, Double> elementsAndPriorities) {
        priorityToElement = new TreeMap<>();
        elementToPriority = new HashMap<>(elementsAndPriorities);
        for (Map.Entry<E, Double> entry : elementToPriority.entrySet()) {
            E element = entry.getKey();
            double priority = entry.getValue();
            if (!priorityToElement.containsKey(priority)) {
                priorityToElement.put(priority, new HashSet<>());
            }
            Set<E> elementsWithPriority = priorityToElement.get(priority);
            elementsWithPriority.add(element);
        }
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        if (!priorityToElement.containsKey(priority)) {
            priorityToElement.put(priority, new HashSet<>());
        }
        Set<E> elementsWithPriority = priorityToElement.get(priority);
        elementsWithPriority.add(element);
        elementToPriority.put(element, priority);
    }

    @Override
    public boolean contains(E element) {
        return elementToPriority.containsKey(element);
    }

    @Override
    public double getPriority(E element) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain element");
        }
        return elementToPriority.get(element);
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        double minPriority = priorityToElement.firstKey();
        Set<E> elementsWithMinPriority = priorityToElement.get(minPriority);
        return firstOf(elementsWithMinPriority);
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        double minPriority = priorityToElement.firstKey();
        Set<E> elementsWithMinPriority = priorityToElement.get(minPriority);
        E element = firstOf(elementsWithMinPriority);
        elementsWithMinPriority.remove(element);
        if (elementsWithMinPriority.isEmpty()) {
            priorityToElement.remove(minPriority);
        }
        elementToPriority.remove(element);
        return element;
    }

    @Override
    public void changePriority(E element, double priority) {
        if (!contains(element)) {
            throw new NoSuchElementException("PQ does not contain " + element);
        }
        double oldPriority = elementToPriority.get(element);
        if (priority != oldPriority) {
            Set<E> elementsWithOldPriority = priorityToElement.get(oldPriority);
            elementsWithOldPriority.remove(element);
            if (elementsWithOldPriority.isEmpty()) {
                priorityToElement.remove(oldPriority);
            }
            elementToPriority.remove(element);
            add(element, priority);
        }
    }

    @Override
    public int size() {
        return elementToPriority.size();
    }

    /**
     * Returns any one element from the given iterable.
     *
     * @param it the iterable of elements.
     * @return any one element from the given iterable.
     */
    private E firstOf(Iterable<E> it) {
        return it.iterator().next();
    }
}
