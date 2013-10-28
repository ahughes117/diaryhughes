package datalayers;

import entities.Contact;
import entities.Entity;
import java.sql.*;
import java.util.ArrayList;
import sql.Connector;

/**
 * The Contact Entity Data Layer Class
 *
 * @author alexhughes
 */
public class ContactDL extends DataLayer {

    public ContactDL(Connector aConnector) {
        super(aConnector);
    }

    public ContactDL(Connector aConnector, Entity anEntity) {
        super(aConnector, anEntity);
    }

    @Override
    public Entity fetchEntity() throws SQLException {
        Contact contact = (Contact) e;

        checkID(contact);

        String query = ""
                + "SELECT * "
                + "FROM contact "
                + "WHERE contactID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, contact.getContactID());

        ResultSet contactR = ps.executeQuery();
        e = resultSetToEntity(contactR).get(0);

        return e;
    }

    @Override
    public ArrayList<Entity> fetchEntities(String aSorting) throws SQLException {
        String query = ""
                + "SELECT * "
                + "FROM contact "
                + "ORDER BY " + aSorting;

        ResultSet contactR = c.sendQuery(query);
        entities = resultSetToEntity(contactR);

        return entities;
    }

    public ArrayList<Contact> getDayContacts(int aDayID) throws SQLException {
        ArrayList<Contact> contactL = new ArrayList();
        Contact con = new Contact();
        con.setDayID(aDayID);
        e = con;

        ArrayList<Entity> entityL = searchEntity();

        //stupid but the only easy way to do it
        for (Entity ent : entityL) {
            contactL.add((Contact) ent);
        }

        return contactL;
    }

    @Override
    public ArrayList<Entity> searchEntity() throws SQLException {
        Contact con = (Contact) e;

        String query = ""
                + "SELECT c.* "
                + "FROM contact c "
                + "LEFT JOIN day_contact dc ON dc.contactID = c.contactID "
                + "WHERE 1=1 ";

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

        if (con.getPicture() != null && !con.getPicture().equals("")) {
            query += " AND c.Picture LIKE '%" + con.getPicture() + "%' ";
        }

        if (con.getComments() != null && !con.getComments().equals("")) {
            query += " AND c.Comments LIKE '%" + con.getComments() + "%' ";
        }

        if (con.getDayID() != Entity.NIL) {
            query += " AND dc.dayID = " + con.getDayID();
        }

        ResultSet contactR = c.sendQuery(query);
        entities = resultSetToEntity(contactR);

        return entities;
    }

    @Override
    public int insertEntity() throws SQLException {
        int id = Entity.NIL;
        Contact con = (Contact) e;

        String query = ""
                + "INSERT INTO contact (Name, Surname, Email, Phone, Picture, Comments, DateCreated) VALUES "
                + "(?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) ";

        PreparedStatement ps = c.prepareStatement(query);

        ps.setString(1, con.getName());
        ps.setString(2, con.getSurname());
        ps.setString(3, con.getEmail());
        ps.setString(4, con.getPhone());
        ps.setString(5, con.getPicture());
        ps.setString(6, con.getComments());

        ps.executeUpdate();

        ResultSet keyR = ps.getGeneratedKeys();
        while (keyR.next()) {
            id = keyR.getInt(1);
        }

        return id;
    }

    @Override
    public void updateEntity() throws SQLException {
        Contact con = (Contact) e;
        checkID(con);

        String query = ""
                + "UPDATE contact "
                + "SET Name = ?, Surname = ?, Email = ?, Phone = ?, Picture = ?, Comments = ? "
                + "WHERE contactID = ? ";

        PreparedStatement ps = c.prepareStatement(query);

        ps.setString(1, con.getName());
        ps.setString(2, con.getSurname());
        ps.setString(3, con.getEmail());
        ps.setString(4, con.getPhone());
        ps.setString(5, con.getPicture());
        ps.setString(6, con.getComments());
        ps.setInt(7, con.getContactID());

        ps.executeUpdate();
    }

    @Override
    public void deleteEntity() throws SQLException {
        Contact con = (Contact) e;
        checkID(con);

        String query = ""
                + "DELETE "
                + "FROM contact "
                + "WHERE contactID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, con.getContactID());

        ps.executeUpdate();
    }

    @Override
    protected ArrayList<Entity> resultSetToEntity(ResultSet aR) throws SQLException {
        ArrayList<Entity> entityL = new ArrayList();
        Contact con;

        while (aR.next()) {
            con = new Contact(
                    aR.getInt("contactID"),
                    aR.getString("Name"),
                    aR.getString("Surname"),
                    aR.getString("Email"),
                    aR.getString("Phone"),
                    aR.getString("Picture"),
                    aR.getString("Comments"),
                    aR.getTimestamp("DateCreated"),
                    aR.getTimestamp("_dateModified"));
            entityL.add(con);
        }
        return entityL;
    }

    private void checkID(Contact aContact) throws SQLException {
        if (aContact.getContactID() == Entity.NIL) {
            throw new SQLException();
        }
    }
}
