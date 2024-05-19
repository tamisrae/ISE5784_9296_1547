package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;
import java.util.*;


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

    /**
     * Computes the intersection point(s) between the current sphere and a given ray.
     */
    @Override
    public List<Point> findIntersections(Ray ray) throws IllegalArgumentException {
        if (ray == null) {
            throw new IllegalArgumentException("Ray cannot be null");
        }
        Point p0 = ray.getHead();
        Vector dir = ray.getDirection();
        if (center.equals(p0)) {//ray stars at the center
            return List.of(ray.getPoint(radius));
        }
        Vector u = this.center.subtract(p0);
        double tm = dir.dotProduct(u);
        double d = Util.alignZero(Math.sqrt(u.lengthSquared() - (tm * tm)));
        if (d >= radius) {//ray does not intersect
            return null;
        }
        double th = Math.sqrt((radius * radius) - (d * d));
        double t1 = Util.alignZero(tm + th);
        double t2 = Util.alignZero(tm - th);
        if (t1 <= 0 && t2 <= 0) {
            return null;
        }
        if (t1 <= 0 && t2 > 0) {
            return List.of(ray.getPoint(t2));
        }
        if (t1 > 0 && t2 <= 0) {
            return List.of(ray.getPoint(t1));
        }
        return List.of(ray.getPoint(t1), ray.getPoint(t2));
    }
}
