package renderer;

import primitives.*;
import scene.Scene;
import geometries.Intersectable.GeoPoint;

import java.util.List;

import static primitives.Util.alignZero;

public class SimpleRayTracer extends RayTracerBase{
    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    /**
     * A basic implementation of the {@code RayTracerBase}
     * class that computes the color of the closest intersection point with the scene.
     */
    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null)
            return scene.background;
        else
            return calcColor(ray.findClosestGeoPoint(intersections), ray);
    }


    /**
     * function calculates color of point
     *
     * @param geoPoint point to color
     * @return color
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return geoPoint.geometry.getEmission().add(scene.ambientLight.getIntensity());
    }

//    /**
//     * function calculates local effects of color on point
//     * @param gp geometry point to color
//     * @param ray ray that intersects
//     * @return color
//     */
//    private Color calcLocalEffects(GeoPoint gp, Ray ray) {
//        Color color = Color.BLACK;
//        Vector vector = ray.getDirection();
//        Vector normal = gp.geometry.getNormal(gp.point);
//        double nv = alignZero(normal.dotProduct(vector));
//        if (nv == 0)
//            return color;
//        Material material = gp.geometry.getMaterial();
//        for (LightSource lightSource : scene.lights) {
//            Vector lightVector = lightSource.getL(gp.point);
//            double nl = alignZero(normal.dotProduct(lightVector));
//            if (nl * nv > 0) {
//                Color lightIntensity = lightSource.getIntensity(gp.point);
//                color = color.add(lightIntensity.scale(calcDiffusive(material, nl)), lightIntensity.scale(calcSpecular(material, normal, lightVector, nl, vector)));
//            }
//        }
//        return color;
//    }

}
