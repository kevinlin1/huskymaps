package deques;

/**
 * A double-ended queue that allows addition, removal, and access to elements at either the front or
 * the back of the collection. The front of the deque contains the first element (aka "head") while
 * the back of the deque contains the last element (aka "tail"). Does not allow null elements, but
 * doesn't check for null elements.
 *
 * @param <E> the type of elements in this deque
 */
public interface Deque<E> {

    /**
     * Adds the given element to the front of this deque.
     *
     * @param element the element to add
     */
    void addFirst(E element);

    /**
     * Adds the given element to the back of this deque.
     *
     * @param element the element to add
     */
    void addLast(E element);

    /**
     * Gets the element at the given index, where 0 is the front, 1 is the next element, and so forth.
     * If no such element exists, returns null. Must not alter the deque!
     *
     * @param index the index to get
     * @return the element at the given index
     */
    E get(int index);

    /**
     * Returns true if and only if this deque is empty.
     *
     * @return true if and only if this deque is empty
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of elements in this deque.
     *
     * @return the number of elements in this deque
     */
    int size();

    /**
     * Removes and returns the element at the front of this deque. Returns null if the deque is empty.
     *
     * @return the element at the front of this deque, or null if the deque is empty
     */
    E removeFirst();

    /**
     * Removes and returns the element at the back of this deque. Returns null if the deque is empty.
     *
     * @return the element at the back of this deque, or null if the deque is empty
     */
    E removeLast();
}
