public class OffByN implements CharacterComparator {
    private int n;

    /** Constructor. */
    public OffByN(int N) {
        n = N;
    }

    /** Returns true if characters are different by exactly n. */
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == n;
    }
}
