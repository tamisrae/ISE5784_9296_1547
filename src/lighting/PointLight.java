package lighting;

import primitives.*;

/**
 * class PointLight represents a point light source
 */
public class PointLight extends Light implements LightSource {

    protected Point position;
    protected double kC = 1;
    protected double kL = 0;
    protected double kQ = 0;

    /**
     * Constructor that sets the light's intensity.
     *
     * @param intensity the light's intensity.
     * @param position  the light's position.
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
    }

    /**
     * setter for kC
     *
     * @param kC - new value for kC
     * @return this PointLight for builder pattern
     */
    @SuppressWarnings("unused")
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * setter for kL
     *
     * @param kL - new value for kL
     * @return this PointLight for builder pattern
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * setter for kQ
     *
     * @param kQ - new value for kQ
     * @return this PointLight for builder pattern
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }


    /**
     * Calculate and return the intensity light on specific point
     *
     * @param point the point on the object (Point)
     * @return the intensity (Color)
     */
    @Override
    public Color getIntensity(Point point) {
        // The intensity of the color of the light
        // (the distribution of the light in the surface area)
        // is proportional to squared distance

        //Calculate the denominator of the proportion
        double distance = this.position.distance(point);
        double distanceSquared = distance * distance;
        double factor = this.kC + this.kL * distance + this.kQ * distanceSquared;

        //Return the final intensity
        return getIntensity().scale(1/factor);
    }


    @Override
    public Vector getL(Point point) {
        return point.subtract(position).normalize();
    }


}