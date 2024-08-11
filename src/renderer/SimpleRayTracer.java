
package renderer;

import java.util.List;
import static primitives.Util.*;
import geometries.Intersectable.GeoPoint;
import lighting.LightSource;
import lighting.SpotLight;
import primitives.Color;
import primitives.Double3;
import primitives.Material;
import primitives.Point;
import primitives.Ray;
import primitives.Util;
import primitives.Vector;
import java.util.LinkedList;

import scene.Scene;

public class SimpleRayTracer extends RayTracerBase
{
    private static final double DELTA = 0.1;
    private static final int MAX_CALC_COLOR_LEVEL = 10;
    private static final double MIN_CALC_COLOR_K = 0.001;
    private static final Double3 INIT_CALC_COLOR_K = Double3.ONE;


    @Override
    /**
     * @param rays List of surrounding rays
     * @return average color
     */
    public Color traceRay(List<Ray> rays)
    {
        if(rays == null)
            return scene.background;
        Color color = scene.background;
        for (Ray ray : rays)
        {
            color = color.add(traceRay(ray));
        }
        color = color.add(scene.ambientLight.getIntensity());
        int size = rays.size();
        return color.reduce(size);

    }

    public SimpleRayTracer(Scene scene) {
        super(scene);
    }

    @Override
    public Color traceRay(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        return intersections == null ? scene.background : calcColor(ray.findClosestGeoPoint(intersections), ray);
    }
    /**

     /**
     * Calculates reflected ray and refraction ray
     *
     * @param geoPoint geometry point
     * @param ray      ray
     * @return color
     */
    private Color calcGlobalEffects(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = Color.BLACK;
        Material material = geoPoint.geometry.getMaterial();
        Ray reflectedRay = constructReflectionRay(geoPoint, geoPoint.geometry.getNormal(geoPoint.point), ray.getDirection());
        Ray refractedRay = constructRefractionRay(geoPoint, geoPoint.geometry.getNormal(geoPoint.point), ray.getDirection());
        return calcGlobalEffects(geoPoint, level, color, material.kR, k, reflectedRay)
                .add(calcGlobalEffects(geoPoint, level, color, material.kT, k, refractedRay));
    }

    /**
     * TODO
     * @param geoPoint
     * @param level
     * @param color
     * @param kx
     * @param k
     * @param ray
     * @return
     */
    private Color calcGlobalEffects(GeoPoint geoPoint, int level, Color color, Double3 kx, Double3 k, Ray ray) {
        Double3 kkx = kx.product(k);
        if (kkx.lowerThan(MIN_CALC_COLOR_K)) return Color.BLACK;
        GeoPoint reflectedPoint = findClosestIntersection(ray);
        if (reflectedPoint != null) {
            color = color.add(calcColor(reflectedPoint, ray, level - 1, kkx).scale(kx));
        }
        return color;
    }

    /**
     * function calculates specular color
     *
     * @param material    material of geometry
     * @param normal      normal of geometry
     * @param lightVector light vector
     * @param nl          dot product of normal and light vector
     * @param vector      direction of ray
     * @return specular color
     */
    private Double3 calcSpecular(Material material, Vector normal, Vector lightVector, double nl, Vector vector) {
        Vector reflectedVector = lightVector.subtract(normal.scale(2 * nl));
        double cosTeta = alignZero(-vector.dotProduct(reflectedVector));
        return cosTeta <= 0 ? Double3.ZERO : material.kS.scale(Math.pow(cosTeta, material.nShininess));

    }

    /**
     * function calculates diffusive color
     *
     * @param material material of geometry
     * @param nl dot product of normal and light vector
     * @return diffusive color
     */
    private Double3 calcDiffusive(Material material, double nl) {
        return material.kD.scale(Math.abs(nl));
    }

    /**
     * function calculates color of point
     *
     * @param geoPoint point to color
     * @return color
     */
    private Color calcColor(GeoPoint geoPoint, Ray ray, int level, Double3 k) {
        Color color = calcLocalEffects(geoPoint, ray, k);
        return 1 == level ? color : color.add(calcGlobalEffects(geoPoint, ray, level, k));
    }




