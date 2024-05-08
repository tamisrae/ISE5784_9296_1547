package geometries;

import primitives.Point;
import primitives.Vector;

/**
 *A class for representing a RadialGeometry
 */
public abstract class RadialGeometry implements Geometry {

    final protected double radius;

    /**
     *ctor
     * @param radius of the RadialGeometry
     */
    public RadialGeometry(double radius){
        this.radius = radius;
    }

}
