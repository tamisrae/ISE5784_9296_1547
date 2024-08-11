package renderer;

import primitives.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.MissingResourceException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import static primitives.Util.*;

/**
 * The Camera class represents a virtual camera in a 3D graphics environment. It
 * is used to define the viewpoint and parameters for rendering scenes. The
 * class utilizes a builder pattern to construct camera instances with specified
 * parameters.
 */
public class Camera implements Cloneable {

    // Private instance variables for camera parameters
    private Point p0;
    private Vector vTo;
    private Vector vUp;
    private Vector vRight;
    private double viewPlaneWidth;
    private double viewPlaneHeight;
    private double viewPlaneDistance;
    private Point viewPlanePC; // View plane center point to save CPU time � it�s always the same
    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    private int numOfRays = 1;// the paramter of the number of rays

    private int threadsCount = 0; // -2 auto, -1 range/stream, 0 no threads, 1+ number of threads
    private final int SPARE_THREADS = 2; // Spare threads if trying to use all the cores
    private double printInterval = 0; // printing progress percentage interval
    private boolean adaptive = false;

    private Camera() {
    }

    /**
     * Static method to obtain a builder for creating a Camera instance.
     *
     * @return A new instance of the Camera.Builder class.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Builder class for constructing Camera instances with specified parameters.
     */
    public static class Builder {

        // Private instance of the Camera class being built
        private final Camera camera = new Camera();

        /**
         * set the adaptive
         *
         * @return the Camera object
         */
        public Builder setadaptive(boolean adaptive) {
            camera.adaptive = adaptive;
            return this;
        }

        public Builder setMultithreading(int threads) {
            if (threads < -2)
                throw new IllegalArgumentException("Multithreading must be -2 or higher");
            if (threads >= -1)
                camera.threadsCount = threads;
            else { // == -2
                int cores = Runtime.getRuntime().availableProcessors() - camera.SPARE_THREADS;
                camera.threadsCount = cores <= 2 ? 1 : cores;
            }
            return this;
        }

        public Builder setDebugPrint(double interval) {
            camera.printInterval = interval;
            return this;
        }

        /**
         * Set the location of the camera.
         *
         * @param location The location (Point) of the camera.
         * @return The Builder instance for method chaining.
         */
        public Builder setLocation(Point location) {
            camera.p0 = location;
            return this;
        }

        /**
         * Set the direction vectors for the camera.
         *
         * @param to The direction vector towards which the camera is pointing.
         * @param up The up vector for the camera orientation.
         * @return The Builder instance for method chaining.
         * @throws IllegalArgumentException if the input vectors are not orthogonal.
         */
        public Builder setDirection(Vector to, Vector up) throws IllegalArgumentException {
            if (!isZero(to.dotProduct(up)))
                throw new IllegalArgumentException("The vectors aren't orthogonal");
            camera.vTo = to.normalize();
            camera.vUp = up.normalize();
            return this;
        }

        /**
         * Set the size of the view plane.
         *
         * @param width  The width of the view plane.
         * @param height The height of the view plane.
         * @return The Builder instance for method chaining.
         * @throws IllegalArgumentException if the input dimensions are not valid.
         */
        public Builder setVpSize(double width, double height) throws IllegalArgumentException {
            if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Invalid length or width");
            }
            camera.viewPlaneWidth = width;
            camera.viewPlaneHeight = height;
            return this;
        }

        /**
         * Set the distance from the camera to the view plane.
         *
         * @param distance The distance from the camera to the view plane.
         * @return The Builder instance for method chaining.
         * @throws IllegalArgumentException if the input distance is not valid.
         */
        public Builder setVpDistance(double distance) throws IllegalArgumentException {
            if (distance <= 0) {
                throw new IllegalArgumentException("Invalid distance");
            }
            camera.viewPlaneDistance = distance;
            return this;
        }

        /**
         *
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            camera.rayTracer = rayTracer;
            return this;
        }

        /**

         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * A setter function for parameter num of rays this function return the object -
         * this for builder pattern
         *
         */
        public Builder setNumOfRays(int numOfRays) {
            if (numOfRays == 0)
                camera.numOfRays = 1;
            else
                camera.numOfRays = numOfRays;
            return this;
        }

