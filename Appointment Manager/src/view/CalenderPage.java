  package view;

import com.toedter.calendar.IDateEvaluator;
import database.dao.AppointmentDAO;
import database.dao.impl.AppointmentDAOImpl;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Appointment;

/**
 * View of the calendar page
 */
public class CalenderPage extends javax.swing.JFrame {

    // To store appointments
    private List<Appointment> appointments;

    // Dao for access appointments data
    private static AppointmentDAO appointmentDAO = new AppointmentDAOImpl();

    private DefaultTableModel dtm;

    /**
     * Creates new form APTtypesPage
     */
    public CalenderPage() {
        initComponents();

        this.dtm = (DefaultTableModel) tableAppointments.getModel();
        // get all the appointments
        this.appointments = appointmentDAO.getAllAppointments();
        refreshCalander();
    }

    private void clearTable() {
        dtm.getDataVector().removeAllElements();
        dtm.fireTableDataChanged();
    }

    private void dailyAppointments(Date selectedDate) {
        clearTable();
        for (Appointment apt : appointments) {
            Date startTime = apt.getStartDateTime();
            if (startTime.getYear() == selectedDate.getYear() && startTime.getMonth() == selectedDate.getMonth()
                    && startTime.getDate() == selectedDate.getDate()) {
                dtm.addRow(new Object[]{apt.getPatient().getName(), apt.getCounselor().getName(),
                    apt.getType().getDescription(), apt.getNotes(), apt.getStartDateTime()});
            }
        }
    }
    
    private void monthlyAppointments(Date selectedDate) {
        clearTable();
        for (Appointment apt : appointments) {
            Date startTime = apt.getStartDateTime();
            if (startTime.getYear() == selectedDate.getYear() && startTime.getMonth() == selectedDate.getMonth()) {
                dtm.addRow(new Object[]{apt.getPatient().getName(), apt.getCounselor().getName(),
                    apt.getType().getDescription(), apt.getNotes(), apt.getStartDateTime()});
            }
        }
    }
    
    private void weeksAppointments(Date selectedDate) {
        clearTable();
        
        for (Appointment apt : appointments) {
            
            int selectedWeek = cmbWeeks.getSelectedIndex();
            int week2 = 0;
            
            Date startTime = apt.getStartDateTime();
            
            Calendar c = Calendar.getInstance();
            c.setTime(startTime);
            
            Calendar c2 = Calendar.getInstance();
            c2.setTime(startTime);
            
            switch(selectedWeek) {
                case 0:
                    selectedWeek += 2;
                    break;
                case 1:
                    selectedWeek += 2;
                    break;
                case 2:
                    selectedWeek += 2;
                    break;
                case 3:
                    selectedWeek += 2;
                    break;
                case 4:
                    selectedWeek += 2;
                    break;
                case 5:
                    selectedWeek = 2;
                    week2 = 3;
                    break;
                case 6:
                    selectedWeek = 4;
                    week2 = 5;
                    break;
                case 7:
                    selectedWeek = 5;
                    week2 = 6;
                    break;
            }
            
            if ((c.get(Calendar.WEEK_OF_MONTH) == selectedWeek || c.get(Calendar.WEEK_OF_MONTH) == week2)
                    && startTime.getYear() == selectedDate.getYear() 
                    && startTime.getMonth() == selectedDate.getMonth()) {
                dtm.addRow(new Object[]{apt.getPatient().getName(), apt.getCounselor().getName(),
                    apt.getType().getDescription(), apt.getNotes(), apt.getStartDateTime()});
            }
        }
    }
    
