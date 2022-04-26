import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    private final Node start;

    /** Build a Huffman decoding trie. */
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> pq = new MinPQ<>();
        for (char ch : frequencyTable.keySet()) {
            pq.insert(new Node(ch, frequencyTable.get(ch), null, null));
        }

        while (pq.size() > 1) {
            Node left = pq.delMin();
            Node right = pq.delMin();
            pq.insert(new Node('\0', left.freq + right.freq, left, right));
        }
        start = pq.delMin();
    }

    /** Find the longest prefix that matches the given querySequence
     * and returns a Match object for that Match. */
    public Match longestPrefixMatch(BitSequence querySequence) {
        Node n = start;
        for (int i = 0, l = querySequence.length(); i < l; i++) {
            int bit = querySequence.bitAt(i);
            if (bit == 0) {
                n = n.left;
            } else {
                n = n.right;
            }
            if (n.isLeaf()) {
                return new Match(querySequence.firstNBits(i + 1), n.ch);
            }
        }
        return null;
    }

    /** Return the inverse of the coding trie. */
    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> lookupTable = new HashMap<>();
        buildLookupTableHelper(start, new BitSequence(), lookupTable);
        return lookupTable;
    }

    private void buildLookupTableHelper(Node n, BitSequence s, Map<Character, BitSequence> map) {
        if (n.isLeaf()) {
            map.put(n.ch, s);
            return;
        }
        buildLookupTableHelper(n.left, s.appended(0), map);
        buildLookupTableHelper(n.right, s.appended(1), map);
    }

    /** Huffman trie node. */
    private static class Node implements Serializable, Comparable<Node>  {
        final char ch;
        final int freq;
        final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        // is the node a leaf node?
        boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }
}
