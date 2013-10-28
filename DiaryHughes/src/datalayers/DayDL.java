package datalayers;

import entities.Contact;
import entities.Day;
import entities.Entity;
import entities.Event;
import java.sql.*;
import java.util.ArrayList;
import sql.Connector;

/**
 * The Day Entity Data Layer Class
 *
 * @author alexhughes
 */
public class DayDL extends DataLayer {

    private EventDL eventDL;
    private ContactDL contactDL;

    public DayDL(Connector aConnector) {
        super(aConnector);
        eventDL = new EventDL(c);
        contactDL = new ContactDL(c);
    }

    public DayDL(Connector aConnector, Entity anEntity) {
        super(aConnector, anEntity);
        eventDL = new EventDL(c);
        contactDL = new ContactDL(c);
    }

    @Override
    public Entity fetchEntity() throws SQLException {
        checkID(e);

        Day d = (Day) e;

        String query = ""
                + "SELECT * "
                + "FROM day "
                + "WHERE dayID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, d.getDayID());

        ResultSet dayR = ps.executeQuery();
        e = resultSetToEntity(dayR).get(0);

        //getting correlated objects
        ArrayList<Contact> contactL = contactDL.getDayContacts(d.getDayID());
        ((Day) e).setContacts(contactL);

        ArrayList<Event> eventL = eventDL.getDayEvents(d.getDayID());
        ((Day) e).setEvents(eventL);

        return e;
    }

    /**
     * Fetches and returns a particular day by it's date.
     *
     * @param aDate
     * @return
     * @throws SQLException
     */
    public Day fetchDayByDate(Date aDate) throws SQLException {
        Day d;

        String query = ""
                + "SELECT * "
                + "FROM day "
                + "WHERE Date = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setDate(1, aDate);

        ResultSet dayR = ps.executeQuery();

        ArrayList<Entity> resultL = resultSetToEntity(dayR);
        if (resultL.isEmpty()) {
            d = null;
        } else {
            d = (Day) resultL.get(0);
        }

        if (d != null) {
            //getting correlated objects
            ArrayList<Contact> contactL = contactDL.getDayContacts(d.getDayID());
            d.setContacts(contactL);

            ArrayList<Event> eventL = eventDL.getDayEvents(d.getDayID());
            d.setEvents(eventL);
        }

        return d;
    }

    @Override
    public ArrayList<Entity> fetchEntities(String aSorting) throws SQLException {
        String query = ""
                + "SELECT * "
                + "FROM day "
                + "ORDER BY " + aSorting;

        ResultSet dayR = c.sendQuery(query);
        entities = resultSetToEntity(dayR);

        return entities;
    }

    @Override
    public ArrayList<Entity> searchEntity() throws SQLException {
        Day d = (Day) e;

        String query = ""
                + "SELECT * "
                + "FROM day d "
                + "INNER JOIN day_contact dc ON dc.dayID = d.dayID "
                + "INNER JOIN contact c ON c.contactID = dc.contactID "
                + "INNER JOIN event e ON e.dayID = d.dayID "
                + "WHERE 1=1 ";

        Contact con = d.getContacts().get(0);
        Event ev = d.getEvents().get(0);

        if (d.getDate() != null) {
            query += " AND d.Date LIKE '" + d.getDate() + "%' ";
        }

        if (d.getSummary() != null && !d.getSummary().equals("")) {
            query += " AND d.Summary LIKE '%" + d.getSummary() + "%' ";
        }

        if (d.getSex() != Entity.NIL) {
            query += " AND d.Sex = " + d.getSex();
        }

        if (d.getWork() != Entity.NIL) {
            query += " AND d.Work = " + d.getWork();
        }

        if (d.getFun() != Entity.NIL) {
            query += " AND d.Fun = " + d.getFun();
        }

        if (d.getSpecial() != Entity.NIL) {
            query += " AND d.Special = " + d.getSpecial();
        }

        if (d.getAlcohol() != Entity.NIL) {
            query += " AND d.Alcohol = " + d.getAlcohol();
        }

        if (d.getExpenses() != Entity.NIL) {
            double floor = d.getExpenses() - 10;
            double ceil = d.getExpenses() + 10;
            query += " AND d.Expenses BETWEEN " + floor + " AND " + ceil;
        }

        if (d.getDateCreated() != null) {
            query += " AND DateCreated LIKE '" + d.getDateCreated().toString() + "%' ";
        }

        if (d.getDateModified() != null) {
            query += " AND _dateModified LIKE '" + d.getDateModified().toString() + "%' ";
        }

        if (con.getName() != null && !con.getName().equals("")) {
            query += " AND c.Name LIKE '%" + con.getName() + "%' ";
        }

        if (con.getSurname() != null && !con.getSurname().equals("")) {
            query += " AND c.Surname LIKE '%" + con.getSurname() + "%' ";
        }

        if (con.getEmail() != null && !con.getEmail().equals("")) {
            query += " AND c.Email LIKE '%" + con.getEmail() + "%' ";
        }

        if (con.getPhone() != null && !con.getPhone().equals("")) {
            query += " AND c.Phone LIKE '%" + con.getPhone() + "%' ";
        }

        if (ev.getDesc() != null && !ev.getDesc().equals("")) {
            query += " AND e.Description LIKE '%" + ev.getDesc() + "%' ";
        }

        if (ev.getTime() != null) {
            query += " AND e.Time LIKE '" + ev.getTime().toString() + "%' ";
        }

        query += " GROUP BY d.dayID ";

        ResultSet dayR = c.sendQuery(query);
        entities = resultSetToEntity(dayR);

        return entities;
    }

