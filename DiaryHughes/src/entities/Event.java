
package entities;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * The Event Entity Class
 * 
 * @author alexhughes
 */
public class Event extends Entity {
    
    private int eventID = NIL;
    private int dayID = NIL;
    private int categoryID = NIL;
    private Category category;
    private String desc;
    private Time time;
    private String picture;
    private ArrayList<Tag> tags;
    private Timestamp dateModified;

    /**
     * Constructor for fetching events
     * 
     * @param eventID
     * @param dayID
     * @param categoryID
     * @param desc
     * @param time
     * @param picture
     * @param dateModified 
     */
    public Event(int eventID, int dayID, int categoryID, String desc, Time time, String picture, Timestamp dateModified) {
        this.eventID = eventID;
        this.dayID = dayID;
        this.categoryID = categoryID;
        this.desc = desc;
        this.time = time;
        this.picture = picture;
        this.dateModified = dateModified;
    }

    /**
     * Constructor for creating a new Event
     * 
     * @param dayID
     * @param categoryID
     * @param desc
     * @param time
     * @param picture 
     */
    public Event(int dayID, int categoryID, String desc, Time time, String picture) {
        this.dayID = dayID;
        this.categoryID = categoryID;
        this.desc = desc;
        this.time = time;
        this.picture = picture;
    }
    
    /**
     * Empty Constructor
     */
    public Event() {
        
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getDayID() {
        return dayID;
    }

    public void setDayID(int dayID) {
        this.dayID = dayID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Timestamp getDateModified() {
        return dateModified;
    }

    public void setDateModified(Timestamp dateModified) {
        this.dateModified = dateModified;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }
}
