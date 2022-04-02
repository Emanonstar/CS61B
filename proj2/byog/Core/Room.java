package byog.Core;

import java.io.Serializable;

public class Room implements Serializable {
    private Position pos;
    private int width;
    private int height;

    Room(Position p, int w, int h) {
        pos = p;
        width = w;
        height = h;
    }

    public Position getPos() {
        return pos;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