    /**
     * Creates the date for calendar view
     *
     * @param day the day
     * @param month the month
     * @param year the year
     * @return the created date
     */
    private Date createDate(int day, int month, int year) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return (c.getTime());
    }

    /**
     * Refresh the calendar and pop-up the appointment days
     */
    private void refreshCalander() {

        HighlightEvaluator evaluator = new HighlightEvaluator();
        int selectedMonth = calendar.getMonthChooser().getMonth();

        // Iterate over all appointments
        for (Appointment apt : appointments) {
            // get the start date 
            Date startDateTime = apt.getStartDateTime();
            Calendar c = Calendar.getInstance();
            c.setTime(startDateTime);
            // sepearte the year, month and day
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // add the start date to the calendar
            evaluator.add(createDate(day, month, year));

        }
        // set the custom date evaluater
        calendar.getDayChooser().addDateEvaluator(evaluator);
        calendar.repaint();
        // refresh the calendar
        calendar.getMonthChooser().setMonth(selectedMonth);

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
        btnHome = new javax.swing.JButton();
        calendar = new com.toedter.calendar.JCalendar();
        jLabel1 = new javax.swing.JLabel();
        btnDailyApts = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAppointments = new javax.swing.JTable();
        labelDate = new javax.swing.JLabel();
        btnWeeksApts = new javax.swing.JButton();
        btnMonthlyApts = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cmbWeeks = new javax.swing.JComboBox<>();

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
        jLabel4.setText("Calender");

        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/home.png"))); // NOI18N
        btnHome.setBorder(null);
        btnHome.setFocusPainted(false);
        btnHome.setOpaque(false);
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        calendar.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel1.setForeground(new java.awt.Color(51, 51, 255));
        jLabel1.setText("blue coloured : Days of appointments ");

        btnDailyApts.setText("Daily");
        btnDailyApts.setFocusPainted(false);
        btnDailyApts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDailyAptsActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(153, 0, 0));
        jLabel2.setText("red coloured : Today ");

        tableAppointments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Patient", "Counselor", "Type", "Notes", "Start Time"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableAppointments.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(tableAppointments);

        labelDate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        labelDate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        btnWeeksApts.setText("View");
        btnWeeksApts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWeeksAptsActionPerformed(evt);
            }
        });

        btnMonthlyApts.setText("Monthly");
        btnMonthlyApts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMonthlyAptsActionPerformed(evt);
            }
        });

        jLabel3.setText("Appointments By:");

        cmbWeeks.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1st Week", "2nd Week", "3rd Week", "4th week", "5th week", "1st Bi-Week", "2nd Bi-Week", "3rd Bi-Week" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(calendar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnMonthlyApts, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbWeeks, 0, 124, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnWeeksApts)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDailyApts))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(147, 147, 147)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(labelDate, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(179, 179, 179))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(calendar, javax.swing.GroupLayout.PREFERRED_SIZE, 345, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnWeeksApts)
                        .addComponent(btnDailyApts))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cmbWeeks, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel1)
                                .addComponent(btnMonthlyApts)
                                .addComponent(jLabel3)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(labelDate, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)
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
    }//GEN-LAST:event_btnHomeActionPerformed

    private void windowClosingActionHandler(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_windowClosingActionHandler
        // go to the home page
        HomePage.getInstance().navigate();
        this.setVisible(false);
    }//GEN-LAST:event_windowClosingActionHandler

    private void btnDailyAptsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDailyAptsActionPerformed
        Date selectedDate = calendar.getDate();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        labelDate.setText("Appointments On " + sdf.format(selectedDate));
        dailyAppointments(selectedDate);
        refreshCalander();
    }//GEN-LAST:event_btnDailyAptsActionPerformed

    private void btnMonthlyAptsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMonthlyAptsActionPerformed
        Date selectedDate = calendar.getDate();
        String pattern = "MMMM-yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        labelDate.setText("Appointments On " + sdf.format(selectedDate));
        monthlyAppointments(selectedDate);
        refreshCalander();
    }//GEN-LAST:event_btnMonthlyAptsActionPerformed

    private void btnWeeksAptsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWeeksAptsActionPerformed
        labelDate.setText("Appointments On " + cmbWeeks.getSelectedItem());
        weeksAppointments(calendar.getDate());
        refreshCalander();
    }//GEN-LAST:event_btnWeeksAptsActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDailyApts;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton btnMonthlyApts;
    private javax.swing.JButton btnWeeksApts;
    private com.toedter.calendar.JCalendar calendar;
    private javax.swing.JComboBox<String> cmbWeeks;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel labelDate;
    private javax.swing.JTable tableAppointments;
    // End of variables declaration//GEN-END:variables

    /**
     * for highlighting the calendar dates
     */
    private class HighlightEvaluator implements IDateEvaluator {

        private final List<Date> list = new ArrayList<>();

        public void add(Date date) {
            list.add(date);
        }

        @Override
        public boolean isSpecial(Date date) {
            return list.contains(date);
        }

        @Override
        public Color getSpecialForegroundColor() {
            return Color.blue;
        }

        @Override
        public Color getSpecialBackroundColor() {
            return Color.yellow;
        }

        @Override
        public String getSpecialTooltip() {
            return calendar.getDate().toString();
        }

        @Override
        public boolean isInvalid(Date date) {
            return false;
        }

        @Override
        public Color getInvalidForegroundColor() {
            return Color.red;
        }

        @Override
        public Color getInvalidBackroundColor() {
            return Color.yellow;
        }

        @Override
        public String getInvalidTooltip() {
            return null;
        }
    }
}
