package mbonavita;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.math.ColorRGBA;

/**
 * Stocke toutes les données des astres.
 * Pour la plupart des données, utilise des dictionnaires avec comme clés d'indexage les noms des astres.
 */
public class SolarSystem {
    // Noms des astres
    public static final String SUN = "Sun";
    public static final String MERCURY = "Mercury";
    public static final String VENUS = "Venus";
    public static final String EARTH = "Earth";
    public static final String MOON = "Moon";
    public static final String MARS = "Mars";
    public static final String DEIMOS = "Deimos";
    public static final String PHOBOS = "Phobos";
    public static final String JUPITER = "Jupiter";
    public static final String IO = "Io";
    public static final String EUROPA = "Europa";
    public static final String SATURN = "Saturn";
    public static final String URANUS = "Uranus";
    public static final String NEPTUNE = "Neptune";
    public static final String PLUTO = "Pluto";
    public static final String KUIPER = "Kuiper";
    public static final String STARS = "Stars"; // Fond de la scène 3D

    // Liste des astres qui seront simulés
    public final List<String> objectNames = Arrays.asList(STARS, SUN, MERCURY, VENUS, EARTH, MOON, MARS, DEIMOS,
            PHOBOS, JUPITER, IO, EUROPA, SATURN, URANUS, NEPTUNE, PLUTO, KUIPER);

    // Liste des astres qui peuvent être suivis par la caméra
    public final List<String> focusList = Arrays.asList(SUN, MERCURY, VENUS, EARTH, MOON, MARS, DEIMOS, PHOBOS,
            JUPITER, IO, EUROPA, SATURN, URANUS, NEPTUNE, PLUTO, KUIPER);

    // Liste des planètes qui ne sont pas des satellites
    public final List<String> mainPlanets = Arrays.asList(MERCURY, VENUS, EARTH, MARS,
            JUPITER, SATURN, URANUS, NEPTUNE, PLUTO);

    // Diamètre
    private final Dictionary<String, Float> size = new Hashtable<>();
    
    // Angle sur l'orbite au départ de la simulation
    private final Dictionary<String, Float> position = new Hashtable<>();

    // Rotation de l'astre sur lui-même au départ de la simulation
    private final Dictionary<String, Vector3f> rotation = new Hashtable<>();

    // Distance de révolution
    private final Dictionary<String, Float> distance = new Hashtable<>();

    // Durée de révolution
    private final Dictionary<String, Float> revolutionPeriod = new Hashtable<>();

    // Durée de rotation
    private final Dictionary<String, Float> rotationPeriod = new Hashtable<>();

    // Masse de l'astre à afficher dans l'interface
    private final Dictionary<String, String> weight = new Hashtable<>();

    // Couleur de l'orbite affichée
    private final Dictionary<String, ColorRGBA> color = new Hashtable<>();

    // Ceinture de Kuiper
    public final int kuiperQuantity = 512; // Nombre total d'astéroides
    public final float kuiperScale = 50f; // Distance à laquelle les astéroides sont éloignés les uns des autres

    // Anneaux de Saturne
    public final float saturnRingWidth = 7f;
    public final float saturnRingDistance = 40f;

    /**
     * Initalise les données de tous les astres.
     */
    public SolarSystem() {
        initObjectData(SUN, 139f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 0f, 1f, 30f, "1.939 * 10^30", ColorRGBA.Yellow);
        initObjectData(MERCURY, 0.488f, FastMath.PI / 2, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 579f, 88f, 59f, "3.301 * 10^23", ColorRGBA.Pink);
        initObjectData(VENUS, 1.21f, FastMath.PI * 7 / 8, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 1082f, 225f, 243f, "4.867 * 10^24", ColorRGBA.Brown);
        initObjectData(EARTH, 1.27f, FastMath.PI * 7 / 8, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 1496f, 365f, 1f, "5.972 * 10^24", ColorRGBA.Cyan);
        initObjectData(MOON, 0.34f, FastMath.PI / 2, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 16f, 27f, 27f, "7.342 * 10^22", ColorRGBA.White);
        initObjectData(MARS, 0.678f, FastMath.PI * 3 / 4, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 2270f, 687f, 1f, "6.417 * 10^23", ColorRGBA.Red);
        initObjectData(PHOBOS, 0.022f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 8f, 0.3f, 0.3f, "1.065 * 10^16",ColorRGBA.White);
        initObjectData(DEIMOS, 0.012f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 9f, 1.3f, 1.3f, "1.476 * 10^15", ColorRGBA.White);
        initObjectData(JUPITER, 13.9f, FastMath.PI * 2, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 7783f, 4328f, 0.4f, "1.898 * 10^27", ColorRGBA.Orange);
        initObjectData(IO, 0.36f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 56f, 1.8f, 1.8f, "8.931 * 10^22", ColorRGBA.White);
        initObjectData(EUROPA, 0.31f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 81f, 3.6f, 3.6f, "4.799 * 10^22", ColorRGBA.White);
        initObjectData(SATURN, 11.6f, FastMath.PI * 15 / 8, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 14290f, 10752f, 0.4f, "5.683 * 10^26", ColorRGBA.Yellow);
        initObjectData(URANUS, 5f, FastMath.PI * 2 / 3, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 28710f, 30000f, 0.6f, "8.681 * 10^25", ColorRGBA.Green);
        initObjectData(NEPTUNE, 4.9f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 44950f, 60000f, 0.6f, "1.024 * 10^26", ColorRGBA.Blue);
        initObjectData(PLUTO, 0.237f, FastMath.PI * 7 / 4, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 59060f, 90000f, 6.4f, "1.939 * 10^30", ColorRGBA.Brown);
        initObjectData(KUIPER, 1.5f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 61000f, 90000f, 1f, "10^25 (total)", ColorRGBA.White);
        initObjectData(STARS, 200000f, 0f, new Vector3f(-FastMath.HALF_PI, 0f, 0f), 0f, 1f, 1f, "", ColorRGBA.White);
    }

    /**
     * Initalise les données d'un astre.
     */
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

    // Getters

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
