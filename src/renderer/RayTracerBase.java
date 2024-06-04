package renderer;

import primitives.*;
import scene.Scene;


/**
 * An abstract class that serves as a father to the RayTracerBasic class
 * whose implements the operation of coloring the rays sent from the view plane.
 *
 * @author Michal Shlomo and Tamar Israeli
 */
public abstract class RayTracerBase {
    /**
     * The scene for rendering.
     */
    protected final Scene scene;

    /**
     * Constructor that takes a scene as a parameter.
     *
     * @param scene The scene for rendering.
     */
    public RayTracerBase(Scene scene) {
        this.scene = scene;
    }

    /**
     * Traces a ray and returns the color.
     *
     * @param ray The ray to trace.
     * @return The color of the traced ray.
     */
    public abstract Color traceRay(Ray ray);
}