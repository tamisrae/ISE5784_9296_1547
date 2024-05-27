package renderer;

import primitives.Point;
import primitives.Vector;
import primitives.Ray;

import java.lang.module.ModuleDescriptor;
import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

public class Camera implements Cloneable{
    /**
     *The point where the Camera is located.
     */
    Point location=null;

    /**
     * Vector to the right of the Camera, up, and where it was pointing.
     */
    Vector vUp=null, vTo=null, vRight=null;

    /**
     * The height of the view plane, the width of the view plane, and its distance from the camera.
     */
    double viewPlaneH =0.0, viewPlaneW=0.0, viewPlaneD=0.0;

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

    public static Builder getBuilder()
    {
        return new Builder();
    }

    public Ray constructRay(int nX, int nY, int j, int i)
    {return null;
    }

    /**
     * inner class
     */
    public static class Builder{
      final Camera camera= new Camera();
        public Builder setLocation(Point location){
            if (location == null) {
                throw new IllegalArgumentException("Location cannot be null");
            }
          camera.location=location;
          return this;
        }
        public Builder setDirection(Vector up, Vector to){
            if (to == null || up == null) {
                throw new IllegalArgumentException("Direction vectors cannot be null");
            }
            if (!isZero(to.dotProduct(up))) {
                throw new IllegalArgumentException("vTo and vUp must be orthogonal");
            }
            camera.vUp=up;
            camera.vTo=to;
            return this;
        }
        public  Builder setVpSize(double width,double height){
            if (alignZero(width) <= 0 || alignZero(height) <= 0)
                throw new IllegalArgumentException("The height and width must be greater than zero");
            camera.viewPlaneW=width;
            camera.viewPlaneH=height;
            return this;
        }
        public Builder setVpDistance(double distance){
            if (distance <= 0) {
                throw new IllegalArgumentException("View plane distance must be positive");
            }
            camera.viewPlaneD=distance;
            return this;
        }

        /**
         * Builds the Camera object.
         * @return the constructed Camera object.
         * @throws MissingResourceException if any required field is missing.
         */
        public Camera build() throws MissingResourceException {

            String missingData="Missing rendering data";

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

            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