    private Color calcLocalEffects(GeoPoint geoPoint, Ray ray,Double3 k)
    {
        Color color = geoPoint.geometry.getEmission();
        Vector vector = ray.getDirection();
        Vector normal = geoPoint.geometry.getNormal(geoPoint.point);
        double nv = alignZero(normal.dotProduct(vector));
        if (nv == 0)
            return color;
        Material material = geoPoint.geometry.getMaterial();
        for (LightSource lightSource : scene.lights) {
            Vector lightVector = lightSource.getL(geoPoint.point);
            double nl = alignZero(normal.dotProduct(lightVector));
            if (nl * nv > 0)
            {
                //if (unshaded(geoPoint, lightVector, normal, lightSource)) {
                Double3 ktr = transparency(geoPoint,lightSource,lightVector,normal);
                if(!ktr.product(k).lowerThan(MIN_CALC_COLOR_K))
                {
                    Color iL=lightSource.getIntensity(geoPoint.point).scale(ktr);
                    color=color.add(iL.scale(calcDiffusive(material, nl)),iL.scale(calcSpecular(material, normal, lightVector, nl, vector)));
                }

            }
        }
        return color;
    }



    private boolean unshaded(GeoPoint geoPoint, Vector l, Vector n, LightSource lightSource) {
        Ray lightRay = new Ray(geoPoint.point, l.scale(-1), n);
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(lightRay);

        if (intersections != null) {
            double distance = lightSource.getDistance(geoPoint.point);
            for (GeoPoint intersection : intersections) {
                if (intersection.point.distance(geoPoint.point) < distance)
                    return false;
            }
        }
        return true;
    }
    /**
     * function will construct a reflection ray
     *
     * @param geoPoint geometry point to check
     * @param normal   normal vector
     * @param vector   direction of ray to point
     * @return reflection ray
     */
    private Ray constructReflectionRay(GeoPoint geoPoint, Vector normal, Vector vector) {
        Vector reflectedVector = vector.subtract(normal.scale(2 * vector.dotProduct(normal)));
        return new Ray(geoPoint.point, reflectedVector, normal);
    }

    /**
     * function will construct a refraction ray
     *
     * @param geoPoint geometry point to check
     * @param normal   normal vector
     * @param vector   direction of ray to point
     * @return refraction ray
     */
    private Ray constructRefractionRay(GeoPoint geoPoint, Vector normal, Vector vector) {
        return new Ray(geoPoint.point, vector, normal);
    }

    /**
     * Find the closest intersection point with a ray.
     *
     * @param ray The ray to checks intersections with.
     * @return The closest intersection point with the ray.
     */
    private GeoPoint findClosestIntersection(Ray ray) {
        List<GeoPoint> intersections = scene.geometries.findGeoIntersections(ray);
        if (intersections == null) return null;
        return ray.findClosestGeoPoint(intersections);
    }

