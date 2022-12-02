package seamfinding.energy;

import seamfinding.Picture;

/**
 * Dual-gradient implementation of the {@link EnergyFunction} interface for {@link Picture} objects.
 *
 * @see EnergyFunction
 * @see Picture
 */
public class DualGradientEnergyFunction implements EnergyFunction {
    /**
     * Returns the horizontal derivative for the (x, y) pixel in the picture.
     *
     * @param picture the input picture.
     * @param x       the x-index into the picture.
     * @param y       the y-index into the picture.
     * @return the horizontal derivative for the (x, y) pixel in the picture.
     */
    private static double horizontalDerivative(Picture picture, int x, int y) {
        if (x == 0) {
            return forwardDiff(picture.get(x, y), picture.get(x + 1, y), picture.get(x + 2, y));
        } else if (x == picture.width() - 1) {
            return forwardDiff(picture.get(x, y), picture.get(x - 1, y), picture.get(x - 2, y));
        } else {
            return centralDiff(picture.get(x - 1, y), picture.get(x + 1, y));
        }
    }

    /**
     * Returns the vertical derivative for the (x, y) pixel in the picture.
     *
     * @param picture the input picture.
     * @param x       the x-index into the picture.
     * @param y       the y-index into the picture.
     * @return the vertical derivative for the (x, y) pixel in the picture.
     */
    private static double verticalDerivative(Picture picture, int x, int y) {
        if (y == 0) {
            return forwardDiff(picture.get(x, y), picture.get(x, y + 1), picture.get(x, y + 2));
        } else if (y == picture.height() - 1) {
            return forwardDiff(picture.get(x, y), picture.get(x, y - 1), picture.get(x, y - 2));
        } else {
            return centralDiff(picture.get(x, y - 1), picture.get(x, y + 1));
        }
    }

    /**
     * Returns the central difference between the two pixels' colors.
     *
     * @param rgb1 the first pixel's color.
     * @param rgb2 the second pixel's color.
     * @return the central difference between the two pixels' colors.
     */
    private static double centralDiff(int rgb1, int rgb2) {
        return Math.pow(red(rgb1) - red(rgb2), 2)
                + Math.pow(green(rgb1) - green(rgb2), 2)
                + Math.pow(blue(rgb1) - blue(rgb2), 2);
    }

    /**
     * Returns the forward/backward difference for the three adjacent pixels' colors.
     *
     * @param rgb1 the first adjacent pixel's color.
     * @param rgb2 the second adjacent pixel's color.
     * @param rgb3 the third adjacent pixel's color.
     * @return the forward/backward difference for the three adjacent pixels' colors.
     */
    private static double forwardDiff(int rgb1, int rgb2, int rgb3) {
        return Math.pow(-3 * red(rgb1) + 4 * red(rgb2) - red(rgb3), 2)
                + Math.pow(-3 * green(rgb1) + 4 * green(rgb2) - green(rgb3), 2)
                + Math.pow(-3 * blue(rgb1) + 4 * blue(rgb2) - blue(rgb3), 2);
    }

    /**
     * Returns the 8-bit red color component as an {@code int}.
     *
     * @param rgb 24-bit color represented as three 8-bit red-green-blue color components.
     * @return the 8-bit red color component as an {@code int}.
     */
    private static int red(int rgb) {
        return (rgb >> 16) & 0xFF;
    }

    /**
     * Returns the 8-bit green color component as an {@code int}.
     *
     * @param rgb 24-bit color represented as three 8-bit red-green-blue color components.
     * @return the 8-bit green color component as an {@code int}.
     */
    private static int green(int rgb) {
        return (rgb >> 8) & 0xFF;
    }

    /**
     * Returns the 8-bit blue color component as an {@code int}.
     *
     * @param rgb 24-bit color represented as three 8-bit red-green-blue color components.
     * @return the 8-bit blue color component as an {@code int}.
     */
    private static int blue(int rgb) {
        return rgb & 0xFF;
    }

    @Override
    public double apply(Picture picture, int x, int y) {
        if (x < 0 || y < 0 || x >= picture.width() || y >= picture.height()) {
            throw new IndexOutOfBoundsException("Invalid (" + x + ", " + y + ") for picture");
        }
        return Math.sqrt(horizontalDerivative(picture, x, y) + verticalDerivative(picture, x, y));
    }
}
