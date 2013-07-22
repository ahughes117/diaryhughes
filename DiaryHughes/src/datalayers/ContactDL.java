package datalayers;

import entities.Contact;
import entities.Entity;
import java.sql.*;
import java.util.ArrayList;

/**
 * The Contact Entity Data Layer Class
 *
 * @author alexhughes
 */
public class ContactDL extends DataLayer {

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

    public ArrayList<Entity> getDayContacts(int aDayID) throws SQLException {
        Contact con = (Contact) e;
        con.setDayID(aDayID);

        return searchEntity();
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

    private void checkID(Contact aContact) throws SQLException {
        if (aContact.getContactID() == Entity.NIL) {
            throw new SQLException();
        }
    }
}
