package mbonavita466;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.jme3.input.CameraInput;
import com.jme3.input.ChaseCamera;
import com.jme3.input.ChaseCamera;
import com.jme3.input.FlyByCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import com.jme3.util.TangentBinormalGenerator;

import java.util.Dictionary;
import java.util.Hashtable;

public class App3 extends SimpleApplication {
    private static SolarSystem3 system;

    private ChaseCamera chaseCam;

    private String texturePath = "Textures/Objects";
    private Dictionary<String, Geometry> objects = new Hashtable<>();

    private Boolean isRunning = true;
    private final Float distanceScale = 1f;
    private final Float sizeScale = 1f;
    private final Float timeScale = 0.025f;

    private final int orbitCircleSamples = 32;
    private final int orbitRadialSamples = 64;
    private final float orbitWidth = 0.02f;

    private final Dictionary<String, Geometry> geos = new Hashtable<>();
    private final Dictionary<String, Node> nodes = new Hashtable<>();

    public static void main(String[] args) {
        App3 app = new App3();
        system = new SolarSystem3();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        initNodes();
        initObjects();
        //initOrbits();

        attachNodes();

        initCamera();
        initKeys();
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(isRunning) {
            /*
            nodes.get("Sun").rotate(0, tpf, 0);
            nodes.get("Earth").rotate(0, tpf, 0);
            nodes.get("Moon").rotate(0, tpf, 0);
            nodes.get("Mars").rotate(0, tpf, 0);
            nodes.get("Jupiter").rotate(0, tpf, 0);
            */

            for (String name : system.objectNames) {
                nodes.get(name).rotate(0, tpf * timeScale * FastMath.PI * 2 / system.getRevolutionPeriod(name), 0);
            }
        }
    }

    private void initNodes() {
        for (String name : system.objectNames) {
            nodes.put(name, new Node(name + "Node"));
        }
    }

    private void attachNodes() {
        nodes.get("Pluto").attachChild(geos.get("Pluto"));
        nodes.get("Neptune").attachChild(geos.get("Neptune"));
        nodes.get("Uranus").attachChild(geos.get("Uranus"));
        nodes.get("Saturn").attachChild(geos.get("Saturn"));

        nodes.get("Io").attachChild(geos.get("Io"));
        nodes.get("Io").setLocalTranslation(geos.get("Jupiter").getLocalTranslation());
        nodes.get("Europe").attachChild(geos.get("Europe"));
        nodes.get("Europe").setLocalTranslation(geos.get("Jupiter").getLocalTranslation());
        nodes.get("Jupiter").attachChild(geos.get("Jupiter"));
        nodes.get("Jupiter").attachChild(nodes.get("Io"));
        nodes.get("Jupiter").attachChild(nodes.get("Europe"));

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

        rootNode.attachChild(nodes.get("Sun"));
    }

    private void initObjects() {

        for (int i = 0; i < system.objectNames.size(); i++) {
            String name = system.objectNames.get(i);
            geos.put(name, createObject(name, name + ".jpg"));
        }
    }

    private Geometry createObject(String objectName, String textureName) {
        Sphere sphere = new Sphere(32,32, 2f);
        Geometry geo = new Geometry(objectName, sphere);

        sphere.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(sphere);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        String texturePath = "Textures/Objects";
        material.setTexture("ColorMap", assetManager.loadTexture(texturePath + "/" + textureName));
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        geo.setMaterial(material);

        transformObjet(geo, objectName);

        return geo;
    }

    private void transformObjet(Geometry geo, String objectName) {
       float size = system.getSize(objectName) * sizeScale;
       float distance = system.getDistance(objectName) * distanceScale;
       Float position = system.getPosition(objectName);
       Vector3f rotation = system.getRotation(objectName);

       geo.scale(size, size, size);
       //geo.setLocalTranslation(FastMath.cos(position) * distance,FastMath.sin(position) * distance, 0f);
       geo.setLocalTranslation(distance, 0f, 0f);
       geo.rotate(rotation.getX(), rotation.getY(), rotation.getZ());
    }

    private void initOrbits() {
        createOrbit("mercuryOrbit", system.getDistance("Mercury"), geos.get("Sun"), nodes.get("Sun"));
        createOrbit("venusOrbit", system.getDistance("Venus"), geos.get("Sun"), nodes.get("Sun"));
        createOrbit("earthOrbit", system.getDistance("Earth"), geos.get("Sun"), nodes.get("Sun"));
        createOrbit("moonOrbit", system.getDistance("Moon"), geos.get("Earth"), nodes.get("Earth"));
        createOrbit("marsOrbit", system.getDistance("Mars"), geos.get("Sun"), nodes.get("Sun"));
        createOrbit("deimosOrbit", system.getDistance("Deimos"), geos.get("Mars"), nodes.get("Mars"));
        createOrbit("phobosOrbit", system.getDistance("Phobos"), geos.get("Mars"), nodes.get("Mars"));
        createOrbit("jupiterOrbit", system.getDistance("Jupiter"), geos.get("Sun"), nodes.get("Sun"));
        createOrbit("ioOrbit", system.getDistance("Io"), geos.get("Jupiter"), nodes.get("Jupiter"));
        createOrbit("europeOrbit", system.getDistance("Europe"), geos.get("Jupiter"), nodes.get("Jupiter"));
        createOrbit("saturnOrbit", system.getDistance("Saturn"), geos.get("Sun"), nodes.get("Sun"));
        createOrbit("uranusOrbit", system.getDistance("Uranus"), geos.get("Sun"), nodes.get("Sun"));
        createOrbit("neptuneOrbit", system.getDistance("Neptune"), geos.get("Sun"), nodes.get("Sun"));
        createOrbit("plutoOrbit", system.getDistance("Pluto"), geos.get("Sun"), nodes.get("Sun"));
    }

    private Geometry createOrbit(String objectName, Float r, Geometry parentObject, Node parentNode) {
        Torus orbit = new Torus(orbitCircleSamples, orbitRadialSamples,  orbitWidth, r);
        Geometry orbitGeo = new Geometry(objectName, orbit);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White);
        orbitGeo.setMaterial(mat);
        
        orbitGeo.rotate(FastMath.HALF_PI, 0, 0);

        parentNode.attachChild(orbitGeo);
        orbitGeo.setLocalTranslation(parentObject.getLocalTranslation());

        return orbitGeo;
    }

    private void initCamera() {
        chaseCam = new ChaseCamera(cam, geos.get("Earth"), inputManager);
        chaseCam.setDragToRotate(true);
        chaseCam.setDefaultDistance(5f);
        chaseCam.setZoomSensitivity(3f);
        chaseCam.setMinVerticalRotation(-1f); 
        chaseCam.setMaxVerticalRotation(1.5f);
    }

    private void initKeys() {
        inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P));

        inputManager.addListener(actionListener, "Pause");
    }

    final private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                    isRunning = !isRunning;
                }
            }
        };
    
        final private AnalogListener analogListener = new AnalogListener() {
            @Override
            public void onAnalog(String name, float value, float tpf) {
    
            }
        };
}