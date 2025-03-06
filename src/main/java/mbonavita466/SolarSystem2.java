package mbonavita466;

import java.util.Dictionary;
import java.util.Hashtable;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;

public class SolarSystem2 {
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

    protected SolarSystem2() {
        initObjectData("Sun", 2f, 0f, new Vector3f(0f, 0f, 0f), 0f, 0f, 25.05f);
        initObjectData("Mercury", 0.3f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 57909f, 87.969f, 25.05f);
        initObjectData("Venus", 6.051f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 108209f, 224.667f, -243.023f);
        initObjectData("Earth", 0.3f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 8f, 365.256f, 0.997f);
        initObjectData("Moon", 0.1f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 1f, 27.321f, 27.321f);
        initObjectData("Mars", 0.2f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 12f, 686.885f, 1.025f);
        initObjectData("Phobos", 0.027f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 9.377f, 0.319f, 0.319f);
        initObjectData("Deimos", 0.015f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 23.46f, 1.262f, 1.262f);
        initObjectData("Jupiter", 0.8f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 20f, 4332.01f, 0.413f);
        initObjectData("Io", 3.643f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 421.8f, 1.769f, 1.769f);
        initObjectData("Europe", 3.121f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 671.1f, 3.551f, 3.551f);
        initObjectData("Saturn", 60.268f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 1426700f, 10754f, 0.448f);
        initObjectData("Uranus", 25.559f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 2870700f, 30698f, -0.718f);
        initObjectData("Neptune", 24.764f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 4498400f, 60216f, 0.671f);
        initObjectData("Pluto", 1.185f, 0f, new Vector3f(FastMath.PI, FastMath.PI, 0f), 5900898440f, 90487f, -6.387f);
    }

    private void initObjectData(String objectName, Float s, Float p, Vector3f r, Float d, Float rot, Float rev) {
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

    /*
    protected SolarSystem2() {
        // Sun
        size.put("Sun", 695.7f);
        position.put("Sun", 0f);
        rotation.put("Sun", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Sun", 0f);
        revolutionPeriod.put("Sun", 0f);
        rotationPeriod.put("Sun", 25.05f);

        // Mercury
        size.put("Mercury", 2.439f);
        position.put("Mercury", 0f);
        rotation.put("Mercury", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Mercury", 57909f);
        revolutionPeriod.put("Mercury", 87.969f);
        rotationPeriod.put("Mercury", 58.645f);

        // Venus
        size.put("Venus", 6.051f);
        position.put("Venus", 0f);
        rotation.put("Venus", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Venus", 108209f);
        revolutionPeriod.put("Venus", 224.667f);
        rotationPeriod.put("Venus", -243.023f);

        // Earth
        size.put("Earth", 6.378f);
        position.put("Earth", 0f);
        rotation.put("Earth", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Earth", 149597f);
        revolutionPeriod.put("Earth", 365.256f);
        rotationPeriod.put("Earth", 0.997f);

        // Moon
        size.put("Moon", 1.737f);
        position.put("Moon", 0f);
        rotation.put("Moon", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Moon", 384.399f);
        revolutionPeriod.put("Moon", 27.321f);
        rotationPeriod.put("Moon", 27.321f);

        // Mars
        size.put("Mars", 3.396f);
        position.put("Mars", 0f);
        rotation.put("Mars", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Mars", 227944f);
        revolutionPeriod.put("Mars", 686.885f);
        rotationPeriod.put("Mars", 1.025f);
        
        // Phobos
        size.put("Phobos", 0.027f);
        position.put("Phobos", 0f);
        rotation.put("Phobos", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Phobos", 9.377f);
        revolutionPeriod.put("Phobos", 0.319f);
        rotationPeriod.put("Phobos", 0.319f);

        // Deimos
        size.put("Deimos", 0.015f);
        position.put("Deimos", 0f);
        rotation.put("Deimos", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Deimos", 23.46f);
        revolutionPeriod.put("Deimos", 1.262f);
        rotationPeriod.put("Deimos", 1.262f);

        // Jupiter
        size.put("Jupiter", 71.492f);
        position.put("Jupiter", 0f);
        rotation.put("Jupiter", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Jupiter", 778340f);
        revolutionPeriod.put("Jupiter", 4332.01f);
        rotationPeriod.put("Jupiter", 0.413f);

        // Io
        size.put("Deimos", 3.643f);
        position.put("Deimos", 0f);
        rotation.put("Deimos", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Deimos", 421.8f);
        revolutionPeriod.put("Deimos", 1.769f);
        rotationPeriod.put("Phobos", 1.769f);

        // Europe
        size.put("Io", 3.121f);
        position.put("Io", 0f);
        rotation.put("Io", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Io", 671.1f);
        revolutionPeriod.put("Io", 3.551f);
        rotationPeriod.put("Io", 3.551f);

        // Saturn
        size.put("Saturn", 60.268f);
        position.put("Saturn", 0f);
        rotation.put("Saturn", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Saturn", 1426700f);
        revolutionPeriod.put("Saturn", 10754f);
        rotationPeriod.put("Saturn", 0.448f);

        // Saturn Rings

        // Uranus
        size.put("Uranus", 25.559f);
        position.put("Uranus", 0f);
        rotation.put("Uranus", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Uranus", 2870700f);
        revolutionPeriod.put("Uranus", 30698f);
        rotationPeriod.put("Uranus", -0.718f);

        // Neptune
        size.put("Neptune", 24.764f);
        position.put("Neptune", 0f);
        rotation.put("Neptune", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Neptune", 4498400f);
        revolutionPeriod.put("Neptune", 60216f);
        rotationPeriod.put("Neptune", 0.671f);

        // Pluto
        size.put("Pluto", 1.185f);
        position.put("Pluto", 0f);
        rotation.put("Pluto", new Vector3f(FastMath.PI, FastMath.PI, 0f));
        distance.put("Pluto", 5900898440f);
        revolutionPeriod.put("Pluto", 90487f);
        rotationPeriod.put("Pluto", -6.387f);

        // Kuiper Belt

    }
    */
}