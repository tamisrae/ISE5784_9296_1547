package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

import static primitives.Util.alignZero;

/**
 *A class for representing a triangle
 */
public class Triangle extends Polygon {

    /**
     *ctor
     * @param d1 first point of the triangle
     * @param d2 second point of the triangle
     * @param d3 third point of the triangle
     */
    public Triangle(Point d1, Point d2, Point d3){
        super(d1,d2,d3);
    }

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        //calculate according to the calculation in the course's book
        //List<Point> points = super.plane.findIntersections(ray);
        List<GeoPoint> points = this.plane.findGeoIntersections(ray);
        if (points == null) //at first find if thar is intersection with the plane of the triangle
            return null;

        Vector v1 = this.vertices.get(0).subtract(ray.getHead());
        Vector v2 = this.vertices.get(1).subtract(ray.getHead());
        Vector v3 = this.vertices.get(2).subtract(ray.getHead());
        Vector n1 = v1.crossProduct(v2).normalize();
        Vector n2 = v2.crossProduct(v3).normalize();
        Vector n3 = v3.crossProduct(v1).normalize();
        double a = ray.getDirection().dotProduct(n1);
        double b = ray.getDirection().dotProduct(n2);
        double c = ray.getDirection().dotProduct(n3);
        if (a == 0 || b == 0 || c == 0) {
            return null;
        }
        if (a * b > 0 && b * c > 0) {
            return List.of(new GeoPoint(this, points.get(0).point));
        }
        return null;
    }

    /**
     * Calculation of intersection with a triangle using the center of gravity of the triangle
     * @param ray
     * @param maxDistance
     * @return
     */
    public List<GeoPoint> findGeoIntersectionsHelper(Ray ray, double maxDistance) {

        List<Point> intersection = this.plane.findIntersections(ray);
        if (intersection == null)
            return null;

        Point p0 = ray.getHead();
        Vector v = ray.getDirection();

        Vector v1 = vertices.get(0).subtract(p0);
        Vector v2 = vertices.get(1).subtract(p0);
        Vector n1 = v1.crossProduct(v2).normalize();
        double vn1 = alignZero(v.dotProduct(n1));
        if (vn1 == 0) return null;

        Vector v3 = vertices.get(2).subtract(p0);
        Vector n2 = v2.crossProduct(v3).normalize();
        double vn2 = alignZero(v.dotProduct(n2));
        if (vn1 * vn2 <= 0) return null;

        Vector n3 = v3.crossProduct(v1).normalize();
        double vn3 = alignZero(v.dotProduct(n3));
        if (vn1 * vn3 <= 0) return null;


        Point intersect = intersection.get(0);
        return (p0.distance(intersect) - maxDistance <=0) ? List.of(new GeoPoint(this, intersect)) : null;
    }

}
