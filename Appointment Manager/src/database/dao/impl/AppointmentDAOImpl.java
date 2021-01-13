package database.dao.impl;

import database.conn.DbConnection;
import database.dao.APTtypeDAO;
import database.dao.AppointmentDAO;
import database.dao.CounselorDAO;
import database.dao.PatientDAO;
import helper.TimeZone;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.APTType;
import model.Appointment;
import model.Counselor;
import model.Patient;

public class AppointmentDAOImpl implements AppointmentDAO {

    // get the database connection
    private static Connection conn = DbConnection.getConnection();

    private PatientDAO pdao = new PatientDAOImpl();
    private APTtypeDAO aptdao = new APTtypeDAOImpl();
    private CounselorDAO cdao = new CounselorDAOImpl();

    @Override
    public int addAppointment(Appointment apt) {
        Integer row = null;
        try {
            String addQuery = "insert into appointment values(?,?,?,?,?,?,?,?,null,null,?,?,?)";
            PreparedStatement smt = conn.prepareStatement(addQuery);
            smt.setInt(1, apt.getId());
            smt.setInt(2, apt.getPatient().getId());
            smt.setInt(3, apt.getCounselor().getId());
            smt.setInt(4, apt.getType().getId());
            smt.setString(5, apt.getNotes());
            smt.setString(6, TimeZone.toCurrentZone(apt.getStartDateTime()).toString());
            smt.setString(7, TimeZone.toMainOfficeZone(apt.getCreatedAt()).toString());
            smt.setString(8, apt.getCreatedBy());
            smt.setInt(9, apt.getPatient().getId());
            smt.setInt(10, apt.getCounselor().getId());
            smt.setInt(11, apt.getType().getId());
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public int updateAppointment(Appointment apt) {
        Integer row = null;
        try {
            String updateQuery = "update appointment set pt_id=?,cr_id=?,apt_type_id=?,notes=?,start_datetime=?,"
                    + "updated_at=?,updated_by=?,patient_pt_id=?,counselor_c_id=?,APTtype_APTtype_id=? "
                    + "where apt_id=?";
            PreparedStatement smt = conn.prepareStatement(updateQuery);
            smt.setInt(1, apt.getPatient().getId());
            smt.setInt(2, apt.getCounselor().getId());
            smt.setInt(3, apt.getType().getId());
            smt.setString(4, apt.getNotes());
            smt.setString(5, TimeZone.toCurrentZone(apt.getStartDateTime()).toString());
            smt.setString(6, TimeZone.toMainOfficeZone(apt.getUpdatedAt()).toString());
            smt.setString(7, apt.getUpdatedBy());
            smt.setInt(8, apt.getPatient().getId());
            smt.setInt(9, apt.getCounselor().getId());
            smt.setInt(10, apt.getType().getId());
            smt.setInt(11, apt.getId());
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public int deleteAppointment(int id) {
        Integer row = null;
        try {
            String deleteQuery = "delete from appointment where apt_id=?";
            PreparedStatement smt = conn.prepareStatement(deleteQuery);
            smt.setInt(1, id);
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public Appointment getAppointmentById(int id) {
        Appointment apt = null;
        ResultSet rs = null;
        try {
            String getQuery = "select * from appointment where apt_id=?";
            PreparedStatement smt = conn.prepareStatement(getQuery);
            smt.setInt(1, id);
            rs = smt.executeQuery();
            while (rs.next()) {
                int pt_id = rs.getInt("pt_id");
                int cr_id = rs.getInt("cr_id");
                int apt_type_id = rs.getInt("apt_type_id");
                String notes = rs.getString("notes");
                DateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
                java.util.Date startDateTime = dateFormat.parse(rs.getString("start_datetime"));
                java.util.Date createdAt = dateFormat.parse(rs.getString("created_at"));
                String createdBy = rs.getString("created_by");
                String updatedTs = rs.getString("updated_at");
                java.util.Date updatedAt = updatedTs == null ? null : dateFormat.parse(updatedTs);
                String updatedBY = rs.getString("updated_by");

                Patient patient = pdao.getPatientById(pt_id);
                APTType aptt = aptdao.getAPTtypeById(apt_type_id);
                Counselor counselor = cdao.getCounselorById(cr_id);

                apt = new Appointment(id, patient, counselor, aptt, notes, startDateTime);
                apt.setCreatedAt(createdAt);
                apt.setCreatedBy(createdBy);
                apt.setUpdatedAt(updatedAt);
                apt.setUpdatedBy(updatedBY);
            }
        } catch (Exception e) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return apt;
    }

    @Override
    public List<Appointment> getAllAppointments() {

        List<Appointment> apts = new ArrayList<>();
        ResultSet rs = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery("select * from appointment");
            while (rs.next()) {
                int apt_id = rs.getInt("apt_id");
                int pt_id = rs.getInt("pt_id");
                int cr_id = rs.getInt("cr_id");
                int apt_type_id = rs.getInt("apt_type_id");
                String notes = rs.getString("notes");
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault());
                java.util.Date startDateTime = dateFormat.parse(rs.getString("start_datetime"));
                java.util.Date createdAt = dateFormat.parse(rs.getString("created_at"));
                String createdBy = rs.getString("created_by");
                String updatedTs = rs.getString("updated_at");
                java.util.Date updatedAt = updatedTs == null ? null : dateFormat.parse(updatedTs);
                String updatedBY = rs.getString("updated_by");

                Patient patient = pdao.getPatientById(pt_id);
                APTType aptt = aptdao.getAPTtypeById(apt_type_id);
                Counselor counselor = cdao.getCounselorById(cr_id);

                Appointment apt = new Appointment(apt_id, patient, counselor, aptt, notes, startDateTime);
                apt.setCreatedAt(createdAt);
                apt.setCreatedBy(createdBy);
                apt.setUpdatedAt(updatedAt);
                apt.setUpdatedBy(updatedBY);
                apts.add(apt);
            }
        } catch (Exception e) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return apts;
    }
    
    @Override
    public int getMaxAppointmentID() {
        int max = 0;
        ResultSet rs = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery("select max(apt_id) from appointment");
            while (rs.next()) {
                max = rs.getInt("max(apt_id)");
            }
        } catch (Exception e) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return max;
    }
    
}
