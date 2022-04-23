import org.junit.Test;
import static org.junit.Assert.*;

public class TestRadixSort {

    private static String[] someStrings = {"a", "abc", "bod", "carter", "zoo"};

    public static void assertIsSorted(String[] a) {
        for (int i = 0; i < a.length - 1; i++) {
            assertTrue(a[i].compareTo(a[i + 1]) <= 0);
        }
    }

    @Test
    public void testRadixSort() {

        String[] sortedStrings = RadixSort.sort(someStrings);
        for (String s : sortedStrings) {
            System.out.println(s);
        }
        assertIsSorted(sortedStrings);
    }
}
