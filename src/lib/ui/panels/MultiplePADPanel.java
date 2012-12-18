package lib.ui.panels;

import javax.swing.*;
import java.awt.*;

import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.vp.*;

import com.sun.j3d.utils.geometry.Text2D;
import java.util.ArrayList;

class CheckerFloor
{
    private final static int FLOOR_LEN = 20;  // should be even

    // colours for floor, etc
    private final static Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

    private BranchGroup floorBG;

    public CheckerFloor() {
        ArrayList greenCoords = new ArrayList();
        floorBG = new BranchGroup();

        boolean isBlue;
        for (int z = -FLOOR_LEN/2; z <= (FLOOR_LEN / 2) - 1; z++) {
            for (int x = -FLOOR_LEN/2; x <= (FLOOR_LEN / 2) - 1; x++) {
                createCoords(x, z, greenCoords);
            }
        }
        addOriginMarker();
        labelAxes();
    }  // end of CheckerFloor()


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


    private void labelAxes()
    // Place numbers along the X- and Z-axes at the integer positions
    {
        Vector3d pt = new Vector3d();
        for (int i=-FLOOR_LEN/2; i <= FLOOR_LEN/2; i++) {
            pt.x = i;
            floorBG.addChild( makeText(pt,""+i) );   // along x-axis
        }

        pt.x = 0;
        for (int i=-FLOOR_LEN/2; i <= FLOOR_LEN/2; i++) {
            pt.z = i;
            floorBG.addChild( makeText(pt,""+i) );   // along z-axis
        }
    }  // end of labelAxes()


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


}  // end of CheckerFloor class

public class MultiplePADPanel extends JPanel {

    private static final int BOUNDSIZE = 100;  // larger than world

    private static final Point3d USERPOSN = new Point3d(0,5,20);
    // initial user position

    private SimpleUniverse su;
    private BranchGroup sceneBG;
    private BoundingSphere bounds;   // for environment nodes

    // private Java3dTree j3dTree;   // frame to hold tree display

    public MultiplePADPanel(int width, int height) {
        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(width, height));

        GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();
        Canvas3D canvas3D = new Canvas3D(config);
        add("Center", canvas3D);
        canvas3D.setFocusable(true);     // give focus to the canvas
        canvas3D.requestFocus();

        su = new SimpleUniverse(canvas3D);

        createSceneGraph();
        initUserPosition();        // set user's viewpoint
        orbitControls(canvas3D);   // controls for moving the viewpoint

        su.addBranchGraph(sceneBG);
    } // end of MultiplePADPanel()

    private void createSceneGraph()
    // initilise the scene
    {
        sceneBG = new BranchGroup();
        bounds = new BoundingSphere(new Point3d(0,0,0), 100);

        lightScene();         // add the lights
        addBackground();      // add the sky
        sceneBG.addChild( new CheckerFloor().getBG() );  // add the floor

        floatingSphere();     // add the floating sphere

        // j3dTree.recursiveApplyCapability( sceneBG );   // set capabilities for tree display

        sceneBG.compile();   // fix the scene
    } // end of createSceneGraph()


    private void lightScene()
        /* One ambient light, 2 directional lights */
    {
        Color3f white = new Color3f(1.0f, 1.0f, 1.0f);

        // Set up the ambient light
        AmbientLight ambientLightNode = new AmbientLight(white);
        ambientLightNode.setInfluencingBounds(bounds);
        sceneBG.addChild(ambientLightNode);

        // Set up the directional lights
        Vector3f light1Direction  = new Vector3f(-1.0f, -1.0f, -1.0f);
        // left, down, backwards
        Vector3f light2Direction  = new Vector3f(1.0f, -1.0f, 1.0f);
        // right, down, forwards

        DirectionalLight light1 =
                new DirectionalLight(white, light1Direction);
        light1.setInfluencingBounds(bounds);
        sceneBG.addChild(light1);

        DirectionalLight light2 =
                new DirectionalLight(white, light2Direction);
        light2.setInfluencingBounds(bounds);
        sceneBG.addChild(light2);
    }  // end of lightScene()



    private void addBackground()
    // A blue sky
    { Background back = new Background();
        back.setApplicationBounds( bounds );
        back.setColor(0.17f, 0.65f, 0.92f);    // sky colour
        sceneBG.addChild( back );
    }  // end of addBackground()



    private void orbitControls(Canvas3D c)
        /* OrbitBehaviour allows the user to rotate around the scene, and to
   zoom in and out.  */
    {
        OrbitBehavior orbit =
                new OrbitBehavior(c, OrbitBehavior.REVERSE_ALL);
        orbit.setSchedulingBounds(bounds);

        ViewingPlatform vp = su.getViewingPlatform();
        vp.setViewPlatformBehavior(orbit);
    }  // end of orbitControls()



    private void initUserPosition()
    // Set the user's initial viewpoint using lookAt()
    {
        ViewingPlatform vp = su.getViewingPlatform();
        TransformGroup steerTG = vp.getViewPlatformTransform();

        Transform3D t3d = new Transform3D();
        steerTG.getTransform(t3d);

        // args are: viewer posn, where looking, up direction
        t3d.lookAt( USERPOSN, new Point3d(0,0,0), new Vector3d(0,1,0));
        t3d.invert();

        steerTG.setTransform(t3d);
    }  // end of initUserPosition()

    private void floatingSphere()
    // A shiny blue sphere located at (0,4,0)
    {
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

        sceneBG.addChild(tg);
    }  // end of floatingSphere()

} // end of MultiplePADPanel class

