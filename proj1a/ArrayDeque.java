public class ArrayDeque<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;


    /** Creates an empty deque. */
    public ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    /** Resizes the underlying array to the target capacity. */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int p = forwards(nextFirst);
        for (int i = 0; i < size; i++) {
            System.arraycopy(items, p, a, i + capacity / 3, 1);
            p = forwards(p);
        }
        items = a;
        nextFirst = backwards(capacity / 3);
        nextLast = capacity / 3 + size;
    }

    private int backwards(int p) {
        if (p == 0) {
            return items.length - 1;
        } else {
            return p - 1;
        }
    }

    /** Adds item to the front of the deque. */
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = item;
        nextFirst = backwards(nextFirst);
        size += 1;
    }

    private int forwards(int p) {
        if (p == items.length - 1) {
            return 0;
        } else {
            return p + 1;
        }
    }
    /** Adds item to the end of the deque. */
    public void addLast(T item) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = item;
        nextLast = forwards(nextLast);
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
        int p = forwards(nextFirst);
        int l = size;
        while (l > 0) {
            System.out.print(items[p] + " ");
            p = forwards(p);
            l -= 1;
        }
        System.out.println("\n");
    }

    /** Removes and returns the item at the front of the deque. If not exits, returns null. */
    public T removeFirst() {
        if (size == 0) {
            return null;
        }
        nextFirst = forwards(nextFirst);
        T first = items[nextFirst];
        size -= 1;
        if (items.length >= 16) {
            double usage = Double.valueOf(size) / items.length;
            if (usage < 0.25) {
                resize(items.length / 2);
            }
        }
        return first;
    }

    /** Removes and returns the item at the back of the deque. If not exists, returns null. */
    public T removeLast() {
        if (size == 0) {
            return null;
        }
        nextLast = backwards(nextLast);
        T last = items[nextLast];
        size -= 1;
        if (items.length >= 16) {
            double usage = Double.valueOf(size) / items.length;
            if (usage < 0.25) {
                resize(items.length / 2);
            }
        }
        return last;
    }

    /** Gets the item at the given index iteratively. */
    public T get(int index) {
        if (index >= size || index < 0) {
            return null;
        }
        int realIndex = nextFirst + index + 1;
        if (realIndex > items.length - 1) {
            realIndex -= items.length;
        }
        return items[realIndex];
    }
}
