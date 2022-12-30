package minpq;

import java.util.Objects;

/**
 * Represents the element-priority pair for use in {@link MinPQ} implementations.
 *
 * @param <E> the type of element represented by this node.
 * @see MinPQ
 */
class PriorityNode<E> {
    private final E element;
    private double priority;

    /**
     * Constructs a pair with the given element and priority.
     *
     * @param element  the element in this pair.
     * @param priority the priority value associated with the element.
     */
    PriorityNode(E element, double priority) {
        this.element = element;
        this.priority = priority;
    }

    /**
     * Returns the element.
     *
     * @return the element.
     */
    E element() {
        return element;
    }

    /**
     * Returns the priority value.
     *
     * @return the priority value.
     */
    double priority() {
        return priority;
    }

    /**
     * Reassigns the priority value for this pair.
     *
     * @param priority the priority value to be assigned.
     */
    void setPriority(double priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof PriorityNode)) {
            return false;
        }
        PriorityNode<?> other = (PriorityNode<?>) o;
        return Objects.equals(this.element, other.element);
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }

    @Override
    public String toString() {
        return element + " (" + priority + ')';
    }
}
