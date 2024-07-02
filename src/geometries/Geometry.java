package geometries;

import primitives.Color;
import primitives.Material;
import primitives.Point;
import primitives.Vector;

/**
 * An interface for Geometric shapes
 */
public abstract class Geometry extends Intersectable {

    protected Color emission = Color.BLACK;

    private Material material = new Material();

    public Material getMaterial() {
        return material;
    }

    /**
     * setEmission function
     *
     * @param material
     * @return this instance of object
     */
    public Geometry setMaterial(Material material) {
        this.material = material;
        return this;
    }

    /**
     * getter for emission field
     * @return {@link  Color} of the shape
     */
    public Color getEmission() {
        return emission;
    }

    /**
     * setter for emission field (builder pattern style)
     * @param emission {@link Color} object to set shape's color to
     * @return this instance of object
     */
    public Geometry setEmission(Color emission) {
        this.emission = emission;
        return this;
    }

    /**
     * abstract function for get the normal
     * @param point on the shape
     * @return normal vector of the shape at the point
     */
    abstract public Vector getNormal(Point point);
}
