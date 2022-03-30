package byog.Core;

public class Position {
        public int xPos;
        public int yPos;

        Position(int x, int y) {
            xPos = x;
            yPos = y;
        }

        public boolean equal(Position p) {
            return xPos == p.xPos && yPos == p.yPos;
        }
}
