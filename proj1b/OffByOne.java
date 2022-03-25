public class OffByOne implements CharacterComparator {

    /** Returns true if characters are different by exactly one. */
    @Override
    public boolean equalChars(char x, char y) {
        return Math.abs(x - y) == 1;
    }
}
