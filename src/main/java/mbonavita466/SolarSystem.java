package mbonavita466;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

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

    public final float kuiperScale = 50f;
    public final int kuiperQuantity = 512;

    public final float saturnRingWidth = 0.7f;
    public final float saturnRingDistance = 4f;

    public SolarSystem() {
        /*
        initObjectData("Sun", 695.7f, 0f, new Vector3f(0f, 0f, 0f), 0f, 0f, 25.05f);
        initObjectData("Mercury", 2.439f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 57909f, 87.969f, 25.05f);
        initObjectData("Venus", 6.051f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 108209f, 224.667f, -243.023f);
        initObjectData("Earth", 6.378f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 149597f, 365.256f, 0.997f);
        initObjectData("Moon", 1.737f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 384.399f, 27.321f, 27.321f);
        initObjectData("Mars", 3.396f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 227944f, 686.885f, 1.025f);
        initObjectData("Phobos", 0.027f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 9.377f, 0.319f, 0.319f);
        initObjectData("Deimos", 0.015f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 23.46f, 1.262f, 1.262f);
        initObjectData("Jupiter", 71.492f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 778340f, 4332.01f, 0.413f);
        initObjectData("Io", 3.643f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 421.8f, 1.769f, 1.769f);
        initObjectData("Europa", 3.121f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 671.1f, 3.551f, 3.551f);
        initObjectData("Saturn", 60.268f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 1426700f, 10754f, 0.448f);
        initObjectData("Uranus", 25.559f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 2870700f, 30698f, -0.718f);
        initObjectData("Neptune", 24.764f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 4498400f, 60216f, 0.671f);
        initObjectData("Pluto", 1.185f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 5900898f, 90487f, -6.387f);
        initObjectData("Stars", 200f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 0f, 1f, 1f);
        initObjectData("Kuiper", 0.15f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 6300000f, 90000f, 5f);
        */
        initObjectData("Sun", 139f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 0f, 1f, 30f, "1.939 * 10^30");
        initObjectData("Mercury", 0.49f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 579f, 88f, 59f, "3.301 * 10^23");
        initObjectData("Venus", 1.21f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 1082f, 225f, 243f, "4.867 * 10^24");
        initObjectData("Earth", 1.27f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 1496f, 365f, 1f, "5.972 * 10^24");
        initObjectData("Moon", 0.34f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 16f, 27f, 27f, "7.342 * 10^22");
        initObjectData("Mars", 0.68f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 2270f, 687f, 1f, "6.417 * 10^23");
        initObjectData("Phobos", 0.02f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 8f, 0.3f, 0.3f, "1.065 * 10^16");
        initObjectData("Deimos", 0.01f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 9f, 1.3f, 1.3f, "1.476 * 10^15");
        initObjectData("Jupiter", 1.39f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 7783f, 4328f, 0.4f, "1.898 * 10^27");
        initObjectData("Io", 0.04f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 5.61f, 1.8f, 1.8f, "8.931 * 10^22");
        initObjectData("Europa", 0.03f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 8f, 3.6f, 3.6f, "4.799 * 10^22");
        initObjectData("Saturn", 1.16f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 14290f, 10752f, 0.4f, "5.683 * 10^26");
        initObjectData("Uranus", 0.5f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 28710f, 30000f, 0.6f, "8.681 * 10^25");
        initObjectData("Neptune", 0.49f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 44950f, 60000f, 0.6f, "1.024 * 10^26");
        initObjectData("Pluto", 0.02f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 59060f, 90000f, 6.4f, "1.939 * 10^30");
        initObjectData("Kuiper", 1f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 61000f, 90000f, 1f, "10^25 (total estimé)");
        initObjectData("Stars", 200000f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 0f, 1f, 1f, "");
    }

    private void initObjectData(String objectName, Float s, Float p, Vector3f r, Float d, Float rev, Float rot, String w) {
        size.put(objectName, s);
        position.put(objectName, p);
        rotation.put(objectName, r);
        distance.put(objectName, d);
        revolutionPeriod.put(objectName, rev);
        rotationPeriod.put(objectName, rot);
        weight.put(objectName, w);
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
}