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

        //getting correlated objects
        ArrayList<Tag> tagL = tagDL.getEventTags(event.getEventID());

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
    public ResultSet fetchEntitiesR(String aSorting) throws SQLException {
        String query = ""
                + "SELECT * "
                + "FROM event "
                + "ORDER BY " + aSorting;

        ResultSet eventR = c.sendQuery(query);

        return eventR;
    }

    @Override
    public ArrayList<Entity> searchEntity() throws SQLException {
        ResultSet eventR = c.sendQuery(buildSearchQuery());
        entities = resultSetToEntity(eventR);

        return entities;
    }
    
    @Override
    public ResultSet searchEntityR() throws SQLException {
        ResultSet eventR = c.sendQuery(buildSearchQuery());
        return eventR;
    }

    @Override
    protected String buildSearchQuery() {
        Event event = (Event) e;
        Tag tag;

        if (event.getTags() != null && !event.getTags().isEmpty()) {
            tag = event.getTags().get(0);
        } else {
            tag = new Tag();
        }

        String query = ""
                + "SELECT * "
                + "FROM event e "
                + "INNER JOIN category c ON e.categoryID = c.categoryID "
                + "INNER JOIN event_tag et ON et.eventID = e.eventID "
                + "INNER JOIN tag t ON et.tagID = t.tagID "
                + "WHERE 1=1 ";

        if (event.getCategory() != null && event.getCategory().getName() != null
                && !event.getCategory().getName().equals("")) {
            query += " AND c.Name LIKE '%" + event.getCategory().getName() + "%' ";
        }

        if (event.getDesc() != null && !event.getDesc().equals("")) {
            query += " AND e.`Desc` LIKE '%" + event.getDesc() + "%' ";
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

        query += " GROUP BY e.eventID ";

        return query;
    }

    @Override
    public int insertEntity() throws SQLException {
        int id = Entity.NIL;
        Event ev = (Event) e;

        String query = ""
                + "INSERT INTO event (dayID, categoryID, Name, `Desc`, Time, Picture, DateCreated) VALUES "
                + "(?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP) ";

        PreparedStatement ps = c.prepareStatement(query);

        ps.setInt(1, ev.getDayID());
        ps.setInt(2, ev.getCategoryID());
        ps.setString(3, ev.getName());
        ps.setString(4, ev.getDesc());
        ps.setTime(5, ev.getTime());
        ps.setString(6, ev.getPicture());

        ps.executeUpdate();

        ResultSet keyR = ps.getGeneratedKeys();
        while (keyR.next()) {
            id = keyR.getInt(1);
        }

        //inserting tags
        insertTags(id, ev.getTags());

        return id;
    }

    @Override
    public void updateEntity() throws SQLException {
        checkID(e);
        Event ev = (Event) e;

        String query = ""
                + "UPDATE event SET "
                + "dayID = ? ,"
                + "categoryID = ? ,"
                + "Name = ? ,"
                + "`Desc` = ? ,"
                + "Time = ? ,"
                + "Picture = ? "
                + "WHERE eventID = ? ";

        PreparedStatement ps = c.prepareStatement(query);

        ps.setInt(1, ev.getDayID());
        ps.setInt(2, ev.getCategoryID());
        ps.setString(3, ev.getName());
        ps.setString(4, ev.getDesc());
        ps.setTime(5, ev.getTime());
        ps.setString(6, ev.getPicture());
        ps.setInt(7, ev.getEventID());

        ps.executeUpdate();

        //inserting tags
        insertTags(ev.getEventID(), ev.getTags());
    }

    @Override
    public void deleteEntity() throws SQLException {
        checkID(e);
        Event ev = (Event) e;

        String query = ""
                + "DELETE "
                + "FROM event "
                + "WHERE eventID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, ev.getEventID());

        ps.executeUpdate();
    }

    @Override
    protected ArrayList<Entity> resultSetToEntity(ResultSet aR) throws SQLException {
        ArrayList<Entity> entityL = new ArrayList();
        Event ev;

        while (aR.next()) {
            ev = new Event(
                    aR.getInt("eventID"),
                    aR.getInt("dayID"),
                    aR.getInt("categoryID"),
                    aR.getString("Name"),
                    aR.getString("`Desc`"),
                    aR.getTime("Time"),
                    aR.getString("Picture"),
                    aR.getTimestamp("DateCreated"),
                    aR.getTimestamp("_dateModified"));
            entityL.add(ev);
        }
        return entityL;
    }

    public void insertTags(int eventID, ArrayList<Tag> tags) throws SQLException {
        String query = ""
                + "INSERT IGNORE INTO event_tag (eventID, tagID) VALUES "
                + "(?, ?) ";

        for (Tag t : tags) {
            PreparedStatement ps = c.prepareStatement(query);

            ps.setInt(1, eventID);
            ps.setInt(2, t.getTagID());

            ps.executeUpdate();
        }
    }

    public ArrayList<Event> getDayEvents(int aDayID) throws SQLException {
        ArrayList<Event> eventL = new ArrayList();
        Event ev = new Event();
        ev.setDayID(aDayID);
        e = ev;

        ArrayList<Entity> entityL = searchEntity();

        //stupid but the only easy way to do it
        for (Entity ent : entityL) {
            eventL.add((Event) ent);
        }

        return eventL;
    }

    private void checkID(Entity anEntity) throws SQLException {
        Event ev = (Event) anEntity;
        if (ev.getEventID() == Entity.NIL) {
            throw new SQLException();
        }
    }
}
