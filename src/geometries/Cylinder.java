package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.isZero;

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
        // if the given point collides with the base point of the axis ray, just return the normal vector (dir)
        if (point.equals(this.axis.getHead())) return this.axis.getDirection();

        //calculating distance of the given point from base point of the axis ray
        double t = this.axis.getDirection().dotProduct(point.subtract(this.axis.getHead()));
        //if the given point is on one of the bases of the cylinder, we just return a normal vector to the base (dir)
        if (isZero(t) || isZero(t - this.height)) return this.axis.getDirection();
        return super.getNormal(point);
    }
}
