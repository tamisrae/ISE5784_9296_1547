package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import static primitives.Util.*;
import java.util.List;

/**
 *A class for representing a Plane
 */
public class Plane implements Geometry{

    private final Point q;
    private final Vector normal;

    /**
     *ctor
     * @param d1 First point to represent the plane
     * @param d2 second point to represent the plane
     * @param d3 third point to represent the plane
     */
    public Plane(Point d1,Point d2, Point d3) {
        q = d1;
        //normal vector is calculated by the cross product of two vectors
        //representing two edges of the plane
        normal = d2.subtract(d1).crossProduct(d2.subtract(d3)).normalize();
    }

    /**
     *ctor
     * @param normal vector for represent the plane
     * @param point a point to represent the plane
     */
    public Plane(Vector normal, Point point){
        q = point;
        this.normal = normal.normalize();
    }

    public Vector getNormal() {
        return normal;
    }
    public Vector getNormal(Point point){ return normal; }

    /**
     * Computes the intersection point(s) between the current plane and a given ray.
     */
    @Override
    public List<Point> findIntersections(Ray ray) {
        if (ray == null) {//ray cannot be null
            throw new IllegalArgumentException("Ray cannot be null");
        }
        if (ray.getHead().equals(this.q)) {//start in the plane
            return null;
        }
        //calculate according to the calculation in the course's book
        Vector rayToNormal = this.q.subtract(ray.getHead());
        double numerator = this.normal.dotProduct(rayToNormal);
        double denominator = this.normal.dotProduct(ray.getDirection());
        if (isZero(denominator)) {
            return null;
        }
        double t = alignZero(numerator / denominator);
        if (t > 0) {
            return List.of(ray.getPoint(t));
        }
        return null;
    }
}