    @Override
    public int insertEntity() throws SQLException {
        int id = Entity.NIL;
        Day d = (Day) e;

        String query = ""
                + "INSERT INTO day (Date, Summary, Sex, Work, Fun, Special, Alcohol, Practice, Expenses, DateCreated) VALUES "
                + "(?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) ";

        PreparedStatement ps = c.prepareStatement(query);

        ps.setDate(1, d.getDate());
        ps.setString(2, d.getSummary());
        ps.setInt(3, d.getSex());
        ps.setInt(4, d.getWork());
        ps.setInt(5, d.getFun());
        ps.setInt(6, d.getSpecial());
        ps.setInt(7, d.getAlcohol());
        ps.setInt(8, d.getPractice());
        ps.setDouble(9, d.getExpenses());

        ps.executeUpdate();

        ResultSet keyR = ps.getGeneratedKeys();
        while (keyR.next()) {
            id = keyR.getInt(1);
        }

        //inserting correlated objects
        if (d.getContacts() != null) {
            insertContacts(id, d.getContacts());
        }

//        if (d.getEvents() != null) {
//            insertEvents(id, d.getEvents());
//        }


        return id;
    }

    @Override
    public void updateEntity() throws SQLException {
        checkID(e);
        Day d = (Day) e;

        String query = ""
                + "UPDATE day SET "
                + "Date = ? ,"
                + "Summary = ? ,"
                + "Sex = ? ,"
                + "Work = ? ,"
                + "Fun = ? ,"
                + "Special = ? ,"
                + "Alcohol = ? ,"
                + "Practice = ?, "
                + "Expenses = ? "
                + "WHERE dayID = ? ";

        PreparedStatement ps = c.prepareStatement(query);

        ps.setDate(1, d.getDate());
        ps.setString(2, d.getSummary());
        ps.setInt(3, d.getSex());
        ps.setInt(4, d.getWork());
        ps.setInt(5, d.getFun());
        ps.setInt(6, d.getSpecial());
        ps.setInt(7, d.getAlcohol());
        ps.setInt(8, d.getPractice());
        ps.setDouble(9, d.getExpenses());
        ps.setInt(10, d.getDayID());

        ps.executeUpdate();

        //inserting correlated objects
        if (d.getContacts() != null) {
            insertContacts(d.getDayID(), d.getContacts());
        }
    }

    @Override
    public void deleteEntity() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected ArrayList<Entity> resultSetToEntity(ResultSet aR) throws SQLException {
        ArrayList<Entity> entityL = new ArrayList();
        Day d;

        while (aR.next()) {
            d = new Day(
                    aR.getInt("dayID"),
                    aR.getDate("Date"),
                    aR.getString("Summary"),
                    aR.getInt("Sex"),
                    aR.getInt("Work"),
                    aR.getInt("Fun"),
                    aR.getInt("Special"),
                    aR.getInt("Alcohol"),
                    aR.getInt("Practice"),
                    aR.getDouble("Expenses"),
                    aR.getTimestamp("DateCreated"),
                    aR.getTimestamp("_dateModified"));
            entityL.add(d);
        }
        return entityL;
    }

    public void insertContacts(int dayID, ArrayList<Contact> contacts) throws SQLException {
        String query = ""
                + "INSERT IGNORE INTO day_contact (dayID, contactID) VALUES "
                + "(?, ?) ";

        for (Contact con : contacts) {
            PreparedStatement ps = c.prepareStatement(query);

            ps.setInt(1, dayID);
            ps.setInt(2, con.getContactID());

            ps.executeUpdate();
        }
    }

    public void checkID(Entity entity) throws SQLException {
        Day d = (Day) entity;
        if (d.getDayID() == Entity.NIL) {
            throw new SQLException();
        }
    }
}
