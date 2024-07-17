package primitives;
import java.util.LinkedList;
import java.util.List;
import geometries.Intersectable.GeoPoint;

import static primitives.Util.*;

public class Ray {
    private final Point head;
    private final Vector direction;
    private static final double DELTA = 0.1;


    /**
     *parameters constructor
     * @param head=point
     * @param direction=vector
     */
    public Ray(Point head, Vector direction) {
        this.head = head;
        this.direction = direction.normalize(); // Assuming normalize() method normalizes the vector
    }
    /**
     * Constructor to initialize ray
     *
     * @param p0  point of the ray
     * @param n   normal vector
     * @param dir direction vector of the ray
     */
    public Ray(Point p0, Vector dir, Vector n) {
        double delta = dir.dotProduct(n) >= 0 ? DELTA : -DELTA;
        this.head = p0.add(n.scale(delta));
        this.direction = dir;
    }


    /**
     *
     * @return head
     */
    public Point getHead() {
        return head;
    }

    /**
     *
     * @return direction
     */
    public Vector getDirection() {
        return direction;
    }

    /**
     * @param obj=point
     * @return If two points are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Ray) {
            if(head.xyz==((Ray) obj).head.xyz){
                return true;
            }
        }
        return false;
    }

    /**
     * @return A string representing the vector class
     */
    public String tostring(){
        return "Ray{"+
                "head='"+head+
                "direction'"+direction+
                "}";

    }


    /**
     *get Point at specific distance in the ray's direction
     *
     * @param t is a distance for reaching new Point
     * @return new {@link Point}
     */

    public Point getPoint(double t) {
        if (isZero(t)) {
            // אם המרחק הוא 0, יש להחזיר את נקודת ההתחלה עצמה
            return head;
        }

        return head.add(direction.scale(t));
    }
    public Point findClosestPoint(List<Point> points) {
        return points == null || points.isEmpty() ? null
                : findClosestGeoPoint(points.stream().map(p -> new GeoPoint(null, p)).toList()).point;
    }

    /**
     * Return the closest GeoPoint from all intersection GeoPoints
     *
     * @param geoPointList list of intersections
     * @return {@link GeoPoint}
     */
    /**public GeoPoint findClosestGeoPoint(List<GeoPoint> geoPointList) {

     GeoPoint closestPoint = null;
     double minDistance = Double.MAX_VALUE;
     double geoPointDistance; // the distance between the "this.p0" to each point in the list

     if (!geoPointList.isEmpty()) {
     for (var geoPoint : geoPointList) {
     geoPointDistance = this.head.distance(geoPoint.point);
     if (geoPointDistance < minDistance) {
     minDistance = geoPointDistance;
     closestPoint = geoPoint;
     }
     }
     }
     return closestPoint;
     }*/
    public GeoPoint findClosestGeoPoint(List<GeoPoint> geoPointList) {

        if (geoPointList == null) {
            return null;
        }

        GeoPoint closestPoint = null;
        double minDistance = Double.MAX_VALUE;
        double geoPointDistance; // the distance between the "this.p0" to each point in the list

        if (!geoPointList.isEmpty()) {
            for (var geoPoint : geoPointList) {
                geoPointDistance = this.head.distance(geoPoint.point);
                if (geoPointDistance < minDistance) {
                    minDistance = geoPointDistance;
                    closestPoint = geoPoint;
                }
            }
        }
        return closestPoint;
    }

    /**
     *
     * @param n         normal to the geometry
     * @param radius    radius of the beam circle
     * @param distance  distance of the eam circle
     * @param numOfRays num of rays in the beam
     * @return list of beam rays
     */
    public List<Ray> generateBeam(Vector n, double radius, double distance, int numOfRays) {
        List<Ray> rays = new LinkedList<Ray>();
        rays.add(this);// Including the main ray
        if (numOfRays == 1 || isZero(radius))// The component (glossy surface /diffuse glass) is turned off
            return rays;

        // the 2 vectors that create the virtual grid for the beam
        Vector nX = direction.createNormal();
        Vector nY = direction.crossProduct(nX);

        Point centerCircle = this.getPoint(distance);
        Point randomPoint;
        Vector v12;

        double rand_x, rand_y, delta_radius = radius / (numOfRays - 1);
        double nv = n.dotProduct(direction);

        for (int i = 1; i < numOfRays; i++) {
            randomPoint = centerCircle;
            rand_x = random(-radius, radius);
            rand_y = randomSign() * Math.sqrt(radius * radius - rand_x * rand_x);

            try {
                randomPoint = randomPoint.add(nX.scale(rand_x));
            } catch (Exception ex) {
            }

            try {
                randomPoint = randomPoint.add(nY.scale(rand_y));
            } catch (Exception ex) {
            }

            v12 = randomPoint.subtract(head).normalize();

            double nt = alignZero(n.dotProduct(v12));

            if (nv * nt > 0) {
                rays.add(new Ray(head, v12));
            }
            radius -= delta_radius;
        }

        return rays;
    }
}

