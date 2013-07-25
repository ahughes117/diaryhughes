package datalayers;

import entities.Entity;
import entities.MetaTranslation;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import sql.Connector;

/**
 * The MetaTranslation Data Layer Class
 *
 * @author alexhughes
 */
public class MetaTranslationDL extends DataLayer {

    public MetaTranslationDL(Connector aConnector) {
        super(aConnector);
    }

    @Override
    public Entity fetchEntity() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Entity> fetchEntities(String aSorting) throws SQLException {
        MetaTranslation mt = (MetaTranslation) e;

        String query = ""
                + "SELECT * "
                + "FROM MetaTranslation ";

        ResultSet metaR = c.sendQuery(query);
        entities = resultSetToEntity(metaR);

        return entities;
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
        MetaTranslation mt;

        while (aR.next()) {
            mt = new MetaTranslation(
                    aR.getString("DB"),
                    aR.getString("English"),
                    aR.getString("Greek"));

            entities.add(mt);
        }
        return entities;
    }
}
