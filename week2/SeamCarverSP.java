import java.util.LinkedList;

public class SeamCarverSP {
    private int[][] distTo;
    private int[][] weight;

    /**
     * pathTo stored as delta where
     * 0 is a link from top vertex
     * -1 is a link from top left vertex
     * 1 is a link from top right vertex
     */
    private byte[][] pathTo;

    private int width;
    private int height;

    private int[] shortestPath;

    public SeamCarverSP(int[][] energy) {
        this(energy, false);
    }

    public SeamCarverSP(int[][] energy, boolean horizontal) {
        if (horizontal) {
            weight = transposeMatrix(energy);
        } else {
            weight = energy;
        }

        height = weight.length;
        width = weight[0].length;

        pathTo = new byte[height][width];
        distTo = new int[height][width];


        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0) {
                    distTo[i][j] = weight[i][j];
                    pathTo[i][j] = 0;
                } else {
                    distTo[i][j] = Integer.MAX_VALUE;
                    pathTo[i][j] = Byte.MAX_VALUE;
                }
            }
        }

        // relax all edges
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                relaxAdj(i, j);
            }
        }

        computeSP();
    }

    public int[] shortestPath() {
        if (shortestPath == null) {
            computeSP();
        }
        return shortestPath;
    }

    private int[][] transposeMatrix(int[][] matrix) {
        int[][] result = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    private void computeSP() {
        LinkedList<Integer> result = new LinkedList<>();

        int minJ = 0;
        for (int j = 1; j < width; j++) {
            if (distTo[height - 1][j] < distTo[height - 1][minJ]) {
                minJ = j;
            }
        }

        int j = minJ;
        result.addFirst(j);

        for (int i = height - 1; i > 0; i--) {
            j += pathTo[i][j];
            result.addFirst(j);
        }

        shortestPath = new int[result.size()];
        for (int i = 0; i < result.size(); i++) {
            shortestPath[i] = result.get(i);
        }
    }

    private void relaxAdj(int i, int j) {
        if (i + 1 == height) return;
        relax(i, j, i + 1, j);
        if (j - 1 >= 0) relax(i, j, i + 1, j - 1);
        if (j + 1 < width) relax(i, j, i + 1, j + 1);
    }

    private void relax(int i, int j, int i2, int j2) {
        int vDist = distTo[i][j];
        int wDist = distTo[i2][j2];
        int wWeight = weight[i2][j2];

        if (vDist + wWeight < wDist) {
            distTo[i2][j2] = vDist + wWeight;
            pathTo[i2][j2] = (byte) (j - j2);
        }
    }

}
