package seamcarving;

import java.util.List;

/**
 * Finds a horizontal seam through the {@link Picture} with the lowest sum of {@link EnergyFunction} costs. A horizontal
 * seam is defined as a path of adjacent or diagonally-adjacent pixels from the left to right edges of an image.
 *
 * @see AdjacencyListSeamFinder
 * @see GenerativeSeamFinder
 * @see DynamicProgrammingSeamFinder
 * @see Picture
 * @see EnergyFunction
 * @see SeamCarver
 */
public interface SeamFinder {

    /**
     * Returns a minimum-energy horizontal seam in the current image as a {@link List} of integers representing the
     * vertical pixel index to remove from each column in the width of the horizontal seam.
     *
     * @param picture the {@link Picture}.
     * @param f       the {@link EnergyFunction}.
     * @return a {@link List} of integers representing the vertical pixels to remove.
     */
    List<Integer> findSeam(Picture picture, EnergyFunction f);
}
