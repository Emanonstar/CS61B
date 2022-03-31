package byog.Core;

import java.util.Random;


public class Hallway {
    private Position start;
    private Position end;


    Hallway(Position p1, Position p2) {
        start = p1;
        end = p2;
    }

    public Position getStart() {
        return start;
    }

    public Position getEnd() {
        return end;
    }

    /** Return true if hallway is horizontal, or false if hallway is vertical. */
    public boolean isHorizontal() {
        return start.getYPos() == end.getYPos();
    }

    /** Return length of hallway. */
    public int len() {
        if (isHorizontal()) {
            return end.getXPos() - start.getXPos();
        }
        return end.getYPos() - start.getYPos();
    }

    /** Return a random position of hallway. */
    public Position randomPoint(Random R) {
        int r = R.nextInt(len());
        if (isHorizontal()) {
            return new Position(start.getXPos() + r, start.getYPos());
        }
        return new Position(start.getXPos(), start.getYPos() + r);
    }
}
