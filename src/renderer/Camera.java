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
//    public void printGrid(int interval, Color color) {
//        if (imageWriter == null) throw new UnsupportedOperationException("MissingResourcesException");
//
//        int nX = imageWriter.getNx();
//        int nY = imageWriter.getNy();
//        for (int i = 0; i < nY; i+=interval)
//            for (int j = 0; j < nX; j++)
//                this.imageWriter.writePixel(j, i, color);
//        for (int i = 0; i < nX; i+=interval)
//            for (int j = 0; j < nX; j++)
//                this.imageWriter.writePixel(i, j, color);
//    }

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
            if(camera.imageWriter==null)
                throw new MissingResourceException(missingData, Camera.class.getName(), "image_writer");
            if(camera.rayTrace==null)
                throw new MissingResourceException(missingData, Camera.class.getName(), "rayTrace");
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}




//
//package renderer;
//
//import primitives.*;
//
//
//import java.util.MissingResourceException;
//
//import static primitives.Util.isZero;
//
///**
// * The Camera class represents a virtual camera in a 3D graphics environment. It
// * is used to define the viewpoint and parameters for rendering scenes. The
// * class utilizes a builder pattern to construct camera instances with specified
// * parameters.
// */
//public class Camera implements Cloneable {
//
//    // Private instance variables for camera parameters
//    private Point p0;
//    private Vector vTo;
//    private Vector vUp;
//    private Vector vRight;
//    private double viewPlaneWidth;
//    private double viewPlaneHeight;
//    private double viewPlaneDistance;
//    private Point viewPlanePC; // View plane center point to save CPU time – it’s always the same
//    private ImageWriter imageWriter;
//    private RayTracerBase rayTracer;
//    // Private constructor to enforce the use of the builder pattern
//    private Camera() {
//    }
//
//    /**
//     * Static method to obtain a builder for creating a Camera instance.
//     *
//     * @return A new instance of the Camera.Builder class.
//     */
//    public static Builder getBuilder() {
//        return new Builder();
//    }
//
//    /**
//     * Builder class for constructing Camera instances with specified parameters.
//     */
//    public static class Builder {
//
//        // Private instance of the Camera class being built
//        private final Camera camera = new Camera();
//
//        /**
//         * Set the location of the camera.
//         *
//         * @param location The location (Point) of the camera.
//         * @return The Builder instance for method chaining.
//         */
//        public Builder setLocation(Point location) {
//            camera.p0 = location;
//            return this;
//        }
//
//        /**
//         * Set the direction vectors for the camera.
//         *
//         * @param to The direction vector towards which the camera is pointing.
//         * @param up The up vector for the camera orientation.
//         * @return The Builder instance for method chaining.
//         * @throws IllegalArgumentException if the input vectors are not orthogonal.
//         */
//        public Builder setDirection(Vector to, Vector up) throws IllegalArgumentException {
//            if (!isZero(to.dotProduct(up)))
//                throw new IllegalArgumentException("The vectors aren't orthogonal");
//            camera.vTo = to.normalize();
//            camera.vUp = up.normalize();
//            return this;
//        }
//
//        /**
//         * Set the size of the view plane.
//         *
//         * @param width  The width of the view plane.
//         * @param height The height of the view plane.
//         * @return The Builder instance for method chaining.
//         * @throws IllegalArgumentException if the input dimensions are not valid.
//         */
//        public Builder setVpSize(double width, double height) throws IllegalArgumentException {
//            if (width <= 0 || height <= 0) {
//                throw new IllegalArgumentException("Invalid length or width");
//            }
//            camera.viewPlaneWidth = width;
//            camera.viewPlaneHeight = height;
//            return this;
//        }
//
//        /**
//         * Set the distance from the camera to the view plane.
//         *
//         * @param distance The distance from the camera to the view plane.
//         * @return The Builder instance for method chaining.
//         * @throws IllegalArgumentException if the input distance is not valid.
//         */
//        public Builder setVpDistance(double distance) throws IllegalArgumentException {
//            if (distance <= 0) {
//                throw new IllegalArgumentException("Invalid distance");
//            }
//            camera.viewPlaneDistance = distance;
//            return this;
//        }
//        /**
//         *
//         */
//        public Builder setRayTracer(RayTracerBase rayTracer) {
//            camera.rayTracer = rayTracer;
//            return this;
//        }
//        /**
//
//         */
//        public Builder setImageWriter(ImageWriter imageWriter) {
//            camera.imageWriter = imageWriter;
//            return this;
//        }
//
//        /**
//         * Build the Camera instance with the specified parameters.
//         *
//         * @return The constructed Camera instance.
//         * @throws MissingResourceException if any required parameter is missing.
//         */
//        public Camera build() throws MissingResourceException {
//// Check for missing arguments
//            String missingArgMsg = "there's a missing argument";
//            String className = "Camera";
//            if (camera.p0 == null)
//                throw new MissingResourceException(missingArgMsg, className, "p0 - the location of the camera");
//            if (camera.vUp == null)
//                throw new MissingResourceException(missingArgMsg, className,
//                        "vUp - one of the direction vectors of the camera");
//            if (camera.vTo == null)
//                throw new MissingResourceException(missingArgMsg, className,
//                        "vTo - one of the direction vectors of the camera");
//
//// Calculate the right vector and normalize it
//            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
//
//// Check for zero values and orthogonality
//            if (Util.alignZero(camera.viewPlaneWidth) == 0)
//                throw new MissingResourceException(missingArgMsg, className, "planeWidth");
//            if (Util.alignZero(camera.viewPlaneHeight) == 0)
//                throw new MissingResourceException(missingArgMsg, className, "planeHeight");
//            if (Util.alignZero(camera.viewPlaneDistance) == 0)
//                throw new MissingResourceException(missingArgMsg, className, "planeDistance");
//            if (!isZero(camera.vRight.dotProduct(camera.vTo)))
//                throw new IllegalArgumentException();
//
//// Check for valid dimensions and distance
//            if (camera.viewPlaneWidth < 0 || camera.viewPlaneHeight < 0) {
//                throw new IllegalArgumentException("Invalid length or width");
//            }
//            if (camera.viewPlaneDistance < 0) {
//                throw new IllegalArgumentException("Invalid distance");
//            }
//
//// Calculate the view plane center point
//            camera.viewPlanePC = camera.p0.add(camera.vTo.scale(camera.viewPlaneDistance));
//// Check for
//            if(camera.imageWriter==null)
//                throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter", null);
//// Check for
//            if(camera.rayTracer==null)
//                throw new MissingResourceException("The render's field rayTracer mustn't be null", "ImageWriter", null);
//// Attempt to clone the camera instance
//            try {
//                return (Camera) camera.clone(); // Cloneable – get a full copy
//            } catch (CloneNotSupportedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//    } // end of Builder class
//
//    /**
//     * This method creates a ray that goes through the center of a pixel.
//     *
//     * @param nX The total number of pixels in the x-direction.
//     * @param nY The total number of pixels in the y-direction.
//     * @param j  The x-coordinate of the pixel.
//     * @param i  The y-coordinate of the pixel.
//     * @return A Ray instance representing the ray through the center of the
//     *         specified pixel.
//     */
//    public Ray constructRay(int nX, int nY, int j, int i) {
//        Point pCenter = p0.add(vTo.scale(viewPlaneDistance));
//
//        double Ry = (double) viewPlaneHeight / nY;
//        double Rx = (double) viewPlaneWidth / nX;
//
//        double yI = -(i - (double) (nY - 1) / 2) * Ry;
//        double xJ = (j - (double) (nX - 1) / 2) * Rx;
//
//        Point pIJ = pCenter;
//        if (xJ != 0)
//            pIJ = pIJ.add(vRight.scale(xJ));
//        if (yI != 0)
//            pIJ = pIJ.add(vUp.scale(yI));
//
//        Vector vIJ = pIJ.subtract(p0);
//        return new Ray(p0, vIJ);
//    }
//    /**
//     * This function renders image's pixel color map from the scene included with the Renderer object,
//     * including the use in beam of rays and adaptive-supersampling if required.
//     * This is a wrapping function to the function renderImageThreaded.
//    // * @param numOfRays the number of rays wanted in the beam when using a beam of rays for picture improvement
//     //* @param adaptiveSupersampling whether the adaptive-supersampling picture improvement is required
//     */
//    public Camera renderImage()  throws MissingResourceException{
//        if (imageWriter == null)
//            throw new MissingResourceException("All the render's fields mustn't be null, including the camera", "Camera", null);
//        if (rayTracer == null)
//            throw new MissingResourceException("All the render's fields mustn't be null, including the imageWriter", "ImageWriter", null);
//        final int nX = imageWriter.getNx();
//        final int nY = imageWriter.getNy();
//        for (int i=0; i<this.imageWriter.getNx(); i++) {
//            for (int j=0; j<this.imageWriter.getNy(); j++) {
//                Ray r = this.constructRay(this.imageWriter.getNx(), this.imageWriter.getNy(), j, i);
//                this.castRay(nX,nY,j,i);
//            }
//        }
//        return this;
//    }
//    /**
//     * Producing a grid on top of the image, while the grid is made of equal squares
//     * @param interval The wanted interval between the grid's rows and columns
//     * @param color The color of the grid
//     * @throws MissingResourceException The render's field imageWriter mustn't be null
//     * @throws IllegalArgumentException The grid is supposed to have squares, therefore the given interval must be a divisor of both Nx and Ny
//     **/
//    public Camera printGrid(int interval, Color color) throws MissingResourceException, IllegalArgumentException {
//        if(this.imageWriter==null)
//            throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter", null);
//
//        if(this.imageWriter.getNx()%interval!=0 || this.imageWriter.getNy()%interval!=0)
//            throw new IllegalArgumentException("The grid is supposed to have squares, therefore the given interval must be a divisor of both Nx and Ny");
//
//        for (int i=0; i<this.imageWriter.getNx(); i++)
//            for (int j=0; j<this.imageWriter.getNy(); j++)
//                if(i%interval==0 || (i+1)%interval==0 || j%interval==0 || (j+1)%interval==0)
//                    this.imageWriter.writePixel(i, j, color);
//        return this;
//    }
//    /**
//     * "Printing" the image - producing an unoptimized png file of the image
//     * @throws MissingResourceException The render's field imageWriter mustn't be null
//     **/
//    public Camera writeToImage() throws MissingResourceException {
//        if(this.imageWriter==null)
//            throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter", null);
//        this.imageWriter.writeToImage();
//        return this;
//    }
//    /**
//     * Cast ray from camera in order to color a pixel
//     * @param nX resolution on X axis (number of pixels in row)
//     * @param nY resolution on Y axis (number of pixels in column)
//     * @param col pixel's column number (pixel index in row)
//     * @param row pixel's row number (pixel index in column)
//
//     */
//    private void castRay(int nX, int nY, int col, int row) {
//        Ray ray = constructRay(nX, nY, col, row);
//        Color color = this.rayTracer.traceRay(ray);//, numOfRays, adaptiveSupersampling);
//        imageWriter.writePixel(col, row, color);
//    }
//}