        /**
         * Build the Camera instance with the specified parameters.
         *
         * @return The constructed Camera instance.
         * @throws MissingResourceException if any required parameter is missing.
         */
        public Camera build() throws MissingResourceException {
            // Check for missing arguments
            String missingArgMsg = "there's a missing argument";
            String className = "Camera";
            if (camera.p0 == null)
                throw new MissingResourceException(missingArgMsg, className, "p0 - the location of the camera");
            if (camera.vUp == null)
                throw new MissingResourceException(missingArgMsg, className,
                        "vUp - one of the direction vectors of the camera");
            if (camera.vTo == null)
                throw new MissingResourceException(missingArgMsg, className,
                        "vTo - one of the direction vectors of the camera");

            // Calculate the right vector and normalize it
            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();

            // Check for zero values and orthogonality
            if (Util.alignZero(camera.viewPlaneWidth) == 0)
                throw new MissingResourceException(missingArgMsg, className, "planeWidth");
            if (Util.alignZero(camera.viewPlaneHeight) == 0)
                throw new MissingResourceException(missingArgMsg, className, "planeHeight");
            if (Util.alignZero(camera.viewPlaneDistance) == 0)
                throw new MissingResourceException(missingArgMsg, className, "planeDistance");
            if (!isZero(camera.vRight.dotProduct(camera.vTo)))
                throw new IllegalArgumentException();

            // Check for valid dimensions and distance
            if (camera.viewPlaneWidth < 0 || camera.viewPlaneHeight < 0) {
                throw new IllegalArgumentException("Invalid length or width");
            }
            if (camera.viewPlaneDistance < 0) {
                throw new IllegalArgumentException("Invalid distance");
            }

            // Calculate the view plane center point
            camera.viewPlanePC = camera.p0.add(camera.vTo.scale(camera.viewPlaneDistance));
            // Check for
            if (camera.imageWriter == null)
                throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter",
                        null);
            // Check for
            if (camera.rayTracer == null)
                throw new MissingResourceException("The render's field rayTracer mustn't be null", "ImageWriter", null);
            // Attempt to clone the camera instance
            try {
                return (Camera) camera.clone(); // Cloneable � get a full copy
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    } // end of Builder class

    /**
     * This method creates a ray that goes through the center of a pixel.
     *
     * @param nX The total number of pixels in the x-direction.
     * @param nY The total number of pixels in the y-direction.
     * @param j  The x-coordinate of the pixel.
     * @param i  The y-coordinate of the pixel.
     * @return A Ray instance representing the ray through the center of the
     *         specified pixel.
     */
    public Ray constructRay(int nX, int nY, int j, int i) {
        Point pCenter =centerScreenPoint();

        double Ry = (double) viewPlaneHeight / nY;
        double Rx = (double) viewPlaneWidth / nX;

        double yI = -(i - (double) (nY - 1) / 2) * Ry;
        double xJ = (j - (double) (nX - 1) / 2) * Rx;

        Point pIJ = pCenter;
        if (xJ != 0)
            pIJ = pIJ.add(vRight.scale(xJ));
        if (yI != 0)
            pIJ = pIJ.add(vUp.scale(yI));

        Vector vIJ = pIJ.subtract(p0);
        return new Ray(p0, vIJ);
    }

    /**
     * This function renders image's pixel color map from the scene included with
     * the Renderer object, including the use in beam of rays. This is a wrapping
     * function to the function renderImageThreaded.
     *
     * @param numOfRays the number of rays wanted in the beam when using a beam of
     *                  rays for picture improvement
     */
    /**
     public Camera renderImage() throws MissingResourceException {
     if (this.imageWriter == null || this.p0 == null || this.vUp == null || this.vRight == null || this.vTo == null)
     throw new MissingResourceException("Missing filed in camera", "", "");
     if (this.rayTracer == null)
     throw new UnsupportedOperationException("Missing rayTracerBase");
     final int nX = imageWriter.getNx();
     final int nY = imageWriter.getNy();


     PixelManager.initialize(nY, nX, printInterval);
     if (threadsCount == 0)
     for (int i = 0; i < nY; ++i)
     for (int j = 0; j < nX; ++j)
     castRay(nX, nY, j, i,numOfRays);
     else if (threadsCount == -1) {
     IntStream.range(0, nY).parallel() //
     .forEach(i -> IntStream.range(0, nX).parallel() //
     .forEach(j -> castRay(nX, nY, j, i,numOfRays)));}
     else {
     var threads = new LinkedList<Thread>();
     while (threadsCount-- > 0)
     threads.add(new Thread(() -> {
     Pixel pixel;
     while ((pixel = PixelManager.nextPixel()) != null)
     castRay(nX, nY, pixel.col(), pixel.row(),numOfRays);
     }));
     for (var thread : threads) thread.start();
     try { for (var thread : threads) thread.join(); } catch (InterruptedException ignore) {}}
     return this;
     }*/

