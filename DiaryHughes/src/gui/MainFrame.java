package gui;

import datalayers.DayDL;
import entities.Contact;
import entities.Day;
import entities.Entity;
import static gui.GUI.NIL;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sql.Connector;
import util.ListParser;
import util.MesDial;
import util.StrVal;

/**
 * The Main Frame of the application
 *
 * @author alexhughes
 */
public class MainFrame extends GUI {

    private Day d;
    private DayDL dDL;

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
            expensesF.setText(decFormat.format(d.getExpenses()));

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

    private void save() throws SQLException {

        if (parseDay()) {
            dDL = new DayDL(c, d);
            if (!existing) {
                id = dDL.insertEntity();
                existing = true;
            } else {
                dDL.updateEntity();
            }
            MesDial.saveSuccess(this);
        }

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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
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
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        saveBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        quitBtn = new javax.swing.JButton();
        clearBtn1 = new javax.swing.JButton();
        clearBtn2 = new javax.swing.JButton();
        clearBtn3 = new javax.swing.JButton();
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Today", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Days", jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Events", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Contacts", jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Tags", jPanel5);

        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        clearBtn.setText("Refresh");

        quitBtn.setText("Quit");
        quitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitBtnActionPerformed(evt);
            }
        });

        clearBtn1.setText("Edit");

        clearBtn2.setText("New");

        clearBtn3.setText("Delete");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clearBtn2, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clearBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clearBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clearBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(clearBtn)
                    .addComponent(quitBtn)
                    .addComponent(clearBtn1)
                    .addComponent(clearBtn2)
                    .addComponent(clearBtn3))
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
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1)
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
            save();
        } catch (SQLException ex) {
            MesDial.conError(this);
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_saveBtnActionPerformed

    private void newEvBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newEvBtnActionPerformed
        if (!EventFrame.isInstanceAlive()) {
            new EventFrame(this, c, NIL, id);
        }
    }//GEN-LAST:event_newEvBtnActionPerformed

    private void newConBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newConBtnActionPerformed
        if (!ContactFrame.isInstanceAlive()) {
            new ContactFrame(this, c, NIL);
        }
    }//GEN-LAST:event_newConBtnActionPerformed

    private void editEvBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editEvBtnActionPerformed
        if (eventL.getSelectedValue() != null) {
            try {
                int id = StrVal.parseIdFromString((String) eventL.getSelectedValue());
                if (EventFrame.isInstanceAlive()) {
                    new EventFrame(this, c, id, id);
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
                int id = StrVal.parseIdFromString((String) contactL.getSelectedValue());
                if (EventFrame.isInstanceAlive()) {
                    new ContactFrame(this, c, id);
                }
            } catch (Exception x) {
                MesDial.conError(this);
                Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, x);
            }
        }
    }//GEN-LAST:event_editConBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox alcoholChk;
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton clearBtn1;
    private javax.swing.JButton clearBtn2;
    private javax.swing.JButton clearBtn3;
    private javax.swing.JList contactL;
    private javax.swing.JLabel dateL;
    private javax.swing.JButton deleteConBtn;
    private javax.swing.JButton deleteEvBtn;
    private javax.swing.JButton editConBtn;
    private javax.swing.JButton editEvBtn;
    private javax.swing.JList eventL;
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
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton newConBtn;
    private javax.swing.JButton newEvBtn;
    private javax.swing.JCheckBox practiceChk;
    private javax.swing.JButton quitBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JCheckBox sexChk;
    private javax.swing.JCheckBox specialChk;
    private javax.swing.JLabel statusL;
    private javax.swing.JTextArea summaryArea;
    private javax.swing.JCheckBox workChk;
    // End of variables declaration//GEN-END:variables
}
