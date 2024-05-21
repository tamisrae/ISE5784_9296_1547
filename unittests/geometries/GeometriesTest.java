package geometries;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.LinkedList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Michal and Tamar
 */
class GeometriesTest {
    /**
     * {@link geometries.Geometries#findIntersections(Ray)}
     */
    @Test
    void testFindIntersections() {
        // ============ Equivalence Partitions Tests ==============
        Sphere sph = new Sphere(1, new Point(1, 0, 0));
        Sphere sph1 = new Sphere(1, new Point(-5, 0, 0));
        Triangle tri = new Triangle(new Point(3, 0, 1), new Point(3, -2, -1), new Point(3, 2, -1));
        // TC01: Some of the shapes are intersected but not all of them
        Geometries geo1 = new Geometries(sph, tri, sph1);
        List<Point> result = geo1.findIntersections(new Ray(new Point(-3, 0, 0), new Vector(1, 0, 0)));
        assertEquals(3, result.size(), "dose not work when some of the shapes are intersected but not all of them");

        // =============== Boundary Values Tests ==================
        // TC02: Empty collection
        Geometries geo2 = new Geometries();
        result = geo2.findIntersections(new Ray(new Point(-3, 0, 0), new Vector(1, 0, 0)));
        assertNull(result, "dose not work when the collection is empty");
        // TC03: None of the shapes is intersected
        result = geo1.findIntersections(new Ray(new Point(-8, 0, 0), new Vector(0, 0, 1)));
        assertNull(result, "dose not work when none of the shapes is intersected");
        // TC04: Just one shape is intersected
        result = geo1.findIntersections(new Ray(new Point(1, 0, -3), new Vector(0, 0, 1)));
        assertEquals(2, result.size(), "dose not work when Just one shape is intersected");
        // TC05: All the shapes are intersected
        result = geo1.findIntersections(new Ray(new Point(-8, 0, 0), new Vector(1, 0, 0)));
        assertEquals(5, result.size(), "dose not work when all the shapes are intersected");

    }

}