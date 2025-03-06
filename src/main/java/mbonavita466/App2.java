package mbonavita466;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.jme3.input.ChaseCamera;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Torus;
import com.jme3.scene.shape.Sphere;
import com.jme3.util.TangentBinormalGenerator;

public class App2 extends SimpleApplication {
    private static SolarSystem2 system;

    private ChaseCamera chaseCam;

    private Geometry sunGeo;
    private Geometry earthGeo;
    private Geometry moonGeo;
    private Geometry marsGeo;
    private Geometry jupiterGeo;

    private Node solarSystemNode;
    private Node sunNode;
    private Node earthNode;
    private Node moonNode;
    private Node marsNode;
    private Node jupiterNode;

    private Boolean isRunning = true;
    private final Float distanceScale = 0.1f;
    private final Float sizeScale = 1f;
    private final Float timeScale = 1f;

    private final int orbitCircleSamples = 48;
    private final int orbitRadialSamples = 32;
    private final float orbitWidth = 0.1f;

    public static void main(String[] args) {
        system = new SolarSystem2();
        App2 app = new App2();
        app.start();
    }

    public App2() {
    }

    @Override
    public void simpleInitApp() {
        initKeys();
        initNodes();
        initObjects();
        initOrbits();
        attachNodes();
        initCam();
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(isRunning) {
            sunNode.rotate(0, tpf * timeScale, 0);
            earthNode.rotate(0, tpf * timeScale, 0);
            moonNode.rotate(0, tpf * timeScale, 0);
        }
    }

    private void initNodes() {
        solarSystemNode = new Node("solarSystemNode");
        sunNode = new Node("sunNode");
        earthNode = new Node("earthNode");
        moonNode = new Node("marsNode");
        marsNode = new Node("marsNode");
        jupiterNode = new Node("jupiterNode");
    }

    private void initObjects() {
        sunGeo = createObject("Sun", "sun.jpg");
        earthGeo = createObject("Earth", "earth.jpg");
        moonGeo = createObject("Moon", "moon.jpg");
        marsGeo = createObject("Mars", "mars.jpg");
        jupiterGeo = createObject("Jupiter", "jupiter.jpg");
    }

    private Geometry createObject(String objectName, String textureName) {
         /**
         * * Crée et retourne un objet du système solaire en lui ajoutant une texture.
         */
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
        /**
         * Donne une taille, une position et une rotation à l'objet.
         */
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
        createOrbits("earthOrbit", system.getDistance("Earth"), sunGeo, sunNode);
        createOrbits("moonOrbit", system.getDistance("Moon"), earthGeo, earthNode);
        createOrbits("marsOrbit", system.getDistance("Mars"), sunGeo, sunNode);
    }

    private Geometry createOrbits(String objectName, Float r, Geometry parentObject, Node orbitNode) {
        Torus orbit = new Torus(orbitCircleSamples, orbitRadialSamples,  orbitWidth, r);
        Geometry orbitGeo = new Geometry(objectName, orbit);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", ColorRGBA.White);
        orbitGeo.setMaterial(mat);
        
        orbitGeo.rotate(FastMath.HALF_PI, 0, 0);

        orbitNode.attachChild(orbitGeo);
        orbitGeo.setLocalTranslation(parentObject.getLocalTranslation());

        return orbitGeo;
    }

    private void attachNodes() {
        jupiterNode.attachChild(jupiterGeo);

        marsNode.attachChild(marsGeo);

        moonNode.attachChild(moonGeo);
        moonNode.setLocalTranslation(earthGeo.getLocalTranslation());

        earthNode.attachChild(earthGeo);
        earthNode.attachChild(moonNode);

        sunNode.attachChild(sunGeo);
        sunNode.attachChild(earthNode);
        sunNode.attachChild(marsNode);
        sunNode.attachChild(jupiterNode);

        solarSystemNode.attachChild(sunNode);
    }

    private void initCam() {
        /*
        flyCam.setEnabled(true);
        flyCam.setDragToRotate(true);
        flyCam.setMoveSpeed(30f);
        flyCam.setRotationSpeed(5f);
        flyCam.setZoomSpeed(50f);
        */
        chaseCam = new ChaseCamera(cam, earthGeo, inputManager);
        chaseCam.setDragToRotate(true);
        chaseCam.setDefaultDistance(5f);
        chaseCam.setZoomSensitivity(3f);
        chaseCam.setMinVerticalRotation(-1f); 
        chaseCam.setMaxVerticalRotation(1.5f);
    }

    private void initKeys() {
        inputManager.addMapping("Pause",  new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Rotate up",   new KeyTrigger(KeyInput.KEY_J));
        inputManager.addMapping("Rotate down",  new KeyTrigger(KeyInput.KEY_K));
        inputManager.addMapping("Rotate right",   new KeyTrigger(KeyInput.KEY_L));
        inputManager.addMapping("Rotate left",  new KeyTrigger(KeyInput.KEY_O));
        inputManager.addMapping("Rotate forward",   new KeyTrigger(KeyInput.KEY_B));
        inputManager.addMapping("Rotate backward",  new KeyTrigger(KeyInput.KEY_N));

        inputManager.addListener(actionListener, "Pause");
        inputManager.addListener(analogListener, "Rotate up", "Rotate down", "Rotate right", "Rotate left", "Rotate forward", "Rotate backward");
    }

    private final ActionListener actionListener = new ActionListener() {
    @Override
    public void onAction(String name, boolean keyPressed, float tpf) {
        if (name.equals("Pause") && !keyPressed) {
                isRunning = !isRunning;
            }
        }
    };

    private final AnalogListener analogListener = new AnalogListener() {
        @Override
        public void onAnalog(String name, float value, float tpf) {
            /*
            if (name.equals("Rotate up")) {
                solarSystemNode.rotate(0, tpf, 0);
            }
            if (name.equals("Rotate down")) {
                solarSystemNode.rotate(0, -tpf, 0);
            }
            if (name.equals("Rotate right")) {
                solarSystemNode.rotate(tpf, 0, 0);
            }
            if (name.equals("Rotate left")) {
                solarSystemNode.rotate(-tpf, 0, 0);
            }
            if (name.equals("Rotate forward")) {
                solarSystemNode.rotate(0, 0, tpf);
            }
            if (name.equals("Rotate backward")) {
                solarSystemNode.rotate(0, 0, -tpf);
            }
            */
        }
    };
}