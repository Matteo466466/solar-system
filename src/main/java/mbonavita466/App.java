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

public class App extends SimpleApplication {
    private static SolarSystem solarSystem; // Accès aux données des astres

    private Dictionary<String, Geometry> geos = new Hashtable<>(); // Liste des astres
    private Dictionary<String, Node> nodes = new Hashtable<>(); // Liste des nodes principaux

    // Orbites
    private Node orbitNode;
    private List<Node> orbitNodes = new ArrayList<>();
    private final int orbitRadialSamples = 256;

    // Ceinture de Kuiper
    private Node kuiperNode;
    Random random = new Random(); // Chaque astéroide a une position aléatoire

    // Camera
    private ChaseCamera chaseCam;
    private int currentFocus = 0;

    // Paramètres de la simulation
    private Boolean isRunning = true;
    private static int timeScale = 6;
    private List<Float> timeScales = Arrays.asList(-1/5f, -1/10f, -1/60f, -1/360f, -1/720f, 1/720f, 1/360f, 1/60f, 1/20f, 1/5f);
    private static Float distanceScale = 1f;

    // UI
    BitmapFont font;
    BitmapText nameText;
    BitmapText weightText;
    BitmapText sizeText;

    private PointLight pointLight;

    public static void main(String[] args) {
        App app = new App();
        solarSystem = new SolarSystem();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        initLight();
        initNodes();
        initObjects();

        createSaturnRing();
        createKuiperBelt();

        initOrbits();
        attachNodes();

        initCamera();
        initKeys();
        initText();
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(isRunning) {
            for (String name : solarSystem.objectNames) {
                if(!name.equals(SolarSystem.STARS) && !name.equals(SolarSystem.KUIPER)) {
                    nodes.get(name).rotate(0, tpf * timeScales.get(timeScale) * FastMath.PI * 2 / solarSystem.getRevolutionPeriod(name), 0);
                    geos.get(name).rotate(0, 0, tpf * timeScales.get(timeScale)  * FastMath.PI * 2 / solarSystem.getRotationPeriod(name));
                }
            }
        }
    }

