package datalayers;

import entities.Entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sql.Connector;

/**
 * The Day Entity Data Layer Class
 *
 * @author alexhughes
 */
public class DayDL extends DataLayer {

    public DayDL(Connector aConnector) {
        super(aConnector);
    }

    public DayDL(Connector aConnector, Entity anEntity) {
        super(aConnector, anEntity);
    }

    @Override
    public Entity fetchEntity() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Entity> fetchEntities(String aSorting) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Entity> searchEntity() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
}
