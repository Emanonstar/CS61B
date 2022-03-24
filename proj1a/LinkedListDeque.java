
public class LinkedListDeque<T> {
    private class Node {
        Node prev;
        T item;
        Node next;

        Node(Node p, T i, Node n) {
            prev = p;
            item = i;
            next = n;
        }
    }

    /* The first item (if it exists) is at sentinel.next.
    The last item (if it exists) is at sentinel.prev. */
    private Node sentinel;
    private int size;

    /** Creates an empty deque. */
    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        size = 0;
    }

    /** Adds item to the front of the deque. */
    public void addFirst(T item) {
        if (sentinel.next == null) {
            Node tmp = new Node(sentinel, item, sentinel);
            sentinel.next = tmp;
            sentinel.prev = tmp;
        } else {
            Node tmp = new Node(sentinel, item, sentinel.next);
            sentinel.next.prev = tmp;
            sentinel.next = tmp;
        }
        size += 1;
    }

    /** Adds item to the end of the deque. */
    public void addLast(T item) {
        if (sentinel.next == null) {
            Node tmp = new Node(sentinel, item, sentinel);
            sentinel.next = tmp;
            sentinel.prev = tmp;
        } else {
            Node tmp = new Node(sentinel.prev, item, sentinel);
            sentinel.prev.next = tmp;
            sentinel.prev = tmp;
        }
        size += 1;
    }

    /** Returns true if deque is empty, false otherwise. */
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque. */
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space. */
    public void printDeque() {
        Node a = sentinel;
        while (a.next != sentinel) {
            System.out.print(a.next.item);
            System.out.print(" ");
            a = a.next;
        }
        System.out.println('\n');
    }

    /** Removes and returns the item at the front of the deque. If not exists, returns null. */
    public T removeFirst() {
        if (sentinel.next == null) {
            return null;
        }
        T i = sentinel.next.item;
        if (size == 1) {
            sentinel.prev = null;
            sentinel.next = null;
        } else {
            sentinel.next.next.prev = sentinel;
            sentinel.next = sentinel.next.next;
        }
        size -= 1;
        return i;
    }

    /** Removes and returns the item at the back of the deque. If not exists, returns null. */
    public T removeLast() {
        if (sentinel.next == null) {
            return null;
        }
        T i = sentinel.prev.item;
        if (size == 1) {
            sentinel.prev = null;
            sentinel.next = null;
        } else {
            sentinel.prev.prev.next = sentinel;
            sentinel.prev = sentinel.prev.prev;
        }
        size -= 1;
        return i;
    }

    /** Gets the item at the given index iteratively. */
    public T get(int index) {
        if (index < 0) {
            return null;
        }
        Node tmp = sentinel;
        int i = 0;
        while (i < size) {
            tmp = tmp.next;
            if (i == index) {
                return tmp.item;
            }
            i++;
        }
        return null;
    }

    /** Gets the item at the given index recursively. */
    public T getRecursive(int index) {
        if (index < 0) {
            return null;
        }
        return getItem(sentinel.next, index);
    }

    private T getItem(Node n, int index) {
        if (n == sentinel || n == null) {
            return null;
        }
        if (index == 0) {
            return n.item;
        }
        return getItem(n.next, index - 1);
    }
}
