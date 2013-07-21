
package entities;

import java.sql.Timestamp;

/**
 * The Tag Entity Class
 * 
 * @author alexhughes
 */
public class Tag {
    
    private int tagID;
    private String name;
    private Timestamp dateModified;

    /**
     * Constructor for fetching tags
     * 
     * @param tagID 
     * @param name
     * @param dateModified 
     */
    public Tag(int tagID, String name, Timestamp dateModified) {
        this.tagID = tagID;
        this.name = name;
        this.dateModified = dateModified;
    }

    /**
     * Constructor for creating new tags
     * 
     * @param name 
     */
    public Tag(String name) {
        this.name = name;
    }

    public int getTagID() {
        return tagID;
    }

    public void setTagID(int tagID) {
        this.tagID = tagID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getDateModified() {
        return dateModified;
    }

    public void setDateModified(Timestamp dateModified) {
        this.dateModified = dateModified;
    }
    
}
