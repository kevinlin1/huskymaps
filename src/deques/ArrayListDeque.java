package deques;

import java.util.ArrayList;

/**
 * An {@link ArrayList} implementation of the {@link Deque} interface.
 *
 * @see Deque
 */
public class ArrayListDeque<E> implements Deque<E> {
    /**
     * The underlying list of elements stored in this deque.
     */
    private final ArrayList<E> list;

    /**
     * Constructs an empty deque.
     */
    public ArrayListDeque() {
        list = new ArrayList<>();
    }

    @Override
    public void addFirst(E element) {
        list.add(0, element);
    }

    @Override
    public void addLast(E element) {
        list.add(element);
    }

    @Override
    public E get(int index) {
        if ((index >= list.size()) || (index < 0)) {
            return null;
        }
        return list.get(index);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public E removeFirst() {
        if (list.size() == 0) {
            return null;
        }
        return list.remove(0);
    }

    @Override
    public E removeLast() {
        if (list.size() == 0) {
            return null;
        }
        return list.remove(size() - 1);
    }

    @Override
    public String toString() {
        return list.toString();
    }
}
