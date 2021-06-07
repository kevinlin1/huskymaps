package seamcarving;

import graphs.DijkstraSolver;
import graphs.ToposortDAGSolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Compare different {@link SeamFinder} implementations to check that they compute the same values.
 *
 * @see SeamFinder
 * @see AdjacencyListSeamFinder
 * @see GenerativeSeamFinder
 * @see DynamicProgrammingSeamFinder
 * @see graphs.ShortestPathSolver
 * @see DijkstraSolver
 * @see ToposortDAGSolver
 */
class SeamFinderMultiTest {
    /**
     * Error tolerance for the minimum-cost seam.
     */
    private static final double EPSILON = 1e-5;
    /**
     * The base directory path for the images.
     */
    private static final String BASE_PATH = "data/seamcarving/";
    /**
     * The image files (.png) and corresponding expected seam costs (.txt) in the {@link #BASE_PATH}.
     */
    private static final String[] FILES = new String[]{
            "HJoceanSmall",
            "stripes",
            "diagonals",
            "diag_test",
            "chameleon",
            "12x10",
            "10x12",
            "10x10",
            "8x3",
            "7x10",
            "7x3",
            "6x5",
            "5x6",
            "4x6",
            "3x8",
            "3x7",
            "3x4",
            "3x3"
    };

    public static void main(String[] args) throws IOException {
        System.out.println("Testing Djikstra Solver");
        test(new AdjacencyListSeamFinder(DijkstraSolver::new));
        System.out.println("\n=============================================");

        System.out.println("Testing Generative Seam Finder");
        test(new GenerativeSeamFinder(DijkstraSolver::new));
        System.out.println("\n=============================================");

        System.out.println("Testing Toposort DAG Solver");
        test(new AdjacencyListSeamFinder(ToposortDAGSolver::new));
        System.out.println("\n=============================================");

        System.out.println("Testing Dynamic Programming Seam Finder");
        test(new DynamicProgrammingSeamFinder());
    }

    /**
     * Tests the given {@link SeamFinder} implementation.
     *
     * @param seamFinder the {@link SeamFinder} implementation.
     * @throws IOException if an error occurs during reading.
     */
    private static void test(SeamFinder seamFinder) throws IOException {
        EnergyFunction f = new DualGradientEnergyFunction();
        System.out.printf("%-30.30s  %-30.30s  %-30.30s%n", "File", "Vertical Passed", "Horizontal Passed");

        for (String fileName : FILES) {
            Picture picture = new Picture(new File(BASE_PATH + fileName + ".png"));

            SeamCarver horzSeamCarver = new SeamCarver(new File(BASE_PATH + fileName + ".png"), f, seamFinder);
            List<Integer> horzSeam = horzSeamCarver.removeHorizontal();
            double horzSeamEnergy = getSeamEnergy(picture, horzSeam, false);
            double horzExpectedEnergy = getExpectedSeamEnergy(fileName, false);
            boolean horzPassed = Math.abs(horzSeamEnergy - horzExpectedEnergy) < EPSILON;

            SeamCarver vertSeamCarver = new SeamCarver(new File(BASE_PATH + fileName + ".png"), f, seamFinder);
            List<Integer> vertSeam = vertSeamCarver.removeVertical();
            double vertSeamEnergy = getSeamEnergy(picture, vertSeam, true);
            double vertExpectedEnergy = getExpectedSeamEnergy(fileName, true);
            boolean vertPassed = Math.abs(vertSeamEnergy - vertExpectedEnergy) < EPSILON;

            System.out.printf(
                    "%-30.30s  %-30.30s  %-30.30s%n",
                    fileName + ".png",
                    vertPassed ? "PASS" : "FAIL",
                    horzPassed ? "PASS" : "FAIL"
            );
            if (!vertPassed) {
                printFail(vertExpectedEnergy, vertSeam.toString(), vertSeamEnergy, true);
            }
            if (!horzPassed) {
                printFail(horzExpectedEnergy, horzSeam.toString(), horzSeamEnergy, false);
            }
        }
    }

    /**
     * Prints information about a failed {@link SeamFinder} test case.
     *
     * @param expectedEnergy the expected energy for a minimum-cost seam.
     * @param seam           the values of the seam computed by the testing algorithm.
     * @param seamEnergy     the actual energy of the seam computed by the testing algorithm.
     * @param isVertical     true if the seam is vertical.
     */
    private static void printFail(double expectedEnergy, String seam, double seamEnergy, boolean isVertical) {
        String prefix = isVertical ? "Vertical" : "Horizontal";
        System.out.println("\t" + prefix + " expected energy:        \t" + expectedEnergy);
        System.out.println("\t" + prefix + " generated seam          \t" + seam);
        System.out.println("\t" + prefix + " generated seam energy:  \t" + seamEnergy);
    }

    /**
     * Returns the energy of the given seam in the picture.
     *
     * @param picture    the input picture.
     * @param seam       the computed seam.
     * @param isVertical true if the seam is vertical.
     * @return the energy of the given seam in the picture.
     */
    private static double getSeamEnergy(Picture picture, List<Integer> seam, boolean isVertical) {
        EnergyFunction f = new DualGradientEnergyFunction();
        double total = 0;
        if (!isVertical) {
            for (int x = 0; x < picture.width(); x++) {
                total += f.apply(picture, x, seam.get(x));
            }
        } else {
            for (int y = 0; y < picture.height(); y++) {
                total += f.apply(picture, seam.get(y), y);
            }
        }
        return total;
    }

    /**
     * Returns the expected energy for a minimum-cost seam in the picture corresponding to the file name.
     *
     * @param fileName   the base file name of the picture.
     * @param isVertical true if the seam is vertical.
     * @return the expected energy for a minimum-cost seam in the picture corresponding to the file name.
     * @throws FileNotFoundException if the expected seam cost file for the given orientation is missing.
     */
    private static double getExpectedSeamEnergy(String fileName, boolean isVertical) throws FileNotFoundException {
        String suffix = isVertical ? "vertical" : "horizontal";
        Scanner reader = new Scanner(new File(BASE_PATH + fileName + "." + suffix + ".txt"));
        double expected = reader.nextDouble();
        reader.close();
        return expected;
    }
}
