package deques;

/**
 * A doubly-linked implementation of the {@link Deque} interface.
 *
 * @see Deque
 */
public class LinkedDeque<E> implements Deque<E> {
    /**
     * The sentinel node representing the front of this deque.
     */
    private final Node<E> front;
    /**
     * The sentinel node representing the back of this deque.
     */
    private final Node<E> back;
    /**
     * The number of elements in this deque.
     */
    private int size;

    /**
     * Constructs an empty deque.
     */
    public LinkedDeque() {
        front = new Node<>(null);
        back = new Node<>(null);
        front.next = back;
        back.prev = front;
        size = 0;
    }

    @Override
    public void addFirst(E element) {
        size += 1;
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void addLast(E element) {
        size += 1;
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public E removeFirst() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public E removeLast() {
        if (size == 0) {
            return null;
        }
        size -= 1;
        // TODO: Replace with your code
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public E get(int index) {
        if ((index >= size) || (index < 0)) {
            return null;
        }
        Node<E> curr = front.next;
        while (index > 0) {
            curr = curr.next;
            index -= 1;
        }
        return curr.value;
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
        Node<E> curr = front.next;
        while (curr != back) {
            result.append(curr.value);
            curr = curr.next;
            if (curr != back) {
                result.append(", ");
            }
        }
        result.append(']');
        return result.toString();
    }

    /**
     * Returns null if the front and back nodes form a valid linked deque. Otherwise, returns a
     * string describing the error.
     *
     * @return null if this deque is valid, or a description of the error
     */
    @SuppressWarnings("unused")
    private String checkInvariants() {
        if (front.prev != null) {
            return "Unexpected reference: front.prev should be <null> but was <" + front.prev + ">";
        } else if (back.next != null) {
            return "Unexpected reference: back.next should be <null> but was <" + back.next + ">";
        }
        String message = checkNode(front, -1);
        if (message != null) {
            return message;
        }
        int i = 0;
        for (Node<E> curr = front.next; curr != back; curr = curr.next) {
            message = checkNode(curr, i);
            if (message != null) {
                return message;
            }
            i += 1;
        }
        return null;
    }

    /**
     * Returns null if the current node is valid with correct references in both directions.
     * Otherwise, returns a string describing the error.
     *
     * @param node the node to validate
     * @param i the index of the node in this deque
     * @return null if this node is valid, or a description of the error
     */
    private String checkNode(Node<E> node, int i) {
        if (node.next == null) {
            return "Unexpected null reference in node at index " + i + ": <" + node + ">";
        } else if (node.next.prev == node) {
            return null;
        } else if (node.next.prev == null) {
            return "Unexpected null reference in node at index " + (i + 1) + ": <" + node.next + ">";
        } else {
            return "Mismatched references:\n"
                    + "node at index " + i + ": <" + node + ">\n"
                    + "node at index " + (i + 1) + ": <" + node.next + ">";
        }
    }

    /**
     * A doubly-linked node containing a single element.
     *
     * @param <T> the type of element in this node
     */
    private static class Node<T> {
        /**
         * The element data value.
         */
        final T value;
        /**
         * The previous node in the deque.
         */
        Node<T> prev;
        /**
         * The next node in the deque.
         */
        Node<T> next;

        /**
         * Constructs a new node with the given value.
         *
         * @param value the element data value for the new node
         */
        public Node(T value) {
            this(value, null, null);
        }

        /**
         * Constructs a new node with the given value, previous node, and next node.
         *
         * @param value the element data value for the new node
         * @param prev the previous node in the deque, or null
         * @param next the next node in the deque, or null
         */
        public Node(T value, Node<T> prev, Node<T> next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{value=" + value + '}';
        }
    }
}
