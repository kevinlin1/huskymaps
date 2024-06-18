package seamfinding;

import seamfinding.energy.EnergyFunction;

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
    List<Integer> findHorizontal(Picture picture, EnergyFunction f);

    /**
     * Returns a minimum-energy vertical seam in the current image as a {@link List} of integers representing the
     * horizontal pixel index to remove from each row in the height of the vertical seam.
     *
     * @param picture the {@link Picture}.
     * @param f       the {@link EnergyFunction}.
     * @return a {@link List} of integers representing the horizontal pixels to remove.
     */
    default List<Integer> findVertical(Picture picture, EnergyFunction f) {
        return findHorizontal(picture.transposed(), f);
    }
}
