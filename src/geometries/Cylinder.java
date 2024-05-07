package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

/**
 *A class for representing a cylinder
 */
public class Cylinder extends Tube{
    private final double height;

    /**
     * ctor
     * @param height of the cylinder
     * @param radius of the cylinder
     * @param ray the direction of the cylinder
     */
    public Cylinder(double height, double radius, Ray ray) {
        super(ray,radius);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        return super.getNormal(point);
    }
}
