////
////package renderer;
////
////import geometries.Geometry;
////import geometries.Intersectable;
////import lighting.LightSource;
////import primitives.*;
////import scene.Scene;
////import geometries.Intersectable.GeoPoint;
////
////import java.util.List;
////
////import static primitives.Util.alignZero;
////
////public class SimpleRayTracer extends RayTracerBase {
////    /**
////     * Constructor that get a scene
////     *
////     * @param scene
////     */
////    public SimpleRayTracer(Scene scene) {
////        super(scene);
////    }
////
////    private static final double DELTA = 0.1;
////
////
////    private boolean unshaded(GeoPoint gp, Vector l, Vector n,LightSource ls){
////        Vector lightDirection=l.scale(-1);
////        Ray ray=new Ray(gp.point,lightDirection);
////        List<Point> intersections=scene.geometries.findIntersections(ray);
////        if (intersections!=null){
////            for (Point point:intersections){
////                if(point.distance(ray.getHead())>ls.getDistance(point))
////                    return false;
////            }
////        }
////        return true;
////    }
////    /**
////     * Get the color of an intersection point
////     * @param point point of intersection
////     * @return Color of the intersection point
////     */
////    /**
////     * private Color calcColor(GeoPoint point) {
////     * return scene.ambientLight.getIntensity();
////     * }
////     */
////
////    private Color calcColor(GeoPoint geoPoint, Ray ray) {
////        return scene.ambientLight.getIntensity().add(geoPoint.geometry.getEmission())
////                .add(calcLocalEffects(geoPoint, ray));
////    }
////
////
////    /**
////     * Calculates the effect of different light sources on a point in the scene
////     * according to the Phong model.
////     *
////     * @param intersection The point on the geometry in the scene.
////     * @param ray          The ray from the camera to the intersection.
////     * @return The color of the point affected by local light sources.
////     */
////    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
////        Vector v = ray.getDirection();
////        Vector n = intersection.geometry.getNormal(intersection.point);
////        double nv = alignZero(n.dotProduct(v));
////        if (nv == 0)
////            return Color.BLACK;
////
////        int nShininess = intersection.geometry.getMaterial().nShininess;
////
////        Double3 kd = intersection.geometry.getMaterial().kD, ks = intersection.geometry.getMaterial().kS;
////
////        Color color = Color.BLACK;
////        for (LightSource lightSource : scene.lights) {
////            Vector l = lightSource.getL(intersection.point);
////            double nl = alignZero(n.dotProduct(l));
////
////            if ((nl * nv > 0)&&unshaded(intersection,l,n,lightSource)) { // sign(nl) == sign(nv)
////                Color lightIntensity = lightSource.getIntensity(intersection.point);
////                color = color.add(calcDiffuse(kd, nl, lightIntensity),
////                        calcSpecular(ks, l, n, nl, v, nShininess, lightIntensity));
////            }
////        }
////        return color;
////    }
////
////    /**
////     * Calculates the diffuse component of light reflection.
////     *
////     * @param kd             The diffuse reflection coefficient.
////     * @param nl             The dot product between the normal vector and the light
////     *                       vector.
////     * @param lightIntensity The intensity of the light source.
////     * @return The color contribution from the diffuse reflection.
////     */
////    private Color calcDiffuse(Double3 kd, double nl, Color lightIntensity) {
////        return lightIntensity.scale(kd.scale(Math.abs(nl)));
////    }
////
////    /**
////     * Calculates the specular component of light reflection.
////     *
////     * @param ks             The specular reflection coefficient.
////     * @param l              The light vector.
////     * @param n              The normal vector.
////     * @param nl             The dot product between the normal vector and the light
////     *                       vector.
////     * @param v              The view vector.
////     * @param nShininess     The shininess coefficient.
////     * @param lightIntensity The intensity of the light source.
////     * @return The color contribution from the specular reflection.
////     */
////    private Color calcSpecular(Double3 ks, Vector l, Vector n, double nl, Vector v, int nShininess,
////                               Color lightIntensity) {
////        Vector r = l.add(n.scale(-2 * nl)); // nl must not be zero!
////        double minusVR = -alignZero(r.dotProduct(v));
////        if (minusVR <= 0) {
////            return new primitives.Color(Color.BLACK.getColor()); // View from direction opposite to r vector
////        }
////        return lightIntensity.scale(ks.scale(Math.pow(minusVR, nShininess)));
////    }
////
////
////    @Override
////    public Color traceRay(Ray ray) {
////
////        List<GeoPoint> intersections = this.scene.geometries.findGeoIntersections(ray);
////
////        if (intersections == null) {
////            return this.scene.background;
////        }
////
////        GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
////
////        return calcColor(closestPoint,ray);
////    }
////}
//package renderer;
//
//import geometries.Geometry;
//import geometries.Intersectable;
//import lighting.LightSource;
//import primitives.*;
//import scene.Scene;
//import geometries.Intersectable.GeoPoint;
//
//import java.util.List;
//
//import static primitives.Util.alignZero;
//import static primitives.Util.isZero;
//
//public class SimpleRayTracer extends RayTracerBase {
//    /**
//     * Constructor that get a scene
//     *
//     * @param scene
//     */
//    public SimpleRayTracer(Scene scene) {
//        super(scene);
//    }
//    private static final double DELTA = 0.1;
//
//
//    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource lightSource, double nl) {
//        Vector lightDirection = l.scale(-1);//from the point to light score
//        Vector epsVector =n.scale(nl<0? DELTA: -DELTA);
//        Point point =gp.point.add(epsVector);
//        Ray lightRay = new Ray(point, lightDirection);
//        List<Point> Intersection = scene.geometries.findIntersections(lightRay);
//        if (Intersection == null) return true;
//
//        for (Point g : Intersection)
//            if(g.distance(lightRay.getHead())<lightSource.getDistance(point))
//                return false;
//
//        return true;
//    }
//
//
//    /**
//     * Get the color of an intersection point
//     * @param point point of intersection
//     * @return Color of the intersection point
//     */
//    /**
//     * private Color calcColor(GeoPoint point) {
//     * return scene.ambientLight.getIntensity();
//     * }
//     */
//
//
//    private Color calcColor(GeoPoint geoPoint, Ray ray) {
//        return scene.ambientLight.getIntensity().add(geoPoint.geometry.getEmission())
//                .add(calcLocalEffects(geoPoint, ray));
//    }
//
//    /**
//     * get point in scene and calculate its color
//     *
//     * @param gp
//     * @param ray
//     * @param level
//     * @param k
//     * @return
//     */
//    /** private Color calcColor(GeoPoint gp, Ray ray, int level, Double3 k) {
//     Vector v = ray.getDirection();
//     Vector n = gp.geometry.getNormal(gp.point);
//     double vn = v.dotProduct(n);
//     if (isZero(vn))
//     return Color.BLACK;
//
//     Color color = calcLocalEffects(gp, ray,k).add(gp.geometry.getEmission());
//
//     return 1 == level ? color : color.add(calcGlobalEffects(gp, v, level, k));
//     }**/
//
//    /**
//     * Calculates the effect of different light sources on a point in the scene
//     * according to the Phong model.
//     *
//     * @param intersection The point on the geometry in the scene.
//     * @param ray          The ray from the camera to the intersection.
//     * @return The color of the point affected by local light sources.
//     */
//    private Color calcLocalEffects(GeoPoint intersection, Ray ray) {
//        Vector v = ray.getDirection();
//        Vector n = intersection.geometry.getNormal(intersection.point);
//        double nv = alignZero(n.dotProduct(v));
//        if (nv == 0)
//            return Color.BLACK;
//        Material material=intersection.geometry.getMaterial();
//        Color color= intersection.geometry.getEmission();
//
//        int nShininess = intersection.geometry.getMaterial().nShininess;
//
//        Double3 kd = intersection.geometry.getMaterial().kD, ks = intersection.geometry.getMaterial().kS;
//
//        for (LightSource lightSource : scene.lights) {
//            Vector l = lightSource.getL(intersection.point);
//            double nl = alignZero(n.dotProduct(l));
//
//            if ((nl * nv > 0)&&unshaded(intersection,l,n,lightSource,nl)) { // sign(nl) == sign(nv)
//                Color lightIntensity = lightSource.getIntensity(intersection.point);
//                color = color.add(calcDiffuse(kd, nl, lightIntensity),
//                        calcSpecular(ks, l, n, nl, v, nShininess, lightIntensity));
//            }
//        }
//        return color;
//    }
//
//    /**
//     * Calculates the diffuse component of light reflection.
//     *
//     * @param kd             The diffuse reflection coefficient.
//     * @param nl             The dot product between the normal vector and the light
//     *                       vector.
//     * @param lightIntensity The intensity of the light source.
//     * @return The color contribution from the diffuse reflection.
//     */
//    private Color calcDiffuse(Double3 kd, double nl, Color lightIntensity) {
//        return lightIntensity.scale(kd.scale(Math.abs(nl)));
//    }
//
//    /**
//     * Calculates the specular component of light reflection.
//     *
//     * @param ks             The specular reflection coefficient.
//     * @param l              The light vector.
//     * @param n              The normal vector.
//     * @param nl             The dot product between the normal vector and the light
//     *                       vector.
//     * @param v              The view vector.
//     * @param nShininess     The shininess coefficient.
//     * @param lightIntensity The intensity of the light source.
//     * @return The color contribution from the specular reflection.
//     */
//    private Color calcSpecular(Double3 ks, Vector l, Vector n, double nl, Vector v, int nShininess,
//                               Color lightIntensity) {
//        Vector r = l.add(n.scale(-2 * nl)); // nl must not be zero!
//        double minusVR = -alignZero(r.dotProduct(v));
//        if (minusVR <= 0) {
//            return new primitives.Color(Color.BLACK.getColor()); // View from direction opposite to r vector
//        }
//        return lightIntensity.scale(ks.scale(Math.pow(minusVR, nShininess)));
//    }
//
//
//    @Override
//    public Color traceRay(Ray ray) {
//
//        List<GeoPoint> intersections = this.scene.geometries.findGeoIntersections(ray);
//
//        if (intersections == null) {
//            return this.scene.background;
//        }
//
//        GeoPoint closestPoint = ray.findClosestGeoPoint(intersections);
//
//        return calcColor(closestPoint,ray);
//    }
//}




