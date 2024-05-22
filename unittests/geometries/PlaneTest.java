package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Plane class
 *
 * @author Michal Shlomo and Tamar Israeli
 */
class PlaneTest {
    /**
     * Test method for {@link geometries.Plane#Plane(primitives.Point, primitives.Point, primitives.Point)}.
     */
    @Test
    public void testConstructor() {
        // =============== Boundary Values Tests ==================
        // TC0:checking if all points are on the same line
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(0, 0, 0), new Point(0, 0, 0), new Point(0, 0, 0)),
                "vector zero dude, next time do stuff properly");
        // TC1: checking if two points are equal
        assertThrows(IllegalArgumentException.class,
                () -> new Plane(new Point(0, 0, 1), new Point(0, 0, 1), new Point(0, 1, 3)),
                "vector zero dude, next time do stuff properly");
    }

    /**
     * Test method for {@link geometries.Plane#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test getting a normal vector from a point on plane
        Point[] pts = {new Point(0, 0, 1), new Point(1, 0, 0), new Point(0, 1, 0)};
        Plane pln = new Plane(pts[0], pts[1], pts[2]);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> pln.getNormal(new Point(0, 1, 0)),
                "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result = pln.getNormal(new Point(0, 0, 1));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Plane's normal is not a unit vector");
        // ensure the result is orthogonal to plane
        assertTrue(pln.getNormal().equals(result) || pln.getNormal().equals(result.scale(-1)),
                "getNormal() wrong result");
    }

    /**
     * Test method for {@link geometries.Plane#findIntersections(Ray)}.
     */
    @Test
    void testFindIntersections() {
        Plane plane = new Plane(new Point(0, 0, 1), new Point(1, 0, 1), new Point(0, 1, 1));

        // ============ Equivalence Partitions Tests ==============
        //The Ray must be neither orthogonal nor parallel to the plane
        //TC01: Ray intersects the plane (1 point)
        Point p1 = new Point(0, 1, 1);
        List<Point> result = plane.findIntersections(new Ray(new Point(2, 0, 0), new Vector(-2, 1, 1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1), result, "Ray crosses plane");

        //TC02: Ray does not intersect the plane (0 point)
        assertNull(plane.findIntersections(new Ray(new Point(1, 0, 0), new Vector(-1, 0, -1))),
                "Ray's line out of plane");


        // =============== Boundary Values Tests ==================
        // ** Group: Ray is parallel to the plane
        // TC03: Ray included in the plane (0 point)
        assertNull(plane.findIntersections(new Ray(new Point(0, 2, 1), new Vector(0, -1, 0))),
                "Ray's line out of plane");

        // TC04: Ray not included in the plane (0 point)
        assertNull(plane.findIntersections(new Ray(new Point(0, 1, 0.5), new Vector(0, -1, 0))),
                "Ray's line out of plane");

        // ** Group: Ray is orthogonal to the plane
        // TC05: Ray start before plane (1 point)
        p1 = new Point(0, 1, 1);
        result = plane.findIntersections(new Ray(new Point(0, 1, 0.5), new Vector(0, 0, 0.5)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p1), result, "Ray crosses plane");

        // TC06: Ray start in  plane (0 point)
        assertNull(plane.findIntersections(new Ray(new Point(0, 1, 1), new Vector(0, 0, 0.5))),
                "Ray's line out of plane");

        // TC06: Ray start after  plane (0 point)
        assertNull(plane.findIntersections(new Ray(new Point(0, 1, 2), new Vector(0, 0, 0.5))),
                "Ray's line out of plane");

        // ** Group: Ray is neither orthogonal nor parallel to and begins at the plane
        // TC07: not the same point which appears as reference point in the plane (0 point)
        assertNull(plane.findIntersections(new Ray(new Point(2, 0, 1), new Vector(-2, 0, 2))),
                "Ray's line out of plane");

        // TC08: the same point which appears as reference point in the plane (0 point)
        assertNull(plane.findIntersections(new Ray(new Point(1, 0, 1), new Vector(-2, 0, 2))),
                "Ray's line out of plane");
    }

}