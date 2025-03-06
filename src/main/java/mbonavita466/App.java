package mbonavita466;

import com.jme3.app.SimpleApplication;
import com.jme3.scene.Node;
import com.jme3.input.CameraInput;
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
import com.jme3.util.TangentBinormalGenerator;

import java.util.Dictionary;
import java.util.Hashtable;

public class App extends SimpleApplication {
    private static App app;
    private static SolarSystem system;

    private String texturePath = "Textures/Objects";
    private Dictionary<String, Geometry> objects = new Hashtable<>();

    private FlyByCamera flyCam;

    private Boolean isRunning = true;

    private Node sunNode;
    private Node earthNode;
    private Node moonNode;

    private Geometry sunGeo;
    private Geometry earthGeo;
    private Geometry moonGeo;

    public static void main(String[] args) {
        app = new App();
        system = new SolarSystem();
        app.start();
    }

    public App() {
    }

    @Override
    public void simpleInitApp() {
        flyCam = new FlyByCamera(cam);
        flyCam.setEnabled(true);
        flyCam.setRotationSpeed(10);
        flyCam.setDragToRotate(true);
        
        InitKeys();

        sunNode = new Node("sunNode");
        earthNode = new Node("earthNode");
        moonNode = new Node("moonNode");

        // Sun
        sunGeo = CreateObject("Sun", "sun.jpg");
        TransformObjet(sunGeo, 1f, 0f, 0f, 0f, 1.6f, 1.6f, 0f);
        objects.put("Sun", sunGeo);

        // Earth
        earthGeo = CreateObject("Earth", "earth.jpg");
        TransformObjet(earthGeo, 0.3f, 0f, 0f, -4f, 1.6f, 1.6f, 0f);
        objects.put("Earth", earthGeo);

        // Moon
        moonGeo = CreateObject("Moon", "moon.jpg");
        TransformObjet(moonGeo, 0.1f, 1f, 0, -0f, 1.6f, 1.6f, 0f);
        objects.put("Earth", moonGeo);
        
        moonNode.attachChild(moonGeo);
        moonNode.setLocalTranslation(earthGeo.getLocalTranslation());
        earthNode.attachChild(earthGeo);
        sunNode.attachChild(sunGeo);

        earthNode.attachChild(moonNode);
        sunNode.attachChild(earthNode);
        rootNode.attachChild(sunNode);
    }

    private void InitFlyCam() {};

    private void InitKeys() {
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

    private Geometry CreateObject(String objectName, String textureName) {
        /**
         * Crée et retourne un objet du système solaire en lui ajoutant une texture.
        */

        Sphere sphere = new Sphere(32,32, 2f);
        Geometry geo = new Geometry(objectName, sphere);

        sphere.setTextureMode(Sphere.TextureMode.Projected);
        TangentBinormalGenerator.generate(sphere);

        Material material = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setTexture("ColorMap", assetManager.loadTexture(texturePath + "/" + textureName));
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        geo.setMaterial(material);

        return geo;
    }

    private void TransformObjet(Geometry geo, float size, float xPos, float yPos, float zPos, float xRot, float yRot, float zRot) {
        /**
         * Donne une taille, une position et une rotation à l'objet.
        */
        geo.scale(size, size, size);
        geo.setLocalTranslation(xPos, yPos, zPos);
        geo.rotate(xRot, yRot, zRot);
    }

    @Override
    public void simpleUpdate(float tpf) {
        if(isRunning) {
            //sunNode.rotate(0*tpf, 0*tpf, 1*tpf);
            earthNode.rotate(0*tpf, 1*tpf, 0*tpf);
            moonNode.rotate(0*tpf, 1*tpf, 0*tpf);
        }
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
            if (name.equals("Rotate up")) {
                sunNode.rotate(0, 1*tpf, 0);
            }
            if (name.equals("Rotate down")) {
                sunNode.rotate(0, -1*tpf, 0);
            }
            if (name.equals("Rotate right")) {
                sunNode.rotate(1*tpf, 0, 0);
            }
            if (name.equals("Rotate left")) {
                sunNode.rotate(-1*tpf, 0, 0);
            }
            if (name.equals("Rotate forward")) {
                sunNode.rotate(0, 0, 1*tpf);
            }
            if (name.equals("Rotate backward")) {
                sunNode.rotate(0, 0, -1*tpf);
            }
        }
    };
}