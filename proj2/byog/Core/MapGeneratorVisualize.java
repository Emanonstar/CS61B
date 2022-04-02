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
        long seed = 236690;
        Random random = new Random(seed);
        MapGenerator mapGenerator = new MapGenerator(random, WIDTH, HEIGHT);
        mapGenerator.generator();
        ter.renderFrame(mapGenerator.tiles);
        System.out.println(TETile.toString(mapGenerator.tiles));
        System.out.println(mapGenerator.player.getYPos());
    }
}
