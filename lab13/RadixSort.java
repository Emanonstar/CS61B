/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    private static final int R = 256;
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // LSD Sort
        int W = 0;
        for (String s : asciis) {
            int l = s.length();
            W = Math.max(W, l);
        }

        String[] sorted = asciis;
        for (int i = W - 1; i >= 0; i--) {
            sorted = sortHelperLSD(sorted, i);
        }
        return sorted;

        /* MSD Sort
        String[] sorted = asciis.clone();
        sortHelperMSD(sorted, 0, sorted.length, 0);
        return sorted;
         */
    }

    /**
     * LSD helper method that performs a nondestructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static String[] sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort

        int[] counts = counts(asciis, index);

        int[] starts = new int[R + 1];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted = new String[asciis.length];
        for (int i = 0; i < asciis.length; i += 1) {
            String item = asciis[i];
            int place;
            if (index > item.length() - 1) {
                place = starts[0];
                sorted[place] = item;
                starts[0] += 1;
            } else {
                place = starts[item.charAt(index) + 1];
                sorted[place] = item;
                starts[item.charAt(index) + 1] += 1;
            }
        }
        return sorted;

        /* A different implementation of LSD.
        List<String>[] buckets = new List[R + 1];
        for (int i = 0; i < R + 1; i++) {
            buckets[i] = new ArrayList<>();
        }

        for (String s : asciis) {
            if (index > s.length() - 1) {
                buckets[0].add(s);
            } else {
                buckets[s.charAt(index) + 1].add(s);
            }
        }

        int k = 0;
        for (List<String> bucket : buckets) {
            for (String s : bucket) {
                asciis[k] = s;
                k++;
            }
        }
        */
    }

    private static int[] counts(String[] asciis, int index) {
        int[] counts = new int[R + 1];
        for (String s : asciis) {
            if (index > s.length() - 1) {
                counts[0]++;
            } else {
                counts[s.charAt(index) + 1]++;
            }
        }
        return counts;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        if (end - start <= 1) {
            return;
        }

        int[] counts = new int[R + 1];
        for (int i = start; i < end; i++) {
            if (index > asciis[i].length() - 1) {
                counts[0]++;
            } else {
                counts[asciis[i].charAt(index) + 1]++;
            }
        }

        int[] starts = new int[R + 1];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        String[] sorted = new String[end - start];
        for (int i = start; i < end; i += 1) {
            String item = asciis[i];
            int place;
            if (index > item.length() - 1) {
                place = starts[0];
                sorted[place] = item;
                starts[0] += 1;
            } else {
                place = starts[item.charAt(index) + 1];
                sorted[place] = item;
                starts[item.charAt(index) + 1] += 1;
            }
        }

        int newStart = 0;
        for (int i = 0; i < R + 1; i++) {
            int newEnd = newStart + counts[i];
            sortHelperMSD(sorted, newStart, newEnd, index + 1);
            newStart = newEnd;
        }
        System.arraycopy(sorted, 0, asciis, start, end - start);
    }
}
