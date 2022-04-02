package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MapGenerator implements Serializable {
    private final int WIDTH;
    private final int HEIGHT;
    private final Random RANDOM;
    private final List<Room> rooms = new ArrayList<>();
    private final List<Hallway> hallways = new ArrayList<>();
    private final List<OpenPoint> openPoints = new ArrayList<>();
    private final int MAXHALLWAYLENGTH;
    private final int MAXROOMSIDE;
    TETile[][] tiles;
    Position player;

    MapGenerator(Random random, int width, int height) {
        RANDOM = random;
        WIDTH = width;
        HEIGHT = height;
        tiles = new TETile[width][height];
        MAXHALLWAYLENGTH = WIDTH / 2;
        MAXROOMSIDE = HEIGHT / 2;
    }

    private void addRoom(Position p, int width, int height) {
        addRectangular(p, width, height, Tileset.FLOOR);
    }

    private void addHorizontalHallway(Hallway h) {
        addRectangular(h.getStart(), h.getEnd().getXPos() - h.getStart().getXPos(),
                1, Tileset.FLOOR);
    }

    private void addVerticalHallway(Hallway h) {
        addRectangular(h.getStart(), 1, h.getEnd().getYPos() - h.getStart().getYPos(),
                Tileset.FLOOR);
    }

    private void addRectangular(Position p, int width, int height, TETile tile) {
        for (int x = p.getXPos(); x < p.getXPos() + width; x++) {
            for (int y = p.getYPos(); y < p.getYPos() + height; y++) {
                tiles[x][y] = tile;
            }
        }
    }

    /** Generate a hallway in tiles. */
    public void hallwayGenerator() {
        if (hallways.isEmpty()) {
            firstHallwayGenerator();
        } else {
            int index = RANDOM.nextInt(hallways.size());
            Hallway h0 = hallways.get(index);
            Position p1 = h0.randomPoint(RANDOM);
            if (h0.isHorizontal()) {
                verticalHallwayGenerator(p1);
            } else {
                horizontalHallwayGenerator(p1);
            }
            for (int i = 0, l = openPoints.size(); i < l; i++) {
                if (p1.equal(openPoints.get(i))) {
                    openPoints.remove(i);
                    break;
                }
            }
        }
    }

    /** Generate a hallway in empty tiles. */
    private void firstHallwayGenerator() {
        int xPos = RANDOM.nextInt(WIDTH - 2) + 1;
        int yPos = RANDOM.nextInt(HEIGHT - 2) + 1;
        Position p1 = new Position(xPos, yPos);
        Position p2;
        OpenPoint op1;
        OpenPoint op2;
        Hallway h;
        int maxLen;
        int len;
        if (randomDrt()) {
            if (randomDrt()) {
                maxLen = min(maxLenXPlus(p1), MAXHALLWAYLENGTH);
                len = RANDOM.nextInt(maxLen);
                if (len == 0) {
                    return;
                }
                p2 = new Position(xPos + len, yPos);
                op1 = new OpenPoint(xPos, yPos, 1);
                op2 = new OpenPoint(xPos + len, yPos, 0);
                h = new Hallway(p1, p2);
            } else {
                maxLen = min(maxLenXNeg(p1), MAXHALLWAYLENGTH);
                len = RANDOM.nextInt(maxLen);
                if (len == 0) {
                    return;
                }
                p2 = new Position(xPos - len, yPos);
                op1 = new OpenPoint(xPos, yPos, 0);
                op2 = new OpenPoint(xPos - len, yPos, 1);
                h = new Hallway(p2, p1);
            }
            addHorizontalHallway(h);
        } else {
            if (randomDrt()) {
                maxLen = min(maxLenYPlus(p1), MAXHALLWAYLENGTH);
                len = RANDOM.nextInt(maxLen);
                if (len == 0) {
                    return;
                }
                p2 = new Position(xPos, yPos + len);
                op1 = new OpenPoint(xPos, yPos, 3);
                op2 = new OpenPoint(xPos, yPos + len, 2);
                h = new Hallway(p1, p2);
            } else {
                maxLen = min(maxLenYNeg(p1), MAXHALLWAYLENGTH);
                len = RANDOM.nextInt(maxLen);
                if (len == 0) {
                    return;
                }
                p2 = new Position(xPos, yPos - len);
                op1 = new OpenPoint(xPos, yPos, 2);
                op2 = new OpenPoint(xPos, yPos - len, 3);
                h = new Hallway(p2, p1);
            }
            addVerticalHallway(h);
        }
        openPoints.add(op1);
        openPoints.add(op2);
        hallways.add(h);
    }

    /** Generate a horizontal hallway in tiles begins at an existing hallway. */
    private void horizontalHallwayGenerator(Position p1) {
        int maxLen;
        int len;
        Position p2;
        OpenPoint op;
        Hallway h;
        if (randomDrt()) {
            int[] possibleMax = {maxLenXPlus(p1), maxLenXPlus(p1.up()),
                    maxLenXPlus(p1.down()), MAXHALLWAYLENGTH};
            maxLen = min(possibleMax);
            len = RANDOM.nextInt(maxLen);
            if (len == 0) {
                return;
            }
            p2 = new Position(p1.getXPos() + len, p1.getYPos());
            op = new OpenPoint(p1.getXPos() + len, p1.getYPos(), 0);
            h = new Hallway(p1, p2);
        } else {
            int[] possibleMax = {maxLenXNeg(p1), maxLenXNeg(p1.up()),
                    maxLenXNeg(p1.down()), MAXHALLWAYLENGTH};
            maxLen = min(possibleMax);
            len = RANDOM.nextInt(maxLen);
            if (len == 0) {
                return;
            }
            p2 = new Position(p1.getXPos() - len, p1.getYPos());
            op = new OpenPoint(p1.getXPos() - len, p1.getYPos(), 1);
            h = new Hallway(p2, p1);
        }
        openPoints.add(op);
        hallways.add(h);
        addHorizontalHallway(h);
    }

    /** Generate a vertical hallway in tiles begins at an existing hallway. */
    private void verticalHallwayGenerator(Position p1) {
        int maxLen;
        int len;
        Position p2;
        OpenPoint op;
        Hallway h;
        if (randomDrt()) {
            int[] possibleMax = {maxLenYPlus(p1), maxLenYPlus(p1.left()),
                    maxLenYPlus(p1.right()), MAXHALLWAYLENGTH};
            maxLen = min(possibleMax);
            len = RANDOM.nextInt(maxLen);
            if (len == 0) {
                return;
            }
            p2 = new Position(p1.getXPos(), p1.getYPos() + len);
            op = new OpenPoint(p1.getXPos(), p1.getYPos() + len, 2);
            h = new Hallway(p1, p2);
        } else {
            int[] possibleMax = {maxLenYNeg(p1), maxLenYNeg(p1.left()),
                    maxLenYNeg(p1.right()), MAXHALLWAYLENGTH};
            maxLen = min(possibleMax);
            len = RANDOM.nextInt(maxLen);
            if (len == 0) {
                return;
            }
            p2 = new Position(p1.getXPos(), p1.getYPos() - len);
            op = new OpenPoint(p1.getXPos(), p1.getYPos() - len, 3);
            h = new Hallway(p2, p1);
        }
        openPoints.add(op);
        hallways.add(h);
        addVerticalHallway(h);
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

    private int min(int i1, int i2) {
        if (i1 < i2) {
            return i1;
        }
        return i2;
    }

    /** Return the bound of a given point in x positive direction. */
    private int maxLenXPlus(Position p) {
        for (int x = p.getXPos() + 1; x < WIDTH - 1; x++) {
            if (tiles[x][p.getYPos()] != Tileset.NOTHING) {
                return x - p.getXPos();
            }
        }
        return WIDTH - 1 - p.getXPos();
    }

    /** Return the bound of a given point in x negative direction. */
    private int maxLenXNeg(Position p) {
        for (int x = p.getXPos() - 1; x > 0; x--) {
            if (tiles[x][p.getYPos()] != Tileset.NOTHING) {
                return p.getXPos() - x;
            }
        }
        return p.getXPos();
    }

    /** Return the bound of a given point in y positive direction. */
    private int maxLenYPlus(Position p) {
        for (int y = p.getYPos() + 1; y < HEIGHT - 1; y++) {
            if (tiles[p.getXPos()][y] != Tileset.NOTHING) {
                return y - p.getYPos();
            }
        }
        return HEIGHT - 1 - p.getYPos();
    }

    /** Return the bound of a given point in y negative direction. */
    private int maxLenYNeg(Position p) {
        for (int y = p.getYPos() - 1; y > 0; y--) {
            if (tiles[p.getXPos()][y] != Tileset.NOTHING) {
                return p.getYPos() - y;
            }
        }
        return p.getYPos();
    }

    /** Add wall to surround all floor. */
    public void addWall() {
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

    /** Generate a room in tiles from an OpenPoint. */
    public void roomGenerator() {
        if (openPoints.isEmpty()) {
            return;
        }
        int len = openPoints.size();
        int index = RANDOM.nextInt(len);
        OpenPoint p = openPoints.remove(index);
        switch (p.getDirection()) {
            case 0:
                rightRoomGenerator(p);
                break;
            case 1:
                leftRoomGenerator(p);
                break;
            case 2:
                upRoomGenerator(p);
                break;
            default:
                downRoomGenerator(p);
        }
    }

    /** Generator a right room from an OpenPoint. */
    private void rightRoomGenerator(OpenPoint p) {
        int maxH1 = min(maxLenYPlus(p), MAXROOMSIDE / 2);
        int height1 = RANDOM.nextInt(maxH1);
        int maxH2 = min(maxLenYNeg(p), MAXROOMSIDE / 2);
        int height2 = RANDOM.nextInt(maxH2);
        int height = height1 + height2 + 1;
        Position start = new Position(p.getXPos(), p.getYPos() - height2);
        int maxWidth = MAXROOMSIDE;
        for (int i = 0; i < height; i++) {
            int tmp = maxLenXPlus(new Position(p.getXPos(), start.getYPos() + i));
            if (tmp < maxWidth) {
                maxWidth = tmp;
            }
        }
        int width = RANDOM.nextInt(maxWidth);
        Room room = new Room(start, width, height);
        addRoom(start, width, height);
        rooms.add(room);
    }

    /** Generator a left room from an OpenPoint. */
    private void leftRoomGenerator(OpenPoint p) {
        int maxH1 = min(maxLenYPlus(p), MAXROOMSIDE / 2);
        int height1 = RANDOM.nextInt(maxH1);
        int maxH2 = min(maxLenYNeg(p), MAXROOMSIDE / 2);
        int height2 = RANDOM.nextInt(maxH2);
        int height = height1 + height2 + 1;
        Position start = new Position(p.getXPos(), p.getYPos() - height2);
        int maxWidth = MAXROOMSIDE;
        for (int i = 0; i < height; i++) {
            int tmp = maxLenXNeg(new Position(p.getXPos(), start.getYPos() + i));
            if (tmp < maxWidth) {
                maxWidth = tmp;
            }
        }
        int width = RANDOM.nextInt(maxWidth);
        start = new Position(start.getXPos() - width + 1, start.getYPos());
        Room room = new Room(start, width, height);
        addRoom(start, width, height);
        rooms.add(room);
    }

    /** Generator an up room from an OpenPoint. */
    private void upRoomGenerator(OpenPoint p) {
        int maxW1 = min(maxLenXPlus(p), MAXROOMSIDE);
        int width1 = RANDOM.nextInt(maxW1);
        int maxW2 = min(maxLenXNeg(p), MAXROOMSIDE);
        int width2 = RANDOM.nextInt(maxW2);
        int width = width1 + width2 + 1;
        Position start = new Position(p.getXPos() - width2, p.getYPos());
        int maxHeight = MAXROOMSIDE;
        for (int i = 0; i < width; i++) {
            int tmp = maxLenYPlus(new Position(start.getXPos() + i, p.getYPos()));
            if (tmp < maxHeight) {
                maxHeight = tmp;
            }
        }
        int height = RANDOM.nextInt(maxHeight);
        Room room = new Room(start, width, height);
        addRoom(start, width, height);
        rooms.add(room);
    }

    /** Generator a down room from an OpenPoint. */
    private void downRoomGenerator(OpenPoint p) {
        int maxW1 = min(maxLenXPlus(p), MAXROOMSIDE);
        int width1 = RANDOM.nextInt(maxW1);
        int maxW2 = min(maxLenXNeg(p), MAXROOMSIDE);
        int width2 = RANDOM.nextInt(maxW2);
        int width = width1 + width2 + 1;
        Position start = new Position(p.getXPos() - width2, p.getYPos());
        int maxHeight = MAXROOMSIDE;
        for (int i = 0; i < width; i++) {
            int tmp = maxLenYNeg(new Position(start.getXPos() + i, p.getYPos()));
            if (tmp < maxHeight) {
                maxHeight = tmp;
            }
        }
        int height = RANDOM.nextInt(maxHeight);
        start = new Position(start.getXPos(), start.getYPos() - height + 1);
        Room room = new Room(start, width, height);
        addRoom(start, width, height);
        rooms.add(room);
    }

    private void playerGenerator() {
        int playerXPos = RANDOM.nextInt(WIDTH);
        int playerYPos = RANDOM.nextInt(HEIGHT);
        while (tiles[playerXPos][playerYPos] != Tileset.FLOOR) {
            playerXPos = RANDOM.nextInt(WIDTH);
            playerYPos = RANDOM.nextInt(HEIGHT);
        }
        tiles[playerXPos][playerYPos] = Tileset.PLAYER;
        player = new Position(playerXPos, playerYPos);
    }

    public TETile[][] generator() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }

        int hallwayCounts = RandomUtils.uniform(RANDOM, 50, 100);
        int roomCounts = RandomUtils.uniform(RANDOM, 20, 30);

        while (hallways.size() < hallwayCounts) {
            hallwayGenerator();
        }

        while (rooms.size() < roomCounts) {
            roomGenerator();
        }

        addWall();
        playerGenerator();
        return tiles;
    }

    public void playerMove (Character m) {
        Position newP = player;
        switch (m) {
            case 'W':
                newP = player.up();
                break;
            case 'S':
                newP = player.down();
                break;
            case 'A':
                newP = player.left();
                break;
            case 'D':
                newP = player.right();
                break;
            default:
        }
        if (tiles[newP.getXPos()][newP.getYPos()].equals(Tileset.FLOOR)) {
            tiles[player.getXPos()][player.getYPos()] = Tileset.FLOOR;
            tiles[newP.getXPos()][newP.getYPos()] = Tileset.PLAYER;
            player = newP;
        }
    }
}
