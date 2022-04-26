import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    /** Map characters to their counts. */
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (char c : inputSymbols) {
            if (frequencyTable.containsKey(c)) {
                int freq = frequencyTable.get(c) + 1;
                frequencyTable.put(c, freq);
            } else {
                frequencyTable.put(c, 1);
            }
        }
        return frequencyTable;
    }

    public static void main(String[] args) {
        char[] inputSymbols = FileUtils.readFile(args[0]);
        Map<Character, Integer> frequencyTable = buildFrequencyTable(inputSymbols);
        BinaryTrie trie = new BinaryTrie(frequencyTable);
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");
        ow.writeObject(trie);
        ow.writeObject(inputSymbols.length);

        Map<Character, BitSequence> lookupTable = trie.buildLookupTable();
        List<BitSequence> bitsequences = new LinkedList<>();
        for (char c : inputSymbols) {
            BitSequence bs = lookupTable.get(c);
            bitsequences.add(bs);
        }
        BitSequence huge = BitSequence.assemble(bitsequences);
        ow.writeObject(huge);
    }
}
