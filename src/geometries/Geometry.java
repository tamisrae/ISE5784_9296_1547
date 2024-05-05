package geometries;

import primitives.Point;
import primitives.Vector;

public abstract interface Geometry {

    public Vector getNormal(Point point);
}
