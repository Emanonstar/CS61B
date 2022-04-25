import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture p;

    public SeamCarver(Picture picture) {
        p = picture;
    }
    // current picture
    public Picture picture() {
        return new Picture(p);
    }

    // width of current picture
    public int width() {
        return p.width();
    }

    // height of current picture
    public int height() {
        return p.height();
    }
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (!isInBounds(x, y)) {
            throw new IndexOutOfBoundsException();
        }
        double xGradient = gradient(p.get(xMinus1(x), y), p.get(xPlus1(x), y));
        double yGradient = gradient(p.get(x, yMinus1(y)), p.get(x, yPlus1(y)));
        return xGradient + yGradient;
    }

    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < width() && y >= 0 && y < height();
    }

    private double gradient(Color c1, Color c2) {
        int diffRed = c1.getRed() - c2.getRed();
        int diffGreen = c1.getGreen() - c2.getGreen();
        int diffBlue = c1.getBlue() - c2.getBlue();
        return diffRed * diffRed + diffGreen * diffGreen + diffBlue * diffBlue;
    }
    private int xMinus1(int x) {
        if (x == 0) {
            return width() - 1;
        }
        return x - 1;
    }

    private int yMinus1(int y) {
        if (y == 0) {
            return height() - 1;
        }
        return y - 1;
    }

    private int xPlus1(int x) {
        if (x == width() - 1) {
            return 0;
        }
        return x + 1;
    }

    private int yPlus1(int y) {
        if (y == height() - 1) {
            return 0;
        }
        return y + 1;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] e = new double[width()][height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                e[x][y] = energy(x, y);
            }
        }

        double[][] M = new double[width()][height()];
        for (int x = 0; x < width(); x++) {
            M[x][0] = e[x][0];
        }


        for (int y = 1; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                double min = Double.MAX_VALUE;
                int[] drts = {-1, 0, 1};
                for (int drt: drts) {
                    int xx = x + drt;
                    int yy = y - 1;
                    if (isInBounds(xx, yy)) {
                        if (M[xx][yy] < min) {
                            min = M[xx][yy];
                        }
                    }
                }
                M[x][y] = min + e[x][y];
            }
        }

        double minM = Double.MAX_VALUE;
        int minIndex = 1;
        for (int x = 0; x < width(); x++) {
            if (M[x][height() - 1] < minM) {
                minM = M[x][height() - 1];
                minIndex = x;
            }
        }
        int[] seam = new int[height()];
        seam[height() - 1] = minIndex;

        for (int y = height() - 1; y > 0; y--) {
            int lastX = 0;
            int x = seam[y];
            double goalM = M[x][y] - e[x][y];
            int[] drts = {-1, 0, 1};
            for (int drt: drts) {
                int xx = x + drt;
                int yy = y - 1;
                if (isInBounds(xx, yy)) {
                    if (M[xx][yy] == goalM) {
                        lastX = xx;
                    }
                }
            }
            seam[y - 1] = lastX;
        }
        return seam;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        p = transpose(p);
        int[] seam = findVerticalSeam();
        p = transpose(p);
        return seam;
    }

    private Picture transpose(Picture picture) {
        Picture tmp = new Picture(height(), width());
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                tmp.set(y, x, picture.get(x, y));
            }
        }
        return tmp;
    }

    // remove vertical seam from picture
    public void removeVerticalSeam(int[] seam) {
        if (seam.length != height() || !validSeam(seam)) {
            throw new IllegalArgumentException();
        }
        SeamRemover.removeVerticalSeam(p, seam);
    }

    // remove horizontal seam from picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam.length != width() || !validSeam(seam)) {
            throw new IllegalArgumentException();
        }
        SeamRemover.removeHorizontalSeam(p, seam);
    }

    private boolean validSeam(int[] seam) {
        for (int i = 0; i < seam.length - 1; i++) {
            int diff = seam[i] - seam[i + 1];
            if (diff < -1 || diff > 1) {
                return false;
            }
        }
        return true;
    }
}
