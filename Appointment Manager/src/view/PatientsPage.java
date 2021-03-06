package view;

import database.dao.AddressDAO;
import database.dao.AppointmentDAO;
import database.dao.PatientDAO;
import database.dao.impl.AddressDAOImpl;
import database.dao.impl.AppointmentDAOImpl;
import database.dao.impl.PatientDAOImpl;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Address;
import model.Appointment;
import model.Patient;

public class PatientsPage extends javax.swing.JFrame {

    // table model for patients table
    private DefaultTableModel dtm;

    private List<Patient> patients;
    private List<Appointment> appointments;

    // DAOs to access data in the database
    private static PatientDAO patientDAO = new PatientDAOImpl();
    private static AddressDAO addressDAO = new AddressDAOImpl();
    private static AppointmentDAO appointmentDAO = new AppointmentDAOImpl();

    /**
     * Creates new form PatientsPage
     */
    public PatientsPage() {
        initComponents();
        this.dtm = (DefaultTableModel) tablePatients.getModel();
        this.patients = patientDAO.getAllPatients();
        this.appointments = appointmentDAO.getAllAppointments();
        refreshTable();

    }

    // clear the jtable
    private void clearTable() {
        dtm.getDataVector().removeAllElements();
        dtm.fireTableDataChanged();
    }

    private boolean hasAppointment(Patient p) {
        // USED: lambda
        if (appointments.stream().anyMatch((appointment) -> (appointment.getPatient().getId() == p.getId()))) {
            return true;
        }
        return false;
    }

    // Refresh the table
    private void refreshTable() {
        clearTable();
        // USED: lambda
        patients.stream().forEach((p) -> {
            Address addr = p.getAddress();
            String updatedAt = p.getUpdatedAt() == null ? "-" : p.getUpdatedAt().toString();
            String updatedBy = p.getUpdatedBy() == null ? "-" : p.getUpdatedBy();
            dtm.addRow(new Object[]{p.getName(), addr.getPhone(), p.getInsuranceProvider(),
                addr.getLine1(), addr.getLine2(), addr.getCity(), addr.getState(), addr.getPostalCode(),
                p.getCreatedAt(), p.getCreatedBy(), updatedAt, updatedBy});
        });
    }

