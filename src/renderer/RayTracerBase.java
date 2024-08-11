package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import scene.Scene;

import java.util.List;

public  abstract class RayTracerBase {
    protected Scene scene;
    /**
     * Constructor that get a scene
     * @param scene
     */
    public RayTracerBase(Scene scene){
        this.scene = scene;
    }

    /**
     * trace the ray and calculate the rey's intersection point color
     * and any other object (or the background if the rey's intersection point
     * doesn't exist)
     *
     * @param ray
     * @return
     */
    abstract Color traceRay(Ray ray);


    /**
     * trace the rays and calculate the reys's intersection point color
     * and any other object (or the background if the rey's intersection point
     * @param rays
     * @return
     */
    abstract Color traceRay(List<Ray> rays);
    /**
     * Checks the color of the pixel with the help of individual rays and averages between
     * them and only if necessary continues to send beams of rays in recursion
     * @param centerP center pixl
     * @param Width Length
     * @param Height width
     * @param minWidth min Width
     * @param minHeight min Height
     * @param cameraLoc Camera location
     * @param Vright Vector right
     * @param Vup vector up
     * @param prePoints pre Points
     * @return Pixel color
     */
    public abstract Color AdaptiveSuperSamplingRec(Point centerP, double Width, double Height, double minWidth, double minHeight, Point cameraLoc, Vector Vright, Vector Vup, List<Point> prePoints);

}
