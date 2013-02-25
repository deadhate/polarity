package sin;

import com.jme3.app.Application;
import com.jme3.bullet.BulletAppState;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.system.AppSettings;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import sin.appstates.GameplayState;
import sin.appstates.MenuState;
import sin.hud.HUD;
import sin.input.InputHandler;
import sin.network.Networking;
import sin.character.Character;
import sin.character.PlayerManager;
import sin.tools.T;
import sin.weapons.RecoilManager;
import sin.weapons.Weapons;

/**
Copyright (c) 2003-2011 jMonkeyEngine
All rights reserved.
 
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:
 
Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
 
Redistributions in binary form must reproduce the above copyright
notice, this list of conditions and the following disclaimer in the
documentation and/or other materials provided with the distribution.
 
Neither the name of 'jMonkeyEngine' nor the names of its contributors 
may be used to endorse or promote products derived from this software 
without specific prior written permission.
 
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 * Game Client
 * @author SinisteRing
 */

public class GameClient extends Application{
    // --- Global Constant Variables --- //
    private static final boolean MODE_DEBUG = false;            // Debug Mode
    private static final String CLIENT_VERSION = "ALPHA 0.06";   // Client Version (Important for client-server connections)
    
    // Important System Variables:
    private static final Logger logger = Logger.getLogger(GameClient.class.getName());
    private static GameClient app;                  // The application itself (this).
    // App States:
    private static BulletAppState bulletAppState;   // Physics State.
    private static GameplayState gameplayState;     // Gameplay State.
    private static MenuState menuState;             // Main Menu State.
    
    // Nodes:
    private Node rootNode = new Node("Root Node");   // Node encompassing all visual elements.
    private Node guiNode = new Node("Gui Node");     // Node encompassing all GUI elements.
    
    // Getters for Nodes:
    public Node getRoot(){
        return rootNode;
    }
    public Node getGUI(){
        return guiNode;
    }
    // Gameplay State Nodes:
    public Node getCollisionNode(){
        return gameplayState.getCollisionNode();
    }
    public Node getMiscNode(){
        return gameplayState.getMiscNode();
    }
    public Node getSingleNode(){
        return gameplayState.getSingleNode();
    }
    public Node getPlayerNode(){
        return gameplayState.getPlayerNode();
    }
    public Node getProjectileNode(){
        return gameplayState.getProjectileNode();
    }
    public Node getTerrain(){
        return gameplayState.getTerrainNode();
    }
    public Node getTracerNode(){
        return gameplayState.getTracerNode();
    }
    
    // Getters for States:
    public GameplayState getGameplayState(){
        return gameplayState;
    }
    public MenuState getMenuState(){
        return menuState;
    }
    public BulletAppState getBulletAppState(){
        return bulletAppState;
    }
    
    // Getters for helper classes:
    public HUD getHUD(){
        return gameplayState.getHUD();
    }
    public Character getCharacter(){
        return gameplayState.getCharacter();
    }
    public AppSettings getSettings(){
        return settings;
    }
    public String getVersion(){
        return CLIENT_VERSION;
    }
    
    // Main:
    @Override
    public void start() {
        super.start();
    }
    public static void main(String[] args) throws IOException {
        // Tone down loggers severely:
        Logger.getLogger("com.jme3").setLevel(Level.WARNING);
        if(MODE_DEBUG){
            try {
                logger.addHandler(new FileHandler("FPSlog.xml"));
                Logger.getLogger("com.jme3").addHandler(new FileHandler("FPSlog2.xml"));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(GameClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        app = new GameClient();
        AppSettings set = new AppSettings(true);
        set.setResolution(1280, 720);
        set.setSamples(0);
        set.setVSync(false);
        set.setRenderer(AppSettings.LWJGL_OPENGL1);
        set.setTitle("Polarity");
        app.setSettings(set);
        app.start();
        
    }

    // Initialization:
    @Override
    public void initialize(){
        super.initialize();

        guiNode.setQueueBucket(Bucket.Gui);
        guiNode.setCullHint(CullHint.Never);
        viewPort.attachScene(rootNode);
        guiViewPort.attachScene(guiNode);
        
        // Initialize keybinds & Tools.
        T.initialize(app);
        InputHandler.initialize(app, context);
        
        // Initialize new HUD & remove debug HUD elements:
        Networking.initialize(app);
        RecoilManager.initialize(app);
        Weapons.initialize(app);
        PlayerManager.initialize(app);
        //Character.initialize(app);
        viewPort.setBackgroundColor(ColorRGBA.Black);
        setPauseOnLostFocus(false);
        
        // Initialize App States:
        bulletAppState = new BulletAppState();  
        gameplayState = new GameplayState();
        menuState = new MenuState();
        // Attach App States:
        stateManager.attach(bulletAppState);
        stateManager.attach(menuState);
        
        bulletAppState.getPhysicsSpace().setAccuracy(0.01f);
    }
    
    // Update:
    @Override
    public void update() {
        super.update(); // makes sure to execute AppTasks
        if (speed == 0 || paused) {
            return;
        }

        float tpf = timer.getTimePerFrame() * speed;
        // update states
        stateManager.update(tpf);
        
        // Update logical and geometric states:
        rootNode.updateLogicalState(tpf);
        guiNode.updateLogicalState(tpf);
        rootNode.updateGeometricState();
        guiNode.updateGeometricState();

        // Render display:
        stateManager.render(renderManager);
        renderManager.render(tpf, context.isRenderable());
        stateManager.postRender();
    }
    
    @Override
    public void destroy(){
        if(Networking.isConnected()){
            Networking.close();
        }
        super.destroy();
    }
}
