package deques;

/**
 * An array implementation of the {@link Deque} interface.
 *
 * @see Deque
 */
public class ArrayDeque<E> implements Deque<E> {
    /**
     * The initial capacity for the underlying array of elements.
     */
    private static final int INITIAL_CAPACITY = 8;
    /**
     * The underlying array of elements stored in this deque.
     */
    private E[] data;
    /**
     * The index for the next element to be inserted by addFirst.
     */
    private int front;
    /**
     * The index for the next element to be inserted by addLast.
     */
    private int back;
    /**
     * The number of elements in this deque.
     */
    private int size;

    /**
     * Constructs an empty deque.
     */
    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        data = (E[]) new Object[INITIAL_CAPACITY];
        front = 0;
        back = 1;
        size = 0;
    }

    @Override
    public void addFirst(E element) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[front] = element;
        front = decrement(front, data.length);
        size += 1;
    }

    @Override
    public void addLast(E element) {
        if (size == data.length) {
            resize(data.length * 2);
        }
        data[back] = element;
        back = increment(back, data.length);
        size += 1;
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            return null;
        }
        front = increment(front, data.length);
        E result = data[front];
        data[front] = null;
        size -= 1;
        if (needsDownsize()) {
            resize(data.length / 2);
        }
        return result;
    }

    @Override
    public E removeLast() {
        if (size == 0) {
            return null;
        }
        back = decrement(back, data.length);
        E result = data[back];
        data[back] = null;
        size -= 1;
        if (needsDownsize()) {
            // The logs narrow the error down to the resize method!
            resize(data.length / 2);
        }
        return result;
    }

    @Override
    public E get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        int place = front + 1 + index;
        return data[place % data.length];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        StringBuilder result = new StringBuilder();
        result.append('[');
        for (int i = 0; i < size; i += 1) {
            result.append(get(i));
            if (i < size - 1) {
                result.append(", ");
            }
        }
        result.append(']');
        return result.toString();
    }

    /**
     * Increments the given index i, wrapping back to 0 if it is equal to length - 1.
     *
     * @param i the given index
     * @param length the wrap limit
     * @return the incremented index
     */
    private static int increment(int i, int length) {
        if (i == length - 1) {
            return 0;
        }
        return i + 1;
    }

    /**
     * Decrements the given index i, wrapping back to length - 1 if it is equal to 0.
     *
     * @param i the given index
     * @param length the wrap limit
     * @return the decremented index
     */
    private static int decrement(int i, int length) {
        if (i == 0) {
            return length - 1;
        }
        return i - 1;
    }

    /**
     * Updates the length of the underlying element data array to the given capacity, copying over
     * elements as necessary.
     *
     * @param capacity the length of the new element data array
     */
    @SuppressWarnings("unchecked")
    private void resize(int capacity) {
        E[] newData = (E[]) new Object[capacity];
        int i = increment(front, size);
        for (int newIndex = 0; newIndex < size; newIndex += 1) {
            newData[newIndex] = data[i];
            i = increment(i, size);
        }
        front = newData.length - 1;
        back = size;
        data = newData;
    }

    /**
     * Returns true if and only if the underlying element data array needs to be downsized. This
     * helps minimize unused memory when many elements are removed from the deque.
     *
     * @return true if an element data downsize is necessary
     */
    private boolean needsDownsize() {
        return ((double) size) / data.length < 0.25 && data.length > INITIAL_CAPACITY;
    }
}
