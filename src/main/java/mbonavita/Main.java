package mbonavita;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Node;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import com.jme3.util.TangentBinormalGenerator;

import java.util.*;

/**
 * Crée une simulation 3D du système solaire.
 */
public class Main extends SimpleApplication {
    // Objet qui stocke les données de tous les astres
    private SolarSystem solarSystem;

    // Liste des Geometry des astres
    private Dictionary<String, Geometry> geos = new Hashtable<>();

    // Liste des nodes principaux (un pour chaque astre)
    private Dictionary<String, Node> nodes = new Hashtable<>();

    /*
     * Orbites
     * Chaque orbite est un ensemble de lignes formant un cercle, avec une couleur et stockées dans un node.
     */
    private List<Node> orbitNodes = new ArrayList<>(); // Un node pour chaque orbite
    private Node orbitNode; // Node parent des nodes des orbites
    private final int orbitResolution = 256; // Nombre de lignes utilisées pour chaque orbite

    /*
     * Ceinture de Kuiper
     * Chaque astéroide est modélisé par une sphère, puis placé à une position aléatoire près de l'orbite.
     */
    private Node kuiperNode;
    Random random = new Random();

    // Source de lumière
    private PointLight pointLight;

    // Caméra
    private ChaseCamera chaseCam;
    private int currentFocus = 0; // Indique quel objet est regardé par la caméra (par défaut le Soleil)

    // Pause la simulation
    private Boolean isRunning = true;

    // Echelle de distance de la simulation
    private Float distanceScale = 1f;
    
    /*
     * Vitesses possibles de la simulation.
     * La vitesse par défaut est à 1 seconde = 1 jour (1/360),
     * et peut aller de 1 seconde = 12 heures (1/720) à 1 seconde = 60 jours (1/6).
     */
    private List<Float> timeSpeeds = Arrays.asList(-1/5f, -1/10f, -1/60f, -1/360f, -1/720f, 1/720f, 1/360f, 1/60f, 1/30f, 1/6f);
    private int currentTimeSpeedIndex = 6;

    // UI
    BitmapFont font;
    BitmapText nameText;
    BitmapText weightText;
    BitmapText sizeText;

    // Noms des actions possibles de l'utilisateur
    String pauseString = "Pause";
    String incTimeString = "IncTime";
    String decTimeString = "DecTime";
    String incFocusString = "IncFocus";
    String decFocuString = "DecFocus";
    String orbitString = "DisableOrbits";

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    /**
     * Crée tous les objets et les nodes de la simulation, initialise la caméra, les raccourcis claviers et l'UI.
     */
    @Override
    public void simpleInitApp() {
        initData();
        initLight();
        initNodes();
        initObjects();
        initOrbits();
        attachNodes();
        initCamera();
        initKeys();
        initText();
    }

    /**
     * Initialise les données du système solaire.
     */
    private void initData() {
        solarSystem = new SolarSystem();
    }

    /**
     * Crée une source de lumière à l'endroit du soleil.
     */
    private void initLight() {
        pointLight = new PointLight();
        pointLight.setColor(ColorRGBA.White);
        pointLight.setPosition(new Vector3f(0f, 0f, 0f));
        rootNode.addLight(pointLight);
    }

    /**
     * Crée un node pour chaque astre et les stocke dans le dictionnaire nodes.
     */
    private void initNodes() {
        for (String name : solarSystem.objectNames) {
            nodes.put(name, new Node(name + "Node"));
        }
    }

    /**
     * Crée, affiche et transforme tous les objets du système solaire.
     * Les Geometry de chaque objet (sauf les anneaux de Saturne et la ceinture de Kuiper) sont stockés dans le dictionnaire geos.
     */
    private void initObjects() {
        // Planètes, Soleil et fond 3D
        for (String name : solarSystem.objectNames) {
            if(!name.equals(SolarSystem.KUIPER)) {
                geos.put(name, createObject(name, name + ".jpg"));
            }
        }

        // Anneaux de Saturne
        createSaturnRing();

        // Ceinture de Kuiper
        createKuiperBelt();
    }

