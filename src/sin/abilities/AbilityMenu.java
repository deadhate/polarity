package sin.abilities;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import java.util.ArrayList;

/**
 *
 * @author SinisteRing
 */
public class AbilityMenu {
    private Node node = new Node("AbilityMenu");
    private ArrayList<AbilityEntry> entries = new ArrayList(1);
    
    public AbilityMenu(Vector3f trans){
        node.setLocalTranslation(trans);
    }
    
    public Node getNode(){
        return node;
    }
    
    public void addAbility(Ability abil){
        AbilityEntry entry = new AbilityEntry(abil);
        entry.setLocalTranslation(new Vector3f(0, -entries.size(), 0));
        node.attachChild(entry.getNode());
        entries.add(entry);
    }
}
