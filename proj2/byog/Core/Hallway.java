package byog.Core;

import java.util.Random;

public class Hallway {
    public Position start;
    public Position end;


    Hallway(Position p1, Position p2) {
        start = p1;
        end = p2;
    }


    public boolean isHorizontal() {
        return start.yPos == end.yPos;
    }

    public int len() {
        if (isHorizontal()) {
            return end.xPos - start.xPos;
        }
        return end.yPos - start.yPos;
    }

    public Position randomPoint(Random R) {
        int r = R.nextInt(len());
        if (isHorizontal()) {
            return new Position(start.xPos + r, start.yPos);
        }
        return new Position(start.xPos, start.yPos + r);
    }
}
