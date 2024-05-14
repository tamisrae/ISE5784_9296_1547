package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;



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


}