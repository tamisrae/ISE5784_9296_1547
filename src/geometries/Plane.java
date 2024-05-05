package geometries;

import primitives.Point;
import primitives.Vector;

public class Plane implements Geometry{

    private final Point q;
    private final Vector normal;

    public Plane(Point d1,Point d2, Point d3) {
        this.q = d1;
        normal=null;
    }
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
