package minpq;

import java.util.Objects;

/**
 * Represents the item-priority pair for use in {@link ExtrinsicMinPQ} implementations.
 *
 * @param <T> the type of element represented by this node.
 * @see ExtrinsicMinPQ
 */
class PriorityNode<T> {
    private final T item;
    private double priority;

    /**
     * Constructs a pair with the given item and priority.
     *
     * @param item     the item in this pair.
     * @param priority the priority value associated with the item.
     */
    PriorityNode(T item, double priority) {
        this.item = item;
        this.priority = priority;
    }

    /**
     * Returns the item.
     *
     * @return the item.
     */
    T item() {
        return item;
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
        PriorityNode other = (PriorityNode) o;
        return Objects.equals(this.item, other.item);
    }

    @Override
    public int hashCode() {
        return item.hashCode();
    }

    @Override
    public String toString() {
        return item + " (" + priority + ')';
    }
}
