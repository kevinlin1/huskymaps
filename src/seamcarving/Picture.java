package seamcarving;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.SplittableRandom;

/**
 * A digital picture represented as red-green-blue color {@code int} pixels.
 */
public class Picture {
    final BufferedImage image;

    /**
     * Constructs a null picture for subclassing purposes.
     */
    Picture() {
        image = null;
    }

    /**
     * Constructs a picture from the given image.
     *
     * @param image the input image.
     */
    private Picture(BufferedImage image) {
        this.image = image;
    }

    /**
     * Constructs an empty picture with the given width and height dimensions.
     *
     * @param width  the horizontal dimension for the picture.
     * @param height the vertical dimension for the picture.
     */
    public Picture(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Constructs a copy of the given picture.
     *
     * @param other the input picture.
     */
    public Picture(Picture other) {
        this(other.width(), other.height());
        for (int i = 0; i < other.width(); i += 1) {
            for (int j = 0; j < other.height(); j += 1) {
                this.set(i, j, other.get(i, j));
            }
        }
    }

    /**
     * Constructs a picture from the given file.
     *
     * @param file the input file.
     * @throws IOException if an error occurs during reading.
     */
    public Picture(File file) throws IOException {
        image = ImageIO.read(file);
    }

    /**
     * Returns a new picture with the given width and height dimensions filled with randomly-generated colors.
     *
     * @param width  the horizontal dimension for the picture.
     * @param height the vertical dimension for the picture.
     * @return a new picture with the given width and height dimensions filled with randomly-generated colors.
     */
    public static Picture random(int width, int height) {
        // https://codereview.stackexchange.com/a/244139
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        long bytesPerPixel = 4L;
        for (int y = 0; y < height; y += 1) {
            int[] row = new SplittableRandom().ints(bytesPerPixel * width, 0, 256).toArray();
            image.getRaster().setPixels(0, y, width, 1, row);
        }
        return new Picture(image);
    }

    /**
     * Returns the 24-bit red-green-blue (RGB) color for the pixel (x, y).
     *
     * @param x the x-index into the picture.
     * @param y the y-index into the picture.
     * @return the 24-bit red-green-blue (RGB) color for the pixel (x, y).
     */
    public int get(int x, int y) {
        return image.getRGB(x, y);
    }

    /**
     * Reassigns the 24-bit red-green-blue (RGB) color for the pixel (x, y).
     *
     * @param x   the x-index into the picture.
     * @param y   the y-index into the picture.
     * @param rgb the 24-bit red-green-blue (RGB) color for the pixel (x, y).
     */
    public void set(int x, int y, int rgb) {
        image.setRGB(x, y, rgb);
    }

    /**
     * Returns the width of the picture.
     *
     * @return the width of the picture.
     */
    public int width() {
        return image.getWidth();
    }

    /**
     * Returns the height of the picture.
     *
     * @return the height of the picture.
     */
    public int height() {
        return image.getHeight();
    }

    /**
     * Writes the picture to the given file path.
     *
     * @param file the file path.
     * @throws IOException if an error occurs during writing.
     */
    public void save(File file) throws IOException {
        String extension = file.getName().substring(file.getName().lastIndexOf('.') + 1);
        if ("jpg".equalsIgnoreCase(extension) || "png".equalsIgnoreCase(extension)) {
            ImageIO.write(image, extension, file);
        } else {
            throw new IllegalArgumentException("File must end in .jpg or .png");
        }
    }
}
