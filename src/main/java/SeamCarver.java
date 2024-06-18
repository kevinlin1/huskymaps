import graphs.shortestpaths.DijkstraSolver;
import seamfinding.energy.DualGradientEnergyFunction;
import seamfinding.energy.EnergyFunction;
import seamfinding.AdjacencyListSeamFinder;
import seamfinding.Picture;
import seamfinding.SeamFinder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Seam carving, an approach for content-aware image resizing. Given a {@link Picture}, an {@link EnergyFunction}, and a
 * {@link SeamFinder} algorithm, {@link #removeHorizontal()} or {@link #removeVertical()} seams from the picture.
 *
 * @see Picture
 * @see EnergyFunction
 * @see SeamFinder
 */
public class SeamCarver {
    /**
     * Path to the input image.
     */
    private static final String INPUT_PATH = "data/seamcarving/HJoceanSmall.png";
    /**
     * Path to the output image.
     */
    private static final String OUTPUT_PATH = "data/seamcarving/result.png";
    /**
     * The {@link EnergyFunction} for determining the minimum-cost seam.
     */
    private final EnergyFunction f;
    /**
     * The {@link SeamFinder} implementation.
     */
    private final SeamFinder seamFinder;
    /**
     * The {@link Picture}.
     */
    private Picture picture;

    /**
     * Constructs a seam carver by reading the {@link Picture} from the file, using the given {@link EnergyFunction} and
     * {@link SeamFinder} implementations.
     *
     * @param file       the file path to the image.
     * @param f          the {@link EnergyFunction}.
     * @param seamFinder the {@link SeamFinder}.
     * @throws IOException if an error occurs during reading.
     */
    public SeamCarver(File file, EnergyFunction f, SeamFinder seamFinder) throws IOException {
        if (file == null || f == null || seamFinder == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }
        this.picture = new Picture(file);
        this.f = f;
        this.seamFinder = seamFinder;
    }

    public static void main(String[] args) throws IOException {
        EnergyFunction f = new DualGradientEnergyFunction();
        SeamFinder seamFinder = new AdjacencyListSeamFinder(DijkstraSolver::new);
        SeamCarver seamCarver = new SeamCarver(new File(INPUT_PATH), f, seamFinder);

        int originalWidth = seamCarver.picture.width();
        int originalHeight = seamCarver.picture.height();
        System.out.println("Current size is " + originalWidth + "x" + originalHeight);
        int newWidth = promptForSize("width", originalWidth);
        int newHeight = promptForSize("height", originalHeight);

        System.out.print("Reducing width... ");
        for (int i = 0; i < originalWidth - newWidth; i++) {
            seamCarver.removeVertical();
            if (i % 10 == 0) {
                System.out.print(originalWidth - i + " ");
            }
        }
        System.out.println();

        System.out.print("Reducing height... ");
        for (int i = 0; i < originalHeight - newHeight; i++) {
            seamCarver.removeHorizontal();
            if (i % 10 == 0) {
                System.out.print(originalHeight - i  + " ");
            }
        }
        seamCarver.picture.save(new File(OUTPUT_PATH));
    }

    /**
     * Gets new size for resizing image.
     *
     * @param dimension the type of dimension ("width" or "height").
     * @param max       the current dimension of this type.
     * @return          the new dimension to resize to.
     */
    private static int promptForSize(String dimension, int max) {
        Scanner console = new Scanner(System.in);
        while (true) {
            System.out.print("Enter a new " + dimension + " in [3, " + max + "]: ");
            int size = console.nextInt();
            if (3 <= size && size <= max) {
                return size;
            }
        }
    }

    /**
     * Removes and returns a minimum-cost horizontal seam from the picture.
     *
     * @return a minimum-cost horizontal seam.
     */
    public List<Integer> removeHorizontal() {
        List<Integer> seam = seamFinder.findHorizontal(picture, f);
        Picture result = new Picture(picture.width(), picture.height() - 1);
        for (int x = 0; x < picture.width(); x += 1) {
            for (int y = 0; y < seam.get(x); y += 1) {
                result.set(x, y, picture.get(x, y));
            }
            for (int y = seam.get(x); y < picture.height() - 1; y += 1) {
                result.set(x, y, picture.get(x, y + 1));
            }
        }
        picture = result;
        return seam;
    }

    /**
     * Removes and returns a minimum-cost vertical seam from the picture.
     *
     * @return a minimum-cost vertical seam.
     */
    public List<Integer> removeVertical() {
        List<Integer> seam = seamFinder.findVertical(picture, f);
        Picture result = new Picture(picture.width() - 1, picture.height());
        for (int y = 0; y < picture.height(); y += 1) {
            for (int x = 0; x < seam.get(y); x += 1) {
                result.set(x, y, picture.get(x, y));
            }
            for (int x = seam.get(y); x < picture.width() - 1; x += 1) {
                result.set(x, y, picture.get(x + 1, y));
            }
        }
        picture = result;
        return seam;
    }
}
