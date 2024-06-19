package geometries;

import primitives.Point;
import primitives.Ray;
import java.util.LinkedList;
import java.util.List;

/**
 *Class for a legend of geometries
 * @author Michal and Tamar
 */
public class Geometries extends Intersectable{
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
    protected List<GeoPoint> findGeoIntersectionsHelper(Ray ray) {
        List<GeoPoint> result = null;
        List<GeoPoint> toAdd = null;
        for (Intersectable geo : geometries) {
            toAdd = geo.findGeoIntersections(ray);
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
