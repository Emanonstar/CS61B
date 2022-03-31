package byog.Core;
import org.junit.Test;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

public class MapGeneratorVisualize {
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;



    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] map = new TETile[WIDTH][HEIGHT];
        long SEED = 123456;
        Random random = new Random(SEED);
        MapGenerator mapGenerator = new MapGenerator(random);
        mapGenerator.generator(map);
        ter.renderFrame(map);
        System.out.println(TETile.toString(map));
    }
}

