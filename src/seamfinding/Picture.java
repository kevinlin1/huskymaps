package seamfinding;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * A digital picture represented as red-green-blue color {@code int} pixels.
 */
public class Picture {
    private final BufferedImage image;

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
    public Picture(BufferedImage image) {
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
     * Constructs a picture from the given file.
     *
     * @param file the input file.
     * @throws IOException if an error occurs during reading.
     */
    public Picture(File file) throws IOException {
        image = ImageIO.read(file);
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

    /**
     * Returns a transposed view of this image where x and y accesses are reversed.
     *
     * @return a transposed view of this image where x and y accesses are reversed.
     */
    public Picture transposed() {
        return new Picture() {
            @Override
            public int get(int x, int y) {
                return Picture.this.get(y, x);
            }

            @Override
            public void set(int x, int y, int rgb) {
                Picture.this.set(y, x, rgb);
            }

            @Override
            public int width() {
                return Picture.this.height();
            }

            @Override
            public int height() {
                return Picture.this.width();
            }

            /**
             * Returns the original image for this transposed view.
             *
             * @return the original image for this transposed view.
             */
            @Override
            public Picture transposed() {
                return Picture.this;
            }
        };
    }
}
