import java.awt.Color;

public class SeamCarver {
    private Picture picture;
    private int[][] energy;
    private static final int BORDER_ENERGY = 195075;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);
        computeEnergy();
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height())
            throw new IndexOutOfBoundsException();
        return energy[y][x];
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return new SeamCarverSP(this.energy, true).shortestPath();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return new SeamCarverSP(this.energy).shortestPath();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new NullPointerException();
        if (seam.length != width()) throw new IllegalArgumentException();

        Picture tmp = new Picture(width(), height() - 1);

        for (int i = 0; i < width(); i++) {
            int s = seam[i];
            for (int j = 0; j < height() - 1; j++) {
                if (j < s) {
                    tmp.set(i, j, picture.get(i, j));
                } else {
                    tmp.set(i, j, picture.get(i, j + 1));
                }
            }
        }

        picture = tmp;
        computeEnergy();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new NullPointerException();
        if (seam.length != height()) throw new IllegalArgumentException();


        Picture tmp = new Picture(width() - 1, height());

        for (int i = 0; i < height(); i++) {
            int s = seam[i];
            for (int j = 0; j < width() - 1; j++) {
                if (j < s) {
                    tmp.set(j, i, picture.get(j, i));
                } else {
                    tmp.set(j, i, picture.get(j + 1, i));
                }
            }
        }

        picture = tmp;
        computeEnergy();
    }

    private void computeEnergy() {
        energy = new int[height()][width()];

        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                int aX = j + 1,
                    bX = j - 1,
                    cY = i + 1,
                    dY = i - 1;

                if (i == 0 || j == 0 || i == height() - 1 || j == width() - 1) {
                    energy[i][j] = BORDER_ENERGY;
                } else {
                    Color a = picture.get(aX, i);
                    Color b = picture.get(bX, i);
                    Color c = picture.get(j, cY);
                    Color d = picture.get(j, dY);

                    double dx = Math.pow(a.getRed() - b.getRed(), 2) +
                            Math.pow(a.getBlue() - b.getBlue(), 2) +
                            Math.pow(a.getGreen() - b.getGreen(), 2);

                    double dy = Math.pow(c.getRed() - d.getRed(), 2) +
                            Math.pow(c.getBlue() - d.getBlue(), 2) +
                            Math.pow(c.getGreen() - d.getGreen(), 2);

                    energy[i][j] = (int) (dx + dy);
                }
            }
        }
    }
}