    // Clear all the text fields
    private void clearTexts() {
        txtName.setText("");
        txtPhone.setText("");
        txtInsPr.setText("");
        txtAddrLine1.setText("");
        txtAddrLine2.setText("");
        txtCity.setText("");
        txtState.setText("");
        txtPostalCode.setText("");
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
        tablePatients = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtPhone = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtAddrLine1 = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtAddrLine2 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtCity = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtState = new javax.swing.JTextField();
        txtInsPr = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtPostalCode = new javax.swing.JTextField();
        btnClear = new javax.swing.JButton();

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
        jLabel4.setText("Manage Patients ");

        tablePatients.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Phone", "Insuarance Pr.", "Address Line1", "Address Line2", "City", "State", "Postal Code", "Created At", "Created By", "Updated At", "Updated By"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablePatients.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePatients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablePatientsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablePatients);

        jLabel1.setText("Name");

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

        jLabel2.setText("Phone");

        jLabel3.setText("Address Line 1");

        jLabel5.setText("Address Line 2");

        jLabel6.setText("City");

        jLabel7.setText("State");

        jLabel8.setText("Insuarance Provider");

        jLabel9.setText("Postal Code");

        btnClear.setText("Clear");
        btnClear.setFocusPainted(false);
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1099, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnAdd, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(18, 18, 18)
                                        .addComponent(txtInsPr, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(130, 130, 130)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel3)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(txtAddrLine1, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel5)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtAddrLine2, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtPostalCode, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)))))
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
                            .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtAddrLine1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtAddrLine2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(txtInsPr, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtState, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtPostalCode, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnClear))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
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
    }//GEN-LAST:event_btnHomeActionPerformed

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        String name = txtName.getText().trim();
        String insPr = txtInsPr.getText().trim();
        String addLine1 = txtAddrLine1.getText().trim();
        String addLine2 = txtAddrLine2.getText().trim();
        String city = txtCity.getText().trim();
        String state = txtState.getText().trim();
        String postalCode = txtPostalCode.getText().trim();
        String phone = txtPhone.getText().trim();

        if (name.isEmpty() || addLine1.isEmpty() || insPr.isEmpty() || city.isEmpty()
                || state.isEmpty() || postalCode.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter missing information");
        } else {

            Address addr = new Address(addressDAO.getMaxAddressID() + 1, addLine1, addLine2, city, state, postalCode, phone);
            addr.setCreatedAt(new Date());
            addr.setCreatedBy(HomePage.getInstance().getUser().getName());

            Patient patient = new Patient(patientDAO.getMaxPatientID() + 1, name, addr, insPr);
            patient.setCreatedAt(new Date());
            patient.setCreatedBy(HomePage.getInstance().getUser().getName());

            patients.add(patient);
            refreshTable();
            addressDAO.addAddress(addr);
            patientDAO.addPatient(patient);
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed

        int selectedRow = tablePatients.getSelectedRow();

        if (selectedRow >= 0) {
            String name = txtName.getText().trim();
            String insPr = txtInsPr.getText().trim();
            String addLine1 = txtAddrLine1.getText().trim();
            String addLine2 = txtAddrLine2.getText().trim();
            String city = txtCity.getText().trim();
            String state = txtState.getText().trim();
            String postalCode = txtPostalCode.getText().trim();
            String phone = txtPhone.getText().trim();

            if (name.isEmpty() || addLine1.isEmpty() || insPr.isEmpty() || city.isEmpty()
                    || state.isEmpty() || postalCode.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter missing information");
            } else {
                Patient patient = patients.get(selectedRow);
                Address addr = patient.getAddress();
                addr.setLine1(addLine1);
                addr.setLine2(addLine2);
                addr.setCity(city);
                addr.setState(state);
                addr.setPostalCode(postalCode);
                addr.setPhone(phone);
                addr.setUpdatedAt(new Date());
                addr.setUpdatedBy(HomePage.getInstance().getUser().getName());

                patient.setName(name);
                patient.setAddress(addr);
                patient.setInsuranceProvider(insPr);
                patient.setUpdatedAt(new Date());
                patient.setUpdatedBy(HomePage.getInstance().getUser().getName());

                patients.set(selectedRow, patient);
                refreshTable();

                addressDAO.updateAddress(addr);
                patientDAO.updatePatient(patient);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select one patient to update");
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int selectedRow = tablePatients.getSelectedRow();

        if (selectedRow >= 0) {
            Patient patient = patients.get(selectedRow);

            if (hasAppointment(patient)) {
                JOptionPane.showMessageDialog(this, "Cannot delete, this patient has an appointment");
                return;
            }

            // delete the type
            patients.remove(selectedRow);
            refreshTable();

            patientDAO.deletePatient(patient.getId());
            addressDAO.deleteAddressById(patient.getAddress().getId());

            // clear the text field
            clearTexts();
        } else {
            JOptionPane.showMessageDialog(this, "Please select one patient to delete");
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void windowClosingActionHandler(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosingActionHandler
        
        HomePage.getInstance().navigate();
    }//GEN-LAST:event_windowClosingActionHandler

    private void tablePatientsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablePatientsMouseClicked
        int selectedRow = tablePatients.getSelectedRow();
        Patient pt = patients.get(selectedRow);
        Address addr = pt.getAddress();

        txtName.setText(pt.getName());
        txtPhone.setText(addr.getPhone());
        txtInsPr.setText(pt.getInsuranceProvider());
        txtAddrLine1.setText(addr.getLine1());
        txtAddrLine2.setText(addr.getLine2());
        txtCity.setText(addr.getCity());
        txtState.setText(addr.getState());
        txtPostalCode.setText(addr.getPostalCode());
    }//GEN-LAST:event_tablePatientsMouseClicked

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        clearTexts();
    }//GEN-LAST:event_btnClearActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablePatients;
    private javax.swing.JTextField txtAddrLine1;
    private javax.swing.JTextField txtAddrLine2;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtInsPr;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhone;
    private javax.swing.JTextField txtPostalCode;
    private javax.swing.JTextField txtState;
    // End of variables declaration//GEN-END:variables
}
