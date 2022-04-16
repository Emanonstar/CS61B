package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private MinPQ<searchNode> pq;
    private searchNode goal;
    private boolean goalFound = false;
    int numOfEnqued = 0;


    private class searchNode implements Comparable<searchNode> {
        WorldState worldState;
        int numOfMove;
        int estimatedDistanceToGoal;
        searchNode prev;

        searchNode(WorldState w, int n, searchNode p) {
            worldState = w;
            numOfMove = n;
            prev = p;
            estimatedDistanceToGoal = w.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(searchNode o) {
            return numOfMove + estimatedDistanceToGoal - o.numOfMove - o.estimatedDistanceToGoal;
        }
    }
    public Solver(WorldState initial) {
        searchNode init = new searchNode(initial, 0, null);
        pq = new MinPQ<>();
        pq.insert(init);
        numOfEnqued += 1;

        while (!goalFound) {
            searchNode x = pq.delMin();
            if (x.worldState.isGoal()) {
                goalFound = true;
                goal = x;
                return;
            }

            for (WorldState n : x.worldState.neighbors()) {
                if (x.prev == null || !n.equals(x.prev.worldState)) {
                    searchNode node = new searchNode(n, x.numOfMove + 1, x);
                    pq.insert(node);
                    numOfEnqued += 1;
                }
            }
        }

    }

    public int moves() {
        return goal.numOfMove;
    }

    public Iterable<WorldState> solution() {
        Stack<WorldState> stateStack = new Stack<>();
        searchNode n = goal;
        while (n != null) {
            stateStack.push(n.worldState);
            n = n.prev;
        }
        return stateStack;
    }
}
