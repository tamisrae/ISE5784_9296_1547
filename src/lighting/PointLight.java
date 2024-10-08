package lighting;

import primitives.Color;
import primitives.Double3;
import primitives.Point;
import primitives.Vector;

public class PointLight extends Light implements LightSource{
    private final Point position; //The position point of the light source in the space

    public double getDistance(Point point) {
        return position.distance(point);
    }
    /**
     * kC is The specular attenuation factor, required to ensure that the denominator in getIntensity > 1
     * kL is The light source attenuation factor
     * kQ is The attenuation factor of the energy coming to the point
     * <p>
     * the formula is: Il = I0/(Kc + Ki*d + Kq*d^2);
     */
    private double kC = 1, kL = 0, kQ = 0; // Light factor -> constant, linear and Quadratic

    /**
     * constructor for the intensity
     *
     * @param intensity of the intensity of the source of the light
     */
    public PointLight(Color intensity, Point position) {
        super(intensity);
        this.position = position;
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

    /**
     * Return normalize direction vector from the light source to the object
     *
     * @param point the point on the object (Point)
     * @return normalize direction vector from the light source to the object (Vector)
     */
    @Override
    public Vector getL(Point point) {
        return point.subtract(this.position).normalize();
    }

    /**
     * setkC function
     *
     * @param kC
     * @return
     */
    public PointLight setKc(double kC) {
        this.kC = kC;
        return this;
    }

    /**
     * setkL function
     *
     * @param kL
     * @return
     */
    public PointLight setKl(double kL) {
        this.kL = kL;
        return this;
    }

    /**
     * setkQ function
     *
     * @param kQ
     * @return
     */
    public PointLight setKq(double kQ) {
        this.kQ = kQ;
        return this;
    }

}
