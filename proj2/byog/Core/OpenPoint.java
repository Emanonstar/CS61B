package byog.Core;

import java.io.Serializable;

public class OpenPoint extends Position implements Serializable {
    private int direction;

    OpenPoint(int x, int y, int drt) {
        super(x, y);
        direction = drt;
    }

    public int getDirection() {
        return direction;
    }
}
