package datalayers;

import entities.Category;
import entities.Entity;
import java.sql.*;
import java.util.ArrayList;
import sql.Connector;

/**
 * The Category Entity Data Layer Class
 *
 * @author alexhughes
 */
public class CategoryDL extends DataLayer {

    public CategoryDL(Connector aConnector) {
        super(aConnector);
    }

    public CategoryDL(Connector aConnector, Entity anEntity) {
        super(aConnector, anEntity);
    }

    @Override
    public Entity fetchEntity() throws SQLException {
        Category cat = (Category) e;

        checkID(cat);

        String query = ""
                + "SELECT * "
                + "FROM category "
                + "WHERE categoryID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, cat.getCategoryID());

        ResultSet catR = ps.executeQuery();
        e = resultSetToEntity(catR).get(0);

        return e;
    }

    @Override
    public ArrayList<Entity> fetchEntities(String aSorting) throws SQLException {
        String query = ""
                + "SELECT * "
                + "FROM category "
                + "ORDER BY " + aSorting;

        ResultSet catR = c.sendQuery(query);
        entities = resultSetToEntity(catR);

        return entities;
    }

    @Override
    public ArrayList<Entity> searchEntity() throws SQLException {
        Category cat = (Category) e;

        String query = ""
                + "SELECT * "
                + "FROM category "
                + "WHERE 1=1 ";

        if (cat.getName() != null && !cat.getName().equals("")) {
            query += " AND Name LIKE '%" + cat.getName() + "%' ";
        }

        if (cat.getDesc() != null && !cat.getDesc().equals("")) {
            query += " AND Desc LIKE '%" + cat.getDesc() + "%' ";
        }

        if (cat.getDateCreated() != null) {
            query += " AND DateCreated LIKE '" + cat.getDateCreated() + "%' ";
        }

        if (cat.getDateModified() != null) {
            query += " AND _dateModified LIKE '" + cat.getDateModified() + "%' ";
        }

        ResultSet catR = c.sendQuery(query);
        entities = resultSetToEntity(catR);

        return entities;
    }

    @Override
    public int insertEntity() throws SQLException {
        int id = Entity.NIL;
        Category cat = (Category) e;

        String query = ""
                + "INSERT INTO category (Name, Desc, DateCreated) VALUES "
                + "(?, ?, CURRENT_TIMESTAMP) ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, cat.getName());
        ps.setString(2, cat.getDesc());

        ps.executeUpdate();

        ResultSet keyR = ps.getGeneratedKeys();
        while (keyR.next()) {
            id = keyR.getInt(1);
        }

        return id;
    }

    @Override
    public void updateEntity() throws SQLException {
        Category cat = (Category) e;

        checkID(cat);

        String query = ""
                + "UPDATE category "
                + "SET Name = ?, Desc = ? "
                + "WHERE categoryID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setString(1, cat.getName());
        ps.setString(2, cat.getDesc());
        ps.setInt(3, cat.getCategoryID());

        ps.executeUpdate();
    }

    @Override
    public void deleteEntity() throws SQLException {
        Category cat = (Category) e;

        checkID(cat);

        String query = ""
                + "DELETE "
                + "FROM category "
                + "WHERE categoryID = ? ";

        PreparedStatement ps = c.prepareStatement(query);
        ps.setInt(1, cat.getCategoryID());

        ps.executeUpdate();
    }

    @Override
    protected ArrayList<Entity> resultSetToEntity(ResultSet aR) throws SQLException {
        Category cat;

        while (aR.next()) {
            cat = new Category(
                    aR.getInt("categoryID"),
                    aR.getString("Name"),
                    aR.getString("Desc"),
                    aR.getTimestamp("DateCreated"),
                    aR.getTimestamp("_dateModified"));

            entities.add(cat);
        }
        return entities;
    }

    /**
     * Mini Helper Function that throws SQLException if the programmer forgot to
     * pass the category id
     */
    private void checkID(Category aCat) throws SQLException {
        if (aCat.getCategoryID() == Entity.NIL) {
            throw new SQLException();
        }
    }
}
