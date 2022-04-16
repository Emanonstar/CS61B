package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private MinPQ<SearchNode> pq;
    private SearchNode goal;
    private boolean goalFound = false;
    int numOfEnqued = 0;

    private class SearchNode implements Comparable<SearchNode> {
        WorldState worldState;
        int numOfMove;
        int estimatedDistanceToGoal;
        SearchNode prev;

        SearchNode(WorldState w, int n, SearchNode p) {
            worldState = w;
            numOfMove = n;
            prev = p;
            estimatedDistanceToGoal = w.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(SearchNode o) {
            return numOfMove + estimatedDistanceToGoal - o.numOfMove - o.estimatedDistanceToGoal;
        }
    }

    /** Constructor which solves the puzzle, computing
     everything necessary for moves() and solution() to
     not have to solve the problem again. Solves the
     puzzle using the A* algorithm. Assumes a solution exists.*/
    public Solver(WorldState initial) {
        SearchNode init = new SearchNode(initial, 0, null);
        pq = new MinPQ<>();
        pq.insert(init);
        numOfEnqued += 1;

        while (!goalFound) {
            SearchNode x = pq.delMin();
            if (x.worldState.isGoal()) {
                goalFound = true;
                goal = x;
                return;
            }

            for (WorldState n : x.worldState.neighbors()) {
                if (x.prev == null || !n.equals(x.prev.worldState)) {
                    SearchNode node = new SearchNode(n, x.numOfMove + 1, x);
                    pq.insert(node);
                    numOfEnqued += 1;
                }
            }
        }

    }

    /** Returns the minimum number of moves to solve the puzzle starting
    at the initial WorldState. */
    public int moves() {
        return goal.numOfMove;
    }

    /** Returns a sequence of WorldStates from the initial WorldState
     to the solution. */
    public Iterable<WorldState> solution() {
        Stack<WorldState> stateStack = new Stack<>();
        SearchNode n = goal;
        while (n != null) {
            stateStack.push(n.worldState);
            n = n.prev;
        }
        return stateStack;
    }
}
