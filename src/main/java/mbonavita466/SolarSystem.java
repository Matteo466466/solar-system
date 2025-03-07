package mbonavita466;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.math.ColorRGBA;

public class SolarSystem {
    public final List<String> objectNames = Arrays.asList("Stars", "Sun", "Mercury", "Venus", "Earth",
                                                        "Moon", "Mars", "Phobos", "Deimos", "Jupiter",
                                                        "Io", "Europa", "Saturn", "Uranus", "Neptune",
                                                        "Pluto");

    public final List<String> focusList = Arrays.asList("Sun", "Mercury", "Venus", "Earth", "Moon",
                                                        "Mars", "Phobos", "Deimos", "Jupiter", "Io",
                                                        "Europa", "Saturn", "Uranus", "Neptune", "Pluto",
                                                        "Kuiper");                                            

    // Diamètre de l'objet
    private final Dictionary<String, Float> size = new Hashtable<>();

    // Position orbitale de l'objet au départ de la simulation
    private final Dictionary<String, Float> position = new Hashtable<>();

    // Rotation de l'objet au départ de la simulation
    private final Dictionary<String, Vector3f> rotation = new Hashtable<>();

    // Distance de révolution
    private final Dictionary<String, Float> distance = new Hashtable<>();

    // Durée de révolution
    private final Dictionary<String, Float> revolutionPeriod = new Hashtable<>();

    // Durée de rotation
    private final Dictionary<String, Float> rotationPeriod = new Hashtable<>();

    // Poids de l'objet
    private final Dictionary<String, String> weight = new Hashtable<>();

    // Couleur de l'orbite
    private final Dictionary<String, ColorRGBA> color = new Hashtable<>();

    public final int kuiperQuantity = 512;
    public final float kuiperScale = 50f;

    public final float saturnRingWidth = 0.7f;
    public final float saturnRingDistance = 4f;

    public SolarSystem() {
        initObjectData("Sun", 139f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 0f, 1f, 30f, "1.939 * 10^30", ColorRGBA.Yellow);
        initObjectData("Mercury", 0.488f, FastMath.PI / 2, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 579f, 88f, 59f, "3.301 * 10^23", ColorRGBA.Pink);
        initObjectData("Venus", 1.21f, FastMath.PI * 7 / 8, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 1082f, 225f, 243f, "4.867 * 10^24", ColorRGBA.Brown);
        initObjectData("Earth", 1.27f, FastMath.PI * 7 / 8, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 1496f, 365f, 1f, "5.972 * 10^24", ColorRGBA.Cyan);
        initObjectData("Moon", 0.34f, FastMath.PI / 2, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 16f, 27f, 27f, "7.342 * 10^22", ColorRGBA.White);
        initObjectData("Mars", 0.678f, FastMath.PI * 3 / 4, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 2270f, 687f, 1f, "6.417 * 10^23", ColorRGBA.Red);
        initObjectData("Phobos", 0.022f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 8f, 0.3f, 0.3f, "1.065 * 10^16",ColorRGBA.White);
        initObjectData("Deimos", 0.012f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 9f, 1.3f, 1.3f, "1.476 * 10^15", ColorRGBA.White);
        initObjectData("Jupiter", 13.9f, FastMath.PI * 2, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 7783f, 4328f, 0.4f, "1.898 * 10^27", ColorRGBA.Orange);
        initObjectData("Io", 0.36f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 56f, 1.8f, 1.8f, "8.931 * 10^22", ColorRGBA.White);
        initObjectData("Europa", 0.31f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 81f, 3.6f, 3.6f, "4.799 * 10^22", ColorRGBA.White);
        initObjectData("Saturn", 11.6f, FastMath.PI * 15 / 8, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 14290f, 10752f, 0.4f, "5.683 * 10^26", ColorRGBA.Yellow);
        initObjectData("Uranus", 5f, FastMath.PI * 2 / 3, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 28710f, 30000f, 0.6f, "8.681 * 10^25", ColorRGBA.Green);
        initObjectData("Neptune", 4.9f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 44950f, 60000f, 0.6f, "1.024 * 10^26", ColorRGBA.Blue);
        initObjectData("Pluto", 0.237f, FastMath.PI * 7 / 4, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 59060f, 90000f, 6.4f, "1.939 * 10^30", ColorRGBA.Brown);
        initObjectData("Kuiper", 1.5f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 61000f, 90000f, 1f, "10^25 (total)", ColorRGBA.White);
        initObjectData("Stars", 200000f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 0f, 1f, 1f, "", ColorRGBA.White);
    }

    private void initObjectData(String objectName, Float s, Float p, Vector3f r, Float d, Float rev, Float rot, String w, ColorRGBA c) {
        size.put(objectName, s);
        position.put(objectName, p);
        rotation.put(objectName, r);
        distance.put(objectName, d);
        revolutionPeriod.put(objectName, rev);
        rotationPeriod.put(objectName, rot);
        weight.put(objectName, w);
        color.put(objectName, c);
    }

    public Float getSize(String objectName) {
        return size.get(objectName);
    }

    public Float getPosition(String objectName) {
        return position.get(objectName);
    }

    public Vector3f getRotation(String objectName) {
        return rotation.get(objectName);
    }

    public Float getDistance(String objectName) {
        return distance.get(objectName);
    }

    public Float getRevolutionPeriod(String objectName) {
        return revolutionPeriod.get(objectName);
    }

    public Float getRotationPeriod(String objectName) {
        return rotationPeriod.get(objectName);
    }

    public String getWeight(String objectName) {
        return weight.get(objectName);
    }

    public ColorRGBA getColor(String objectName) {
        return color.get(objectName);
    }
}