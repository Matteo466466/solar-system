package mbonavita466;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class SolarSystem3 {
    public final List<String> objectNames = Arrays.asList("Sun", "Mercury", "Venus", "Earth", "Moon", "Mars", "Phobos", "Deimos", "Jupiter", "Io", "Europe", "Saturn", "Uranus", "Neptune", "Pluto");

    // Diamètre de l'objet en milliers de kilomètres
    private final Dictionary<String, Float> size = new Hashtable<>();

    // Position orbitale de l'objet au départ de la simulation
    private final Dictionary<String, Float> position = new Hashtable<>();

    // Rotation de l'objet au départ de la simulation
    private final Dictionary<String, Vector3f> rotation = new Hashtable<>();

    // Distance de révolution en milliers de kilomètres
    private final Dictionary<String, Float> distance = new Hashtable<>();

    // Durée de révolution en jours
    private final Dictionary<String, Float> revolutionPeriod = new Hashtable<>();

    // Durée de rotation en jours
    private final Dictionary<String, Float> rotationPeriod = new Hashtable<>();

    public SolarSystem3() {
        /*
        initObjectData("Sun", 6.957f, 0f, new Vector3f(0f, 0f, 0f), 0f, 0f, 25.05f);
        initObjectData("Mercury", 2.439f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 57909f, 87.969f, 25.05f);
        initObjectData("Venus", 6.051f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 108209f, 224.667f, -243.023f);
        initObjectData("Earth", 6.378f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 149597f, 365.256f, 0.997f);
        initObjectData("Moon", 1.737f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 384.399f, 27.321f, 27.321f);
        initObjectData("Mars", 3.396f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 227944f, 686.885f, 1.025f);
        initObjectData("Phobos", 0.027f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 9.377f, 0.319f, 0.319f);
        initObjectData("Deimos", 0.015f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 23.46f, 1.262f, 1.262f);
        initObjectData("Jupiter", 71.492f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 778340f, 4332.01f, 0.413f);
        initObjectData("Io", 3.643f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 421.8f, 1.769f, 1.769f);
        initObjectData("Europe", 3.121f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 671.1f, 3.551f, 3.551f);
        initObjectData("Saturn", 60.268f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 1426700f, 10754f, 0.448f);
        initObjectData("Uranus", 25.559f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 2870700f, 30698f, -0.718f);
        initObjectData("Neptune", 24.764f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 4498400f, 60216f, 0.671f);
        initObjectData("Pluto", 1.185f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 5900898440f, 90487f, -6.387f);
        */

        initObjectData("Sun", 2f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 0f, 1f, 25.05f);
        initObjectData("Mercury", 0.3f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 5f, 0.8f, 25.05f);
        initObjectData("Venus", 0.3f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 8f, 2f, -243.023f);
        initObjectData("Earth", 0.3f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 10f, 3f, 0.997f);
        initObjectData("Moon", 0.1f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 1f, 0.3f, 27.321f);
        initObjectData("Mars", 0.2f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 14f, 7f, 1.025f);
        initObjectData("Phobos", 0.1f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 2f, 0.1f, 0.319f);
        initObjectData("Deimos", 0.05f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 1f, 0.08f, 1.262f);
        initObjectData("Jupiter", 0.9f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 22f, 43f, 0.413f);
        initObjectData("Io", 0.1f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 4f, 0.05f, 1.769f);
        initObjectData("Europe", 0.05f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 6f, 0.1f, 3.551f);
        initObjectData("Saturn", 0.6f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 30f, 10f, 0.448f);
        initObjectData("Uranus", 0.7f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 45f, 30f, -0.718f);
        initObjectData("Neptune", 0.7f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 50f, 60f, 0.671f);
        initObjectData("Pluto", 0.1f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 80f, 90f, -6.387f);
    }

    private void initObjectData(String objectName, Float s, Float p, Vector3f r, Float d, Float rev, Float rot) {
        size.put(objectName, s);
        position.put(objectName, p);
        rotation.put(objectName, r);
        distance.put(objectName, d);
        rotationPeriod.put(objectName, rot);
        revolutionPeriod.put(objectName, rev);
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
}