package view;

import database.dao.APTtypeDAO;
import database.dao.AppointmentDAO;
import database.dao.CounselorDAO;
import database.dao.PatientDAO;
import database.dao.impl.APTtypeDAOImpl;
import database.dao.impl.AppointmentDAOImpl;
import database.dao.impl.CounselorDAOImpl;
import database.dao.impl.PatientDAOImpl;
import exceptions.AptOverlappingException;
import exceptions.AptScheduleException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.APTType;
import model.Appointment;
import model.Counselor;
import model.Patient;

public class AppointmentPage extends javax.swing.JFrame {

    private DefaultComboBoxModel dcbmPatients, dcbmAptTypes, dcbmCounselors;
    private DefaultTableModel dtm;

    private List<Appointment> appointments;

    private static AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private static PatientDAO patientDAO = new PatientDAOImpl();
    private static APTtypeDAO aPTtypeDAO = new APTtypeDAOImpl();
    private static CounselorDAO counselorDAO = new CounselorDAOImpl();

    /**
     * Creates new form AppointmentPage
     */
    public AppointmentPage() {
        initComponents();
        this.dtm = (DefaultTableModel) tableAppointments.getModel();

        this.dcbmPatients = (DefaultComboBoxModel) cbxPatients.getModel();
        this.dcbmCounselors = (DefaultComboBoxModel) cbxCounselors.getModel();
        this.dcbmAptTypes = (DefaultComboBoxModel) cbxAptTypes.getModel();

        this.appointments = appointmentDAO.getAllAppointments();
        refreshTable();

        // popup combo boxes
        List<Patient> allPatients = patientDAO.getAllPatients();
        for (Patient pt : allPatients) {
            dcbmPatients.addElement(pt);
        }

        List<APTType> allAPTTypes = aPTtypeDAO.getAllAPTTypes();
        for (APTType apt : allAPTTypes) {
            dcbmAptTypes.addElement(apt);
        }

        List<Counselor> allCounselors = counselorDAO.getAllCounselors();
        for (Counselor cr : allCounselors) {
            dcbmCounselors.addElement(cr);
        }

    }

    private void clearTable() {
        dtm.getDataVector().removeAllElements();
        dtm.fireTableDataChanged();
    }

    private void refreshTable() {
        clearTable();
        for (Appointment apt : appointments) {
            String updatedAt = apt.getUpdatedAt() == null ? "-" : apt.getUpdatedAt().toString();
            String updatedBy = apt.getUpdatedBy() == null ? "-" : apt.getUpdatedBy();
            dtm.addRow(new Object[]{apt.getPatient().getName(), apt.getCounselor().getName(),
                apt.getType().getDescription(), apt.getNotes(), apt.getStartDateTime(), apt.getCreatedAt(),
                apt.getCreatedBy(), updatedAt, updatedBy});
        }
    }

    // Clear the text fields
    private void clearFields() {
        txtNotes.setText("");
    }

    /**
     * Checks the appointment date validity 
     * @param date the specified date
     * @throws AptScheduleException 
     */
    private void checkAppointmentDate(Date date) throws AptScheduleException {

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DATE);
        int year = c.get(Calendar.YEAR);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        int hourOfDay = c.get(Calendar.HOUR_OF_DAY);

        // Veterans Day (November 11th)
        if (day == 11 && month == Calendar.NOVEMBER) {
            throw new AptScheduleException("Appointment cannot be sheduled on Veterans Day (November 11th)");
        }

        // Martin Luther King, Jr. Day (3rd Monday of January)
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        c.set(Calendar.WEEK_OF_MONTH, 4);

        int x = c.get(Calendar.DAY_OF_MONTH);
        int y = c.get(Calendar.MONTH);
        if (day == x && y == month) {
            throw new AptScheduleException("Cannot be sheduled on Martin Luther King, Jr. Day (3rd Monday of January)");
        }
        
        c = Calendar.getInstance();
        c.setTime(date);
        
        // Memorial Day (last Monday of May)
        if (c.get(Calendar.MONTH) == Calendar.MAY
            && c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
            && c.get(Calendar.DAY_OF_MONTH) > (31 - 7) ) {
            throw new AptScheduleException("Cannot be sheduled on Memorial Day (last Monday of May)");
        }

        // Thanksgiving and the day after (4th Thursday of November and the day after)
        c.set(Calendar.MONTH, Calendar.NOVEMBER);
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
        c.set(Calendar.WEEK_OF_MONTH, 5);

        if ((day == c.get(Calendar.DAY_OF_MONTH) || day == c.get(Calendar.DAY_OF_MONTH) + 1) && c.get(Calendar.MONTH) == month) {
            throw new AptScheduleException("Cannot be sheduled on Thanksgiving and the day after");
        }

