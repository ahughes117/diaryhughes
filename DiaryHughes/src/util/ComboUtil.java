
package util;

import entities.*;
import java.util.ArrayList;
import javax.swing.JComboBox;

/**
 * SQL ResultSet to JCombo utility
 * 
 * @author alexhughes
 */
public class ComboUtil {
    
    /**
     * Receives a JCombo object and populates it with items from an Entity List.
     * 
     * @param anEntityL
     * @param aCombo 
     */
    public static void fillCombo(ArrayList<Entity> anEntityL, JComboBox aCombo) {
        ArrayList<String> items = new ArrayList();
                
        for(Entity ent: anEntityL) {
            //casting and adding items
            if(ent instanceof Category) {
                Category cat = (Category)ent;
                items.add(cat.getCategoryID() + ", " + cat.getName());
            }
        }
        
        //converting the ArrayList to a String Array
        for(int i=0; i<items.size(); i++) {
            aCombo.addItem((String)items.get(i));
        }
    }
    
}
