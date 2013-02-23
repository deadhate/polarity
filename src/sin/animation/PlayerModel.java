package sin.animation;

import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import sin.tools.T;
import sin.world.World;

/**
 *
 * @author SinisteRing
 */
public class PlayerModel {
    // Index Holders:
    private static final int HEAD = 0;
    private static final int TORSO = 1;
    private static final int ARM_R = 2;
    private static final int ARM_L = 3;
    private static final int LEG_R = 4;
    private static final int LEG_L = 5;

    // Instance Variables:
    private Node node = new Node();
    private Node[] parts = new Node[6];
    //private int id;
    private RigidBodyControl rbc;
    private float[] angles = new float[3];
    private boolean initialized = false;

    public void GenHead(Node node, String name){
        World.CG.createSphere(node, name, .7f, T.v3f(0, .5f, 0), "Textures/wall.png", Sphere.TextureMode.Polar);
        World.CG.createSphere(node, name, .5f, T.v3f(0, .1f, 0), "Textures/BC_Tex.png", Sphere.TextureMode.Projected);
    }
    public void GenTorso(Node node, String name){
        World.CG.createBox(node, name, T.v3f(1.2f, .6f, .4f), T.v3f(0, .75f, 0), "Textures/BC_Tex.png", T.v2f(1, 1));
        World.CG.createBox(node, name, T.v3f(.95f, .4f, .35f), T.v3f(0, -.2f, 0), "Textures/BC_Tex.png", T.v2f(1, 1));
        float radius = .75f;
        Vector3f trans = T.v3f(-.6f, .85f, 0);
        World.CG.createSphere(node, name, radius, trans, "Textures/BC_Tex.png", Sphere.TextureMode.Projected);
        trans.setX(trans.getX()*-1);
        World.CG.createSphere(node, name, radius, trans, "Textures/BC_Tex.png", Sphere.TextureMode.Projected);
        radius = .5f;
        trans = T.v3f(.3f, -.7f, -.1f);
        World.CG.createSphere(node, name, radius, trans, "Textures/BC_Tex.png", Sphere.TextureMode.Projected);
        trans.setX(trans.getX()*-1);
        World.CG.createSphere(node, name, radius, trans, "Textures/BC_Tex.png", Sphere.TextureMode.Projected);
    }
    public void GenArm(Node node, String name){
        World.CG.createSphere(node, name, .65f, T.v3f(0,0,0), "Textures/BC_Tex.png", Sphere.TextureMode.Projected);
        Quaternion facing = new Quaternion().fromAngleAxis(-FastMath.PI/2.3f, Vector3f.UNIT_X);
        Geometry g = World.CG.createCylinder(node, name, .45f, 1.7f, T.v3f(0, -.95f, -.2f), "Textures/BC_Tex.png", T.v2f(1, 1));
        g.setLocalRotation(facing);
        World.CG.createSphere(node, name, .5f, T.v3f(0, -1.6f, -.35f), "Textures/BC_Tex.png", Sphere.TextureMode.Projected);
        World.CG.createCylinder(node, name, .42f, 1.7f, T.v3f(0, -1.65f, .55f), "Textures/BC_Tex.png", T.v2f(1, 1));
        World.CG.createBox(node, name, T.v3f(.5f, .5f, .5f), T.v3f(0, -1.65f, 1.5f), "Textures/BC_Tex.png", T.v2f(1, 1));
    }
    public void GenLeg(Node node, String name){
        World.CG.createSphere(node, name, .5f, T.v3f(0, 0, 0), "Textures/BC_Tex.png", Sphere.TextureMode.Projected);
        Quaternion facing = new Quaternion().fromAngleAxis(FastMath.PI/2.3f, Vector3f.UNIT_X);
        Geometry g = World.CG.createCylinder(node, name, .45f, 1.2f, T.v3f(0, -.5f, .1f), "Textures/BC_Tex.png", T.v2f(1, 1));
        g.setLocalRotation(facing);
        World.CG.createSphere(node, name, .5f, T.v3f(0, -1.2f, .2f), "Textures/BC_Tex.png", Sphere.TextureMode.Projected);
        facing = new Quaternion().fromAngleAxis(FastMath.PI/1.8f, Vector3f.UNIT_X);
        g = World.CG.createCylinder(node, name, .45f, 1.2f, T.v3f(0, -1.8f, .1f), "Textures/BC_Tex.png", T.v2f(1, 1));
        g.setLocalRotation(facing);
        World.CG.createSphere(node, name, .5f, T.v3f(0, -2.3f, 0), "Textures/BC_Tex.png", Sphere.TextureMode.Projected);
        World.CG.createBox(node, name, T.v3f(.4f, .2f, 1), T.v3f(0, -3f, .3f), "Textures/BC_Tex.png", T.v2f(1, 1));
    }

