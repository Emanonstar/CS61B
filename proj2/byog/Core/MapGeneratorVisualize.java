package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import java.util.Random;

public class MapGeneratorVisualize {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] map = new TETile[WIDTH][HEIGHT];
        long seed = 946080;
        Random random = new Random(seed);
        MapGenerator mapGenerator = new MapGenerator(random);
        mapGenerator.generator(map);
        ter.renderFrame(map);
        System.out.println(TETile.toString(map));
    }
}
