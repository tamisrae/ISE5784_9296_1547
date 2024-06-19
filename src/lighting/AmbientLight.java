package lighting;
import primitives.Color;
import primitives.Double3;
/**
 * Represents an ambient light source
 * @author Michal Shlomo and Tamar Israeli
 */
public class AmbientLight extends Light {

    /**
     * Represents a constant representing no ambient light.
     */
    public static final AmbientLight NONE = new AmbientLight(Color.BLACK, Double3.ZERO);


    /**
     * Constructs an AmbientLight object with the given intensity and attenuation factor (Double3).
     * @param iA The intensity of the ambient light.
     * @param kA        The attenuation factor of the ambient light.
     */
    public AmbientLight(Color iA, Double3 kA) {
        super(iA.scale(kA));
    }

    /**
     * Constructs an AmbientLight object with the given intensity and attenuation factor (double).
     * @param iA The intensity of the ambient light.
     * @param kA        The attenuation factor of the ambient light.
     */
    public AmbientLight(Color iA, double kA) {
        super(iA.scale(kA));
    }


}