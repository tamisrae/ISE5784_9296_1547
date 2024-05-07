package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 *A class for representing a tube
 */
public class Tube extends RadialGeometry{

    protected final Ray axis;

    /**
     *ctor
     * @param axis direction
     * @param radius radius
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        return null;
    }
}
