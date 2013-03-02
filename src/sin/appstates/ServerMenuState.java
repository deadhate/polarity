package sin.appstates;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.logging.Level;
import java.util.logging.Logger;
import sin.GameServer;

/**
 *
 * @author SinisteRing
 */
public class ServerMenuState extends AbstractAppState implements ScreenController{
    public static GameServer app;
    
    private Nifty nifty;
    private Screen screen;
    
    public void action(String action){
        // Main Menu:
        if(action.equals("start")){
            app.getStateManager().attach(app.getListenState());
            nifty.gotoScreen("empty");
        }else if(action.equals("quit")){
            app.stop();
        }
    }
    
    @Override
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        ServerMenuState.app = (GameServer) app;
        
        //Turn off the super annoying loggers:
        Logger.getLogger("de.lessvoid.nifty").setLevel(Level.SEVERE);
        Logger.getLogger("NiftyInputEventHandlingLog").setLevel(Level.SEVERE);
        Logger.getLogger("NiftyEventBusLog").setLevel(Level.SEVERE);
        Logger.getAnonymousLogger().getParent().setLevel(Level.SEVERE);
        Logger.getLogger("de.lessvoid.nifty.*").setLevel(Level.SEVERE);
        
        // Initialize nifty display:
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                ServerMenuState.app.getAssetManager(), ServerMenuState.app.getInputManager(),
                ServerMenuState.app.getAudioRenderer(), ServerMenuState.app.getGuiViewPort());
        this.nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/ServerGUI.xml", "menu");
        ServerMenuState.app.getGuiViewPort().addProcessor(niftyDisplay);
    }
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        this.screen = screen;
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void onStartScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void onEndScreen() {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}