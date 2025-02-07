package hw4.puzzle;

import java.util.Arrays;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState {
    private int[] puzzle1D;
    private int N;

    /** Constructs a board from an N-by-N array of tiles where
    tiles[i][j] = tile at row i, column j. */
    public Board(int[][] tiles) {
        N = tiles.length;
        puzzle1D = new int[N * N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                puzzle1D[xyTo1D(i, j)] = tiles[i][j];
            }
        }
    }

    private int xyTo1D(int i, int j) {
        return i * N + j;
    }

    /** Returns value of tile at row i, column j (or 0 if blank). */
    public int tileAt(int i, int j) {
        if (!inBounds(i) || !inBounds(j)) {
            throw new IndexOutOfBoundsException();
        }
        return puzzle1D[xyTo1D(i, j)];
    }

    private boolean inBounds(int index) {
        return index >= 0 && index < size();
    }

    /** Returns the board size N. */
    public int size() {
        return N;
    }

    /**
     *  @author Josh Hug
     *  Returns neighbors of this board.
     */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == 0) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = 0;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = 0;
                }
            }
        }
        return neighbors;
    }

    /** Hamming estimate described below. */
    public int hamming() {
        int dst = 0;
        int l = N * N - 1;
        for (int i = 0; i < l; i++) {
            if (puzzle1D[i] != i + 1) {
                dst += 1;
            }
        }
        return dst;
    }

    /** Manhattan estimate described below. */
    public int manhattan() {
        int dst = 0;
        for (int i = 0; i < N * N; i++) {
            int tile = puzzle1D[i];
            if (tile != i + 1 && tile != 0) {
                dst = dst + Math.abs(xOf(tile - 1) - xOf(i)) + Math.abs(yOf(tile - 1) - yOf(i));
            }
        }
        return dst;
    }

    private int xOf(int index) {
        return index / N;
    }

    private int yOf(int index) {
        return index % N;
    }

    /** Estimated distance to goal. */
    @Override
    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    /** Returns true if this board's tile values are the same
     position as y's. */
    @Override
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }

        Board board = (Board) y;
        return Arrays.equals(puzzle1D, board.puzzle1D);
    }

    @Override
    public int hashCode() {
        return puzzle1D.hashCode();
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }
}
