package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final Random RANDOM = new Random(2222);

    private static void addHexagon(TETile[][] tiles, int xPosition, int yPosition, int size) {
        TETile randomTile = randomTile();
        for (int r = 0; r < 2 * size; r++) {
            int xStart = xStart(r, size, xPosition);
            int xNumber = rowNumber(r, size);
            int y = r + yPosition;
            for (int x = xStart; x < xStart + xNumber; x++) {
                tiles[x][y] = randomTile;
            }
        }
    }

    /** Returns the tiles' start x position when row and size of the hexagon are given. */
    private static int xStart(int row, int size, int xPosition) {
        if (row < size) {
            return xPosition - row;
        }
        int lastRowIndex = 2 * size - 1;
        return xPosition - (lastRowIndex - row);
    }

    /** Returns the tiles' number when row and size of the hexagon are given. */
    private static int rowNumber(int row, int size) {
        if (row < size) {
            return size + 2 * row;
        }
        int lastRowIndex = 2 * size - 1;
        return size + 2 * (lastRowIndex - row);
    }

    /** Draws a tesselation of hexagons given size and start position. */
    public static void drawHexagons(TETile[][] tiles, int xBegin, int yBegin, int size) {
        drawColHexagons(tiles, xBegin, yBegin, size, 5);
        drawColHexagons(tiles, xBegin - (2 * size - 1), yBegin + size, size, 4);
        drawColHexagons(tiles, xBegin + (2 * size - 1), yBegin + size, size, 4);
        drawColHexagons(tiles, xBegin - 2 * (2 * size - 1), yBegin + 2 * size, size, 3);
        drawColHexagons(tiles, xBegin + 2 * (2 * size - 1), yBegin + 2 * size, size, 3);
    }

    /** Draws a column of hexagons given size and start position recursively. */
    private static void drawColHexagons(TETile[][] tiles, int xBegin, int yBegin,
                                        int size, int num) {
        if (xBegin - (size - 1) < 0 || xBegin + (size - 1) >= WIDTH
                || yBegin + (2 * size - 1) >= HEIGHT || yBegin < 0) {
            return;
        }
        if (num == 0) {
            return;
        }
        addHexagon(tiles, xBegin, yBegin, size);
        drawColHexagons(tiles, xBegin, yBegin + 2 * size, size, num - 1);
    }

    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(8);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOOR;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.FLOWER;
            case 4: return Tileset.WATER;
            case 5: return Tileset.SAND;
            case 6: return Tileset.MOUNTAIN;
            default: return Tileset.TREE;
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] hexWorld = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                hexWorld[x][y] = Tileset.NOTHING;
            }
        }
        int size = 3;
        drawHexagons(hexWorld, (WIDTH - size) / 2, 0, size);
        ter.renderFrame(hexWorld);
    }

    @Test
    public void testRowNumber() {
        assertEquals(2, HexWorld.rowNumber(0, 2));
        assertEquals(2, HexWorld.rowNumber(3, 2));
        assertEquals(4, HexWorld.rowNumber(1, 2));
        assertEquals(4, HexWorld.rowNumber(2, 2));

        assertEquals(5, HexWorld.rowNumber(0, 5));
        assertEquals(5, HexWorld.rowNumber(9, 5));
        assertEquals(13, HexWorld.rowNumber(5, 5));
        assertEquals(13, HexWorld.rowNumber(4, 5));
    }

    @Test
    public void testColStart() {
        assertEquals(10, HexWorld.xStart(0, 2, 10));
        assertEquals(10, HexWorld.xStart(3, 2, 10));
        assertEquals(9, HexWorld.xStart(2, 2, 10));
        assertEquals(9, HexWorld.xStart(1, 2, 10));

        assertEquals(10, HexWorld.xStart(0, 3, 10));
        assertEquals(9, HexWorld.xStart(1, 3, 10));
        assertEquals(10, HexWorld.xStart(5, 3, 10));
        assertEquals(8, HexWorld.xStart(3, 3, 10));
    }
}
