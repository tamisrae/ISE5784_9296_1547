package geometries;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

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

    @Override
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        Vector dir = ray.getDirection();
        Vector v = axis.getDirection();
        double dirV = dir.dotProduct(v);

        if (ray.getHead().equals(axis.getHead())) { // In case the ray starts on the p0.
            if (isZero(dirV))
                return List.of(new Intersectable.GeoPoint(this, ray.getPoint(radius)));

            if (dir.equals(v.scale(dir.dotProduct(v))))
                return null;


            return List.of(new Intersectable.GeoPoint(this, ray.getPoint(
                    Math.sqrt(radius * radius / dir.subtract(v.scale(dir.dotProduct(v))).lengthSquared()))));


        }
        Vector deltaP = ray.getHead().subtract(axis.getHead());
        double dpV = deltaP.dotProduct(v);

        double a = 1 - dirV * dirV;
        double b = 2 * (dir.dotProduct(deltaP) - dirV * dpV);
        double c = deltaP.lengthSquared() - dpV * dpV - radius * radius;

        if (isZero(a)) {
            if (isZero(b)) { // If a constant equation.
                return null;
            }
            return List.of(new Intersectable.GeoPoint(this,ray.getPoint(-c / b))); // if it's linear, there's a solution.
        }

        double discriminant = alignZero(b * b - 4 * a * c);

        if (discriminant < 0) // No real solutions.
            return null;

        double t1 = alignZero(-(b + Math.sqrt(discriminant)) / (2 * a)); // Positive solution.
        double t2 = alignZero(-(b - Math.sqrt(discriminant)) / (2 * a)); // Negative solution.

        if (discriminant <= 0) // No real solutions.
            return null;

        if (t1 > 0 && t2 > 0) {
            List<GeoPoint> points = new LinkedList<>();
            points.add(new Intersectable.GeoPoint(this,ray.getPoint(t1)));
            points.add(new Intersectable.GeoPoint(this,ray.getPoint(t2)));
            return points;
        }
        else if (t1 > 0) {
            List<GeoPoint> points = new LinkedList<>();
            points.add(new Intersectable.GeoPoint(this,ray.getPoint(t1)));
            return  points;
        }
        else if (t2 > 0) {
            List<GeoPoint> points = new LinkedList<>();
            points.add(new Intersectable.GeoPoint(this,ray.getPoint(t2)));
            return points;
        }
        return null;
    }
}
