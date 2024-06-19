package geometries;
import primitives.Point;
import primitives.Ray;

import java.util.List;

import static primitives.Util.*;

public abstract class Intersectable {

    /**
     * function that returns a list of all intersections of a ray with the geometry
     *
     * @param ray the ray to check intersection with the geometry object
     * @return a list of all intersections points
     */
    public List<Point> findIntersections(Ray ray) {
        var geoList = findGeoIntersections(ray);
        return geoList == null ? null
                : geoList.stream().map(gp -> gp.point).toList();
    }


    /**
     * function that returns a list of all intersections of a ray
     * @param ray the ray to check for intersections
     * @return a list of all intersections points
     */
    public List<GeoPoint> findGeoIntersections(Ray ray) {
        return findGeoIntersectionsHelper(ray);
    }

    /**
     * helper function that returns a list of all intersections of a ray
     * @param ray the ray to check for intersections
     * @return a list of all intersections points
     */
    protected abstract List<GeoPoint> findGeoIntersectionsHelper(Ray ray);

    /**
     * helper class to connect point to Geometry
     */
    public static class GeoPoint {

        /**
         * geometric shape
         */
        public final Geometry geometry;
        /**
         * point in/on the geometric shape
         */
        public final Point point;

        /**
         * constructor
         * @param geometry geometric shape
         * @param point point on/in geometric shape
         */
        public GeoPoint(Geometry geometry, Point point) {
            this.geometry = geometry;
            this.point = point;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GeoPoint geoPoint = (GeoPoint) o;
            return geometry.equals(geoPoint.geometry) && point.equals(geoPoint.point);
        }

        @Override
        public String toString() {
            return "GeoPoint: " +
                    "geometry: " + geometry +
                    ", point: " + point ;
        }
    }

}
