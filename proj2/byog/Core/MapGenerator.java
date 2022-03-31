package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator {
    private final int WIDTH = Game.WIDTH;
    private final int HEIGHT = Game.HEIGHT;

    private final Random RANDOM;

    private final List<Room> rooms = new ArrayList<>();
    private final List<Hallway> hallways = new ArrayList<>();
    private final List<OpenPoint> openPoints= new ArrayList<>();

    MapGenerator (Random random) {
        RANDOM = random;
    }

    private void addRoom(TETile[][] tiles, Position p, int width, int height) {
        addRectangular(tiles, p, width, height, Tileset.FLOOR);
        //System.out.println("Add room from (" + p.xPos + ", " + p.yPos +
                //"), width: " + width + ", height: " + height);
    }

    private void addHorizontalHallway(TETile[][] tiles, Hallway h) {
        addRectangular(tiles, h.start, h.end.xPos - h.start.xPos, 1, Tileset.FLOOR);
        //System.out.println("Add horizontal hallway from (" + h.start.xPos + ", " + h.start.yPos + ") to (" + h.end.xPos + ", " + h.end.yPos + ")");
    }

    private void addVerticalHallway(TETile[][] tiles, Hallway h) {
        addRectangular(tiles, h.start, 1, h.end.yPos - h.start.yPos, Tileset.FLOOR);
        //System.out.println("Add vertical hallway from (" + h.start.xPos + ", " + h.start.yPos + ") to (" + h.end.xPos + ", " + h.end.yPos + ")");
    }

    private void addRectangular(TETile[][] tiles, Position p, int width, int height, TETile tile) {
        for (int x = p.xPos; x < p.xPos + width; x++) {
            for (int y = p.yPos; y < p.yPos + height; y++) {
                tiles[x][y] = tile;
            }
        }
    }

    /** Generate a hallway in tiles. */
    public void hallwayGenerator(TETile[][] tiles) {
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
            // Horizontal Hallway
            if (randomDrt()) {
                if (randomDrt()) {
                    maxLen = maxLenXPlus(p1,tiles);
                    len = RANDOM.nextInt(maxLen);
                    if (len == 0) {
                        return;
                    }
                    p2 = new Position(xPos + len, yPos);
                    op1 = new OpenPoint(xPos, yPos, 1);
                    op2 = new OpenPoint(xPos + len, yPos, 0);
                    h = new Hallway(p1, p2);
                } else {
                    maxLen = maxLenXNeg(p1,tiles);
                    len = RANDOM.nextInt(maxLen);
                    if (len == 0) {
                        return;
                    }
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
                    if (len == 0) {
                        return;
                    }
                    p2 = new Position(xPos, yPos + len);
                    op1 = new OpenPoint(xPos, yPos, 3);
                    op2 = new OpenPoint(xPos, yPos + len, 2);
                    h = new Hallway(p1, p2);
                } else {
                    maxLen = maxLenYNeg(p1,tiles);
                    len = RANDOM.nextInt(maxLen);
                    if (len == 0) {
                        return;
                    }
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
                    int[] posibleMax = {maxLenYPlus(p1,tiles), maxLenYPlus(p1.left(),tiles), maxLenYPlus(p1.right(),tiles)};
                    maxLen = min(posibleMax);
                    len = RANDOM.nextInt(maxLen);
                    if (len == 0) {
                        return;
                    }
                    p2 = new Position(p1.xPos, p1.yPos + len);
                    op = new OpenPoint(p1.xPos, p1.yPos + len, 2);
                    h = new Hallway(p1, p2);
                } else {
                    int[] posibleMax = {maxLenYNeg(p1,tiles), maxLenYNeg(p1.left(),tiles), maxLenYNeg(p1.right(),tiles)};
                    maxLen = min(posibleMax);
                    len = RANDOM.nextInt(maxLen);
                    if (len == 0) {
                        return;
                    }
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
                    int[] posibleMax = {maxLenXPlus(p1,tiles), maxLenXPlus(p1.up(),tiles), maxLenXPlus(p1.down(),tiles)};
                    maxLen = min(posibleMax);
                    len = RANDOM.nextInt(maxLen);
                    if (len == 0) {
                        return;
                    }
                    p2 = new Position(p1.xPos + len, p1.yPos);
                    op = new OpenPoint(p1.xPos + len, p1.yPos, 0);
                    h = new Hallway(p1, p2);
                } else {
                    int[] posibleMax = {maxLenXNeg(p1,tiles), maxLenXNeg(p1.up(),tiles), maxLenXNeg(p1.down(),tiles)};
                    maxLen = min(posibleMax);
                    len = RANDOM.nextInt(maxLen);
                    if (len == 0) {
                        return;
                    }
                    p2 = new Position(p1.xPos - len, p1.yPos);
                    op = new OpenPoint(p1.xPos - len, p1.yPos, 1);
                    h = new Hallway(p2, p1);
                }
                openPoints.add(op);
                hallways.add(h);
                addHorizontalHallway(tiles, h);
            }
            for (int i = 0, l = openPoints.size(); i < l; i++) {
                if (p1.equal(openPoints.get(i))) {
                    openPoints.remove(i);
                    break;
                }
            }
        }
    }

    private boolean randomDrt() {
        int drt = RANDOM.nextInt(2);
        return drt == 0;
    }

    /** Return the minimum item of an int array. */
    private int min(int [] list) {
        int result = list[0];
        for (int i: list) {
            if (i < result) {
                result = i;
            }
        }
        return result;
    }

    /** Return the bound of a given point in x positive direction. */
    private int maxLenXPlus(Position p, TETile[][]tiles) {
        for (int x = p.xPos + 1; x < WIDTH - 1; x++) {
            if (tiles[x][p.yPos] != Tileset.NOTHING) {
                return x - p.xPos;
            }
        }
        return WIDTH - 1 - p.xPos;
    }

    /** Return the bound of a given point in x negative direction. */
    private int maxLenXNeg(Position p, TETile[][]tiles) {
        for (int x = p.xPos - 1; x > 0; x--) {
            if (tiles[x][p.yPos] != Tileset.NOTHING) {
                return p.xPos - x;
            }
        }
        return p.xPos;
    }

    /** Return the bound of a given point in y positive direction. */
    private int maxLenYPlus(Position p, TETile[][]tiles) {
        for (int y = p.yPos + 1; y < HEIGHT- 1; y++) {
            if (tiles[p.xPos][y] != Tileset.NOTHING) {
                return y - p.yPos;
            }
        }
        return HEIGHT - 1 - p.yPos;
    }

    /** Return the bound of a given point in y negative direction. */
    private int maxLenYNeg(Position p, TETile[][]tiles) {
        for (int y = p.yPos - 1; y > 0; y--) {
            if (tiles[p.xPos][y] != Tileset.NOTHING) {
                return p.yPos - y;
            }
        }
        return p.yPos;
    }

    /** Add wall to surround all floor. */
    public void addWall(TETile[][] tiles) {
        int[][] drt = {{-1, -1}, {0, -1}, {1, -1}, {-1, 0}, {1, 0}, {-1, 1}, {0, 1}, {1, 1}};
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (tiles[x][y] == Tileset.FLOOR) {
                    for (int i = 0; i < 8; i++) {
                        int xPos = x + drt[i][0];
                        int yPos = y + drt[i][1];
                        if (xPos >= 0 && xPos < WIDTH && yPos >= 0 && yPos < HEIGHT) {
                            if (tiles[xPos][yPos] == Tileset.NOTHING) {
                                tiles[xPos][yPos] = Tileset.WALL;
                            }
                        }
                    }
                }
            }
        }
    }

    /** Generate a hallway in tiles. */
    public void roomGenerator(TETile[][] tiles) {
        if (openPoints.isEmpty()) {
            return;
        }
        int len = openPoints.size();
        int index = RANDOM.nextInt(len);
        OpenPoint p = openPoints.remove(index);
        switch (p.direction) {
            case 0:
                int maxH1 = maxLenYPlus(p, tiles);
                int height1 = RANDOM.nextInt(maxH1);
                int maxH2 = maxLenYNeg(p,tiles);
                int height2 = RANDOM.nextInt(maxH2);
                int height = height1 + height2 + 1;
                Position start = new Position(p.xPos,p.yPos - height2);
                int maxWidth = WIDTH;
                for (int i = 0; i < height; i++) {
                    int tmp = maxLenXPlus(new Position(p.xPos, start.yPos + i), tiles);
                    if (tmp < maxWidth) {
                        maxWidth = tmp;
                    }
                }
                int width = RANDOM.nextInt(maxWidth);
                Room room = new Room(start, width, height);
                addRoom(tiles, start, width, height);
                rooms.add(room);
                break;
            case 1:
                maxH1 = maxLenYPlus(p, tiles);
                height1 = RANDOM.nextInt(maxH1);
                maxH2 = maxLenYNeg(p,tiles);
                height2 = RANDOM.nextInt(maxH2);
                height = height1 + height2 + 1;
                start = new Position(p.xPos,p.yPos - height2);
                maxWidth = WIDTH;
                for (int i = 0; i < height; i++) {
                    int tmp = maxLenXNeg(new Position(p.xPos, start.yPos + i), tiles);
                    if (tmp < maxWidth) {
                        maxWidth = tmp;
                    }
                }
                width = RANDOM.nextInt(maxWidth);
                start = new Position(start.xPos - width + 1, start.yPos);
                room = new Room(start, width, height);
                addRoom(tiles, start, width, height);
                rooms.add(room);
                break;
            case 2:
                int maxW1 = maxLenXPlus(p, tiles);
                int width1 = RANDOM.nextInt(maxW1);
                int maxW2 = maxLenXNeg(p,tiles);
                int width2 = RANDOM.nextInt(maxW2);
                width = width1 + width2 + 1;
                start = new Position(p.xPos - width2,p.yPos);
                int maxHeight = HEIGHT;
                for (int i = 0; i < width; i++) {
                    int tmp = maxLenYPlus(new Position(start.xPos + i, p.yPos), tiles);
                    if (tmp < maxHeight) {
                        maxHeight = tmp;
                    }
                }
                height = RANDOM.nextInt(maxHeight);
                room = new Room(start, width, height);
                addRoom(tiles, start, width, height);
                rooms.add(room);
                break;
            default:
                maxW1 = maxLenXPlus(p, tiles);
                width1 = RANDOM.nextInt(maxW1);
                maxW2 = maxLenXNeg(p,tiles);
                width2 = RANDOM.nextInt(maxW2);
                width = width1 + width2 + 1;
                start = new Position(p.xPos - width2,p.yPos);
                maxHeight = HEIGHT;
                for (int i = 0; i < width; i++) {
                    int tmp = maxLenYNeg(new Position(start.xPos + i, p.yPos), tiles);
                    if (tmp < maxHeight) {
                        maxHeight = tmp;
                    }
                }
                height = RANDOM.nextInt(maxHeight);
                start = new Position(start.xPos, start.yPos - height + 1);
                room = new Room(start, width, height);
                addRoom(tiles, start, width, height);
                rooms.add(room);
        }
    }

    public void generator(TETile[][] map) {
        //TERenderer ter = new TERenderer();
        //ter.initialize(WIDTH, HEIGHT);

        //TETile[][] map = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                map[x][y] = Tileset.NOTHING;
            }
        }

        int hallwayCounts = RandomUtils.uniform(RANDOM,50, 100);
//        for (int i = 0; i < hallwayCounts; i++) {
//            hallwayGenerator(map);
//            //System.out.println(hallways.size());
//        }
        while (hallways.size() < hallwayCounts) {
            hallwayGenerator(map);
        }

        int roomCounts = RandomUtils.uniform(RANDOM,20, 30);
//        for (int i = 0; i < roomCounts; i++) {
//            roomGenerator(map);
//            //System.out.println(hallways.size());
//        }
        while (rooms.size() < roomCounts) {
            roomGenerator(map);
        }

        addWall(map);
        //ter.renderFrame(map);
    }
}
