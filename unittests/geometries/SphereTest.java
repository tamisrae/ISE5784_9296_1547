package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for geometries.Sphere class
 *
 * @author Michal Shlomo and Tamar Israeli
 */
class SphereTest {
    /**
     * Test method for {@link geometries.Sphere#getNormal(primitives.Point)}.
     */
    @Test
    void testGetNormal() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: test getting a normal vector from any point on sphere
        Sphere sph = new Sphere( 3,new Point(1, 4, 5));
        // ensure there are no exceptions
        assertDoesNotThrow(() -> sph.getNormal(new Point(4, 4, 5)),
                "getNormal() throws an unexpected exception");
        // generate the test result
        Vector result = sph.getNormal(new Point(4, 4, 5));
        // ensure |result| = 1
        assertEquals(1, result.length(), 0.00000001, "Sphere's normal is not a unit vector");
        // ensure the result is orthogonal to sphere at the given point
        assertEquals(new Vector(1, 0, 0), result, "getNormal() wrong result");
    }
    /**
     * Test method for {@link geometries.Sphere#findIntersections(Ray)}.
     */
    @Test
    public void testFindIntersections() {
        Sphere sphere = new Sphere(1,new Point(1, 0, 0));
        // ============ Equivalence Partitions Tests ==============
        // TC01: Ray's line is outside the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(1, 1, 0))),
                "Ray's line out of sphere");
        final Point p001 = new Point(0, 0, -1);
        // TC02: Ray starts before and crosses the sphere (2 points)
        Point p1 = new Point(0.4, 0, -0.8);
        Point p2 = new Point(2, 0, 0);
        List<Point> result = sphere.findIntersections(new Ray(p001, new Vector(2, 0, 1)))
                        .stream().sorted(Comparator.comparingDouble(p->p.distance(p001))).toList();
        assertEquals(2, result.size(), "Wrong number of points");
        if (result.get(0).getX() > result.get(1).getX())
            result = List.of(result.get(1), result.get(0));
        assertEquals(List.of(p1, p2), result, "Ray crosses sphere");

        // TC03: Ray starts inside the sphere (1 point)
        result = sphere.findIntersections(new Ray(new Point(1.5, 0, -0.4), new Vector(0.5, 0, 0.4)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p2), result, "Ray crosses sphere");

        // TC04: Ray starts after the sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(-1, 0, 0))),
                "Ray's line out of sphere");

        // =============== Boundary Values Tests ==================
        // ** Group: Ray's line crosses the sphere (but not the center)
        // TC11: Ray starts at sphere and goes inside (1 point)
        result = sphere.findIntersections(new Ray(new Point(1, 0, -1), new Vector(1, 0, 1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p2), result,"Ray crosses sphere");

        // TC12: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(0, 0, 0), new Vector(-1, 0, 0))),
                "Ray's line out of sphere");

        // ** Group: Ray's line goes through the center
        // TC13: Ray starts before the sphere (2 points)
        Point p3 = new Point(1, 0, 1);
        Point p4 = new Point(1, 0, -1);
        Point p01 = new Point(1, 0, 2);
        result = new LinkedList<>(sphere.findIntersections(new Ray(p01, new Vector(0, 0, -1))));
        assertEquals(2, result.size(), "Wrong number of points");
        result.sort(Comparator.comparingDouble(point -> point.distanceSquared(p01)));
        assertEquals(List.of(p3, p4), result, "Ray crosses sphere");

        // TC14: Ray starts at sphere and goes inside (1 point)
        result = sphere.findIntersections(new Ray(new Point(1, 0, 1), new Vector(0, 0, -1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p4), result, "Ray crosses sphere");

        // TC15: Ray starts inside (1 point)
        result = sphere.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 0, -1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p4), result, "Ray crosses sphere");

        // TC16: Ray starts at the center (1 point)
        result = sphere.findIntersections(new Ray(new Point(1, 0, 0), new Vector(0, 0, -1)));
        assertEquals(1, result.size(), "Wrong number of points");
        assertEquals(List.of(p4), result, "Ray crosses sphere");

        // TC17: Ray starts at sphere and goes outside (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(1, 0, 1), new Vector(0, 0, 1))),
                "Ray's line out of sphere");

        // TC18: Ray starts after sphere (0 points)
        assertNull(sphere.findIntersections(new Ray(new Point(1, 0, -2), new Vector(0, 0, -1))),
                "Ray's line out of sphere");

        // ** Group: Ray's line is tangent to the sphere (all tests 0 points)
        // TC19: Ray starts before the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0, -1, 0), new Vector(0, 1, 0))),
                "Ray's line out of sphere");

        // TC20: Ray starts at the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0, 0, 0), new Vector(0, 1, 0))),
                "Ray's line out of sphere");

        // TC21: Ray starts after the tangent point
        assertNull(sphere.findIntersections(new Ray(new Point(0, 1, 0), new Vector(0, 1, 0))),
                "Ray's line out of sphere");

        // ** Group: Special cases
        // TC19: Ray's line is outside, ray is orthogonal to ray start to sphere's center line
        assertNull(sphere.findIntersections(new Ray(new Point(-1, 0, 0), new Vector(0, 1, 0))),
                "Ray's line out of sphere");
    }
}