    /**
     * Crée et affiche un astre à partir d'une sphère et d'une texture, puis transforme et retourne le Geometry.
     */
    private Geometry createObject(String objectName, String textureName) {
        Material material;
        String texturePath = "Textures/Objects";
        Sphere sphere = new Sphere(32,32, 2f, true, false);
        Geometry geo = new Geometry(objectName, sphere);

        sphere.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(sphere);

        // La texture du Soleil et du fond 3D doit être lumineuse
        if(objectName.equals(SolarSystem.SUN) || objectName.equals(SolarSystem.STARS)) {
            material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            material.setTexture("ColorMap", assetManager.loadTexture(texturePath + "/" + textureName));
        }
        // Les planètes n'émettent pas de lumière
        else {
            material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            material.setTexture("DiffuseMap", assetManager.loadTexture(texturePath + "/" + textureName));
            material.setColor("Diffuse", ColorRGBA.White);
        }

        material.setColor("GlowColor", ColorRGBA.Black);
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        material.getAdditionalRenderState().setFaceCullMode(com.jme3.material.RenderState.FaceCullMode.Off);
        
        geo.setMaterial(material);  
        transformObjet(geo, objectName);

        return geo;
    }

    /**
     * Donne une taille, une position et une rotation de départ à l'objet.
     */
    private void transformObjet(Geometry geo, String objectName) {
        float size = solarSystem.getSize(objectName) * distanceScale;
        float distance = solarSystem.getDistance(objectName) * distanceScale;
        Float position = solarSystem.getPosition(objectName);
        Vector3f rotation = solarSystem.getRotation(objectName);

        geo.scale(size, size, size);
        geo.setLocalTranslation(FastMath.sin(position) * distance, 0, FastMath.cos(position) * distance);
        geo.rotate(rotation.getX(), rotation.getY(), rotation.getZ());
    }

    /**
     * Crée et affiche les anneaux de Saturne en utilisant un tore aplati (avec 2 faces seulement),
     * puis transforme l'objet et l'attache au même node que celui de Saturne.
     */
    private void createSaturnRing() {
        String parentObjectName = SolarSystem.SATURN;
        String textureName = "Textures/Objects/Saturnring.jpg";

        Torus torus = new Torus(32, 2, solarSystem.saturnRingWidth, solarSystem.saturnRingDistance);
        Geometry torusGeo = new Geometry("Torus", torus);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture(textureName));
        torusGeo.setMaterial(material);

        torusGeo.setLocalTranslation(geos.get(parentObjectName).getLocalTranslation());
        torusGeo.rotate(-FastMath.HALF_PI, 0, FastMath.PI / 8);

