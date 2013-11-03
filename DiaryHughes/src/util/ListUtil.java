package util;

import entities.*;
import java.util.ArrayList;
import javax.swing.JList;

/**
 * SQL ResultSet to JList utility
 *
 * @author alexhughes
 */
public class ListUtil {

    /**
     * Receives a JList object and populates it with items from an Entity List.
     * 
     * @param anEntityL
     * @param aList 
     */
    public static void fillList(ArrayList<Entity> anEntityL, JList aList) {
        ArrayList<String> items = new ArrayList();
        String[] itemArray = new String[anEntityL.size()];
        
        for (Entity ent : anEntityL) {
            //casting and adding items
            if (ent instanceof Contact) {
                Contact c = (Contact) ent;
                items.add(c.getContactID() + ", " + c.getName() + " " + c.getSurname());
            } else if (ent instanceof Event) {
                Event e = (Event) ent;
                items.add(e.getEventID() + ", " + e.getName());
            } else if (ent instanceof Tag) {
                Tag t = (Tag) ent;
                items.add(t.getTagID() + ", " + t.getName());
            }
        }

        //converting the ArrayList to a String Array
        for (int i = 0; i < items.size(); i++) {
            itemArray[i] = items.get(i);
        }
        
        //removing and then adding
        aList.removeAll();
        aList.setListData(itemArray);
    }
}
