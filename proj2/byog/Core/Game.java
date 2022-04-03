package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import edu.princeton.cs.introcs.StdDraw;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;


public class Game {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 30;
    private boolean gameOver;

    Game() {
        gameOver = false;
    }

    /**
     * Method used for playing a fresh game. The game should start from the main menu.
     */
    public void playWithKeyboard() {
        MapGenerator mapGenerator = null;
        boolean isMenu = true;
        drawMenu();
        while (isMenu) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                switch (c) {
                    case 'N':
                    case 'n':
                        isMenu = false;
                        long seed = solicitNCharsInput();
                        Random random = new Random(seed);
                        mapGenerator = new MapGenerator(random, WIDTH, HEIGHT);
                        mapGenerator.generator();
                        break;
                    case 'L':
                    case 'l':
                        isMenu = false;
                        mapGenerator = loadWorld();
                        break;
                    case 'Q':
                    case 'q':
                        return;
                    default:
                }
            }
        }


        StdDraw.clear(Color.BLACK);
        ter.initialize(WIDTH + 6, HEIGHT + 4, 3, 2);
        ter.renderFrame(mapGenerator.tiles);

        String s = "";
        char last = ' ';
        int lastX = (int) StdDraw.mouseX() - 3;
        int lastY = (int) StdDraw.mouseY() - 2;
        while (!gameOver) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                c = Character.toUpperCase(c);
                mapGenerator.playerMove(c);
                //ter.renderFrame(mapGenerator.tiles);
                drawHUD(s, mapGenerator.tiles);

                if (last == ':' && c == 'Q') {
                    saveWorld(mapGenerator);
                    gameOver = true;
                }
                last = c;
            }

            int x = (int) StdDraw.mouseX() - 3;
            int y = (int) StdDraw.mouseY() - 2;
            if (lastX != x || lastY != y) {
                if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
                    s = mapGenerator.tiles[x][y].description();
                }
                drawHUD(s, mapGenerator.tiles);
            }
            lastX = x;
            lastY = y;
        }
    }

    /**
     * Method used for autograding and testing the game code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The game should
     * behave exactly as if the user typed these characters into the game after playing
     * playWithKeyboard. If the string ends in ":q", the same world should be returned as if the
     * string did not end with q. For example "n123sss" and "n123sss:q" should return the same
     * world. However, the behavior is slightly different. After playing with "n123sss:q", the game
     * should save, and thus if we then called playWithInputString with the string "l", we'd expect
     * to get the exact same world back again, since this corresponds to loading the saved game.
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] playWithInputString(String input) {
        // Fill out this method to run the game using the input passed in,
        // and return a 2D tile representation of the world that would have been
        // drawn if the same inputs had been given to playWithKeyboard().

        input = input.toUpperCase();
        if (!input.startsWith("N") && !input.startsWith("L")) {
            return null;
        }

        MapGenerator mapGenerator;
        int index;
        if (input.startsWith("L")) {
            mapGenerator = loadWorld();
            index = 0;
        } else {
            index = -1;
            for (int i = 1; i < input.length(); i++) {
                if (input.charAt(i) == 'S') {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                return null;
            }
            long seed = Long.parseLong(input.substring(1, index));
            Random random = new Random(seed);
            mapGenerator = new MapGenerator(random, WIDTH, HEIGHT);
            mapGenerator.generator();
        }


        for (int i = index + 1; i < input.length(); i++) {
            if (input.charAt(i) == ':' && input.charAt(i + 1) == 'Q') {
                saveWorld(mapGenerator);
                break;
            }
            mapGenerator.playerMove(input.charAt(i));
        }

        return mapGenerator.tiles;
    }

    private static MapGenerator loadWorld() {
        File f = new File("./world.ser");
        if (f.exists()) {
            try {
                FileInputStream fs = new FileInputStream(f);
                ObjectInputStream os = new ObjectInputStream(fs);
                MapGenerator loadWorld = (MapGenerator) os.readObject();
                os.close();
                return loadWorld;
            } catch (FileNotFoundException e) {
                System.out.println("file not found");
                System.exit(0);
            } catch (IOException e) {
                System.out.println(e);
                System.exit(0);
            } catch (ClassNotFoundException e) {
                System.out.println("class not found");
                System.exit(0);
            }
        }

        /* In the case no World has been saved yet, we return a new one. */
        return null;
    }

    private static void saveWorld(MapGenerator mapGenerator) {
        if (mapGenerator == null) {
            return;
        }

        File f = new File("./world.ser");
        try {
            if (!f.exists()) {
                f.createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(f);
            ObjectOutputStream os = new ObjectOutputStream(fs);
            os.writeObject(mapGenerator);
            os.close();
        }  catch (FileNotFoundException e) {
            System.out.println("file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e);
            System.exit(0);
        }
    }

    private void drawMenu() {
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(StdDraw.BLACK);
        StdDraw.setPenColor(StdDraw.WHITE);

        Font bigFont = new Font("Monaco", Font.BOLD, 50);
        Font smallFont = new Font("Monaco", Font.BOLD, 30);

        StdDraw.setFont(bigFont);
        StdDraw.text(0.5, 0.8, "CS61B: THE GAME");
        StdDraw.setFont(smallFont);
        StdDraw.text(0.5, 0.6, "New Game (N)");
        StdDraw.text(0.5, 0.5, "Load Game (L)");
        StdDraw.text(0.5, 0.4, "Quit (Q)");
        StdDraw.show();
    }

    private void drawFrame(String s) {
        // Draw actual text
        StdDraw.clear(Color.BLACK);

        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(0.5, 0.5, s);
        StdDraw.show();
    }

    private Long solicitNCharsInput() {
        String seed = "";
        while (true) {
            if (seed.equals("")) {
                drawFrame("Please enter the seed: ");
            }
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 'S' || c == 's') {
                    StdDraw.pause(1000);
                    return Long.parseLong(seed);
                }
                seed += c;
                drawFrame(seed);
            }
        }
    }

    private void drawHUD(String s, TETile[][] world) {
//        Font font = new Font("Monaco", Font.BOLD, 20);
//        StdDraw.setFont(font);
        StdDraw.clear(Color.BLACK);
        ter.renderFrame(world);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(2, HEIGHT + 3, s);
        StdDraw.show();
    }
}
