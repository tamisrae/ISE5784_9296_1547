package primitives;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RayTest {

    @Test
    public void testGetPoint() {
    Ray ray = new Ray(new Point(0, 0, 1), new Vector(1, 0, 0));

    //===== Equivalence Partitions Tests =====
    // TC01: When t is positive, and the wanted point is on the ray itself
    assertEquals(new Point(1, 0, 1), ray.getPoint(1), "Bad point on the ray itself");
    //TC02: When t is negative, and the wanted point is on the ray's continuation
    assertEquals(new Point(-1, 0, 1), ray.getPoint(-1),"Bad point on the ray's continuation");

    //======= Boundary Values Tests =====
    // TC11: When t is 0, and the wanted point is the ray's starting-point
    assertEquals(new Point(0, 0, 1), ray.getPoint(0),"Bad point on the ray's starting-point");
    }

    /**
     * Test method for {@link primitives.Ray#findClosestPoint(List)}.
     */
    @Test
    void testFindClosestPoint() {
        // ============ Equivalence Partition Test ==============
        // TC01: the closest point is in the middle of the list.
        Ray r = new Ray(new Point(1,1,1), new Vector(2,2,2));
        Point p1 = new Point(3,2,3);
        Point p2 = new Point(1, 2, 1);
        Point p3 = new Point(3, 4, 5);
        // ensure there are no exceptions
        assertDoesNotThrow(() -> r.findClosestPoint(List.of(p1,p2,p3)), "findClosestPoint() throws an unexpected exception");
        // generate the test result
        Point result = r.findClosestPoint(List.of(p1,p2,p3));
        assertEquals(p2, result, "The correct point is in the middle of the list");
        // =============== Boundary Values Tests ==================
        // TC11: empty list.
        assertNull(r.findClosestPoint(List.of()), "There are no points listed");
        // TC12: The closest point is at the beginning of the list.
        // generate the test result
        result = r.findClosestPoint(List.of(p2,p1,p3));
        assertEquals(p2, result, "The correct point is in the beginning of the list");
        // TC13: The closest point is at the end of the list.
        // generate the test result
        result = r.findClosestPoint(List.of(p3,p1,p2));
        assertEquals(p2, result, "The correct point is in the end of the list");
    }
}