        nodes.get(parentObjectName).attachChild(torusGeo);
    }

    /**
     * Crée et affiche les astéroïdes de la ceinture de Kuiper.
     * Tout autour de l'orbite, des sphères sont créées, chacune à une position aléatoire près de l'orbite.
     * 
     * J'ai décidé de ne placer les astéroïdes que sur une partie de l'orbite,
     * car pour en placer sur tout le tour il aurait fallu créer trop d'objets en mémoire.
     */
    private void createKuiperBelt() {
        String objectName = SolarSystem.KUIPER;
        kuiperNode = new Node("kuiperNode");

        int quantity = solarSystem.kuiperQuantity;
        float scale = solarSystem.kuiperScale;
        float distance = solarSystem.getDistance(objectName);
        float size = solarSystem.getSize(objectName);
        Vector3f rotation = solarSystem.getRotation(objectName);

        float x;
        float y;
        float z;

        for (int i = 0; i < quantity; i++) {
            // Position aléatoire de l'astéroïde
            x = (float)Math.cos(i * FastMath.PI / (quantity * 4)) * distance;
            y = 0;
            z = (float)Math.sin(i * FastMath.PI / (quantity * 4)) * distance;

            x += (random.nextFloat() * scale * 2) - scale;
            y += (random.nextFloat() * scale * 2) - scale;
            z += (random.nextFloat() * scale * 2) - scale;

            // Création de l'astéroïde
            Sphere sphere = new Sphere(32,32, 2f, true, false);
            Geometry geo = new Geometry(objectName, sphere);

            sphere.setTextureMode(Sphere.TextureMode.Projected);
            TangentBinormalGenerator.generate(sphere);

            Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            material.setTexture("ColorMap", assetManager.loadTexture("Textures/Objects/Kuiper.jpg"));
            material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
            material.getAdditionalRenderState().setFaceCullMode(com.jme3.material.RenderState.FaceCullMode.Off);
            geo.setMaterial(material);

            geo.scale(size, size, size);
            geo.setLocalTranslation(x * distanceScale, y * distanceScale, z * distanceScale);
            geo.rotate(rotation.getX(), rotation.getY(), rotation.getZ());

            // On attache l'astéroïde au node de la ceinture de Kuiper
            kuiperNode.attachChild(geo);

            // On stocke l'astéroïde du milieu dans le dictionnaire geos.
            if(i == (quantity / 2)) geos.put(SolarSystem.KUIPER, geo);
        }
        
        kuiperNode.setLocalTranslation(geos.get("Sun").getLocalTranslation());
        rootNode.attachChild(kuiperNode);
        nodes.put(SolarSystem.KUIPER, kuiperNode);
    }

    private void initOrbits() {
        orbitNode = new Node("orbitNode");

        createOrbit(SolarSystem.MERCURY, geos.get(SolarSystem.SUN), nodes.get(SolarSystem.SUN));
        createOrbit(SolarSystem.VENUS, geos.get(SolarSystem.SUN), nodes.get(SolarSystem.SUN));
        createOrbit(SolarSystem.EARTH, geos.get(SolarSystem.SUN), nodes.get(SolarSystem.SUN));
        createOrbit(SolarSystem.MOON, geos.get(SolarSystem.EARTH), nodes.get(SolarSystem.EARTH));
        createOrbit(SolarSystem.MARS, geos.get(SolarSystem.SUN), nodes.get(SolarSystem.SUN));
        createOrbit(SolarSystem.DEIMOS, geos.get(SolarSystem.MARS), nodes.get(SolarSystem.MARS));
        createOrbit(SolarSystem.PHOBOS, geos.get(SolarSystem.MARS), nodes.get(SolarSystem.MARS));
        createOrbit(SolarSystem.JUPITER, geos.get(SolarSystem.SUN), nodes.get(SolarSystem.SUN));
        createOrbit(SolarSystem.IO, geos.get(SolarSystem.JUPITER), nodes.get(SolarSystem.JUPITER));
        createOrbit(SolarSystem.EUROPA, geos.get(SolarSystem.JUPITER), nodes.get(SolarSystem.JUPITER));
        createOrbit(SolarSystem.SATURN, geos.get(SolarSystem.SUN), nodes.get(SolarSystem.SUN));
        createOrbit(SolarSystem.URANUS, geos.get(SolarSystem.SUN), nodes.get(SolarSystem.SUN));
        createOrbit(SolarSystem.NEPTUNE, geos.get(SolarSystem.SUN), nodes.get(SolarSystem.SUN));
        createOrbit(SolarSystem.PLUTO, geos.get(SolarSystem.SUN), nodes.get(SolarSystem.SUN));

        rootNode.attachChild(orbitNode);
    }

    private void createOrbit(String objectName, Geometry parentObject, Node parentNode)
    {
        Node node = new Node(objectName + "orbit");

        float distance = solarSystem.getDistance(objectName);
        float x1;
        float x2;
        float y1;
        float y2;

        for(int i = 0; i < orbitResolution; i++) {
            x1 = (float)Math.cos(2 * i * FastMath.PI / orbitResolution) * distance;
            y1 = (float)Math.sin(2 * i * FastMath.PI / orbitResolution) * distance;
            x2 = (float)Math.cos(2 * (i + 1) * FastMath.PI / orbitResolution) * distance;
            y2 = (float)Math.sin(2 * (i + 1) * FastMath.PI / orbitResolution) * distance;

            Geometry orbitGeo = new Geometry("Line", new Line(new Vector3f(x1, 0, y1), new Vector3f(x2, 0, y2)));
            Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            mat.setColor("Color", solarSystem.getColor(objectName));
            orbitGeo.setMaterial(mat);

            orbitGeo.setLocalTranslation(parentObject.getLocalTranslation());
            node.attachChild(orbitGeo);
        }

        parentNode.attachChild(node);
        orbitNodes.add(node);
    }

    private void attachNodes() {
        nodes.get("Pluto").attachChild(geos.get("Pluto"));
        nodes.get("Neptune").attachChild(geos.get("Neptune"));
        nodes.get("Uranus").attachChild(geos.get("Uranus"));
        nodes.get("Saturn").attachChild(geos.get("Saturn"));

        nodes.get("Io").attachChild(geos.get("Io"));
        nodes.get("Io").setLocalTranslation(geos.get("Jupiter").getLocalTranslation());
        nodes.get("Europa").attachChild(geos.get("Europa"));
        nodes.get("Europa").setLocalTranslation(geos.get("Jupiter").getLocalTranslation());
        nodes.get("Jupiter").attachChild(geos.get("Jupiter"));
        nodes.get("Jupiter").attachChild(nodes.get("Io"));
        nodes.get("Jupiter").attachChild(nodes.get("Europa"));

        nodes.get("Deimos").attachChild(geos.get("Deimos"));
        nodes.get("Deimos").setLocalTranslation(geos.get("Mars").getLocalTranslation());
        nodes.get("Phobos").attachChild(geos.get("Phobos"));
        nodes.get("Phobos").setLocalTranslation(geos.get("Mars").getLocalTranslation());
        nodes.get("Mars").attachChild(geos.get("Mars"));
        nodes.get("Mars").attachChild(nodes.get("Deimos"));
        nodes.get("Mars").attachChild(nodes.get("Phobos"));

        nodes.get("Moon").attachChild(geos.get("Moon"));
        nodes.get("Moon").setLocalTranslation(geos.get("Earth").getLocalTranslation());
        nodes.get("Earth").attachChild(geos.get("Earth"));
        nodes.get("Earth").attachChild(nodes.get("Moon"));

        nodes.get("Venus").attachChild(geos.get("Venus"));
        nodes.get("Mercury").attachChild(geos.get("Mercury"));

        nodes.get("Sun").attachChild(geos.get("Sun"));
        nodes.get("Sun").attachChild(nodes.get("Mercury"));
        nodes.get("Sun").attachChild(nodes.get("Venus"));
        nodes.get("Sun").attachChild(nodes.get("Earth"));
        nodes.get("Sun").attachChild(nodes.get("Mars"));
        nodes.get("Sun").attachChild(nodes.get("Jupiter"));
        nodes.get("Sun").attachChild(nodes.get("Saturn"));
        nodes.get("Sun").attachChild(nodes.get("Uranus"));
        nodes.get("Sun").attachChild(nodes.get("Neptune"));
        nodes.get("Sun").attachChild(nodes.get("Pluto"));

        nodes.get("Stars").attachChild(geos.get("Stars"));

        rootNode.attachChild(nodes.get("Stars"));
        rootNode.attachChild(nodes.get("Sun"));
    }

    /**
     * Initialise une chaseCam pour pouvoir observer chaque astre. La caméra est dirigée vers le Soleil par défaut.
     * Il est possible de pivoter l'angle de vue avec le clic de la souris.
     */
    private void initCamera() {
        cam.setFrustumFar(1000000f);
        chaseCam = new ChaseCamera(cam, geos.get(SolarSystem.SUN), inputManager);
        chaseCam.setDragToRotate(true);
        chaseCam.setMinVerticalRotation(-1f);
        chaseCam.setMaxVerticalRotation(1f);
        chaseCam.setDefaultVerticalRotation(0.7f);
        adjustFocus(currentFocus); // Ajuste la distance et le zoom
    }

    /**
     * Change la distance, le niveau de zoom et la vitesse de zoom de la caméra.
     */
    private void adjustCamera(float defaultDistance, float minDistance, float maxDistance, float zoomSpeed)
    {
        chaseCam.setDefaultDistance(defaultDistance);
        chaseCam.setMinDistance(minDistance);
        chaseCam.setMaxDistance(maxDistance);
        chaseCam.setZoomSensitivity(zoomSpeed);
    }

    /**
     * Change les paramètres de la caméra en fonction de l'astre observé.
     * 
     * Comme les tailles et les distances varient beaucoup, par exemple entre Jupiter et Phobos,
     * il faut ajuster la distance de la caméra par rapport à l'astre, la vitesse et le niveau de zoom.
     */
    private void adjustFocus(int focus)
    {
        String name = solarSystem.focusList.get(focus);

        switch(name) {
            case SolarSystem.SUN:
                adjustCamera(1500f, 800f, 500000f, 300f);
                break;
            case SolarSystem.JUPITER:
            case SolarSystem.SATURN:
            case SolarSystem.URANUS:
            case SolarSystem.NEPTUNE:
                adjustCamera(120f, 70f, 5000f, 70f);
                break;
            case SolarSystem.MOON:
            case SolarSystem.MERCURY:
                adjustCamera(15f, 5f, 3000f, 20f);
                break;
            case SolarSystem.KUIPER:
                adjustCamera(1000f, 200f, 3000f, 50f);
                break;
            case SolarSystem.IO:
            case SolarSystem.EUROPA:
            case SolarSystem.PHOBOS:
            case SolarSystem.DEIMOS:
            case SolarSystem.PLUTO:
                adjustCamera(4f, 2f, 100f, 1f);
                break;
            default:
                adjustCamera(25f, 15f, 5000f, 30f);
                break;
        }
    }

    /**
     * Affiche l'astre suivant.
     */
    private void incFocus() {
        currentFocus++;
        if(currentFocus >= solarSystem.focusList.size()) currentFocus = 0;
        chaseCam.setSpatial(geos.get(solarSystem.focusList.get(currentFocus)));
        displayText(currentFocus);
        adjustFocus(currentFocus);
    }

    /**
     * Affiche l'astre précédent.
     */
    private void decFocus() {
        currentFocus--;
        if(currentFocus < 0) currentFocus = solarSystem.focusList.size() - 1;
        chaseCam.setSpatial(geos.get(solarSystem.focusList.get(currentFocus)));
        displayText(currentFocus);
        adjustFocus(currentFocus);
    }

    /**
     * Initialise les textes affichés à l'écran.
     */
    private void initText() {
        font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        nameText = createText(30, 450, 120);
        sizeText = createText(14, 450, 80);
        weightText = createText(14, 450, 60);
        displayText(currentFocus);
    }

    /**
     * Crée, affiche et retourne un BitmapText avec une position et une police d'écriture.
     */
    private BitmapText createText(float fontSize, float x, float y) {
        BitmapText text = new BitmapText(font);
        text.setText("");
        text.setSize(fontSize);
        text.setLocalTranslation(x, y, 0);
        guiNode.attachChild(text);
        return text;
    }

    /**
     * Affiche les informations d'un astre à l'écran.
     */
    private void displayText(int currentFocus) {
        // Nom de l'astre
        String name = solarSystem.focusList.get(currentFocus);
        nameText.setText(name);
        if(name.equals(SolarSystem.KUIPER)) nameText.setText("Kuiper's Belt");

        // Masse
        weightText.setText("Weight : " + solarSystem.getWeight(name) + " kg");

        // Taille
        // La taille doit être divisée par 10 pour Phobos et Deimos
        int size = (int)(solarSystem.getSize(name) * 10000f);
        if(name.equals(SolarSystem.PHOBOS)|| name.equals(SolarSystem.DEIMOS) || name.equals(SolarSystem.KUIPER)) size /= 10;
        sizeText.setText("Diameter : " + size + " km");
    }

    /**
     * Active/désactive la pause de la simulation.
     */
    private void togglePause() {
        isRunning = !isRunning;
    }

    /**
     * Augmente la vitesse de la simulation.
     */
    private void incTime() {
        currentTimeSpeedIndex++;
        if(currentTimeSpeedIndex >= timeSpeeds.size()) currentTimeSpeedIndex = 0;
    }

    /**
     * Réduit la vitesse de la simulation.
     */
    private void decTime() {
        currentTimeSpeedIndex--;
        if(currentTimeSpeedIndex < 0) currentTimeSpeedIndex = timeSpeeds.size() - 1;
    }

    /**
     * Désactive l'affichage des orbites.
     */
    private void disableOrbits() {
        for (Node node : orbitNodes) {
            Node parent = node.getParent();
            if (parent != null) parent.detachChild(node);
        }
    }
    
    /**
     * Fait tourner chaque astre autour du Soleil et sur lui-même.
     */
    @Override
    public void simpleUpdate(float tpf) {
        if(isRunning) {
            for (String name : solarSystem.objectNames) {
                if(!name.equals(SolarSystem.STARS) && !name.equals(SolarSystem.KUIPER)) {
                    // Révolution des astres
                    nodes.get(name).rotate(0, tpf * timeSpeeds.get(currentTimeSpeedIndex) * FastMath.PI * 2 / solarSystem.getRevolutionPeriod(name), 0);
                    // Rotation des astres
                    geos.get(name).rotate(0, 0, tpf * timeSpeeds.get(currentTimeSpeedIndex)  * FastMath.PI * 2 / solarSystem.getRotationPeriod(name));
                }
            }
        }
    }

    /**
     * Initialise les raccourcis claviers.
     */
    private void initKeys() {
        inputManager.addMapping(pauseString,  new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping(incTimeString,  new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping(decTimeString,  new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addMapping(incFocusString,  new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping(decFocuString,  new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping(orbitString,  new KeyTrigger(KeyInput.KEY_SPACE));

        inputManager.addListener(actionListener, pauseString);
        inputManager.addListener(actionListener, incTimeString);
        inputManager.addListener(actionListener, decTimeString);
        inputManager.addListener(actionListener, incFocusString);
        inputManager.addListener(actionListener, decFocuString);
        inputManager.addListener(actionListener, orbitString);
    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals(pauseString) && !keyPressed) {
                togglePause();
            }
            if (name.equals(incTimeString) && !keyPressed) {
                incTime();
            }
            if (name.equals(decTimeString) && !keyPressed) {
                decTime();
            }
            if (name.equals(incFocusString) && !keyPressed) {
                incFocus();
            }
            if (name.equals(decFocuString) && !keyPressed) {
                decFocus();
            }
            if (name.equals(orbitString) && !keyPressed) {
                disableOrbits();
            }
        }
    };
}