package renderer;

import geometries.Intersectable;
import lighting.LightSource;
import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * SimpleRayTracer class represents a simple ray tracer
 */
public class SimpleRayTracer extends RayTracerBase {

    private static final double DELTA = 0.1;

    private static final int MAX_CALC_COLOR_LEVEL = 10;

    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INITIAL_K = Double3.ONE;

    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        var point = this.findClosestIntersection(ray);
        if (point == null) {
            return scene.background;
        }
        return calcColor(point, ray);
    }

    /**
     * Calculates the color of a point in the scene.
     *
     * @param geoPoint The point on the geometry in the scene.
     * @param ray The ray from the camera to the intersection.
     * @return The color of the point.
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return scene.ambientLight.getIntensity().add(calcColor(geoPoint, ray, MAX_CALC_COLOR_LEVEL, INITIAL_K));
    }

    /**
     * get point in scene and calculate its color
     *
     * @param gp
     * @param ray
     * @param level
     * @param k
     * @return
     */
    private Color calcColor(GeoPoint gp, Ray ray, int level, Double3 k) {
        Vector v = ray.getDirection();
        Vector n = gp.geometry.getNormal(gp.point);
        double vn = v.dotProduct(n);
        if (isZero(vn))
            return Color.BLACK;

        Color color = calcLocalEffects(gp, ray,k).add(gp.geometry.getEmission());

        return 1 == level ? color : color.add(calcGlobalEffects(gp, v, level, k));
    }


    /**
     * Calculate global effect reflected and refracted
     *
     * @param gp
     * @param v
     * @param level
     * @return
     */
    private Color calcGlobalEffects(GeoPoint gp, Vector v, int level, Double3 k) {
        Color color = Color.BLACK;
        Material material = gp.geometry.getMaterial();
        Double3 kr = material.kR;
        Double3 kkr = k.product(kr);
        Vector n = gp.geometry.getNormal(gp.point);
        double vn = v.dotProduct(n);
        Ray reflectedRay = constructReflectedRay(gp.point, v, n, vn);
        Ray refractedRay = constructRefractedRay(gp.point, v, n);
        if (!kkr.lowerThan(MIN_CALC_COLOR_K)) {
            color = color.add(calcGlobalEffect(reflectedRay, level - 1, kr, kkr));
        }
        Double3 kt = material.kT;
        Double3 kkt = k.product(kt);
        if (!kkt.lowerThan(MIN_CALC_COLOR_K)) {
            color = color.add(calcGlobalEffect(refractedRay, level - 1, kt, kkt));
        }
        return color;
    }

    private Color calcGlobalEffect(Ray ray, int level, Double3 k, Double3 kx) {
        Double3 kkx = k.product(kx);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        GeoPoint gp = findClosestIntersection(ray);
        return (gp == null ? scene.background.scale(kx) :
                isZero( gp.geometry.getNormal(gp.point).dotProduct(ray.getDirection()) ) ? Color.BLACK
                        : calcColor(gp, ray, level - 1, kkx).scale(k));
    }


    /**
     * get light and gp and move ao all the objects between them and calculate the
     * transparency
     *
     * @param gp
     * @param light
     * @param l
     * @param n
     * @return
     */
    private Double3 transparency(GeoPoint geopoint, LightSource light, Vector l, Vector n) {
        Vector lightDirection = l.scale(-1); // from point to light source
        Ray lightRay = new Ray(geopoint.point, lightDirection, n); //build ray with delta
        double lightDistance = light.getDistance(geopoint.point);

        var intersections = this.scene.geometries.findGeoIntersections(lightRay);
        if (intersections == null){
            return Double3.ONE; //no intersections
        }
        Double3 ktr = Double3.ONE;
        for (GeoPoint gp : intersections) {
            if (alignZero(gp.point.distance(geopoint.point) - lightDistance) <= 0) {
                ktr = ktr.product(gp.geometry.getMaterial().kT); //the more transparency the less shadow
                if (ktr.lowerThan(MIN_CALC_COLOR_K)) return Double3.ZERO;
            }
        }
        return ktr;
    }

    /**
     * Calculate refracted ray
     *
     * @param pointGeo
     * @param v
     * @return
     */
    private Ray  constructRefractedRay(Point pointGeo, Vector v, Vector n) {
        return new Ray(pointGeo, v, n);
    }

    /**
     * Calculate Reflected ray
     *
     * @param pointGeo
     * @param v
     * @param n
     * @param vn
     * @return
     */
    private Ray constructReflectedRay(Point pointGeo, Vector v, Vector n, double vn) {

        // 𝒓=𝒗 −𝟐∙(𝒗∙𝒏)∙𝒏
        Vector r = v.subtract(n.scale(2 * vn));
        return new Ray(pointGeo, r, n);
    }

    /**
     * Calculates the effect of different light sources on a point in the scene
     * according to the Phong model.
     *
     * @param intersection The point on the geometry in the scene.
     * @param ray The ray from the camera to the intersection.
     * @return The color of the point affected by local light sources.
     */
    private Color calcLocalEffects(GeoPoint intersection, Ray ray, Double3 kx) {
        int nShininess = intersection.geometry.getMaterial().nShininess;
        Double3 kd = intersection.geometry.getMaterial().kD;
        Double3 ks = intersection.geometry.getMaterial().kS;
        Color color = Color.BLACK;
        Vector n = intersection.geometry.getNormal(intersection.point);
        double nv = alignZero(n.dotProduct(ray.getDirection()));

        for (LightSource lightSource : scene.lights) {
            Vector l = lightSource.getL(intersection.point);
            double nl = alignZero(n.dotProduct(l));
            if (nl * nv > 0) {
                Double3 ktr = transparency(intersection, lightSource, l, n);
                if (!ktr.product(kx).lowerThan(MIN_CALC_COLOR_K)) {
                    Color lightIntensity = lightSource.getIntensity(intersection.point).scale(ktr);
                    color = color.add(calcDiffuse(kd, nl, lightIntensity),
                            calcSpecular(ks, l, n, nl, ray.getDirection(), nShininess, lightIntensity));
                }


            }
        }
        return color;

    }

    /**
     * Calculates the diffuse component of light reflection.
     *
     * @param kd The diffuse reflection coefficient.
     * @param nl The dot product between the normal vector and the light
     * vector.
     * @param lightIntensity The intensity of the light source.
     * @return The color contribution from the diffuse reflection.
     */
    private Color calcDiffuse(Double3 kd, double nl, Color lightIntensity) {
        return lightIntensity.scale(kd.scale(nl<0?-nl:nl));
    }

    /**
     * Calculates the specular component of light reflection.
     *
     * @param ks The specular reflection coefficient.
     * @param l The light vector.
     * @param n The normal vector.
     * @param nl The dot product between the normal vector and the light
     * vector.
     * @param v The view vector.
     * @param nShininess The shininess coefficient.
     * @param lightIntensity The intensity of the light source.
     * @return The color contribution from the specular reflection.
     */
    private Color calcSpecular(Double3 ks, Vector l, Vector n, double nl, Vector v, int nShininess,
                               Color lightIntensity) {
        Vector r = l.add(n.scale(-2 * nl)); // nl must not be zero!
        double minusVR = -alignZero(r.dotProduct(v));
        if (minusVR <= 0) {
            return Color.BLACK; // View from direction opposite to r vector
        }
        return lightIntensity.scale(ks.scale(Math.pow(minusVR, nShininess)));
    }

    private boolean unshaded(GeoPoint gp, Vector l, Vector n, LightSource lightSource, double nl) {
        Vector lightDirection = l.scale(-1);//from the point to light score
        Ray lightRay = new Ray(gp.point, lightDirection, n);
        List<GeoPoint> Intersection = scene.geometries.findGeoIntersections(lightRay);
        if (Intersection == null) return true;

        for (GeoPoint g : Intersection)
            if (g.geometry.getMaterial().kT == Double3.ZERO)

                return false;
        return true;
    }

    /**
     * get ray and return the closet intersection geoPoint
     *
     * @param ray
     * @return
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        return ray.findClosestGeoPoint(scene.geometries.findGeoIntersections(ray));
    }


}
