package gui;

import datalayers.CategoryDL;
import entities.Category;
import static gui.GUI.NIL;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import sql.Connector;
import util.MesDial;

/**
 * Category Frame. CRUD for Category Entity
 *
 * @author alexhughes
 */
public class CategoryFrame extends GUI {

    private static boolean instanceAlive = false;
    private Category cat;
    private CategoryDL catDL;

    /**
     * Creates new form CategoryFrame
     */
    public CategoryFrame(GUI aPFrame, Connector aConnector, int anID) {
        super(aPFrame, aConnector, anID);
        instanceAlive = true;

        initComponents();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                shutdown();
            }
        });

        if (anID != NIL) {
            try {
                loadCategory();
            } catch (SQLException ex) {
                MesDial.conError(this);
                Logger.getLogger(CategoryFrame.class.getName()).log(Level.SEVERE, null, ex);
                shutdown();
            }
            existing = true;
        } else {
            existing = false;
        }

        super.setFrameLocationCenter();
        this.setVisible(true);
    }

    private Category parseCategory() {
        cat = new Category();
        return cat;
    }

    private void loadCategory() throws SQLException {
    }

    private void save() throws SQLException {
    }

    public static boolean isInstanceAlive() {
        return instanceAlive;
    }

    @Override
    protected void shutdown() {
        instanceAlive = false;
        super.shutdown();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
