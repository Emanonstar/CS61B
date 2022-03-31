package byog.Core;

public class Position {
        public int xPos;
        public int yPos;

        Position(int x, int y) {
            xPos = x;
            yPos = y;
        }

        /** Return current position and given position whether same position. */
        public boolean equal(Position p) {
            return xPos == p.xPos && yPos == p.yPos;
        }

        /** Return left position of current position. */
        public Position left() {
            return new Position(xPos - 1, yPos);
        }

        /** Return right position of current position. */
        public Position right() {
            return new Position(xPos + 1, yPos);
        }

        /** Return up position of current position. */
        public Position up() {
            return new Position(xPos, yPos + 1);
        }

        /** Return down position of current position. */
        public Position down() {
            return new Position(xPos, yPos - 1);
        }
}
