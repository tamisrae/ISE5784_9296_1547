package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void testSubtract() {
        Vector v = new Vector(1,5,7);
        assertEquals(new Vector(-1,2,3), v.subtract(new Point(2,3,4)));
    }

    @Test
    void testAdd() {
        Point p = new Point(1,5,7);
        assertEquals(new Point(3,8,11), p.add(new Vector(2,3,4)));
    }

    @Test
    void testDistance() {
        Point p = new Point(1,5,7);
        assertEquals(3.7416, p.distance(new Point(2,3,4)), 0.001);
    }

    @Test
    void testDistanceSquared() {
        Point p = new Point(1,5,7);
        assertEquals(14, p.distanceSquared(new Point(2,3,4)));
    }
}