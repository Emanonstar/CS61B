package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {
    private static final int WIDTH = 80;
    private static final int HEIGHT = 30;
    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);
    private static List<Room> rooms = new ArrayList<>();
    private static List<Hallway> hallways = new ArrayList<>();
    private static List<OpenPoint> openPoints= new ArrayList<>();


    private static void addRoom(TETile[][] tiles, Position p, int width, int height) {
        addRectangular(tiles, p, width, height, Tileset.FLOOR);
    }

    private static void addHorizontalHallway(TETile[][] tiles, Hallway h) {
        addRectangular(tiles, h.start, h.end.xPos - h.start.xPos, 1, Tileset.FLOOR);
    }

    private static void addVerticalHallway(TETile[][] tiles, Hallway h) {
        addRectangular(tiles, h.start, 1, h.end.yPos - h.start.yPos, Tileset.FLOOR);
    }

    private static void addRectangular(TETile[][] tiles, Position p, int width, int height, TETile tile) {
        for (int x = p.xPos; x < p.xPos + width; x++) {
            for (int y = p.yPos; y < p.yPos + height; y++) {
                tiles[x][y] = tile;
            }
        }
    }

    public static void roomGenerator(TETile[][] tiles) {
        if (openPoints.isEmpty()) {
            return;
        }
        int len = openPoints.size();
        int i = RANDOM.nextInt(len);
        Position p = openPoints.remove(i);
        int width = RANDOM.nextInt();
        int height = RANDOM.nextInt();
    }

    public static void hallwayGenerator(TETile[][] tiles) {
        int maxLen;
        Hallway h;
        int len;
        Position p1;
        Position p2;
        if (hallways.isEmpty()) {
            int xPos = RANDOM.nextInt(WIDTH - 2) + 1;
            int yPos = RANDOM.nextInt(HEIGHT - 2) + 1;
            p1 = new Position(xPos, yPos);
            OpenPoint op1;
            OpenPoint op2;
            // Horizon Hallway
            if (randomDrt()) {
                if (randomDrt()) {
                    maxLen = maxLenXPlus(p1,tiles);
                    len = RANDOM.nextInt(maxLen);
                    p2 = new Position(xPos + len, yPos);
                    op1 = new OpenPoint(xPos, yPos, 1);
                    op2 = new OpenPoint(xPos + len, yPos, 0);
                    h = new Hallway(p1, p2);
                } else {
                    maxLen = maxLenXNeg(p1,tiles);
                    len = RANDOM.nextInt(maxLen);
                    p2 = new Position(xPos - len, yPos);
                    op1 = new OpenPoint(xPos, yPos, 0);
                    op2 = new OpenPoint(xPos - len, yPos, 1);
                    h = new Hallway(p2, p1);
                }
                openPoints.add(op1);
                openPoints.add(op2);
                hallways.add(h);
                addHorizontalHallway(tiles, h);
            } else {
                if (randomDrt()) {
                    maxLen = maxLenYPlus(p1,tiles);
                    len = RANDOM.nextInt(maxLen);
                    p2 = new Position(xPos, yPos + len);
                    op1 = new OpenPoint(xPos, yPos, 3);
                    op2 = new OpenPoint(xPos, yPos + len, 2);
                    h = new Hallway(p1, p2);
                } else {
                    maxLen = maxLenYNeg(p1,tiles);
                    len = RANDOM.nextInt(maxLen);
                    p2 = new Position(xPos, yPos - len);
                    op1 = new OpenPoint(xPos, yPos, 2);
                    op2 = new OpenPoint(xPos, yPos - len, 3);
                    h = new Hallway(p2, p1);
                }
                openPoints.add(op1);
                openPoints.add(op2);
                hallways.add(h);
                addVerticalHallway(tiles, h);
            }
        }
        else {
            int index = RANDOM.nextInt(hallways.size());
            Hallway h0 = hallways.get(index);
            OpenPoint op;
            p1 = h0.randomPoint(RANDOM);
            if (h0.isHorizontal()) {
                if (randomDrt()) {
                    maxLen = maxLenYPlus(p1,tiles);
                    len = RANDOM.nextInt(maxLen);
                    p2 = new Position(p1.xPos, p1.yPos + len);
                    op = new OpenPoint(p1.xPos, p1.yPos + len, 2);
                    h = new Hallway(p1, p2);
                } else {
                    maxLen = maxLenYNeg(p1,tiles);
                    len = RANDOM.nextInt(maxLen);
                    p2 = new Position(p1.xPos, p1.yPos - len);
                    op = new OpenPoint(p1.xPos, p1.yPos - len, 3);
                    h = new Hallway(p2, p1);
                }
                openPoints.add(op);
                hallways.add(h);
                addVerticalHallway(tiles, h);
            }
            else {
                if (randomDrt()) {
                    maxLen = maxLenXPlus(p1,tiles);
                    len = RANDOM.nextInt(maxLen);
                    p2 = new Position(p1.xPos + len, p1.yPos);
                    op = new OpenPoint(p1.xPos + len, p1.yPos, 0);
                    h = new Hallway(p1, p2);
                } else {
                    maxLen = maxLenXNeg(p1,tiles);
                    len = RANDOM.nextInt(maxLen);
                    p2 = new Position(p1.xPos - len, p1.yPos);
                    op = new OpenPoint(p1.xPos - len, p1.yPos, 1);
                    h = new Hallway(p2, p1);
                }
                openPoints.add(op);
                hallways.add(h);
                addVerticalHallway(tiles, h);
            }
            for (int i = 0, l = openPoints.size(); i < l; i++) {
                if (p1.equal(openPoints.get(i))) {
                    openPoints.remove(i);
                    break;
                }
            }
        }
    }

    private static boolean randomDrt() {
        int drt = RANDOM.nextInt(2);
        return drt == 0;
    }

    /** Return the bound of a given point in x positive direction. */
    private static int maxLenXPlus(Position p, TETile[][]tiles) {
        for (int x = p.xPos + 1; x < WIDTH - 1; x++) {
            if (tiles[x][p.yPos] != Tileset.NOTHING) {
                return x - p.xPos;
            }
        }
        return WIDTH - 1 - p.xPos;
    }

    /** Return the bound of a given point in x negtive direction. */
    private static int maxLenXNeg(Position p, TETile[][]tiles) {
        for (int x = p.xPos - 1; x > 0; x--) {
            if (tiles[x][p.yPos] != Tileset.NOTHING) {
                return p.xPos - x;
            }
        }
        return p.xPos;
    }

    /** Return the bound of a given point in y positive direction. */
    private static int maxLenYPlus(Position p, TETile[][]tiles) {
        for (int y = p.yPos + 1; y < HEIGHT- 1; y++) {
            if (tiles[p.xPos][y] != Tileset.NOTHING) {
                return y - p.yPos;
            }
        }
        return HEIGHT - 1 - p.yPos;
    }

    /** Return the bound of a given point in y negtive direction. */
    private static int maxLenYNeg(Position p, TETile[][]tiles) {
        for (int y = p.yPos - 1; y > 0; y--) {
            if (tiles[p.xPos][y] != Tileset.NOTHING) {
                return p.yPos - y;
            }
        }
        return p.yPos;
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] map = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                map[x][y] = Tileset.NOTHING;
            }
        }

        for (int i = 0; i < 10; i++) {
            hallwayGenerator(map);
        }
        ter.renderFrame(map);
    }
}
