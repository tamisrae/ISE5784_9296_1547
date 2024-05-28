package primitives;

import static primitives.Util.isZero;

/**
 * Represents a ray in space
 */
public class Ray {
    final private Point head;
    final private Vector direction;

    /**
     * ctor
     * @param point Start point
     * @param vector The direction
     */
    public Ray (Point point, Vector vector){
        head = point;
        direction = vector.normalize();
    }

    /**
     * getter for starting point of the ray head
     * @return head
     */
    public Point getHead() {
        return head;
    }

    /**
     * getter for direction vector of the ray direction
     * @return direction
     */
    public Vector getDirection() {
        return this.direction;
    }

    /**
     * getter for the value of progress of length t on the starting point
     * @param t The parameter value.
     * @return The point on the line corresponding to the parameter value.
     */
    public Point getPoint(double t) {
        return isZero(t) ? head : head.add(direction.scale(t));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return (obj instanceof Ray other)
                && this.head.equals(other.head)
                && this.direction.equals(other.direction);
    }

    @Override
    public String toString() {
        return head+" "+direction;
    }

}

