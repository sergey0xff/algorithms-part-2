/**
 * **********************************************************************
 * Compilation:  javac ShowEnergy.java
 * Execution:    java ShowEnergy input.png
 * Dependencies: SeamCarver.java SCUtility.java Picture.java StdDraw.java
 * <p/>
 * <p/>
 * Read image from file specified as command line argument. Show original
 * image (only useful if image is large enough).
 * <p/>
 * ***********************************************************************
 */

public class ShowEnergy {

    public static void main(String[] args) {
        String filename = "inputs/seamCarving/3x7.png";
        if (args.length > 0) {
            filename = args[0];
        }
        Picture inputImg = new Picture(filename);
        System.out.printf("image is %d columns by %d rows\n", inputImg.width(), inputImg.height());
        inputImg.show();
        SeamCarver sc = new SeamCarver(inputImg);

        System.out.printf("Displaying energy calculated for each pixel.\n");
        SCUtility.showEnergy(sc);

    }

}