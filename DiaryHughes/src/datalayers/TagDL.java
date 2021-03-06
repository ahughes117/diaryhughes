package datalayers;

import entities.Entity;
import entities.Event;
import entities.Tag;
import java.sql.*;
import java.util.ArrayList;
import sql.Connector;

/**
 * The Tag Entity Data Layer Class
 *
 * @author alexhughes
 */
public class TagDL extends DataLayer {

    public TagDL(Connector aConnector) {
        super(aConnector);
    }

    public TagDL(Connector aConnector, Entity anEntity) {
        super(aConnector, anEntity);
    }

    @Override
    public Entity fetchEntity() throws SQLException {
        Tag tag = (Tag) e;
        checkID(tag);

        String query = ""
                + "SELECT * "
                + "FROM tag "
                + "WHERE tagID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, tag.getTagID());

        ResultSet tagR = ps.executeQuery();
        e = resultSetToEntity(tagR).get(0);

        return e;
    }

    @Override
    public ArrayList<Entity> fetchEntities(String aSorting) throws SQLException {
        String query = ""
                + "SELECT * "
                + "FROM tag "
                + "ORDER BY " + aSorting;

        ResultSet tagR = c.sendQuery(query);
        entities = resultSetToEntity(tagR);

        return entities;
    }

    @Override
    public ResultSet fetchEntitiesR(String aSorting) throws SQLException {
        String query = ""
                + "SELECT * "
                + "FROM tag "
                + "ORDER BY " + aSorting;

        ResultSet tagR = c.sendQuery(query);
        return tagR;
    }

    @Override
    public ArrayList<Entity> searchEntity() throws SQLException {
        ResultSet tagR = c.sendQuery(buildSearchQuery());
        entities = resultSetToEntity(tagR);

        return entities;
    }
    
    @Override
    public ResultSet searchEntityR() throws SQLException {
        ResultSet tagR = c.sendQuery(buildSearchQuery());
        return tagR;
    }
    
    @Override
    protected String buildSearchQuery() {
        Tag tag = (Tag) e;

        String query = ""
                + "SELECT * "
                + "FROM tag "
                + "WHERE 1=1 ";

        if (tag.getName() != null && !tag.getName().equals("")) {
            query += " AND Name LIKE '%" + tag.getName() + "%' ";
        }

        if (tag.getDateModified() != null) {
            query += " AND _dateModified LIKE '" + tag.getDateModified() + "%' ";
        }
        return query;
    }

    @Override
    public int insertEntity() throws SQLException {
        int id = Entity.NIL;
        Tag tag = (Tag) e;

        String query = ""
                + "INSERT INTO tag (Name) VALUES "
                + "(?)";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, tag.getName().toLowerCase());

        ps.executeUpdate();

        ResultSet keyR = ps.getGeneratedKeys();
        while (keyR.next()) {
            id = keyR.getInt(1);
        }

        return id;
    }

    @Override
    public void updateEntity() throws SQLException {
        Tag tag = (Tag) e;
        checkID(tag);

        String query = ""
                + "UPDATE tag "
                + "SET Name = ? "
                + "WHERE tagID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, tag.getName());
        ps.setInt(2, tag.getTagID());

        ps.executeUpdate();
    }

    @Override
    public void deleteEntity() throws SQLException {
        Tag tag = (Tag) e;
        checkID(tag);

        String query = ""
                + "DELETE "
                + "FROM tag "
                + "WHERE tagID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, tag.getTagID());

        ps.executeUpdate();
    }

    @Override
    protected ArrayList<Entity> resultSetToEntity(ResultSet aR) throws SQLException {
        ArrayList<Entity> entityL = new ArrayList();
        Tag tag;

        while (aR.next()) {
            tag = new Tag(
                    aR.getInt("tagID"),
                    aR.getString("Name"),
                    aR.getTimestamp("_dateModified"));

            entityL.add(tag);
        }
        return entityL;
    }

    /**
     * Checks whether a tag already exists in the database
     *
     * @return
     * @throws SQLException
     */
    public boolean exists() throws SQLException {
        boolean exists = false;
        Tag t = (Tag) e;

        String query = ""
                + "SELECT count(*) "
                + "FROM tag "
                + "WHERE Name LIKE ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, t.getName());

        ResultSet resultR = ps.executeQuery();
        while (resultR.next()) {
            if (resultR.getInt(1) != 0) {
                exists = true;
            }
        }

        return exists;
    }

    /**
     * Returns the events correlated to a tag
     *
     * @param aTagID
     * @return
     * @throws SQLException
     */
    public ArrayList<Entity> getTagEvents(int aTagID) throws SQLException {
        ArrayList<Entity> events = new ArrayList();
        Event event;

        String query = ""
                + "SELECT e.* "
                + "FROM event e "
                + "INNER JOIN event_tag et ON et.eventID = e.eventID "
                + "WHERE et.tagID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, aTagID);

        ResultSet resultR = ps.executeQuery();
        while (resultR.next()) {
            event = new Event();
            event.setEventID(resultR.getInt("eventID"));
            event.setName(resultR.getString("Name"));
            events.add(event);
        }

        return events;
    }

    /**
     * Returns the tags of a particular event
     *
     * @param anEventID
     * @return
     * @throws SQLException
     */
    public ArrayList<Tag> getEventTags(int anEventID) throws SQLException {
        ArrayList<Tag> tags = new ArrayList();
        Tag tag;

        String query = ""
                + "SELECT * "
                + "FROM tag t "
                + "INNER JOIN event_tag et ON et.tagID = t.tagID "
                + "WHERE et.eventID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, anEventID);

        ResultSet tagR = ps.executeQuery();
        entities = resultSetToEntity(tagR);

        //stupid but the only easy way
        for (Entity ent : entities) {
            tags.add((Tag) ent);
        }

        return tags;
    }

    private void checkID(Tag aTag) throws SQLException {
        if (aTag.getTagID() == Entity.NIL) {
            throw new SQLException();
        }
    }
}
