package byog.lab6;

import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    private final int width;
    private final int height;
    private int round;
    private final Random rand;
    private boolean gameOver;
    private boolean playerTurn;
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }
        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
     * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
     */
    public MemoryGame(int width, int height, long seed) {
        this.width = width;
        this.height = height;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
        // Initialize random number generator
        rand = new Random(seed);
    }

    /** Generate random string of letters of length n. */
    public String generateRandomString(int n) {
        String result = "";
        for (int i = 0; i < n; i++) {
            int index = rand.nextInt(CHARACTERS.length);
            result += CHARACTERS[index];
        }
        return result;
    }

    /** Take the string and display it in the center of the screen.
        If game is not over, display relevant game information at the top of the screen. */
    public void drawFrame(String s) {
        // Draw actual text
        StdDraw.clear(Color.BLACK);
        Font bigFont = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(bigFont);
        StdDraw.setPenColor(Color.white);
        StdDraw.text(width / 2, height / 2, s);

        // Draw GUI
        if (!gameOver) {
            Font smallFont = new Font("Monaco", Font.BOLD, 20);
            StdDraw.setFont(smallFont);
            StdDraw.textLeft(1, height - 1, "Round: " + round);
            if (playerTurn) {
                StdDraw.text(width / 2, height - 1, "Type!");
            } else {
                StdDraw.text(width / 2, height - 1, "Watch!");
            }
            StdDraw.textRight(width - 1, height - 1, ENCOURAGEMENT[round % ENCOURAGEMENT.length]);
        }

        StdDraw.show();
    }

    /** Display each character in letters, making sure to blank the screen between letters. */
    public void flashSequence(String letters) {
        for (int i = 0; i < letters.length(); i++) {
            drawFrame(letters.substring(i, i + 1));
            StdDraw.pause(1000);
            drawFrame("");
            StdDraw.pause(500);
        }
    }

    /** Read n letters of player input. */
    public String solicitNCharsInput(int n) {
        String result = "";
        int i = 0;
        while (i < n) {
            drawFrame(result);
            if (StdDraw.hasNextKeyTyped()) {
                result += StdDraw.nextKeyTyped();
                drawFrame(result);
                i++;
            }
        }
        StdDraw.pause(500);
        return result;
    }

    public void startGame() {
        // Set any relevant variables before the game starts
        round = 1;
        gameOver = false;
        playerTurn = false;

        // Establish Game loop
        while (!gameOver) {
            drawFrame("Round: " + round);
            StdDraw.pause(1500);

            String answer = generateRandomString(round);
            flashSequence(answer);

            playerTurn = true;
            String actual = solicitNCharsInput(round);

            if (answer.equals(actual)) {
                round += 1;
                playerTurn = false;
                drawFrame("Correct, well done!");
                StdDraw.pause(1500);
            } else {
                gameOver = true;
            }
        }
        drawFrame("Game Over! You made it to round:" + round);
    }

}
