package seamfinding.energy;

import seamfinding.Picture;

/**
 * Determines the energy of a given (x, y) pixel index in a {@link Picture}.
 *
 * @see DualGradientEnergyFunction
 * @see Picture
 */
public interface EnergyFunction {
    /**
     * Returns the energy of pixel (x, y) in the given picture.
     *
     * @param picture the input picture.
     * @param x       the x-index into the picture.
     * @param y       the y-index into the picture.
     * @return the energy of pixel (x, y) in the given picture.
     */
    double apply(Picture picture, int x, int y);
}
