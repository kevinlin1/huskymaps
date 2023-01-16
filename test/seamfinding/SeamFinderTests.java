package seamfinding;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import seamfinding.energy.DualGradientEnergyFunction;
import seamfinding.energy.EnergyFunction;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.SplittableRandom;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Abstract class providing test cases for all implementations of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class SeamFinderTests {
    /**
     * Error tolerance for the minimum-cost seam.
     */
    private static final double EPSILON = 1e-5;
    /**
     * The base directory path for the images.
     */
    private static final String BASE_PATH = "data/seamcarving/";
    /**
     * The {@link EnergyFunction} implementation.
     */
    private static final EnergyFunction f = new DualGradientEnergyFunction();
    /**
     * The {@link SeamFinder} implementation to test.
     */
    private SeamFinder seamFinder;

    /**
     * Returns a new instance of the {@link SeamFinder} interface.
     *
     * @return a new instance of the {@link SeamFinder} interface
     */
    public abstract SeamFinder createSeamFinder();

    @BeforeAll
    void setup() {
        seamFinder = createSeamFinder();
    }

    @ParameterizedTest
    @ValueSource(strings = {"HJoceanSmall", "stripes", "diagonals", "diag_test", "chameleon",
                            "12x10", "10x12", "10x10", "8x3", "7x10", "7x3", "6x5", "5x6", "4x6",
                            "3x8", "3x7", "3x4", "3x3"})
    void precomputedImages(String basename) throws IOException {
        Picture picture = new Picture(new File(BASE_PATH + basename + ".png"));

        double horizontalExpected = precomputedEnergy(basename, "horizontal");
        List<Integer> horizontalSeam = seamFinder.findHorizontal(picture, f);
        checkHorizontal(picture, horizontalSeam);
        double horizontalActual = energyHorizontal(picture, horizontalSeam);
        assertEquals(horizontalExpected, horizontalActual, EPSILON, () -> String.format(
                "\n" +
                "Horizontal expected energy: %s\n" +
                "           actual energy:   %s\n" +
                "           actual seam:     %s",
                horizontalExpected, horizontalActual, horizontalSeam));

        double verticalExpected = precomputedEnergy(basename, "vertical");
        List<Integer> verticalSeam = seamFinder.findVertical(picture, f);
        checkHorizontal(picture.transposed(), verticalSeam);
        double verticalActual = energyHorizontal(picture.transposed(), verticalSeam);
        assertEquals(verticalExpected, verticalActual, EPSILON, () -> String.format(
                "\n" +
                "Vertical expected energy: %s\n" +
                "         actual energy:   %s\n" +
                "         actual seam:     %s",
                verticalExpected, verticalActual, verticalSeam));
    }

    /**
     * Returns the expected energy for a minimum-cost seam in the picture corresponding to the file name.
     *
     * @param basename    the base file name of the picture.
     * @param orientation the seam orientation, either "horizontal" or "vertical".
     * @return the expected energy for a minimum-cost seam in the picture corresponding to the file name.
     * @throws FileNotFoundException if the expected seam cost file for the given orientation is missing.
     */
    private static double precomputedEnergy(String basename, String orientation) throws FileNotFoundException {
        File file = new File(BASE_PATH + basename + "." + orientation + ".txt");
        try (Scanner reader = new Scanner(file)) {
            return reader.nextDouble();
        }
    }

    /**
     * Checks that the seam is a valid horizontal seam in the picture.
     *
     * @param picture the {@link Picture}.
     * @param seam    the seam to remove.
     */
    private static void checkHorizontal(Picture picture, List<Integer> seam) {
        if (seam == null || seam.isEmpty()) {
            throw new NullPointerException("Seam cannot be null or empty");
        } else if (seam.size() != picture.width()) {
            throw new IllegalArgumentException("Seam length does not match image width");
        }
        for (int i = 1; i < seam.size(); i += 1) {
            if (Math.abs(seam.get(i - 1) - seam.get(i)) > 1) {
                throw new IllegalArgumentException("Seam value too far from predecessor at index " + i);
            }
        }
    }

    /**
     * Returns the energy of the horizontal seam by applying the given {@link EnergyFunction} on the {@link Picture}.
     *
     * @param picture the {@link Picture}.
     * @param seam    the seam to remove.
     * @return the energy of the horizontal seam by applying the given {@link EnergyFunction} on the {@link Picture}.
     */
    private static double energyHorizontal(Picture picture, List<Integer> seam) {
        double energy = 0.0;
        for (int x = 0; x < picture.width(); x += 1) {
            energy += f.apply(picture, x, seam.get(x));
        }
        return energy;
    }

    @Nested
    @Disabled
    class RuntimeExperiments {
        /**
         * Maximum image dimensions in pixels. Making this smaller means experiments run faster.
         */
        private static final int MAX_SIZE = 500;
        /**
         * Step size increment. Making this smaller means experiments run slower.
         */
        private static final int STEP = 25;
        /**
         * Number of trials to per implementation run. Making this smaller means experiments run faster.
         */
        private static final int NUM_TRIALS = 25;

        @Test
        void randomPictures() {
            SplittableRandom spRandom = new SplittableRandom(373);
            for (int size = STEP; size <= MAX_SIZE; size += STEP) {
                System.out.print(size);
                System.out.print(',');

                // Generate a random square-size picture using the spRandom source
                Picture picture = randomPicture(size, size, spRandom);

                // Record the total runtimes accumulated across all trials
                long totalTime = 0;

                for (int i = 0; i < NUM_TRIALS; i += 1) {
                    // Measure the time to find a horizontal seam
                    long start = System.nanoTime();
                    seamFinder.findHorizontal(picture, f);
                    long time = System.nanoTime() - start;
                    totalTime += time;
                }

                // Output the averages to 10 decimal places.
                System.out.print(totalTime / (double) NUM_TRIALS);
                System.out.println();
            }
        }

        /**
         * Returns a new picture with the given width and height dimensions filled with random colors.
         *
         * @param width    the horizontal dimension for the picture.
         * @param height   the vertical dimension for the picture.
         * @param spRandom the {@link SplittableRandom} instance for generating random colors.
         * @see <a href="https://codereview.stackexchange.com/a/244139">Fastest way to create random pixel image</a>
         * @return a new picture with the given width and height dimensions filled with random colors.
         */
        private Picture randomPicture(int width, int height, SplittableRandom spRandom) {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            long bytesPerPixel = 4L;
            for (int y = 0; y < height; y += 1) {
                int[] row = spRandom.ints(bytesPerPixel * width, 0, 256).toArray();
                image.getRaster().setPixels(0, y, width, 1, row);
            }
            return new Picture(image);
        }
    }
}
