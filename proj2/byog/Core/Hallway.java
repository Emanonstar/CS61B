package byog.Core;

import java.util.Random;


public class Hallway {
    public Position start;
    public Position end;


    Hallway(Position p1, Position p2) {
        start = p1;
        end = p2;
    }

    /** Return true if hallway is horizontal, or false if hallway is vertical. */
    public boolean isHorizontal() {
        return start.yPos == end.yPos;
    }

    /** Return length of hallway. */
    public int len() {
        if (isHorizontal()) {
            return end.xPos - start.xPos;
        }
        return end.yPos - start.yPos;
    }

    /** Return a random position of hallway. */
    public Position randomPoint(Random R) {
        int r = R.nextInt(len());
        if (isHorizontal()) {
            return new Position(start.xPos + r, start.yPos);
        }
        return new Position(start.xPos, start.yPos + r);
    }
}
