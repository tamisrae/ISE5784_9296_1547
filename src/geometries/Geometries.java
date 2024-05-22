package geometries;

import primitives.Point;
import primitives.Ray;
import java.util.LinkedList;
import java.util.List;

/**
 *Class for a legend of geometries
 * @author Michal and Tamar
 */
public class Geometries implements Intersectable{
    final private List<Intersectable> geometries = new LinkedList<>();

    /**
     * ctor
     */
    public Geometries() {
    }

    /**
     * ctor
     * @param geometries an unknown number of geometries
     */
    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    /**
     * Add geometries to list
     * @param geometries an unknown number of geometries
     */
    public void add(Intersectable... geometries) {
        this.geometries.addAll(List.of(geometries));
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        List<Point> result = null;
        List<Point> toAdd = null;
        for (Intersectable geo : geometries) {
            toAdd = geo.findIntersections(ray);
            if (toAdd != null) {
                if(result==null) {
                    result = new LinkedList<>();
                }
                result.addAll(toAdd);
            }
        }
        return result;
    }
}
