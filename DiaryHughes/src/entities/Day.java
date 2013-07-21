
package entities;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * The Day Entity Class
 * 
 * @author alexhughes
 */
public class Day {
    
    private int dayID;
    private Date date;
    private String summary;
    private int sex;
    private int work;
    private int fun;
    private int special;
    private int alcohol;
    private double expenses;
    private ArrayList<Event> events;
    private ArrayList<Contact> contacts;
    private Timestamp dateCreated;
    private Timestamp dateModified;

    /**
     * Full Constructor for fetching a day
     * 
     * @param dayID
     * @param date
     * @param summary
     * @param sex
     * @param work
     * @param fun
     * @param special
     * @param alcohol
     * @param expenses
     * @param dateCreated
     * @param dateModified 
     */
    public Day(int dayID, Date date, String summary, int sex, int work, int fun, int special, int alcohol, double expenses, Timestamp dateCreated, Timestamp dateModified) {
        this.dayID = dayID;
        this.date = date;
        this.summary = summary;
        this.sex = sex;
        this.work = work;
        this.fun = fun;
        this.special = special;
        this.alcohol = alcohol;
        this.expenses = expenses;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    /**
     * Constructor for creating a new Day
     * 
     * @param date
     * @param summary
     * @param sex
     * @param work
     * @param fun
     * @param special
     * @param alcohol
     * @param expenses 
     */
    public Day(Date date, String summary, int sex, int work, int fun, int special, int alcohol, double expenses) {
        this.date = date;
        this.summary = summary;
        this.sex = sex;
        this.work = work;
        this.fun = fun;
        this.special = special;
        this.alcohol = alcohol;
        this.expenses = expenses;
    }

    /**
     * Blank Constructor
     */
    public Day() {
    }

    public int getDayID() {
        return dayID;
    }

    public void setDayID(int dayID) {
        this.dayID = dayID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getWork() {
        return work;
    }

    public void setWork(int work) {
        this.work = work;
    }

    public int getFun() {
        return fun;
    }

    public void setFun(int fun) {
        this.fun = fun;
    }

    public int getSpecial() {
        return special;
    }

    public void setSpecial(int special) {
        this.special = special;
    }

    public int getAlcohol() {
        return alcohol;
    }

    public void setAlcohol(int alcohol) {
        this.alcohol = alcohol;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
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

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}
