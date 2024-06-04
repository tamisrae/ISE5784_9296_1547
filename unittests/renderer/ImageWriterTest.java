package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import static java.awt.Color.BLUE;
import static java.awt.Color.RED;

/**
 * A test class for the ImageWriter class.
 * @author Michal Shlomo and Tamar Israeli
 */
class ImageWriterTest {

    /**
     * Tests the functionality of the ImageWriter class by creating an image with
     * specified dimensions and writing pixels with alternating colors.
     */
    @Test
    void testImageWriter() {
        final int width = 801;
        final int height = 501;
        // Set the interval for alternating colors
        final int step = 50;
        final Color color1 = new Color(BLUE);
        final Color color2 = new Color(RED);

        // Create a new ImageWriter object with specified dimensions
        ImageWriter images = new ImageWriter("images", width, height);

        // Loop through the image pixels and write alternating colors
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                // Set color based on the interval
                images.writePixel(i, j, i % step == 0 || j % step == 0 ? color1 : color2);

        // Write the image to file
        images.writeToImage();
    }
}