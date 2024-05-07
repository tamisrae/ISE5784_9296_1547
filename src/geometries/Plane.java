package geometries;

import primitives.Point;
import primitives.Vector;

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
        this.q = d1;
        normal=null;
    }

    /**
     *ctor
     * @param normal vector for represent the plane
     * @param point a point to represent the plane
     */
    public Plane(Vector normal, Point point){
        q=point;
        this.normal= normal.normalize();
    }

    public Vector getNormal() {
        return normal;
    }
    public Vector getNormal(Point point){
        return null;
    }
}
