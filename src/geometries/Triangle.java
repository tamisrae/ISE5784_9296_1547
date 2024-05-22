package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

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
    public List<Point> findIntersections(Ray ray) {
        //calculate according to the calculation in the course's book
        Point p = super.plane.findIntersections(ray).get(0);

        if (p == null) {//at first find if thar is intersection with the plane of the triangle
            return null;
        }

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
            return List.of(p);
        }
        return null;
    }
}
