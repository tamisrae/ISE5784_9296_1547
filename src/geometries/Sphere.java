package geometries;

import primitives.Point;
import primitives.Vector;

/**
 * A class for representing a sphere
 */
public class Sphere extends RadialGeometry{

    private final Point center;

    /**
     *ctor
     * @param radius radius
     * @param center center point
     */
    public Sphere(double radius,Point center){
        super(radius);
        this.center=center;
    }

    @Override
    public Vector getNormal(Point point) {
        return point.subtract(this.center).normalize();
    }
}
