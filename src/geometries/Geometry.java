package geometries;

import primitives.Point;
import primitives.Vector;

/**
 *An interface for Geometric shapes
 */
public abstract interface Geometry {

    /**
     * virtual function for get the normal
     * @param point on the shape
     * @return normal vector of the shape at the point
     */
    public Vector getNormal(Point point);
}
