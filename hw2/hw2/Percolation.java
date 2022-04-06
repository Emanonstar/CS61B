package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import org.junit.Test;
import static org.junit.Assert.*;


public class Percolation {
    private final int LENGTH;
    private boolean[][] sites;
    private WeightedQuickUnionUF opensites;
    private int numberOfOpensite;

    /** create N-by-N grid, with all sites initially blocked. */
    public Percolation(int N) {
        LENGTH = N;
        sites = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sites[i][j] = false;
            }
        }
        opensites = new WeightedQuickUnionUF(N * N + 2);
        numberOfOpensite = 0;
    }

    /** open the site (r, c) if it is not open already. */
    public void open(int r, int c) {
        if (r < 0 || r > LENGTH - 1 || c < 0 || c > LENGTH - 1) {
            throw new IndexOutOfBoundsException();
        }
        if (sites[r][c]) {
            return;
        }

        sites[r][c] = true;
        numberOfOpensite += 1;

        if (r == 0) {
            opensites.union(0, xyTo1D(r, c));
        }
        if (r == LENGTH - 1) {
            opensites.union(LENGTH * LENGTH - 1, xyTo1D(r, c));
        }

        int[][] drt = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] d: drt) {
            int rr = r + d[0];
            int cc = c + d[1];
            if (rr < 0 || rr > LENGTH - 1 || cc < 0 || cc > LENGTH - 1) {
                continue;
            }
            if (isOpen(rr, cc)) {
                opensites.union(xyTo1D(r, c), xyTo1D(rr, cc));
            }
        }
    }

    private int xyTo1D(int r, int c) {
        return r * sites.length + c + 1;
    }

    /** Return whether the site (r, c) is open. */
    public boolean isOpen(int r, int c) {
        if (r < 0 || r > LENGTH - 1 || c < 0 || c > LENGTH - 1) {
            throw new IndexOutOfBoundsException();
        }
        return sites[r][c];
    }

    /** Return whether the site (r, c) is full. */
    public boolean isFull(int r, int c) {
        if (r < 0 || r > LENGTH - 1 || c < 0 || c > LENGTH - 1) {
            throw new IndexOutOfBoundsException();
        }
        return opensites.connected(0, xyTo1D(r, c));
    }

    /** Return number of open sites. */
    public int numberOfOpenSites() {
        return numberOfOpensite;
    }

    /** Return whether the system percolate. */
    public boolean percolates() {
        return opensites.connected(0, LENGTH * LENGTH - 1);
    }


    @Test
    public static void main(String[] args) {
        Percolation p = new Percolation(3);
        p.open(1, 1);
        assertFalse(p.isOpen(0, 0));
        assertTrue(p.isOpen(1, 1));
        p.open(0, 1);
        p.open(2, 0);
        assertTrue(p.isFull(0, 1));
        assertTrue(p.isFull(1, 1));
        assertFalse(p.isFull(2, 0));
        assertFalse(p.percolates());
        p.open(2, 1);
        assertEquals(4, p.numberOfOpenSites());
        assertTrue(p.percolates());
    }
}
