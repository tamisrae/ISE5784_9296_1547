package geometries;
import primitives.Point;
import primitives.Ray;

import java.util.List;

import static primitives.Util.*;

public interface Intersectable {
    List<Point> findIntersections(Ray ray);
}