    /**

     Renders the image using the configured ray tracer and image writer.

     @throws UnsupportedOperationException If the image writer or ray tracer is missing.
     */
    public Camera renderImage()
    {
        if (this.imageWriter == null || this.p0 == null || this.vUp == null || this.vRight == null || this.vTo == null)
            throw new MissingResourceException("Missing filed in camera", "", "");
        if (this.rayTracer == null)
            throw new UnsupportedOperationException("Missing rayTracerBase");


        Pixel.initialize(imageWriter.getNy(), imageWriter.getNx(), 1);

        if(threadsCount==0){
            for (int i = 0; i < this.imageWriter.getNy(); i++) {
                for (int j = 0; j < this.imageWriter.getNy(); j++) {
                    Color color = castRay(j,i);
                    this.imageWriter.writePixel(j, i, color);
                }
            }
            return this;
        }

        if (!adaptive) {
            while (threadsCount-- > 0) {//number of threads to be used for rendering
                new Thread(() -> {//This creates and starts a new thread
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone())//A new Pixel object is created,The loop continues as long as there are more pixels to process.
                        imageWriter.writePixel(pixel.col, pixel.row, rayTracer.traceRay(constructRay(imageWriter.getNx(), imageWriter.getNy(), pixel.col, pixel.row)));//For each pixel, imageWriter.writePixel() is called to write the color of the pixel traced by rayTracer.traceRay()
                }).start();
            }
            Pixel.waitToFinish();// starting all the threads,until all pixels to be processed
        } else {//adaptive rendering should be used.
            while (threadsCount-- > 0) {
                new Thread(() -> {
                    for (Pixel pixel = new Pixel(); pixel.nextPixel(); Pixel.pixelDone())
                        imageWriter.writePixel(pixel.col, pixel.row, AdaptiveSuperSampling(imageWriter.getNx(), imageWriter.getNy(), pixel.col, pixel.row,numOfRays));//AdaptiveSuperSampling() to determine the color of each pixel.
                }).start();
            }
            Pixel.waitToFinish();
        }
        return this;
    }

    private Color castRay(int j,int i){
        Ray ray = constructRay(
                this.imageWriter.getNx(),
                this.imageWriter.getNy(),
                j,
                i);
        return this.rayTracer.traceRay(ray);
    }

    /**
     * Checks the color of the pixel with the help of individual rays and averages between them and only
     * if necessary continues to send beams of rays in recursion
     * @param nX Pixel length
     * @param nY Pixel width
     * @param j The position of the pixel relative to the y-axis
     * @param i The position of the pixel relative to the x-axis
     * @param numOfRays The amount of rays sent
     * @return Pixel color
     */
    private Color AdaptiveSuperSampling(int nX, int nY, int j, int i,  int numOfRays)  {
        Vector Vright = vRight;
        Vector Vup = vUp;
        Point cameraLoc = p0;
        int numOfRaysInRowCol = (int)Math.floor(Math.sqrt(numOfRays));//number of rays in each row and column
        if(numOfRaysInRowCol == 1)  return rayTracer.traceRay(constructRay(nX, nY, j, i));

        Point pIJ = getCenterOfPixel(nX, nY, j, i);

        //calculate the height and width of a pixel in the view plane
        double rY = alignZero(viewPlaneHeight / nY);
        // the ratio Rx = w/Nx, the width of the pixel
        double rX = alignZero(viewPlaneWidth / nX);
        //these lines calculate the height (PRy) and width (PRx) of the sub-pixels
        double PRy = rY/numOfRaysInRowCol;
        double PRx = rX/numOfRaysInRowCol;
        return rayTracer.AdaptiveSuperSamplingRec(pIJ, rX, rY, PRx, PRy,cameraLoc,Vright, Vup,null);//he result is the color of the pixel determined through adaptive super-sampling
    }
    /**
     * get the center point of the pixel in the view plane
     *
     * @param nX number of pixels in the width of the view plane
     * @param nY number of pixels in the height of the view plane
     * @param j  index row in the view plane
     * @param i  index column in the view plane
     * @return the center point of the pixel
     */
    private Point getCenterOfPixel(int nX, int nY, int j, int i) {
        // calculate the ratio of the pixel by the height and by the width of the view plane
        // the ratio Ry = h/Ny, the height of the pixel
        double rY = alignZero(viewPlaneHeight / nY);
        // the ratio Rx = w/Nx, the width of the pixel
        double rX = alignZero(viewPlaneWidth / nX);

        // Xj = (j - (Nx -1)/2) * Rx
        double xJ = alignZero((j - ((nX - 1d) / 2d)) * rX);
        // Yi = -(i - (Ny - 1)/2) * Ry
        double yI = alignZero(-(i - ((nY - 1d) / 2d)) * rY);

        Point pIJ =centerScreenPoint(); // the center of the screen point


        if (!isZero(xJ)) {
            pIJ = pIJ.add(vRight.scale(xJ));
        }
        if (!isZero(yI)) {
            pIJ = pIJ.add(vUp.scale(yI));
        }
        return pIJ;
    }

    /**
     * Producing a grid on top of the image, while the grid is made of equal squares
     *
     * @param interval The wanted interval between the grid's rows and columns
     * @param color    The color of the grid
     * @throws MissingResourceException The render's field imageWriter mustn't be
     *                                  null
     * @throws IllegalArgumentException The grid is supposed to have squares,
     *                                  therefore the given interval must be a
     *                                  divisor of both Nx and Ny
     **/
    public Camera printGrid(int interval, Color color) throws MissingResourceException, IllegalArgumentException {
        if (this.imageWriter == null)
            throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter", null);

        if (this.imageWriter.getNx() % interval != 0 || this.imageWriter.getNy() % interval != 0)
            throw new IllegalArgumentException(
                    "The grid is supposed to have squares, therefore the given interval must be a divisor of both Nx and Ny");

        for (int i = 0; i < this.imageWriter.getNx(); i++)
            for (int j = 0; j < this.imageWriter.getNy(); j++)
                if (i % interval == 0 || (i + 1) % interval == 0 || j % interval == 0 || (j + 1) % interval == 0)
                    this.imageWriter.writePixel(i, j, color);
        return this;
    }

    /**
     * "Printing" the image - producing an unoptimized png file of the image
     * delegation to imageWriter
     *
     * @throws MissingResourceException The render's field imageWriter mustn't be
     *                                  null
     **/
    public Camera writeToImage() throws MissingResourceException {
        if (this.imageWriter == null)
            throw new MissingResourceException("The render's field imageWriter mustn't be null", "ImageWriter", null);
        this.imageWriter.writeToImage();
        return this;
    }

    /**
     * Cast ray from camera in order to color a pixel
     *
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row pixel's row number (pixel index in column)
     *
     */
    private void castRay(int nX, int nY, int col, int row) {
        Ray ray = constructRay(nX, nY, col, row);
        Color color = this.rayTracer.traceRay(ray);
        imageWriter.writePixel(col, row, color);
    }
    /**
     * Cast ray from camera in order to color a pixel
     *
     * @param nX  resolution on X axis (number of pixels in row)
     * @param nY  resolution on Y axis (number of pixels in column)
     * @param col pixel's column number (pixel index in row)
     * @param row pixel's row number (pixel index in column)
     *
     */
    private void castRay(int nX, int nY, int col, int row, int numOfRays) {
        if (numOfRays == 1 || numOfRays == 0) {
            Ray ray = constructRay(nX, nY, col, row);
            Color rayColor = rayTracer.traceRay(ray);
            imageWriter.writePixel(col, row, rayColor);
        } else {
            List<Ray> rays = constructBeamThroughPixel(nX, nY, col, row, numOfRays);
            Color rayColor = rayTracer.traceRay(rays);
            imageWriter.writePixel(col, row, rayColor);
        }

    }
    /**
     * Constructs a beam of rays passing through a specific pixel on the image
     * plane.
     *
     * @param nX         The number of pixels in the X-axis of the image.
     * @param nY         The number of pixels in the Y-axis of the image.
     * @param j          The X-coordinate of the pixel.
     * @param i          The Y-coordinate of the pixel.
     * @param raysAmount The desired number of rays in the beam.
     * @return A list of rays forming the beam passing through the specified pixel.
     * @throws IllegalArgumentException if the distance between the screen and the
     *                                  camera is 0.
     */
    public List<Ray> constructBeamThroughPixel(int nX, int nY, int j, int i, int raysAmount) {
        // The distance between the screen and the camera cannot be 0
        if (isZero(viewPlaneDistance)) {
            throw new IllegalArgumentException("distance cannot be 0");
        }

        int numOfRays = (int) Math.floor(Math.sqrt(raysAmount)); // number of rays in each row or column

        // If there is only one ray, return the single ray through the center of the
        // pixel
        if (numOfRays == 1)
            return List.of(constructRay(nX, nY, j, i));

        // Height and width of a pixel
        double Ry = viewPlaneHeight / nY;// Ry is the height of a single pixel on the view plane.
        double Rx = viewPlaneWidth / nX;
        // Center of the pixel
        double Yi = (i - (nY - 1) / 2d) * Ry;// ny is the total number of pixels in the vertical direction (height of
        // the image grid).
        double Xj = (j - (nX - 1) / 2d) * Rx;
        // Height and width distance between each sub-pixel ray
        double PRy = Ry / numOfRays; // height distance between each ray
        double PRx = Rx / numOfRays; // width distance between each ray

        List<Ray> sample_rays = new ArrayList<>();
        // Generate rays through sub-pixel areas
        for (int row = 0; row < numOfRays; ++row) {
            for (int column = 0; column < numOfRays; ++column) {
                sample_rays.add(constructRaysThroughPixel(PRy, PRx, Yi, Xj, row, column)); // add the ray
            }
        }
        sample_rays.add(constructRay(nX, nY, j, i)); // add the center screen ray
        return sample_rays;
    }

    /**
     * In this function we treat each pixel like a little screen of its own and
     * divide it to smaller "pixels". Through each one we construct a ray. This
     * function is similar to ConstructRayThroughPixel.
     *
     * @param Ry       height of each grid block we divided the pixel into
     * @param Rx       width of each grid block we divided the pixel into
     * @param yi       distance of original pixel from (0,0) on Y axis
     * @param xj       distance of original pixel from (0,0) on X axis
     * @param j        j coordinate of small "pixel"
     * @param i        i coordinate of small "pixel"
     * @return beam of rays through pixel
     */
    private Ray constructRaysThroughPixel(double Ry, double Rx, double yi, double xj, int j, int i) {
        Point Pc =centerScreenPoint(); // the center of the screen point
        /**
         * p0 is the camera position. vTo is a vector pointing from the camera to the
         * center of the view plane. viewPlaneDistance is the distance from the camera
         * to the view plane. Pc is the center point of the view plane.
         *
         */
        double y_sample_i = (i * Ry + Ry / 2d); // The pixel starting point on the y axis
        double x_sample_j = (j * Rx + Rx / 2d); // The pixel starting point on the x axis

        Point Pij = Pc; // The point at the pixel through which a beam is fired
        // Moving the point through which a beam is fired on the x axis
        if (!isZero(x_sample_j + xj)) {
            Pij = Pij.add(vRight.scale(x_sample_j + xj));
        }
        // Moving the point through which a beam is fired on the y axis
        if (!isZero(y_sample_i + yi)) {
            Pij = Pij.add(vUp.scale(-y_sample_i - yi));
        }
        Vector Vij = Pij.subtract(p0);// Vij is the vector from the camera position p0 to the point Pij.
        return new Ray(p0, Vij);// create the ray throw the point we calculate here
    }
    /*
     * vRight is a vector pointing to the right on the view plane. Pij is moved
     * horizontally by scaling vRight with the sum of x_sample_j and xj. This step
     * adjusts the point Pij horizontally on the view plane.
     *
     * vUp is a vector pointing upwards on the view plane. Pij is moved vertically
     * by scaling vUp with the negative sum of y_sample_i and yi. This step adjusts
     * the point Pij vertically on the view plane.
     */
    /**
     * a function to calculate the center of the screen
     * @return
     */
    private Point centerScreenPoint() {
        return p0.add(vTo.scale(viewPlaneDistance));
    }
}
