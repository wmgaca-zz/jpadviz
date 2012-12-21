package lib.ui.panels;

import java.awt.*;

import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.vp.*;

import com.sun.j3d.utils.geometry.Text2D;
import lib.ui.panels.base.MultiplePanel;

import java.util.ArrayList;

class PADFloor
{
    private final static int FLOOR_LEN = 100;  // should be even

    // colours for floor, etc
    private final static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    private BranchGroup floorBG;

    public PADFloor() {
        ArrayList greenCoords = new ArrayList();
        floorBG = new BranchGroup();

        boolean isBlue;
        for (int z = -FLOOR_LEN/2; z <= (FLOOR_LEN / 2) - 1; z++) {
            for (int x = -FLOOR_LEN/2; x <= (FLOOR_LEN / 2) - 1; x++) {
                createCoords(x, z, greenCoords);
            }
        }
        addOriginMarker();
    }

    private void createCoords(int x, int z, ArrayList coords) {
        // points created in counter-clockwise order
        Point3f p1 = new Point3f(x, 0.0f, z+1.0f);
        Point3f p2 = new Point3f(x+1.0f, 0.0f, z+1.0f);
        Point3f p3 = new Point3f(x+1.0f, 0.0f, z);
        Point3f p4 = new Point3f(x, 0.0f, z);
        coords.add(p1);
        coords.add(p2);
        coords.add(p3);
        coords.add(p4);
    }


    private void addOriginMarker()
    // A red square centered at (0,0,0), of length 0.5
    {  // points created counter-clockwise, a bit above the floor
        Point3f p1 = new Point3f(-0.25f, 0.01f, 0.25f);
        Point3f p2 = new Point3f(0.25f, 0.01f, 0.25f);
        Point3f p3 = new Point3f(0.25f, 0.01f, -0.25f);
        Point3f p4 = new Point3f(-0.25f, 0.01f, -0.25f);

        ArrayList oCoords = new ArrayList();
        oCoords.add(p1); oCoords.add(p2);
        oCoords.add(p3); oCoords.add(p4);

        //floorBG.addChild( new ColouredTiles(oCoords, medRed) );
    } // end of addOriginMarker();

    private TransformGroup makeText(Vector3d vertex, String text) {
        // Create a Text2D object at the specified vertex
        Text2D message = new Text2D(text, white, "SansSerif", 36, Font.BOLD );
        // 36 point bold Sans Serif

        TransformGroup tg = new TransformGroup();
        Transform3D t3d = new Transform3D();
        t3d.setTranslation(vertex);
        tg.setTransform(t3d);
        tg.addChild(message);
        return tg;
    } // end of getTG()


    public BranchGroup getBG() {
        return floorBG;
    }


}  // end of PADFloor class

public class MultiplePADPanel extends MultiplePanel {

    protected static final int BoundSize = 100;  // larger than world

    protected static final Point3d UserPosition = new Point3d(0,5,20);

    protected SimpleUniverse universe;
    protected BranchGroup branchGroup;
    protected BoundingSphere boundingSphere;

    // private Java3dTree j3dTree;   // frame to hold tree display

    public MultiplePADPanel(int width, int height) {
        super(width, height);

        setLayout(new BorderLayout());
        setOpaque(false);

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.setFocusable(true);     // give focus to the canvas
        canvas3D.requestFocus();

        universe = new SimpleUniverse(canvas3D);

        createSceneGraph();
        initUserPosition();
        orbitControls(canvas3D);

        universe.addBranchGraph(branchGroup);
    }

    @Override
    public void customPaintComponent(Graphics2D g2d) {
    }

    /**
     * Initialise the scene.
     */
    private void createSceneGraph() {
        branchGroup = new BranchGroup();
        boundingSphere = new BoundingSphere(new Point3d(0,0,0), BoundSize);

        // add the lights
        lightScene();

        // add the sky
        addBackground();

        branchGroup.addChild(new PADFloor().getBG());

        floatingSphere();
        branchGroup.compile();
    }

    /**
     * Light the scene.
     * One ambient light, two directional lights.
     */
    private void lightScene() {
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

        // Ambient light
        AmbientLight ambientLightNode = new AmbientLight(white);
        ambientLightNode.setInfluencingBounds(boundingSphere);
        branchGroup.addChild(ambientLightNode);

        // Directional light: left, down, backwards
        DirectionalLight light1 = new DirectionalLight(white, new Vector3f(-1.0f, -1.0f, -1.0f));
        light1.setInfluencingBounds(boundingSphere);
        branchGroup.addChild(light1);

        // Directional light: right, down, forwards
        DirectionalLight light2 = new DirectionalLight(white, new Vector3f(1.0f, -1.0f, 1.0f));
        light2.setInfluencingBounds(boundingSphere);
        branchGroup.addChild(light2);
    }

    private void addBackground() {
        Background back = new Background();
        back.setApplicationBounds(boundingSphere);
        back.setColor(0.17f, 0.65f, 0.92f);    // sky colour
        branchGroup.addChild(back);
    }

    private void orbitControls(Canvas3D c) {
        OrbitBehavior orbitBehavior = new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
        orbitBehavior.setSchedulingBounds(boundingSphere);
        universe.getViewingPlatform().setViewPlatformBehavior(orbitBehavior);
    }

    private void initUserPosition() {
        ViewingPlatform vp = universe.getViewingPlatform();
        TransformGroup steerTG = vp.getViewPlatformTransform();

        Transform3D t3d = new Transform3D();
        steerTG.getTransform(t3d);

        // args are: viewer posn, where looking, up direction
        t3d.lookAt(UserPosition, new Point3d(0,0,0), new Vector3d(0,1,0));
        t3d.invert();

        steerTG.setTransform(t3d);
    }

    private void floatingSphere() {
        // Create the blue appearance node
        Color3f black = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f blue = new Color3f(0.3f, 0.3f, 0.8f);
        Color3f specular = new Color3f(0.9f, 0.9f, 0.9f);

        Material blueMat= new Material(blue, black, blue, specular, 25.0f);
        // sets ambient, emissive, diffuse, specular, shininess
        blueMat.setLightingEnable(true);

        Appearance blueApp = new Appearance();
        blueApp.setMaterial(blueMat);

        // position the sphere
        Transform3D t3d = new Transform3D();
        t3d.set( new Vector3f(0,4,0));
        TransformGroup tg = new TransformGroup(t3d);
        tg.addChild(new Sphere(2.0f, blueApp));   // set its radius and appearance

        branchGroup.addChild(tg);
    }

}

