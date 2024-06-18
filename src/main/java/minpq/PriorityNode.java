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

    public PriorityNode(E element, double priority) {
        this.element = element;
        this.priority = priority;
    }

    public E getElement() {
        return element;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "PriorityNode{" +
                "element=" + element +
                ", priority=" + priority +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriorityNode<?> that = (PriorityNode<?>) o;
        return element.equals(that.element);
    }

    @Override
    public int hashCode() {
        return Objects.hash(element, priority); // not guaranteed to work in a hash table
    }
}
