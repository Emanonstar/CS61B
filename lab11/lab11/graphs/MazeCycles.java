package lab11.graphs;

import edu.princeton.cs.algs4.Stack;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    public MazeCycles(Maze m) {
        super(m);
    }

    @Override
    public void solve() {
        int begin = maze.xyTo1D(1, 1);
        int end = maze.xyTo1D(maze.N(), maze.N());
        for (int s = begin; s <= end; s++) {
            if (!marked[s]) {
                boolean hasCycle = mc(s);
                if (hasCycle) {
                    break;
                }
            }
        }
    }

    private boolean mc(int s) {
        distTo[s] = 0;
        int[] tmpEdgeTo = new int[edgeTo.length];
        for (int i = 0; i < tmpEdgeTo.length; i++) {
            tmpEdgeTo[i] = edgeTo[i];
        }

        Stack<Integer> fringe = new Stack<>();
        fringe.push(s);

        while (!fringe.isEmpty()) {
            int v = fringe.pop();
            marked[v] = true;
            announce();
            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    tmpEdgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    fringe.push(w);
                } else {
                    if (w != tmpEdgeTo[v]) {
                        int i = v;
                        while (i != w) {
                            edgeTo[i] = tmpEdgeTo[i];
                            i = tmpEdgeTo[i];
                        }
                        edgeTo[w] = v;
                        announce();
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

