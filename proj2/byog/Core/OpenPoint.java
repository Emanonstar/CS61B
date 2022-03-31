package byog.Core;

public class OpenPoint extends Position {
    private int direction;

    OpenPoint(int x, int y, int drt) {
        super(x, y);
        direction = drt;
    }

    public int getDirection() {
        return direction;
    }
}