    private final ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                isRunning = !isRunning;
            }
            if (name.equals("IncFocus") && !keyPressed) {
                incFocus();
            }
            if (name.equals("DecFocus") && !keyPressed) {
                decFocus();
            }
            if (name.equals("ToggleOrbitDisplay") && !keyPressed) {
                toggleOrbitDisplay();
            }
            if (name.equals("IncTime") && !keyPressed) {
                incTime();
            }
            if (name.equals("DecTime") && !keyPressed) {
                decTime();
            }
        }
    };

    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {}
    };

    private void initLight() {
        pointLight = new PointLight();
        pointLight.setColor(ColorRGBA.White);
        pointLight.setPosition(new Vector3f(-0.f, -0.f, -0.f));
        rootNode.addLight(pointLight);
    }

    private void initNodes() {
        for (String name : solarSystem.objectNames) {
            nodes.put(name, new Node(name + "Node"));
        }
    }

    private void initObjects() {
        for (int i = 0; i < solarSystem.objectNames.size(); i++) {
            String name = solarSystem.objectNames.get(i);
            if(!name.equals(SolarSystem.KUIPER) && !name.equals(SolarSystem.SUN)) {
                geos.put(name, createObject(name, name + ".jpg"));
            }
            geos.put(SolarSystem.SUN, createSun(SolarSystem.SUN, SolarSystem.SUN + ".jpg"));
        }
    }

    private Geometry createSun(String objectName, String textureName) {
        Sphere sphere = new Sphere(32,32, 2f, true, false);
        Geometry geo = new Geometry(objectName, sphere);

        sphere.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(sphere);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        String texturePath = "Textures/Objects";
        material.setTexture("ColorMap", assetManager.loadTexture(texturePath + "/" + textureName));
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        material.getAdditionalRenderState().setFaceCullMode(com.jme3.material.RenderState.FaceCullMode.Off);
        material.setColor("GlowColor", ColorRGBA.Black);// Controls shininess/reflection
        geo.setMaterial(material);

        transformObjet(geo, objectName);
        return geo;
    }

    private Geometry createObject(String objectName, String textureName) {
        Sphere sphere = new Sphere(32,32, 2f, true, false);
        Geometry geo = new Geometry(objectName, sphere);

        sphere.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(sphere);

        Material material = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        String texturePath = "Textures/Objects";
        material.setTexture("DiffuseMap", assetManager.loadTexture(texturePath + "/" + textureName));
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        material.getAdditionalRenderState().setFaceCullMode(com.jme3.material.RenderState.FaceCullMode.Off);
        material.setColor("GlowColor", ColorRGBA.Black);// Controls shininess/reflection
        material.setColor("Diffuse", ColorRGBA.White);
        geo.setMaterial(material);

        transformObjet(geo, objectName);
        return geo;
    }

    private void transformObjet(Geometry geo, String objectName) {
       float size = solarSystem.getSize(objectName) * distanceScale;
       float distance = solarSystem.getDistance(objectName) * distanceScale;
       Float position = solarSystem.getPosition(objectName);
       Vector3f rotation = solarSystem.getRotation(objectName);

       geo.scale(size, size, size);
       geo.setLocalTranslation(FastMath.sin(position) * distance, 0, FastMath.cos(position) * distance);
       geo.rotate(rotation.getX(), rotation.getY(), rotation.getZ());
    }

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
            x = (float)Math.cos(i * FastMath.PI / (quantity * 8)) * distance;
            y = 0;
            z = (float)Math.sin(i * FastMath.PI / (quantity * 8)) * distance;

            x += (random.nextFloat() * scale * 2) - scale;
            y += (random.nextFloat() * scale * 2) - scale;
            z += (random.nextFloat() * scale * 2) - scale;

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
            kuiperNode.attachChild(geo);

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

        for(int i = 0; i < orbitRadialSamples; i++) {
            x1 = (float)Math.cos(2 * i * FastMath.PI / orbitRadialSamples) * distance;
            y1 = (float)Math.sin(2 * i * FastMath.PI / orbitRadialSamples) * distance;
            x2 = (float)Math.cos(2 * (i + 1) * FastMath.PI / orbitRadialSamples) * distance;
            y2 = (float)Math.sin(2 * (i + 1) * FastMath.PI / orbitRadialSamples) * distance;

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

    private void initCamera() {
        cam.setFrustumFar(1000000f);
        chaseCam = new ChaseCamera(cam, geos.get(SolarSystem.SUN), inputManager);
        chaseCam.setDragToRotate(true);
        chaseCam.setMinVerticalRotation(-1f);
        chaseCam.setMaxVerticalRotation(1f);
        chaseCam.setDefaultVerticalRotation(0.7f);
        adjustFocus(currentFocus);
    }

    private void initKeys() {
        inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("IncFocus",  new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("DecFocus",  new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("ToggleOrbitDisplay",  new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("IncTime",  new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("DecTime",  new KeyTrigger(KeyInput.KEY_DOWN));

        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(actionListener, "IncFocus");
        inputManager.addListener(actionListener, "DecFocus");
        inputManager.addListener(actionListener, "ToggleOrbitDisplay");
        inputManager.addListener(actionListener, "IncTime");
        inputManager.addListener(actionListener, "DecTime");
    }

    private void initText() {
        font = assetManager.loadFont("Interface/Fonts/Default.fnt");
        nameText = createText(30, 450, 120);
        sizeText = createText(14, 450, 80);
        weightText = createText(14, 450, 60);
        displayText(currentFocus);
    }

    private BitmapText createText(float fontSize, float x, float y) {
        BitmapText text = new BitmapText(font);
        text.setText("");
        text.setSize(fontSize);
        text.setLocalTranslation(x, y, 0);
        guiNode.attachChild(text);
        return text;
    }

    private void displayText(int currentFocus) {
        String name = solarSystem.focusList.get(currentFocus);

        nameText.setText(name);
        if(name.equals(SolarSystem.KUIPER)) nameText.setText("Kuiper's Belt");

        weightText.setText("Weight : " + solarSystem.getWeight(name) + " kg");

        int size = (int)(solarSystem.getSize(name) * 10000f);
        if(name.equals(SolarSystem.PHOBOS)|| name.equals(SolarSystem.DEIMOS) || name.equals(SolarSystem.KUIPER)) size /= 10;
        sizeText.setText("Diameter : " + size + " km");
    }

    private void incTime() {
        timeScale++;
        if(timeScale >= timeScales.size()) timeScale = 0;
    }

    private void decTime() {
        timeScale--;
        if(timeScale < 0) timeScale = timeScales.size() - 1;
    }

    private void incFocus() {
        currentFocus++;
        if(currentFocus >= solarSystem.focusList.size()) currentFocus = 0;
        chaseCam.setSpatial(geos.get(solarSystem.focusList.get(currentFocus)));
        displayText(currentFocus);
        adjustFocus(currentFocus);
    }

    private void decFocus() {
        currentFocus--;
        if(currentFocus < 0) currentFocus = solarSystem.focusList.size() - 1;
        chaseCam.setSpatial(geos.get(solarSystem.focusList.get(currentFocus)));
        displayText(currentFocus);
        adjustFocus(currentFocus);
    }

    private void toggleOrbitDisplay() {
        disableOrbitDisplay();
    }

    private void disableOrbitDisplay() {
        for (Node node : orbitNodes) {
            Node parent = node.getParent();
            if (parent != null) parent.detachChild(node);
        }
    }

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

    private void adjustCamera(float defaultDistance, float minDistance, float maxDistance, float zoomSpeed)
    {
        chaseCam.setDefaultDistance(defaultDistance);
        chaseCam.setMinDistance(minDistance);
        chaseCam.setMaxDistance(maxDistance);
        chaseCam.setZoomSensitivity(zoomSpeed);
    }
}
