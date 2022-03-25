public interface Deque<T> {

    /** Adds item to the front of the deque. */
    void addFirst(T item);

    /** Adds item to the end of the deque. */
    void addLast(T item);

    /** Returns true if deque is empty, false otherwise. */
    boolean isEmpty();

    /** Returns the number of items in the deque. */
    int size();

    /** Prints the items in the deque from first to last, separated by a space. */
    void printDeque();

    /** Removes and returns the item at the front of the deque. If not exists, returns null. */
    T removeFirst();

    /** Removes and returns the item at the back of the deque. If not exists, returns null. */
    T removeLast();

    /** Gets the item at the given index iteratively. */
    T get(int index);
}
