package primitives;

public class Vector extends Point {

    public Vector(double x, double y, double z) {
        super(x, y, z);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector(0,0,0) is not allowed");
    }

    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector(0,0,0) is not allowed");
    }


    public Vector add(Vector vector) {
        return new Vector(xyz.add(vector.xyz));
    }

    public Vector scale(double scalar) {
        return new Vector(xyz.scale(scalar));
    }

    public double dotProduct(Vector vector) {
        return vector.xyz.d1 * xyz.d1 +
                vector.xyz.d2 * xyz.d2 +
                vector.xyz.d3 * xyz.d3;
    }

    public Vector crossProduct(Vector vector) {
        double ax = xyz.d1;
        double ay = xyz.d2;
        double az = xyz.d3;

        double bx = vector.xyz.d1;
        double by = vector.xyz.d2;
        double bz = vector.xyz.d3;

        double cx = ay * bz - az * by;
        double cy = az * bx - ax * bz;
        double cz = ax * by - ay * bx;

        return new Vector(cx, cy, cz);
    }

    public double lengthSquared(){
        return xyz.d1 * xyz.d1 +
                xyz.d2 * xyz.d2 +
                xyz.d3 * xyz.d3;
    }

    public double length() {
        return Math.sqrt(lengthSquared());
    }

    public Vector normalize(){
        double len = length();
        if (len == 0)
            throw new ArithmeticException("Divide by zero!");
        if (len == 1)
            return this;
        return new Vector(xyz.reduce((len)));
    }

    @Override
    public String toString() {
        return "Vector{" + this.xyz + "}";
    }
}
