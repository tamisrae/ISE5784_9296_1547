package primitives;

import org.junit.jupiter.api.Test;

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
}