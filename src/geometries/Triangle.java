package geometries;

import primitives.Point;

/**
 *A class for representing a triangle
 */
public class Triangle extends Polygon {

    /**
     *ctor
     * @param d1 first point of the triangle
     * @param d2 second point of the triangle
     * @param d3 third point of the triangle
     */
    public Triangle(Point d1, Point d2, Point d3){
        super(d1,d2,d3);
    }
}
