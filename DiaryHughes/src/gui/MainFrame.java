package gui;

import datalayers.*;
import entities.Day;
import static gui.GUI.NIL;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import sql.Connector;
import util.MesDial;
import util.StrVal;
import util.TableParser;

/**
 * The Main Frame of the application
 *
 * @author alexhughes
 */
public class MainFrame extends GUI {

    private Day d;
    private DayDL dDL;
    private EventDL eDL;
    private ContactDL cDL;
    private TagDL tDL;
    private CategoryDL catDL;

    /**
     * Creates new form MainFrame
     */
    public MainFrame(GUI aPFrame, Connector aConnector) {
        super(aPFrame, aConnector, NIL);

        initComponents();
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                shutdown();
            }
        });
        try {
            loadDay();
        } catch (SQLException ex) {
            MesDial.conError(this);
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception x) {
            MesDial.programError(this);
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, x);
        }

        super.setFrameLocationCenter();
        this.setVisible(true);
    }

    private void clear() {
        dateL.setText("Date: ");
        summaryArea.setText("");
        expensesF.setText("");

        sexChk.setSelected(false);
        workChk.setSelected(false);
        funChk.setSelected(false);
        specialChk.setSelected(false);
        alcoholChk.setSelected(false);
        practiceChk.setSelected(false);
        practiceChk.setVisible(false);

        eventL.removeAll();
        contactL.removeAll();
    }

    private boolean parseDay() {
        boolean parsingSuccessful = true;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        d = new Day();
        if (existing) {
            d.setDayID(id);
        }

        try {
            d.setDate(StrVal.dateParser(dateFormat.format(new java.util.Date())));
        } catch (Exception ex) {
            parsingSuccessful = false;
            MesDial.dateError(this);
            Logger.getLogger(DayFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            if (!expensesF.getText().equals("")) {
                d.setExpenses(Double.valueOf(expensesF.getText()));
            }
        } catch (Exception ex) {
            parsingSuccessful = false;
            MesDial.doubleError(this);
            Logger.getLogger(DayFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        d.setSummary(summaryArea.getText());

        if (sexChk.isSelected()) {
            d.setSex(1);
        } else {
            d.setSex(0);
        }

        if (workChk.isSelected()) {
            d.setWork(1);
        } else {
            d.setWork(0);
        }

        if (funChk.isSelected()) {
            d.setFun(1);
        } else {
            d.setFun(0);
        }

        if (specialChk.isSelected()) {
            d.setSpecial(1);
        } else {
            d.setSpecial(0);
        }

        if (alcoholChk.isSelected()) {
            d.setAlcohol(1);
        } else {
            d.setAlcohol(0);
        }

        if (practiceChk.isSelected()) {
            d.setPractice(1);
        } else {
            d.setPractice(0);
        }

        return parsingSuccessful;
    }

    private void loadDay() throws SQLException, Exception {
        dDL = new DayDL(c);

        DecimalFormat decFormat = new DecimalFormat("#.##");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        java.util.Date date = new java.util.Date();
        java.sql.Date sqlDate = StrVal.dateParser(dateFormat.format(date));

        //fetching day details
        d = dDL.fetchDayByDate(sqlDate);

        //first clearing fields
        clear();

        //setting values on fields and check boxes
        dateL.setText(dateL.getText() + dateFormat.format(date));
        statusL.setText(date.toString());

        if (d != null) {
            existing = true;
            id = d.getDayID();

            summaryArea.setText(d.getSummary());
            if (d.getExpenses() != -1) {
                expensesF.setText(decFormat.format(d.getExpenses()));
            }

            if (d.getSex() == 1) {
                sexChk.setSelected(true);
            }

            if (d.getWork() == 1) {
                workChk.setSelected(true);
            }

            if (d.getFun() == 1) {
                funChk.setSelected(true);
            }

            if (d.getSpecial() == 1) {
                specialChk.setSelected(true);
            }

            if (d.getAlcohol() == 1) {
                alcoholChk.setSelected(true);
            }
        } else {
            existing = false;
        }
    }

    private void save(boolean confirmationDialog) throws SQLException {

        if (parseDay()) {
            dDL = new DayDL(c, d);
            if (!existing) {
                id = dDL.insertEntity();
                existing = true;
            } else {
                dDL.updateEntity();
            }
            if (confirmationDialog) {
                MesDial.saveSuccess(this);
            }
        }
    }

    private void loadDayTable() throws SQLException {
        dDL = new DayDL(c);
        TableParser.fillTable(null, dayTable);
    }

    protected void shutdown() {
        try {
            c.closeConnection();
            pFrame.setVisible(true);
            this.dispose();
        } catch (SQLException ex) {
            Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Loads the JTable of each tab
     *
     * @param anEntity
     */
    private void refresh(String anEntity) {
        if (anEntity.equals("day")) {
            dDL = new DayDL(c);
            try {
                TableParser.fillTable(dDL.fetchEntitiesR("Date DESC"), dayTable);
            } catch (SQLException ex) {
                MesDial.conError(this);
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (anEntity.equals("event")) {
            eDL = new EventDL(c);
            try {
                TableParser.fillTable(eDL.fetchEntitiesR("_dateModified DESC"), eventTable);
            } catch (SQLException ex) {
                MesDial.conError(this);
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (anEntity.equals("contact")) {
            cDL = new ContactDL(c);
            try {
                TableParser.fillTable(cDL.fetchEntitiesR("Name ASC"), contactTable);
            } catch (SQLException ex) {
                MesDial.conError(this);
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (anEntity.equals("category")) {
            catDL = new CategoryDL(c);
            try {
                TableParser.fillTable(catDL.fetchEntitiesR("Name ASC"), categoryTable);
            } catch (SQLException ex) {
                MesDial.conError(this);
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (anEntity.equals("tag")) {
            tDL = new TagDL(c);
            try {
                TableParser.fillTable(tDL.fetchEntitiesR("Name ASC"), tagTable);
            } catch (SQLException ex) {
                MesDial.conError(this);
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Checks and returns the id of a selected row. Returns -1 if anything goes
     * wrong
     *
     * @param aTable
     * @return
     */
    private int checkRowsAndGetID(JTable aTable) {
        int anID = -1;

        if (aTable.getSelectedRowCount() == 0) {
            MesDial.noRowSelected(this);
        } else if (aTable.getSelectedRowCount() > 1) {
            MesDial.multipleRowsSelected(this);
        } else {
            try {
                anID = Integer.parseInt((String) ((DefaultTableModel) aTable.getModel()).getValueAt(aTable.getSelectedRow(), 0));
            } catch (Exception x) {
                MesDial.programError(this);
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, x);
            }
        }
        return anID;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabbedPane = new javax.swing.JTabbedPane();
        todayPanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        eventL = new javax.swing.JList();
        newEvBtn = new javax.swing.JButton();
        editEvBtn = new javax.swing.JButton();
        deleteEvBtn = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        contactL = new javax.swing.JList();
        newConBtn = new javax.swing.JButton();
        editConBtn = new javax.swing.JButton();
        deleteConBtn = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        dateL = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        summaryArea = new javax.swing.JTextArea();
        sexChk = new javax.swing.JCheckBox();
        workChk = new javax.swing.JCheckBox();
        funChk = new javax.swing.JCheckBox();
        specialChk = new javax.swing.JCheckBox();
        alcoholChk = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        expensesF = new javax.swing.JTextField();
        practiceChk = new javax.swing.JCheckBox();
        dayPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        dayTable = new javax.swing.JTable();
        eventPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        eventTable = new javax.swing.JTable();
        contactPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        contactTable = new javax.swing.JTable();
        tagPanel = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tagTable = new javax.swing.JTable();
        categoryPanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        categoryTable = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        saveBtn = new javax.swing.JButton();
        refreshBtn = new javax.swing.JButton();
        quitBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        newBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        statusL = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("DiaryHughes");

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Events"));

        eventL.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(eventL);

        newEvBtn.setText("New");
        newEvBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newEvBtnActionPerformed(evt);
            }
        });

        editEvBtn.setText("Edit");
        editEvBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editEvBtnActionPerformed(evt);
            }
        });

        deleteEvBtn.setText("Delete");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(newEvBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editEvBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteEvBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newEvBtn)
                    .addComponent(editEvBtn)
                    .addComponent(deleteEvBtn)))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Contacts"));

        jScrollPane3.setViewportView(contactL);

        newConBtn.setText("New");
        newConBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newConBtnActionPerformed(evt);
            }
        });

        editConBtn.setText("Edit");
        editConBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editConBtnActionPerformed(evt);
            }
        });

        deleteConBtn.setText("Delete");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(newConBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editConBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deleteConBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(newConBtn)
                    .addComponent(editConBtn)
                    .addComponent(deleteConBtn)))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Details"));

        dateL.setText("Date: ");

        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("Summary"));

        summaryArea.setColumns(20);
        summaryArea.setLineWrap(true);
        summaryArea.setRows(5);
        summaryArea.setWrapStyleWord(true);
        jScrollPane1.setViewportView(summaryArea);

        sexChk.setText("Sex");

        workChk.setText("Work");

        funChk.setText("Fun");

        specialChk.setText("Special");

        alcoholChk.setText("Alcohol");

        jLabel2.setText("Expenses:");

        practiceChk.setText("Practice");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(expensesF))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateL)
                            .addComponent(funChk)
                            .addComponent(specialChk)
                            .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sexChk)
                                    .addComponent(workChk))
                                .addGap(40, 40, 40)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(practiceChk)
                                    .addComponent(alcoholChk))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dateL)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sexChk)
                    .addComponent(alcoholChk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(workChk)
                    .addComponent(practiceChk))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(funChk)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(specialChk)
                .addGap(60, 60, 60)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(expensesF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37))
        );

        javax.swing.GroupLayout todayPanelLayout = new javax.swing.GroupLayout(todayPanel);
        todayPanel.setLayout(todayPanelLayout);
        todayPanelLayout.setHorizontalGroup(
            todayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, todayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(todayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        todayPanelLayout.setVerticalGroup(
            todayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(todayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(todayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(todayPanelLayout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tabbedPane.addTab("Today", todayPanel);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Days"));

        dayTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane4.setViewportView(dayTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout dayPanelLayout = new javax.swing.GroupLayout(dayPanel);
        dayPanel.setLayout(dayPanelLayout);
        dayPanelLayout.setHorizontalGroup(
            dayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        dayPanelLayout.setVerticalGroup(
            dayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Days", dayPanel);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Events"));

        eventTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane5.setViewportView(eventTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout eventPanelLayout = new javax.swing.GroupLayout(eventPanel);
        eventPanel.setLayout(eventPanelLayout);
        eventPanelLayout.setHorizontalGroup(
            eventPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eventPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        eventPanelLayout.setVerticalGroup(
            eventPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(eventPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Events", eventPanel);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Contacts"));

        contactTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane6.setViewportView(contactTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout contactPanelLayout = new javax.swing.GroupLayout(contactPanel);
        contactPanel.setLayout(contactPanelLayout);
        contactPanelLayout.setHorizontalGroup(
            contactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contactPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        contactPanelLayout.setVerticalGroup(
            contactPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(contactPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        tabbedPane.addTab("Contacts", contactPanel);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Tags"));

        tagTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane7.setViewportView(tagTable);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout tagPanelLayout = new javax.swing.GroupLayout(tagPanel);
        tagPanel.setLayout(tagPanelLayout);
        tagPanelLayout.setHorizontalGroup(
            tagPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tagPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        tagPanelLayout.setVerticalGroup(
            tagPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tagPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Tags", tagPanel);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Categories"));

        categoryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane8.setViewportView(categoryTable);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 489, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout categoryPanelLayout = new javax.swing.GroupLayout(categoryPanel);
        categoryPanel.setLayout(categoryPanelLayout);
        categoryPanelLayout.setHorizontalGroup(
            categoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        categoryPanelLayout.setVerticalGroup(
            categoryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(categoryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabbedPane.addTab("Categories", categoryPanel);

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        refreshBtn.setText("Refresh");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        quitBtn.setText("Quit");
        quitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitBtnActionPerformed(evt);
            }
        });

        editBtn.setText("Edit");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        newBtn.setText("New");
        newBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("Delete");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(newBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(refreshBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(quitBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveBtn)
                    .addComponent(refreshBtn)
                    .addComponent(quitBtn)
                    .addComponent(editBtn)
                    .addComponent(newBtn)
                    .addComponent(deleteBtn))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel10.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        statusL.setText("jLabel1");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusL)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(statusL)
                .addGap(0, 4, Short.MAX_VALUE))
        );

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tabbedPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tabbedPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void quitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitBtnActionPerformed
        shutdown();
    }//GEN-LAST:event_quitBtnActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        try {
            save(true);
        } catch (SQLException ex) {
            MesDial.conError(this);
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void newEvBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newEvBtnActionPerformed
        try {
            save(false);
            if (!EventFrame.isInstanceAlive()) {
                new EventFrame(this, c, NIL, id);
            }
        } catch (SQLException ex) {
            MesDial.conError(this);
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_newEvBtnActionPerformed

    private void newConBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newConBtnActionPerformed
        try {
            save(false);
            if (!ContactFrame.isInstanceAlive()) {
                new ContactFrame(this, c, NIL);
            }
        } catch (SQLException ex) {
            MesDial.conError(this);
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_newConBtnActionPerformed

    private void editEvBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEvBtnActionPerformed
        if (eventL.getSelectedValue() != null) {
            try {
                int eventID = StrVal.parseIdFromString((String) eventL.getSelectedValue());
                if (!EventFrame.isInstanceAlive()) {
                    new EventFrame(this, c, id, eventID);
                }
            } catch (Exception x) {
                MesDial.conError(this);
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, x);
            }
        }
    }//GEN-LAST:event_editEvBtnActionPerformed

    private void editConBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editConBtnActionPerformed
        if (contactL.getSelectedValue() != null) {
            try {
                int contactID = StrVal.parseIdFromString((String) contactL.getSelectedValue());
                if (!EventFrame.isInstanceAlive()) {
                    new ContactFrame(this, c, contactID);
                }
            } catch (Exception x) {
                MesDial.conError(this);
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, x);
            }
        }
    }//GEN-LAST:event_editConBtnActionPerformed

    private void newBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newBtnActionPerformed

        if (tabbedPane.getSelectedIndex() == 1) {
            if (!DayFrame.isInstanceAlive()) {
                new DayFrame(this, c, NIL);
            }
        } else if (tabbedPane.getSelectedIndex() == 2) {
        } else if (tabbedPane.getSelectedIndex() == 3) {
            if (!ContactFrame.isInstanceAlive()) {
                new ContactFrame(this, c, NIL);
            }
        } else if (tabbedPane.getSelectedIndex() == 4) {
            if (!TagFrame.isInstanceAlive()) {
                new TagFrame(this, c, NIL);
            }
        } else if (tabbedPane.getSelectedIndex() == 5) {
            if (!CategoryFrame.isInstanceAlive()) {
                new CategoryFrame(this, c, NIL);
            }
        }

    }//GEN-LAST:event_newBtnActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        if (tabbedPane.getSelectedIndex() == 1) {
            refresh("day");
        } else if (tabbedPane.getSelectedIndex() == 2) {
            refresh("event");
        } else if (tabbedPane.getSelectedIndex() == 3) {
            refresh("contact");
        } else if (tabbedPane.getSelectedIndex() == 4) {
            refresh("tag");
        } else if (tabbedPane.getSelectedIndex() == 5) {
            refresh("category");
        }
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        int entityID;
        if (tabbedPane.getSelectedIndex() == 1) {
            entityID = checkRowsAndGetID(dayTable);
            if (entityID != -1 && !DayFrame.isInstanceAlive()) {
                new DayFrame(this, c, entityID);
            }
        } else if (tabbedPane.getSelectedIndex() == 2) {
            entityID = checkRowsAndGetID(eventTable);
            if (entityID != -1 && !EventFrame.isInstanceAlive()) {
                new EventFrame(this, c, id, entityID);
            }
        } else if (tabbedPane.getSelectedIndex() == 3) {
            entityID = checkRowsAndGetID(contactTable);
            if (entityID != -1 && !ContactFrame.isInstanceAlive()) {
                new ContactFrame(this, c, entityID);
            }
        } else if (tabbedPane.getSelectedIndex() == 4) {
            entityID = checkRowsAndGetID(tagTable);
            if (entityID != -1 && !TagFrame.isInstanceAlive()) {
                new TagFrame(this, c, entityID);
            }
        } else if (tabbedPane.getSelectedIndex() == 5) {
            entityID = checkRowsAndGetID(categoryTable);
            if (entityID != -1 && !CategoryFrame.isInstanceAlive()) {
                new CategoryFrame(this, c, entityID);
            }
        }
    }//GEN-LAST:event_editBtnActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox alcoholChk;
    private javax.swing.JPanel categoryPanel;
    private javax.swing.JTable categoryTable;
    private javax.swing.JList contactL;
    private javax.swing.JPanel contactPanel;
    private javax.swing.JTable contactTable;
    private javax.swing.JLabel dateL;
    private javax.swing.JPanel dayPanel;
    private javax.swing.JTable dayTable;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton deleteConBtn;
    private javax.swing.JButton deleteEvBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton editConBtn;
    private javax.swing.JButton editEvBtn;
    private javax.swing.JList eventL;
    private javax.swing.JPanel eventPanel;
    private javax.swing.JTable eventTable;
    private javax.swing.JTextField expensesF;
    private javax.swing.JCheckBox funChk;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JButton newBtn;
    private javax.swing.JButton newConBtn;
    private javax.swing.JButton newEvBtn;
    private javax.swing.JCheckBox practiceChk;
    private javax.swing.JButton quitBtn;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JCheckBox sexChk;
    private javax.swing.JCheckBox specialChk;
    private javax.swing.JLabel statusL;
    private javax.swing.JTextArea summaryArea;
    private javax.swing.JTabbedPane tabbedPane;
    private javax.swing.JPanel tagPanel;
    private javax.swing.JTable tagTable;
    private javax.swing.JPanel todayPanel;
    private javax.swing.JCheckBox workChk;
    // End of variables declaration//GEN-END:variables
}
