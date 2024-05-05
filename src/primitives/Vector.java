package primitives;

public class Vector extends Point {

    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    Vector(Double3 xyz) {
        super(xyz);
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    public Vector add(Vector vector) {
        return null;
    }

    public Vector scale(double num) {
        return null;
    }

    public Vector dotProduct(Vector vector) {
        return null;
    }

    public Vector crossProduct(Vector vector) {
        return null;
    }

    public double lengthSquared(){
        return 0;
    }

    public double length() {
        return 0;
    }

    public Vector normalize(){
        return null;
    }
}
