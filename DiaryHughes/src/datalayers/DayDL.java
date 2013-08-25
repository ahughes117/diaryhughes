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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateEntity() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteEntity() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected ArrayList<Entity> resultSetToEntity(ResultSet aR) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void checkID(Entity entity) throws SQLException {
        Day d = (Day) entity;
        if (d.getDayID() == Entity.NIL) {
            throw new SQLException();
        }
    }
}
