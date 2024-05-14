package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static primitives.Util.isZero;

/**
 * Unit tests for primitives.Point class
 * @author Michal Shlomo and Tamar Israeli
 */
class PointTest {

    /**
     * Test method for {@link primitives.Point#subtract(primitives.Point)}.
     */
    @Test
    void testSubtract() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(6, 4, 8);

        //TC01: test that subtraction result is right
        assertEquals(new Vector(-5, -2, -5), p1.subtract(p2), "subtract() wrong result");

        // =============== Boundary Values Tests ==================
        // TC11: test zero vector from subtraction of the same point from itself
        assertThrows(IllegalArgumentException.class, () -> p1.subtract(p1),
                "subtract() for same point does not throw an exception");
    }


    /**
     * Test method for {@link primitives.Point#add(primitives.Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(3, 6, 8);
        Vector v1 = new Vector(6, 4, 8);

        // TC01: test that addition result is right
        assertEquals(new Point(9, 10, 16), p1.add(v1), "add() wrong result");
    }

    /**
     * Test method for {@link primitives.Point#distance(primitives.Point)}.
     */
    @Test
    void testDistance() {
        // ============ Equivalence Partitions Tests ==============
        Point p1 = new Point(3, 6, 8);
        Point p2 = new Point(15, 2, 9);

        //TC01: test that distance result is right
        assertEquals(161, java.lang.Math.pow(p1.distance(p2), 2), 0.00001, "distance() wrong result");

        // =============== Boundary Values Tests ==================
        // TC11: test distance zero from distance of the same point from itself
        assertTrue(isZero(p1.distance(p1)), "distance() when equals 0 wrong result");
    }

    /**
     * Test method for {@link primitives.Point#distanceSquared(primitives.Point)}.
     */
    @Test
    void testDistanceSquared() {
        Point p1 = new Point(1, 2, 3);
        Point p2 = new Point(2, -3, 4);
        Point p0 = new Point(0, 0, 0);
        // ============ Equivalence Partitions Tests ==============
        // TC0: that the function actually works
        assertEquals(27, p1.distanceSquared(p2), 0.00001, "ERROR: Point.distanceSquared() does not work properly");
        // =============== Boundary Values Tests ==================
        // TC1: squared distance from p0 (0,0,0)
        assertEquals(14, p1.distanceSquared(p0), 0.00001,
                "ERROR: Point.distanceSquared() does not work when distance is from p0");
        // TC2: squared distance from the same point = 0
        assertTrue(isZero(p1.distanceSquared(p1)), //
                "ERROR: Point.distanceSquared does not work from point to itself");
    }
}