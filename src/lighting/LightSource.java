package lighting;

import primitives.*;

/**
 * Interface for light sources.
 */
public interface LightSource {

    /**
     * returns the color of the light source
     * @param point point to check the color
     * @return color of the light source
     */
    Color getIntensity(Point point);

    /**
     * returns the direction of the light source
     * @param point point to check the direction
     * @return direction of the light source
     */
    Vector getL(Point point);
    double getDistance(Point point);

}