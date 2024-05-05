package primitives;

public class Ray {
    final private Point head;
    final private Vector direction;

    public Ray (Point point, Vector vector){
        head = point;
        direction = vector.normalize();
    }

}
