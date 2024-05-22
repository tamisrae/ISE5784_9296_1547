package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;

import primitives.Ray;
import primitives.Vector;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for geometries.Triangle class
 *
 * @author Michal Shlomo and Tamar Israeli
 */
class TriangleTest {
    /**
     * Test method for {@link geometries.Triangle#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test getting a normal vector from a point on triangle
        Point[] pts = {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0)};
        Triangle tri = new Triangle(pts[0], pts[1], pts[2]);

        // ensure there are no exceptions
        assertDoesNotThrow(() -> tri.getNormal(new Point(0, 1, 0)),
                "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result = tri.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Triangle's normal is not a unit vector");
        // ensure the result is orthogonal to all the edges
        for (int i = 0; i < 3; ++i)
            assertTrue(isZero(result.dotProduct(pts[i].subtract(pts[i == 0 ? 2 : i - 1]))),
                    "Triangle's normal is not orthogonal to one of the vectors of the plane");
    }

    /**
     * Test method for {@link geometries.Sphere#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        Triangle triangle = new Triangle(new Point(-1, 0, 1), new Point(1, 0, 1), new Point(0, 2, 1));
        // ============ Equivalence Partitions Tests ==============
        // TC01: The point of intersection inside the triangle (1 point)
        Point p = new Point(0, 1, 1);
        List<Point> result = triangle.findIntersections(new Ray(new Point(0, 2, 0),
                new Vector(0, -1, 1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p), result, "Ray crosses triangle");

        // TC02: The point of intersection is outside the triangle opposite a side (0 points)
        result = triangle.findIntersections(new Ray(new Point(0, 2, 0),
                new Vector(2, -1, 1)));
        assertNull(result, "Ray's line out of triangle");

        // TC03: The point of intersection is outside the triangle opposite a vertex (0 points)
        result = triangle.findIntersections(new Ray(new Point(0, 2, 1),
                new Vector(0, 1, 1)));
        assertNull(result, "Ray's line out of triangle");

        // =============== Boundary Values Tests ==================
        // TC11: The intersection point is on a side (0 points)
        result = triangle.findIntersections(new Ray(new Point(0, 2, 0),
                new Vector(-0.5, -1, 1)));
        assertNull(result, "Wrong number of points");

        // TC12: The intersection point is on a vertex (0 points)
        result = triangle.findIntersections(new Ray(new Point(0, 2, 0),
                new Vector(2, -2, 1)));
        assertNull(result, "Wrong number of points");

        // TC13: The intersection point is on the continuation of an edge (0 points)
        result = triangle.findIntersections(new Ray(new Point(0, 2, 0),
                new Vector(-1, -2, 1)));
        assertNull(result, "Wrong number of points");
    }

}