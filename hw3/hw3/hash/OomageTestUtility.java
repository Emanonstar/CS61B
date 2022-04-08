package hw3.hash;

import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* Returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int[] buckets = new int[M];
        for (int i = 0; i < M; i++) {
            buckets[i] = 0;
        }

        for (Oomage oomage: oomages) {
            int bucketNumber = (oomage.hashCode() & 0x7FFFFFFF) % M;
            buckets[bucketNumber] += 1;
        }
        int N = oomages.size();
        double lowerBound = (double) N / 50;
        double upperBound = (double) N / 2.5;
        for (int i = 0; i < M; i++) {
            if (buckets[i] > upperBound || buckets[i] < lowerBound) {
                return false;
            }
        }
        return true;
    }
}
