package primitives;

/**
 * Represents a ray in space
 */
public class Ray {
    final private Point head;
    final private Vector direction;

    /**
     * ctor
     * @param point Start point
     * @param vector The diraction
     */
    public Ray (Point point, Vector vector){
        head = point;
        direction = vector.normalize();
    }

}
