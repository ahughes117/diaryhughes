
package entities;

import java.sql.Timestamp;

/**
 * The Category Entity Class
 * 
 * @author alexhughes
 */
public class Category extends Entity {
    
    private int categoryID = NIL;
    private String name;
    private String desc;
    private Timestamp dateCreated;
    private Timestamp dateModified;

    /**
     * Full Constructor for fetching categories
     * 
     * @param categoryID
     * @param name
     * @param desc
     * @param dateCreated
     * @param dateModified 
     */
    public Category(int categoryID, String name, String desc, Timestamp dateCreated, Timestamp dateModified) {
        this.categoryID = categoryID;
        this.name = name;
        this.desc = desc;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public Timestamp getDateModified() {
        return dateModified;
    } 
}
