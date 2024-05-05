package primitives;

import test.Main;

import java.util.Objects;

/**
 * A class for representing a point in space
 */
public class Point {
    public static final Point ZERO = new Point(Double3.ZERO);
    protected final Double3 xyz;

    /**
     * ctor
     * @param x first coordinate
     * @param y second coordinate
     * @param z third coordinate
     */
    public Point(double x, double y, double z) {
        xyz = new Double3(x,y,z);
    }

    /**
     * ctor
     * @param xyz point
     */
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

    /**
     * Vector subtraction
     * @param point
     * @return the subtraction between the 2 vectors
     */
    public Vector subtract(Point point) {
       return new Vector(xyz.subtract(point.xyz));
    }

    /**
     * Vector subtraction
     * @param vector
     * @return the connection between the 2 vectors
     */
    public Point add(Vector vector) {
        return new Point(xyz.add(vector.xyz));
    }

    /**
     * Calculation of distance between 2 points
     * @param point
     * @return the calculation of distance between 2 points
     */
    public double distance(Point point) {
        double dis = distanceSquared(point);
        return Math.sqrt(dis);
    }

    /**
     * Calculation of distance between 2 points in a square
     * @param point
     * @return the alculation of distance between 2 points in a square
     */
    public double distanceSquared(Point point) {
        double x = this.xyz.d1 - point.xyz.d1;
        double y = this.xyz.d2 - point.xyz.d2;
        double z = this.xyz.d3 - point.xyz.d3;
        return x * x + y * y + z * z;
    }
}
