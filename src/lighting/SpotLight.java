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
    private double NarrowBeam = 1;

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
    public Color getIntensity(Point p) {
        double cos = alignZero(direction.dotProduct(getL(p)));
        return NarrowBeam != 1
                ? super.getIntensity(p).scale(Math.pow(Math.max(0, direction.dotProduct(getL(p))), NarrowBeam))
                : super.getIntensity(p).scale(Math.max(0, direction.dotProduct(getL(p))));
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

    public SpotLight setNarrowBeam(double NarrowBeam) {
        this.NarrowBeam=NarrowBeam;
        return this;
    }

}

