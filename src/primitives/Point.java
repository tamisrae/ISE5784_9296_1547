package primitives;

import test.Main;

import java.util.Objects;

public class Point {
    public static final Point ZERO = new Point(Double3.ZERO);
    protected final Double3 xyz;

    public Point(double x, double y, double z) {
        xyz = new Double3(x,y,z);
    }

    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        return ((obj instanceof Point point) && this.xyz.equals(point.xyz));
    }

    @Override
    public String toString() {
        return "Point{" + xyz.toString() + '}';
    }

    public Vector subtract(Point point) {
       return new Vector(xyz.subtract(point.xyz));
    }

    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    public double distance(Point point) {
        double dis = distanceSquared(point);
        return Math.sqrt(dis);
    }

    public double distanceSquared(Point point) {
        double x = this.xyz.d1 - point.xyz.d1;
        double y = this.xyz.d2 - point.xyz.d2;
        double z = this.xyz.d3 - point.xyz.d3;
        return x * x + y * y + z * z;
    }
}