    private Color calcColor(GeoPoint geoPoint, Ray ray) {
        return scene.ambientLight.getIntensity().add(
                calcColor(geoPoint, ray, MAX_CALC_COLOR_LEVEL, INIT_CALC_COLOR_K));
    }
    /**
     * Gets the discount-factor of the half or full shading on the intersection-geoPoint regarding one of the light-sources.
     * @param light the light-source
     * @param l the light-vector of the light-source
     * @param n the intersected-geometry's normal
     * @return The discount-factor of the shading on this intersection-geoPoint
     */
//    private Double3 transparency(GeoPoint gp, LightSource light,Vector l, Vector n)
//    {
//        Vector lightDiraction=l.scale(-1);
//        Ray lightRay=new Ray(gp.point, lightDiraction,n);
//        List<GeoPoint> intersections=scene.geometries.findGeoIntersections(lightRay);
//        if(intersections==null)
//        {
//            return Double3.ONE;
//        }
//        double distance=light.getDistance(gp.point);
//        Double3 ktr = Double3.ONE;//Cumulative transparency factor
//        for(GeoPoint geoPoint : intersections)//we are going through all the intersections
//        {
//            if(alignZero(geoPoint.point.distance(gp.point))<=distance)
//            {
//                ktr=ktr.product(geoPoint.geometry.getMaterial().kT);//scale ktr with the pecents of trancerency of the intersection
//                if (ktr.lowerThan(MIN_CALC_COLOR_K))
//                    return Double3.ZERO;
//            }//in the end ktr is the percents of all the light through all the intersections
//        }
//        return ktr;
//    }
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
    @Override
    public Color AdaptiveSuperSamplingRec(Point centerP, double Width, double Height, double minWidth, double minHeight, Point cameraLoc, Vector Vright, Vector Vup, List<Point> prePoints) {
        if (Width < minWidth * 2 || Height < minHeight * 2) {
            return this.traceRay(new Ray(cameraLoc, centerP.subtract(cameraLoc))) ;
        }

        List<Point> nextCenterPList = new LinkedList<>();//center
        List<Point> cornersList = new LinkedList<>();
        List<primitives.Color> colorList = new LinkedList<>();
        Point tempCorner;
        Ray tempRay;
        for (int i = -1; i <= 1; i += 2){//This nested loop iterates over the four corners of the current pixel. For each corner:
            for (int j = -1; j <= 1; j += 2) {
                tempCorner = centerP.add(Vright.scale(i * Width / 2)).add(Vup.scale(j * Height / 2));
                cornersList.add(tempCorner);//The corner point is calculated and added to cornersList.
                if (prePoints == null || !isInList(prePoints, tempCorner)) {
                    tempRay = new Ray(cameraLoc, tempCorner.subtract(cameraLoc));//a ray is traced from the camera to the corner point,
                    nextCenterPList.add(centerP.add(Vright.scale(i * Width / 4)).add(Vup.scale(j * Height / 4)));//The center point for the next recursion is added to nextCenterPList
                    colorList.add(traceRay(tempRay));//// and the resulting color is added to colorList
                }
            }
        }


        if (nextCenterPList == null || nextCenterPList.size() == 0) {//If there are no next center points to process the method returns black.
            return primitives.Color.BLACK;
        }

        //This checks if all the colors in colorList are almost equal. If they are, the method returns the first color.
        //This is an optimization to avoid unnecessary recursion when the colors are already similar.
        boolean isAllEquals = true;
        primitives.Color tempColor = colorList.get(0);
        for (primitives.Color color : colorList) {
            if (!tempColor.isAlmostEquals(color))
                isAllEquals = false;
        }
        if (isAllEquals && colorList.size() > 1)
            return tempColor;

        //If the colors are not all equal,
        tempColor = primitives.Color.BLACK;//the method initializes tempColor to black
        for (Point center : nextCenterPList) {//and recursively calls AdaptiveSuperSamplingRec for each next center point, adding the resulting colors.
            tempColor = tempColor.add(AdaptiveSuperSamplingRec(center, Width/2,  Height/2,  minWidth,  minHeight ,  cameraLoc, Vright, Vup, cornersList));
        }
        return tempColor.reduce(nextCenterPList.size());// //The accumulated color is then averaged by calling reduce() with the size of nextCenterPList.


    }
    /**
     * Find a point in the list
     * @param pointsList the list
     * @param point the point that we look for
     * @return
     */
    private boolean isInList(List<Point> pointsList, Point point) {
        for (Point tempPoint : pointsList) {
            if(point.equals(tempPoint))
                return true;
        }
        return false;
    }
    /**
     * get list of ray
     *
     * @param rays
     * @param level
     * @param kkx
     * @return average color of the intersection of the rays
     */
    Color calcAverageColor(List<Ray> rays, int level, Double3 kkx) {
        Color color = Color.BLACK;

        for (Ray ray : rays) {
            GeoPoint intersection = findClosestIntersection(ray);
            color = color.add(intersection == null ? scene.background : calcColor(intersection, ray, level - 1, kkx));
        }

        return color.reduce(rays.size());
    }
}