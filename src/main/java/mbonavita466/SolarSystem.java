package mbonavita466;

import java.util.Dictionary;
import java.util.Hashtable;

import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

public class SolarSystem {
    // Diamètre de l'objet (par rapport à celui de la terre)
    protected Dictionary<String, Float> size = new Hashtable<>();

    // Position de l'objet au départ de la simulation
    protected Dictionary<String, Float> position = new Hashtable<>();

    // Rotation de l'objet au départ de la simulation
    protected Dictionary<String, Float> rotation = new Hashtable<>();

    // Distance de révolution en Unités Astronomiques
    protected Dictionary<String, Float> distance = new Hashtable<>();

    // Durée de révolution en années terrestres
    protected Dictionary<String, Float> yearDuration = new Hashtable<>();

    protected SolarSystem() {
        // Sun
        size.put("Sun", 218f);
        distance.put("Sun", 0f);
        yearDuration.put("Sun", 0f);

        // Mercury
        size.put("Mercury", 0.383f);
        distance.put("Mercury", 0.3870993f);
        yearDuration.put("Mercury", 0.2408467f);

        // Venus
        size.put("Venus", 0.95f);
        distance.put("Venus", 0.723336f);
        yearDuration.put("Venus", 0.61519726f);

        // Earth
        size.put("Earth", 1f);
        distance.put("Earth", 1.000003f);
        yearDuration.put("Earth", 1.0000174f);

        // Moon
        size.put("Moon", 0.472f);
        distance.put("Moon", 0.002569f);
        yearDuration.put("Moon", 0.07479f);

        // Mars
        size.put("Mars", 0.532f);
        distance.put("Mars", 1.52371f);
        yearDuration.put("Mars", 1.8808158f);
        
        // Phobos

        // Deimos

        // Jupiter
        size.put("Jupiter", 11.209f);
        distance.put("Jupiter", 5.2029f);
        yearDuration.put("Jupiter", 11.862615f);

        // Io

        // Europe

        // Saturn
        size.put("Saturn", 9.449f);
        distance.put("Saturn", 9.537f);
        yearDuration.put("Saturn", 29.447498f);

        // Saturn Rings

        // Uranus
        size.put("Uranus", 4.007f);
        distance.put("Uranus", 19.189f);
        yearDuration.put("Uranus", 84.016846f);

        // Neptune
        size.put("Neptune", 3.883f);
        distance.put("Neptune", 30.0699f);
        yearDuration.put("Neptune", 164.79132f);

        // Pluto
        size.put("Pluto", 0.646f);
        distance.put("Pluto", 39.4821f);
        yearDuration.put("Pluto", 248.0208f);

        // Kuiper Belt

    }
}
