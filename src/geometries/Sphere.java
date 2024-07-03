package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import primitives.Util;
import java.util.*;

import static java.lang.Math.sqrt;
import static primitives.Util.alignZero;


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
        Vector u = center.subtract(ray.getHead());
        double tM = alignZero(ray.getDirection().dotProduct(u));
        double d = alignZero(sqrt(u.lengthSquared() - tM * tM));
        double tH = alignZero(sqrt(radius * radius - d * d));
        double t1 = alignZero(tM + tH);
        double t2 = alignZero(tM - tH);

        // If there are no intersections, return null
        if (d >= radius)
            return null;

        if (t1 <= 0 && t2 <= 0)
            return null;

        // If there are two intersections, return them as a list
        if (t1 > 0 && t2 > 0) {
            List<Point> result = List.of(ray.getPoint(t1), ray.getPoint(t2));
            if (result.get(0).getX() < result.get(1).getX())
                result = List.of(result.get(1), result.get(0));
            return result;
        }

        // If there is one intersection, return it as a list
        if (t1 > 0)
            return List.of(ray.getPoint(t1));
        else
            return List.of(ray.getPoint(t2));
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {

        Vector pointToCenter;
        try {
            pointToCenter = center.subtract(ray.getHead());
        } catch (IllegalArgumentException ignore) {
            return List.of(new GeoPoint(this, ray.getPoint(radius)));
        }

        double tm = pointToCenter.dotProduct(ray.getDirection());
        double distanceFromCenterSquared = pointToCenter.dotProduct(pointToCenter) - tm * tm;
        double thSquared = radius*radius- distanceFromCenterSquared;
        //check that ray crosses area of sphere, if not then return null
        if (alignZero(thSquared) <= 0) return null;

        double th = sqrt(thSquared);
        double secondDistance = tm + th;
        if (alignZero(secondDistance) <= 0) return null;
        double firstDistance = tm - th;
        GeoPoint gp2 = new GeoPoint(this, ray.getPoint(secondDistance));
        return firstDistance <= 0 ? List.of(gp2) : List.of(new GeoPoint(this, ray.getPoint(firstDistance)), gp2);
    }
}




