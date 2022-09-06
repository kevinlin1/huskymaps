package seamcarving.seamfinding;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import seamcarving.SeamCarver;
import seamcarving.energy.DualGradientEnergyFunction;
import seamcarving.energy.EnergyFunction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

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
        SeamCarver seamCarver;
        EnergyFunction f = new DualGradientEnergyFunction();
        File file = new File(BASE_PATH + basename + ".png");

        double horzExpected = precomputedEnergy(basename, "horizontal");
        seamCarver = new SeamCarver(file, f, seamFinder);
        List<Integer> horzSeam = seamCarver.removeHorizontal();
        double horzActual = seamCarver.lastRemovedSeamEnergy();
        assertEquals(horzExpected, horzActual, EPSILON, () -> String.format(
                "Horizontal expected energy: %s\n" +
                "           actual energy:   %s\n" +
                "           actual seam:     %s",
                horzExpected, horzActual, horzSeam.toString()));

        double vertExpected = precomputedEnergy(basename, "vertical");
        seamCarver = new SeamCarver(file, f, seamFinder);
        List<Integer> vertSeam = seamCarver.removeVertical();
        double vertActual = seamCarver.lastRemovedSeamEnergy();
        assertEquals(vertExpected, vertActual, EPSILON, () -> String.format(
                "Vertical expected energy: %s\n" +
                "         actual energy:   %s\n" +
                "         actual seam:     %s",
                vertExpected, vertActual, vertSeam.toString()));
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
}
