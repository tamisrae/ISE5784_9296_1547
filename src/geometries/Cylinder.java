package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

public class Cylinder extends Tube{
    private final double height;

    public Cylinder(double height, double radius, Ray ray) {
        super(ray,radius);
        this.height = height;
    }

    @Override
    public Vector getNormal(Point point) {
        return super.getNormal(point);
    }
}