        // check for outside of business hours (Monday through Friday, 9AM to 9PM Eastern Standard Time)
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY || hourOfDay < 9 || hourOfDay > 21) {
            throw new AptScheduleException("Cannot be sheduled on outside of business hourshours (Monday through Friday, 9AM to 9PM)");
        }
    }

    /**
     * This method checks the overlapping appointments according to the new date
     * to be scheduled for the same counselor and patient Assume one checking up
     * will be happened for a 30 minutes
     *
     * @param date the new date to be scheduled
     * @throws AptOverlappingException
     */
    private void checkOverlapping(Patient pt, Counselor cr, Date date, boolean isUpdate) throws AptOverlappingException {

        Calendar c = Calendar.getInstance();
        // set the date to the calendar
        c.setTime(date);

        // check for patients
        c.add(Calendar.MINUTE, 30);
        Date next30MinTime = c.getTime();
        c.add(Calendar.HOUR, -1);
        Date prev30MinTime = c.getTime();

        // USED: lambda convert appiontments list to stream then filters the reords of list by checking 
        // the same patient between the 1 hr and 30 mins of date time
        // if no record fount from the list then it will return ZERO otherwise returns the number of records
        long count = appointments.stream() 
                .filter(apt -> apt.getPatient().equals(pt) && apt.getStartDateTime().before(next30MinTime)
                        && apt.getStartDateTime().after(prev30MinTime))
                .count();

        if (count > 0 && !isUpdate) {
            throw new AptOverlappingException("Cannot be sheduled overlapping appointments for the same patient");
        }

        // USED: lambda convert appiontments list to stream then filters the reords of list by checking 
        // the same Counselor between the 1 hr and 30 mins of date time
        // if no record fount from the list then it will return ZERO otherwise returns the number of records
        count = appointments.stream()
                .filter(apt -> apt.getCounselor().equals(cr) && apt.getStartDateTime().before(next30MinTime)
                        && apt.getStartDateTime().after(prev30MinTime))
                .count();

        if (count > 0 && !isUpdate) {
            throw new AptOverlappingException("Cannot be sheduled overlapping appointments for the same counselor");
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

        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAppointments = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtNotes = new javax.swing.JTextArea();
        cbxPatients = new javax.swing.JComboBox();
        cbxCounselors = new javax.swing.JComboBox();
        cbxAptTypes = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        txtStartTime = new javax.swing.JSpinner();
        btnViewPatient = new javax.swing.JButton();
        lblAlertMessage = new javax.swing.JLabel();

        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                windowClosingActionHandler(evt);
            }
        });

        jLabel4.setBackground(new java.awt.Color(32, 123, 198));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(102, 102, 102));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Manage Appointments");

        tableAppointments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Patient", "Counselor", "Type", "Notes", "Start Time", "Created At", "Created By", "Updated At", "Updated By"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableAppointments.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableAppointments.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableAppointmentsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableAppointments);
        if (tableAppointments.getColumnModel().getColumnCount() > 0) {
            tableAppointments.getColumnModel().getColumn(2).setPreferredWidth(50);
            tableAppointments.getColumnModel().getColumn(6).setPreferredWidth(50);
        }

        jLabel1.setText("Patient");

        btnAdd.setText("Add");
        btnAdd.setFocusPainted(false);
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.setFocusPainted(false);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/home.png"))); // NOI18N
        btnHome.setBorder(null);
        btnHome.setFocusPainted(false);
        btnHome.setOpaque(false);
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        jLabel2.setText("Counselor");

        jLabel3.setText("Type");

        jLabel5.setText("Notes");

        txtNotes.setColumns(20);
        txtNotes.setLineWrap(true);
        txtNotes.setRows(5);
        txtNotes.setWrapStyleWord(true);
        jScrollPane2.setViewportView(txtNotes);

        cbxPatients.setFocusable(false);

        cbxCounselors.setFocusable(false);

        cbxAptTypes.setFocusable(false);

        jLabel6.setText("Start Date Time");

        txtStartTime.setModel(new javax.swing.SpinnerDateModel(new java.util.Date(), null, null, java.util.Calendar.YEAR));
        txtStartTime.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        txtStartTime.setEditor(new javax.swing.JSpinner.DateEditor(txtStartTime, "MM/dd/yyyy hh:mm aa"));

        btnViewPatient.setText("View Patient");
        btnViewPatient.setFocusPainted(false);
        btnViewPatient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnViewPatientActionPerformed(evt);
            }
        });

        lblAlertMessage.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lblAlertMessage.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1090, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnViewPatient, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(364, 364, 364)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel3))
                                .addGap(56, 56, 56)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(cbxPatients, 0, 229, Short.MAX_VALUE)
                                    .addComponent(cbxCounselors, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cbxAptTypes, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(166, 166, 166)
                                .addComponent(jLabel5)
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(29, 29, 29)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txtStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(194, 194, 194))
                                    .addComponent(lblAlertMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addComponent(cbxPatients, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(cbxCounselors, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(cbxAptTypes, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtStartTime, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAlertMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnViewPatient))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed

        HomePage.getInstance().navigate();
        this.setVisible(false);
        PatientViewPage.getInstance().setVisible(false);
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String notes = txtNotes.getText().trim();
        Date date = (Date) txtStartTime.getValue();
        
        if (date.before(new Date())) {
            JOptionPane.showMessageDialog(this, "Cannot be scheduled for the past times");
            return;
        }

        try {
            checkAppointmentDate(date);
        } catch (AptScheduleException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
            return;
        }

        if (dcbmPatients.getSize() <= 0) {
            JOptionPane.showMessageDialog(this, "No patients in the system");
            return;
        }

        if (dcbmAptTypes.getSize() <= 0) {
            JOptionPane.showMessageDialog(this, "No appointment types in the system");
            return;
        }

        if (notes.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all the information");
        } else {

            APTType aptt = (APTType) dcbmAptTypes.getSelectedItem();
            Patient patient = (Patient) dcbmPatients.getSelectedItem();
            Counselor cr = (Counselor) dcbmCounselors.getSelectedItem();

            try {
                checkOverlapping(patient, cr, date, false);
            } catch (AptOverlappingException ex) {
                lblAlertMessage.setText(ex.getMessage());
                return;
            }

            Appointment appointment = new Appointment(appointmentDAO.getMaxAppointmentID() + 1,
                    patient, cr, aptt, notes, date);

            appointment.setCreatedAt(new Date());
            appointment.setCreatedBy(HomePage.getInstance().getUser().getName());
            appointments.add(appointment);
            refreshTable();
            appointmentDAO.addAppointment(appointment);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed

        int selectedRow = tableAppointments.getSelectedRow();

        if (selectedRow >= 0) {

            String notes = txtNotes.getText().trim();
            Date date = (Date) txtStartTime.getValue();

            if (date.before(new Date())) {
                JOptionPane.showMessageDialog(this, "Cannot be scheduled for the past times");
                return;
            }

            try {
                checkAppointmentDate(date);
            } catch (AptScheduleException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
                return;
            }

            if (dcbmPatients.getSize() <= 0) {
                JOptionPane.showMessageDialog(this, "No patients in the system");
                return;
            }

            if (dcbmAptTypes.getSize() <= 0) {
                JOptionPane.showMessageDialog(this, "No appointment types in the system");
                return;
            }

            if (notes.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter all the information");
            } else {

                APTType aptt = (APTType) dcbmAptTypes.getSelectedItem();
                Patient patient = (Patient) dcbmPatients.getSelectedItem();
                Counselor cr = (Counselor) dcbmCounselors.getSelectedItem();

                try {
                    checkOverlapping(patient, cr, date, true);
                } catch (AptOverlappingException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                    return;
                }

                Appointment appointment = appointments.get(selectedRow);

                appointment.setType(aptt);
                appointment.setCounselor(cr);
                appointment.setPatient(patient);
                appointment.setNotes(notes);
                appointment.setStartDateTime(date);

                appointment.setCreatedAt(new Date());
                appointment.setCreatedBy(HomePage.getInstance().getUser().getName());
                appointments.set(selectedRow, appointment);
                refreshTable();
                appointmentDAO.updateAppointment(appointment);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select one appointment to update");
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int selectedRow = tableAppointments.getSelectedRow();

        if (selectedRow >= 0) {
            Appointment app = appointments.get(selectedRow);
            appointments.remove(selectedRow);
            refreshTable();
            appointmentDAO.deleteAppointment(app.getId());
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, "Please select one appointment to delete");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void windowClosingActionHandler(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosingActionHandler
        PatientViewPage.getInstance().setVisible(false);
        HomePage.getInstance().navigate();
    }//GEN-LAST:event_windowClosingActionHandler

    private void tableAppointmentsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableAppointmentsMouseClicked
        int selectedRow = tableAppointments.getSelectedRow();

        Appointment app = appointments.get(selectedRow);
        txtNotes.setText(app.getNotes());
        txtStartTime.setValue(app.getStartDateTime());
        cbxAptTypes.setSelectedItem(app.getType());
        cbxCounselors.setSelectedItem(app.getCounselor());
        cbxPatients.setSelectedItem(app.getPatient());

    }//GEN-LAST:event_tableAppointmentsMouseClicked

    private void btnViewPatientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnViewPatientActionPerformed
        int selectedRow = tableAppointments.getSelectedRow();

        if (selectedRow >= 0) {
            Appointment app = appointments.get(selectedRow);
            PatientViewPage.getInstance().navigate(app.getPatient());
        } else {
            JOptionPane.showMessageDialog(this, "Please select one appointment to view the patient");
        }
    }//GEN-LAST:event_btnViewPatientActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton btnViewPatient;
    private javax.swing.JComboBox cbxAptTypes;
    private javax.swing.JComboBox cbxCounselors;
    private javax.swing.JComboBox cbxPatients;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAlertMessage;
    private javax.swing.JTable tableAppointments;
    private javax.swing.JTextArea txtNotes;
    private javax.swing.JSpinner txtStartTime;
    // End of variables declaration//GEN-END:variables
}
