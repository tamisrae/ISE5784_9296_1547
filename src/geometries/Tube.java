package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.List;

/**
 *A class for representing a tube
 */
public class   Tube extends RadialGeometry{

    protected final Ray axis;

    /**
     *ctor
     * @param axis direction
     * @param radius radius
     */
    public Tube(Ray axis, double radius) {
        super(radius);
        this.axis = axis;
    }

    @Override
    public Vector getNormal(Point point) {
        double t = this.axis.getDirection().dotProduct(point.subtract(this.axis.getHead()));
        return point.subtract(this.axis.getPoint(t)).normalize();
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        return null;
    }
}
