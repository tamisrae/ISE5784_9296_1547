package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import scene.Scene;

import java.util.List;

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
        List<Point> intersections = scene.geometries.findIntersections(ray);
        if (intersections == null)
            return scene.background;
        else
            return calcColor(ray.findClosestPoint(intersections));
    }

    /**
     * Computes the color of the intersection point using the Phong reflection model.
     *
     * @param point the intersection point
     * @return the color of the intersection point
     */
    private Color calcColor(Point point) {
        return this.scene.ambientLight.getIntensity();
    }
}
