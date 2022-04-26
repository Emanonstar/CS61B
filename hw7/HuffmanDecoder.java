public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader or = new ObjectReader(args[0]);
        Object x = or.readObject();
        Object y = or.readObject();
        Object z = or.readObject();

        BinaryTrie huffmanTrie = (BinaryTrie) x;
        int numOfSymbols = (int) y;
        BitSequence bitSequence = (BitSequence) z;

        char[] ouputSymbols = new char[numOfSymbols];
        for (int i = 0; i < numOfSymbols; i++) {
            Match match = huffmanTrie.longestPrefixMatch(bitSequence);
            ouputSymbols[i] = match.getSymbol();
            bitSequence = bitSequence.allButFirstNBits(match.getSequence().length());
        }

        FileUtils.writeCharArray(args[1], ouputSymbols);
    }
}
