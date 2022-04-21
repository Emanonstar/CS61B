import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;

public class Trie<T> {
    private final Node<T> root;

    /** Constructor of trie map. */
    public Trie() {
        root = new Node<>(false);
    }

    /** Add (s, t) to map. */
    public void add(String s, T t) {
        addHelper(s, t, root);
    }

    private void addHelper(String s, T t, Node<T> n) {
        if (s.isEmpty()) {
            n.isKey = true;
            n.values.add(t);
            return;
        }

        char c = s.charAt(0);
        if (!n.next.containsKey(c)) {
            n.next.put(c, new Node<>(false));
        }
        Node<T> nextNode = n.next.get(c);
        String left = s.substring(1);
        addHelper(left, t, nextNode);
    }

    /** Get the values of key: s. */
    public List<T> get(String s) {
        return getHelper(s, root);
    }

    private List<T>  getHelper(String s, Node<T> n) {
        if (s.isEmpty()) {
            if (n.isKey) {
                return n.values;
            }
            return null;
        }
        char c = s.charAt(0);
        if (!n.next.containsKey(c)) {
            return null;
        }
        Node<T> nextNode = n.next.get(c);
        String left = s.substring(1);
        return getHelper(left, nextNode);
    }

    /** Return whether the trie map contains key: s. */
    public boolean contains(String s) {
        return containsHelper(s, root);
    }

    private boolean containsHelper(String s, Node<T> n) {
        if (s.isEmpty()) {
            return n.isKey;
        }
        char c = s.charAt(0);
        if (!n.next.containsKey(c)) {
            return false;
        }
        Node<T> nextNode = n.next.get(c);
        String left = s.substring(1);
        return containsHelper(left, nextNode);
    }

    /** Return a list of key begin with prefix. */
    public List<T> keysWithPrefix(String prefix) {
        Node<T> start = findNode(prefix, root);
        List<T> result = new ArrayList<>();
        colHelp(prefix, result, start);
        return result;
    }

    private Node<T> findNode(String s, Node<T> n) {
        if (s.isEmpty()) {
            return n;
        }
        char c = s.charAt(0);
        if (!n.next.containsKey(c)) {
            return null;
        }
        Node<T> nextNode = n.next.get(c);
        String left = s.substring(1);
        return findNode(left, nextNode);
    }

    private void colHelp(String s, List<T> x, Node<T> n) {
        if (n.isKey) {
            x.addAll(n.values);
        }
        for (char c : n.next.keySet()) {
            colHelp(s + c, x, n.next.get(c));
        }
    }
    private static class Node<T> {
        boolean isKey;
        List<T> values;
        Map<Character, Node<T>> next;

        Node(boolean b) {
            isKey = b;
            values = new ArrayList<>();
            next = new TreeMap<>();
        }
    }

    /** Test trie
    public static void main(String[] args) {
        Trie<Integer> trie = new Trie<>();
        trie.add("sam", 0);
        trie.add("sad", 1);
        trie.add("sap", 2);
        trie.add("same", 3);
        trie.add("a", 4);
        trie.add("awls", 5);
        trie.add("a16", 6);
        System.out.println(trie.contains("sam"));
        System.out.println(trie.contains("same"));
        System.out.println(trie.contains("sa"));
        System.out.println(trie.contains("a"));
        System.out.println(trie.contains("b"));
        System.out.println(trie.keysWithPrefix("a"));
        for (String s : trie.keysWithPrefix("sa")) {
            System.out.println(s + ": " + trie.get(s));
        }
    }
    */
}

