package datalayers;

import entities.Entity;
import entities.Event;
import entities.Tag;
import java.sql.*;
import java.util.ArrayList;
import sql.Connector;

/**
 * The Event Entity Data Layer Class
 *
 * @author alexhughes
 */
public class EventDL extends DataLayer {

    private TagDL tagDL;
    private CategoryDL catDL;

    public EventDL(Connector aConnector) {
        super(aConnector);
        tagDL = new TagDL(c);
        catDL = new CategoryDL(c);
    }

    public EventDL(Connector aConnector, Entity anEntity) {
        super(aConnector, anEntity);
        tagDL = new TagDL(c, e);
        catDL = new CategoryDL(c, e);
    }

    @Override
    public Entity fetchEntity() throws SQLException {
        Event event = (Event) e;
        checkID(event);

        String query = ""
                + "SELECT * "
                + "FROM event "
                + "WHERE eventID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, event.getEventID());

        ResultSet eventR = ps.executeQuery();
        e = resultSetToEntity(eventR).get(0);

        //it is a very stupid way to do it, but is giving spaghetti code and headache otherwise
        ArrayList<Entity> tagEL = tagDL.getEventTags(event.getEventID());
        ArrayList<Tag> tagL = new ArrayList();

        for (Entity entity : tagEL) {
            tagL.add((Tag) entity);
        }

        ((Event) e).setTags(tagL);

        return e;
    }

    @Override
    public ArrayList<Entity> fetchEntities(String aSorting) throws SQLException {
        String query = ""
                + "SELECT * "
                + "FROM event "
                + "ORDER BY " + aSorting;

        ResultSet eventR = c.sendQuery(query);
        entities = resultSetToEntity(eventR);

        return entities;
    }

    @Override
    public ArrayList<Entity> searchEntity() throws SQLException {
        Event event = (Event) e;
        Tag tag = event.getTags().get(0);

        String query = ""
                + "SELECT * "
                + "FROM event e "
                + "INNER JOIN category c ON e.categoryID = c.categoryID "
                + "INNER JOIN event_tag et ON et.eventID = e.eventID "
                + "INNER JOIN tag t ON et.tagID = t.tagID "
                + "GROUP BY e.eventID "
                + "WHERE 1=1 ";

        if (event.getCategory().getName() != null && !event.getCategory().getName().equals("")) {
            query += " AND c.Name LIKE '%" + event.getCategory().getName() + "%' ";
        }

        if (event.getDesc() != null && !event.getDesc().equals("")) {
            query += " AND e.Desc LIKE '%" + event.getDesc() + "%' ";
        }

        if (event.getTime() != null) {
            query += " AND e.Time LIKE '" + event.getTime().toString() + "%' ";
        }

        if (event.getDateModified() != null) {
            query += " AND e._dateModified LIKE '" + event.getDateModified() + "%' ";
        }

        if (tag.getName() != null && !tag.getName().equals("")) {
            query += " AND t.Name LIKE '%" + tag.getName() + "%' ";
        }

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

    private void checkID(Entity anEntity) throws SQLException {
        throw new SQLException();
    }
}
