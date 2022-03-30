package byog.Core;

public class OpenPoint extends Position{
    public int direction;

    OpenPoint(int x, int y, int drt) {
        super(x, y);
        direction = drt;
    }
}
