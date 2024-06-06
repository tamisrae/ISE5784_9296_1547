package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import java.lang.module.ModuleDescriptor;
import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Camera implements Cloneable {
    /**
     * The point where the Camera is located.
     */
    Point location = null;

    /**
     * Vector to the right of the Camera, up, and where it was pointing.
     */
    Vector vUp = null, vTo = null, vRight = null;

    /**
     * The height of the view plane, the width of the view plane, and its distance from the camera.
     */
    double viewPlaneH = 0.0, viewPlaneW = 0.0, viewPlaneD = 0.0;

    /**
     * Intended for creating the image file
     */
    private ImageWriter imageWriter;
    /**
     * Intended for dyeing the rays.
     */
    private RayTracerBase rayTrace;

    /**
     * empty constructor
     */
    private Camera() {
    }

    /**
     * getter for Camera location.
     *
     * @return Camera location.
     */
    public Point getLocation() {
        return location;
    }

    /**
     * getter for above the Camera.
     *
     * @return above the Camera.
     */
    public Vector getVUp() {
        return vUp;
    }

    /**
     * getter for Camera direction.
     *
     * @return Camera direction.
     */
    public Vector getVTo() {
        return vTo;
    }

    /**
     * getter for view plane height.
     *
     * @return view plane height.
     */
    public double getViewPlaneH() {
        return viewPlaneH;
    }

    /**
     * getter for view plane width.
     *
     * @return view plane width.
     */
    public double getViewPlaneW() {
        return viewPlaneW;
    }

    /**
     * getter for view plane distance.
     *
     * @return view plane distance.
     */
    public double getViewPlaneD() {
        return viewPlaneD;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pc = location.add(vTo.scale(viewPlaneD));     // center of the view plane
        double Ry = viewPlaneH / (double) nY;                  // Ratio - pixel height
        double Rx = viewPlaneW / (double) nX;                  // Ratio - pixel width

        double yJ = alignZero(-(i - ((double) nY - 1) / 2) * Ry);       // move pc Yi pixels
        double xJ = alignZero((j - ((double) nX - 1) / 2) * Rx);        // move pc Xj pixels

        Point PIJ = pc;
        if (!isZero(xJ)) PIJ = PIJ.add(vRight.scale(xJ));
        if (!isZero(yJ)) PIJ = PIJ.add(vUp.scale(yJ));

        return new Ray(location, PIJ.subtract(location));
    }

    /**
     * Calculates a color for a specific pixel in an image.
     *
     * @param nX The number of pixels in a row in the view plane.
     * @param nY The number of pixels in a column in the view plane.
     * @param j  The row number of the pixel.
     * @param i  The column number of the pixel.
     */
    private void castRay(int nX, int nY, int j, int i) {
        this.imageWriter.writePixel(j, i, this.rayTrace.traceRay(this.constructRay(nX, nY, j, i)));
    }


    /**
     * Renders the image by casting rays from the camera through each pixel of the image and writing the resulting color to the imageWriter.
     * Throws UnsupportedOperationException if any of the required resources are missing (rayTracerBase, imageWriter, width, height, distance).
     */
    public Camera renderImage() {
        if (this.rayTrace == null || this.imageWriter == null || this.viewPlaneW == 0 || this.viewPlaneH == 0 || this.viewPlaneD == 0)
            throw new UnsupportedOperationException("MissingResourcesException");

        int nX = imageWriter.getNx();
        int nY = imageWriter.getNy();
        for (int i = 0; i < nY; i++)
            for (int j = 0; j < nX; j++)
                this.castRay(nX, nY, j, i);
        return this;
    }



    /**
     * Draws a grid on the image by writing a specified color to the pixels that fall on the grid lines.
     * Throws UnsupportedOperationException if imageWriter object is null.
     *
     * @param interval The spacing between grid lines.
     * @param color    The color to use for the grid lines.
     */
    public Camera printGrid(int interval, Color color) throws MissingResourceException, IllegalArgumentException {
        if (this.imageWriter == null)
            throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter", null);

        if (this.imageWriter.getNx() % interval != 0 || this.imageWriter.getNy() % interval != 0)
            throw new IllegalArgumentException("The grid is supposed to have squares, therefore the given interval must be a divisor of both Nx and Ny");

        for (int i = 0; i < this.imageWriter.getNx(); i++)
            for (int j = 0; j < this.imageWriter.getNy(); j++)
                if (i % interval == 0 || (i + 1) % interval == 0 || j % interval == 0 || (j + 1) % interval == 0)
                    this.imageWriter.writePixel(i, j, color);
        return this;
    }




        /**
         * "Printing" the image - producing an unoptimized png file of the image
         * @throws MissingResourceException The render's field imageWriter mustn't be null
         **/
    public Camera writeToImage() throws MissingResourceException {
        if (this.imageWriter == null)
            throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter", null);
        this.imageWriter.writeToImage();
        return this;
    }
        /**
         * inner class
         */
    public static class Builder {

        private final Camera camera = new Camera();

        /**
         * Set the location of the camera.
         *
         * @param location the location point of the camera.
         * @return the Builder object itself for method chaining.
         * @throws IllegalArgumentException if the location is null
         */
        public Builder setLocation(Point location) throws IllegalArgumentException{
            if (location == null) {
                throw new IllegalArgumentException("Location cannot be null");
            }
            camera.location = location;
            return this;
        }

        /**
         * Set the direction vectors of the camera.
         *
         * @param vTo the forward direction vector.
         * @param vUp the up direction vector.
         * @return the Builder object itself for method chaining.
         * @throws IllegalArgumentException if the vectors are null or not orthogonal
         */
        public Builder setDirection(Vector vTo, Vector vUp) throws IllegalArgumentException{
            if (vTo == null || vUp == null) {
                throw new IllegalArgumentException("Direction vectors cannot be null");
            }
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Set the size of the view plane.
         * @param width the width of the view plane.
         * @param height the height of the view plane.
         * @return the Builder object itself for method chaining.
         * @throws IllegalArgumentException if the dimensions aren't positive
         */
        public Builder setVpSize(double width, double height) throws IllegalArgumentException  {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("View plane dimensions must be positive");
            }
            camera.viewPlaneW = width;
            camera.viewPlaneH = height;
            return this;
        }

        /**
         * Set the distance between the camera and the view plane.
         * @param distance the distance between the camera and the view plane.
         * @return the Builder object itself for method chaining.
         * @throws IllegalArgumentException if the distance is not positive
         */
        public Builder setVpDistance(double distance) throws IllegalArgumentException {
            if (distance <= 0) {
                throw new IllegalArgumentException("View plane distance must be positive");
            }
            camera.viewPlaneD = distance;
            return this;
        }

        /**
         * setter for image writer.
         *
         * @param imageWriter image writer.
         * @return the Camera.
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter= imageWriter;
            return this;
        }

        /**
         * Setter for ray tracer base.
         *
         * @param rayTracerBase Ray tracer base.
         * @return The Camera.
         */
        public Builder setRayTracer(RayTracerBase rayTracerBase) {
            camera.rayTrace = rayTracerBase;
            return this;
        }
        /**
         * Builds the Camera object.
         * @return the constructed Camera object.
         * @throws MissingResourceException if any required field is missing.
         */
        public Camera build() throws MissingResourceException {

            String missingData = "Missing rendering data";

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            if (camera.location == null) {
                throw new MissingResourceException(missingData, Camera.class.getName(), "location");
            }
            if (camera.vTo == null || camera.vUp == null || camera.vRight == null) {
                throw new MissingResourceException(missingData, Camera.class.getName(), "direction vectors");
            }
            if (camera.viewPlaneW == 0.0 || camera.viewPlaneH == 0.0) {
                throw new MissingResourceException(missingData, Camera.class.getName(), "view plane size");
            }
            if (camera.viewPlaneD == 0.0) {
                throw new MissingResourceException(missingData, Camera.class.getName(), "view plane distance");
            }
            if(camera.imageWriter == null)
                throw new MissingResourceException(missingData, Camera.class.getName(), "image_writer");
            if(camera.rayTrace == null)
                throw new MissingResourceException(missingData, Camera.class.getName(), "rayTrace");
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}





