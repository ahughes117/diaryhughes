
package entities;

/**
 * The MetaTranslation Entity Class
 * 
 * @author alexhughes
 */
public class MetaTranslation extends Entity {
    
    private String db;
    private String english;
    private String greek;

    /**
     * Constructor for fetching MetaTranslation objects
     * 
     * @param db
     * @param english
     * @param greek 
     */
    public MetaTranslation(String db, String english, String greek) {
        this.db = db;
        this.english = english;
        this.greek = greek;
    }

    public String getDb() {
        return db;
    }

    public String getEnglish() {
        return english;
    }

    public String getGreek() {
        return greek;
    }
}
