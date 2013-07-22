package entities;

import java.sql.Timestamp;

/**
 * The Contact Entity Class
 *
 * @author alexhughes
 */
public class Contact extends Entity {

    private int contactID = NIL;
    private int dayID = NIL;
    private String name;
    private String surname;
    private String email;
    private String phone;
    private String picture;
    private String comments;
    private Timestamp dateCreated;
    private Timestamp dateModified;

    /**
     * Full Constructor for fetching contacts
     *
     * @param contactID
     * @param name
     * @param surname
     * @param email
     * @param phone
     * @param picture
     * @param comments
     * @param dateCreated
     * @param dateModified
     */
    public Contact(int contactID, String name, String surname, String email, String phone, String picture, String comments, Timestamp dateCreated, Timestamp dateModified) {
        this.contactID = contactID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.picture = picture;
        this.comments = comments;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    /**
     * Constructor for creating new contact
     *
     * @param name
     * @param surname
     * @param email
     * @param phone
     * @param picture
     * @param comments
     */
    public Contact(String name, String surname, String email, String phone, String picture, String comments) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.picture = picture;
        this.comments = comments;
    }

    public Contact() {
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public int getDayID() {
        return dayID;
    }

    public void setDayID(int dayID) {
        this.dayID = dayID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Timestamp getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Timestamp dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Timestamp getDateModified() {
        return dateModified;
    }

    public void setDateModified(Timestamp dateModified) {
        this.dateModified = dateModified;
    }
}
