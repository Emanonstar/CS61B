package lab9;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Yuxing Li
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null) {
            return null;
        }

        if (key.compareTo(p.key) < 0) {
            return getHelper(key, p.left);
        }
        if (key.compareTo(p.key) > 0) {
            return getHelper(key, p.right);
        }
        return p.value;
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null) {
            size += 1;
            return new Node(key, value);
        }
        if (key.compareTo(p.key) < 0) {
            p.left = putHelper(key, value, p.left);
        } else if (key.compareTo(p.key) > 0) {
            p.right = putHelper(key, value, p.right);
        } else {
            p.value = value;
        }
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /** Returns a Set view of the keys in the subtree rooted in P. */
    private Set<K> keySetHelper(Node p) {
        Set<K> kSet = new HashSet<>();
        if (p == null) {
            return kSet;
        }
        kSet.add(p.key);
        kSet.addAll(keySetHelper(p.left));
        kSet.addAll(keySetHelper(p.right));
        return kSet;
    }

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        return keySetHelper(root);
    }

    /** Removes KEY from the subtree rooted in P if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    private V removeHelper(K key, Node p, Node prt) {
        if (p == null) {
            return null;
        }

        if (key.compareTo(p.key) < 0) {
            return removeHelper(key, p.left, p);
        }
        if (key.compareTo(p.key) > 0) {
            return removeHelper(key, p.right, p);
        }

        V result = p.value;
        Node tmp = findMax(p.left, p);
        if (p.equals(tmp)) {
            if (p == root) {
                root = null;
            } else {
                prt.right = p.left;
            }
        } else {
            removeHelper(tmp.key, p.left, p);
            size += 1;
            p.key = tmp.key;
            p.value = tmp.value;
        }
        size -= 1;
        return result;
    }

    /** Returns the Node has the biggest key in the subtree rooted in P,
     *  returns parent Node if P is NULL. */
    private Node findMax(Node p, Node prt) {
        if (p == null) {
            return prt;
        }
        return findMax(p.right, p);
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        return removeHelper(key, root, root);
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (containsKey(key) && get(key).equals(value)) {
            return removeHelper(key, root, null);
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
