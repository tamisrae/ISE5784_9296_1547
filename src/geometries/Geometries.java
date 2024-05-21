package geometries;

import primitives.Point;
import primitives.Ray;
import java.util.LinkedList;
import java.util.List;

public class Geometries {
    final private List<Intersectable> geometries = new LinkedList<>();

    public Geometries() {
    }

    public Geometries(Intersectable... geometries) {
        add(geometries);
    }

    public void add(Intersectable... geometries) {
        this.geometries.addAll(List.of(geometries));
    }

    public List<Point> findIntersections(Ray ray){
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
