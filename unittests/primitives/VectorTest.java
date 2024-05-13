package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorTest {

    @Test
    void testAdd() {
        Vector v1 = new Vector(10, 13, 15);
        Vector v2 = new Vector(5, 9, 3);
        assertEquals(new Vector(15, 22, 18), v1.add(v2));

        Vector v3 = new Vector(3, 4, 5);
        Vector v4 = new Vector(-3, -4, -5);
        assertThrows(IllegalArgumentException.class, () -> v3.add(v4));
    }

    @Test
    void testScale() {
        Vector v1 = new Vector(10, 13, 15);
        assertEquals(new Vector(20, 26, 30), v1.scale(2));
    }

    @Test
    void testDotProduct() {
        Vector v1 = new Vector(5, 12, 4);
        Vector v2 = new Vector(5, 2, 3);
        assertEquals(61, v1.dotProduct(v2));
    }

    @Test
    void testCrossProduct() {
        Vector v1 = new Vector(5, 12, 4);
        Vector v2 = new Vector(5, 2, 3);
        assertEquals(new Vector(28,5,-50), v1.crossProduct(v2));
    }

    @Test
    void testLengthSquared() {
        Vector v1 = new Vector(5, 12, 4);
        assertEquals(185, v1.lengthSquared());
    }

    @Test
    void testLength() {
        Vector v1 = new Vector(5, 12, 4);
        assertEquals(13.6014, v1.length(), 0.0001);
    }

    @Test
    void testNormalize() {
        Vector v = new Vector(2, -4, 1);
        assertEquals(new Vector(0.4364, -0.8728, 0.2182), v.normalize());
    }
}