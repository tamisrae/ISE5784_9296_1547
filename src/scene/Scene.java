package scene;
import geometries.Geometries;
import lighting.AmbientLight;
import primitives.Color;
import lighting.LightSource;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a scene in a computer graphics environment.
 * @author Michal Shlomo and Tamar Israeli
 */
public class Scene {

    /**
     * The name of the scene.
     */
    public final String name;

    /**
     * The background color of the scene.
     */
    public Color background = Color.BLACK;

    /**
     * The ambient light of the scene.
     */
    public AmbientLight ambientLight = AmbientLight.NONE;

    /**
     * The geometries in the scene.
     */
    public Geometries geometries = new Geometries();

    /**
     * the light sources in the scene
     */
    public List<LightSource> lights = new LinkedList<>();

    /**
     * Constructs a new Scene object with the given name.
     * @param name The name of the scene.
     */
    public Scene(String name) {
        this.name = name;
    }

    /**
     * Sets the background color of the scene.
     * @param background The background color.
     * @return The updated Scene object.
     */
    public Scene setBackground(Color background) {
        this.background = background;
        return this;
    }

    /**
     * Sets the ambient light of the scene.
     * @param ambientLight The ambient light.
     * @return The updated Scene object.
     */
    public Scene setAmbientLight(AmbientLight ambientLight) {
        this.ambientLight = ambientLight;
        return this;
    }

    /**
     * Sets the geometries of the scene.
     * @param geometries The geometries.
     * @return The updated Scene object.
     */
    public Scene setGeometries(Geometries geometries) {
        this.geometries = geometries;
        return this;
    }

    /**
     * setter for lights
     * @param lights list of the lights in the scene
     * @return this object
     */
    public Scene setLights(List<LightSource> lights) {
        this.lights = lights;
        return this;
    }
}