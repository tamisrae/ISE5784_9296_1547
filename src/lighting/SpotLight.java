package lighting;

import primitives.Color;
import primitives.Point;
import primitives.Vector;

import static primitives.Util.alignZero;

/**
 * SpotLight class
 */

public class SpotLight extends PointLight {

    private final Vector direction;


    /**
     * Constructor that sets the light's intensity.
     *
     * @param intensity the light's intensity.
     * @param position  the light's position.
     * @param direction the light's direction.
     */
    public SpotLight(Color intensity, Point position, Vector direction) {
        super(intensity, position);
        this.direction = direction.normalize();
    }


    /**
     *Stregth of light,getIntensity
     */
    @Override
    public Color getIntensity(Point p){
        double d2 = position.distanceSquared(p);
        double d = Math.sqrt(d2);
        return intensity.scale(Math.max(0, direction.dotProduct(getL(p)))).scale(1.0d/ (kC + kL *d + kQ *d2));

    }

    /**
     * setter for kC
     *
     * @param kC - new value for kC
     * @return this PointLight for builder pattern
     */
    @SuppressWarnings("unused")
    public SpotLight setKc(double kC) {
        super.setKc(kC);
        return this;
    }

    /**
     * setter for kL
     *
     * @param kL - new value for kL
     * @return this PointLight for builder pattern
     */
    public SpotLight setKl(double kL) {
        super.setKl(kL);
        return this;
    }

    /**
     * setter for kQ
     *
     * @param kQ - new value for kQ
     * @return this PointLight for builder pattern
     */
    public SpotLight setKq(double kQ) {
        super.setKq(kQ);
        return this;
    }


}