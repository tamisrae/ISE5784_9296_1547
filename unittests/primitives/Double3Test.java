package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Double3Test {

    @Test
    void testAdd() {
        Double3 d1 = new Double3(1, 5, 10);
        Double3 d2 = new Double3(7, 9, 3);
        assertEquals(new Double3(8, 14, 13), d1.add(d2));
    }

    @Test
    void testSubtract() {
        Double3 d1 = new Double3(1, 5, 10);
        Double3 d2 = new Double3(7, 9, 3);
        assertEquals(new Double3(-6, -4, 7), d1.subtract(d2));
    }

    @Test
    void testScale() {
        Double3 d1 = new Double3(1, 5, 10);
        assertEquals(new Double3(2, 10, 20), d1.scale(2));
    }

    @Test
    void testReduce() {
        Double3 d1 = new Double3(1, 5, 10);
        assertEquals(new Double3(0.5, 2.5, 5), d1.reduce(2));
    }

    @Test
    void testProduct() {
        Double3 d1 = new Double3(1, 5, 10);
        Double3 d2 = new Double3(7, 9, 3);
        assertEquals(new Double3(7, 45, 30), d1.product(d2));
    }

//    @Test
//    void testLowerThan() {
//        Double3 d1 = new Double3(1, 5, 10);
//    }
//
//    @Test
//    void testTestLowerThan() {
//    }
}