    public PlayerModel(){
        //
    }

    public Node getNode(){
        return node;
    }
    public boolean isInitialized(){
        return initialized;
    }
    
    public void initModel(int id){
        String sid = "";
        if(id < 10){
            sid = "0"+id;
        }else if(id < 100){
            sid = Integer.toString(id);
        }
        parts[HEAD] = new Node("Head");
        GenHead(parts[HEAD], sid+"head");
        parts[HEAD].setLocalTranslation(T.v3f(0, 1.75f, 0));

        parts[TORSO] = new Node("Torso");
        GenTorso(parts[TORSO], sid+"torso");

        parts[ARM_R] = new Node("ArmRight");
        GenArm(parts[ARM_R], sid+"arm");
        parts[ARM_R].setLocalTranslation(T.v3f(-1.2f, 1.1f, 0));

        parts[ARM_L] = new Node("ArmLeft");
        GenArm(parts[ARM_L], sid+"arm");
        parts[ARM_L].setLocalTranslation(T.v3f(1.2f, 1.1f, 0));

        parts[LEG_R] = new Node("LegRight");
        GenLeg(parts[LEG_R], sid+"leg");
        parts[LEG_R].setLocalTranslation(T.v3f(-.5f, -1, 0));

        parts[LEG_L] = new Node("LegLeft");
        GenLeg(parts[LEG_L], sid+"leg");
        parts[LEG_L].setLocalTranslation(T.v3f(.5f, -1, 0));

        int i = 0;
        while(i < parts.length){
            node.attachChild(parts[i]);
            i++;
        }
        initialized = true;
    }
    public void update(Vector3f loc, Quaternion rot){
        node.setLocalTranslation(loc);
        rot.toAngles(angles);
        node.setLocalRotation(new Quaternion().fromAngles(0, angles[1], 0));
        parts[0].setLocalRotation(new Quaternion().fromAngles(angles[0], 0, 0));
        parts[2].setLocalRotation(new Quaternion().fromAngles(angles[0], 0, 0));
        parts[3].setLocalRotation(new Quaternion().fromAngles(angles[0], 0, 0));
        //body[HEAD].setLocalRotation(rot);
        //node.setLocalRotation(new Quaternion(0, rot.getX(), 0, rot.getZ()));
    }
    public void create(Node node, Vector3f loc){
        //loc = T.v3f(0, 0, 0);
        CollisionShape cs = CollisionShapeFactory.createMeshShape(this.node);
        rbc = new RigidBodyControl(cs, 0);
        rbc.setKinematic(true);
        this.node.addControl(rbc);
        //bulletAppState.getPhysicsSpace().add(rbc);
        this.node.setLocalTranslation(loc);
        node.attachChild(this.node);
    }
    public void destroy(){
        node.removeControl(rbc);
        node.getParent().detachChild(node);
        World.getBulletAppState().getPhysicsSpace().remove(rbc);
        //app.getPlayerNode().detachChild(node);
    }
}