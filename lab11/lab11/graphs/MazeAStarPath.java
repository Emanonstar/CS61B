package lab11.graphs;

import edu.princeton.cs.algs4.IndexMinPQ;


/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int s;
    private int t;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        s = maze.xyTo1D(sourceX, sourceY);
        t = maze.xyTo1D(targetX, targetY);
        distTo[s] = 0;
        edgeTo[s] = s;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        return Math.abs(maze.toX(v) - maze.toX(t)) + Math.abs(maze.toY(v) - maze.toY(t));
    }

    /** Finds vertex estimated to be closest to target. */
    private int findMinimumUnmarked() {
        return -1;
        /* You do not have to use this method. */
    }

    /** Performs an A star search from vertex s. */
    private void astar(int v) {
        IndexMinPQ<Integer> fringe = new IndexMinPQ<>(maze.V());
        marked[v] = true;

        for (int i = 0; i < maze.V(); i++) {
            fringe.insert(i, distTo[i]);
        }

        while (!fringe.isEmpty()) {
            int p = fringe.delMin();
            for (int w : maze.adj(p)) {
                if (!marked[w]) {
                    marked[w] = true;
                    edgeTo[w] = p;
                    distTo[w] = distTo[p] + 1;
                    fringe.decreaseKey(w, distTo[w] + h(w));
                    announce();

                    if (w == t) {
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void solve() {
        astar(s);
    }

}

