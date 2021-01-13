package database.dao.impl;

import database.conn.DbConnection;
import database.dao.AddressDAO;
import database.dao.PatientDAO;
import helper.TimeZone;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Address;
import model.Patient;

public class PatientDAOImpl implements PatientDAO {

    // get the database connection
    private static Connection conn = DbConnection.getConnection();
    
    private AddressDAO adao = new AddressDAOImpl();

    @Override
    public int addPatient(Patient patient) {
        Integer row = null;
        try {
            String addQuery = "insert into patient values(?,?,?,?,?,?,null,null,?)";
            PreparedStatement smt = conn.prepareStatement(addQuery);
            smt.setInt(1, patient.getId());
            smt.setString(2, patient.getName());
            smt.setInt(3, patient.getAddress().getId());
            smt.setString(4, patient.getInsuranceProvider());
            smt.setTimestamp(5, new Timestamp(TimeZone.toMainOfficeZone(patient.getCreatedAt()).getTime()));
            smt.setString(6, patient.getCreatedBy());
            smt.setInt(7, patient.getAddress().getId());
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(PatientDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public int updatePatient(Patient patient) {
        Integer row = null;
        try {
            String updateQuery = "update patient set pt_name=?,INS_PR=?,"
                    + "updated_at=?,updated_by=? where pt_id=?";
            PreparedStatement smt = conn.prepareStatement(updateQuery);
            smt.setString(1, patient.getName());
            smt.setString(2, patient.getInsuranceProvider());
            smt.setTimestamp(3, new Timestamp(TimeZone.toMainOfficeZone(patient.getUpdatedAt()).getTime()));
            smt.setString(4, patient.getUpdatedBy());
            smt.setInt(5, patient.getId());
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(PatientDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public int deletePatient(int id) {
        Integer row = null;
        try {
            String deleteQuery = "delete from patient where pt_id=?";
            PreparedStatement smt = conn.prepareStatement(deleteQuery);
            smt.setInt(1, id);
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(PatientDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public Patient getPatientById(int id) {
        Patient patient = null;
        ResultSet rs = null;
        try {
            String getQuery = "select * from patient where pt_id=?";
            PreparedStatement smt = conn.prepareStatement(getQuery);
            smt.setInt(1, id);
            rs = smt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("pt_name");
                int address_id = rs.getInt("address_id");
                String ins_pr = rs.getString("INS_PR");
                java.util.Date createdAt = new java.util.Date(rs.getTimestamp("created_at").getTime());
                String createdBy = rs.getString("created_by");
                Timestamp updatedTs = rs.getTimestamp("updated_at");
                java.util.Date updatedAt = updatedTs == null ? null : new java.util.Date(updatedTs.getTime());
                String updatedBY = rs.getString("updated_by");

                // get the address
                Address address = adao.getAddressById(address_id);

                patient = new Patient(id, name, address, ins_pr);
                patient.setCreatedAt(TimeZone.toCurrentZone(createdAt));
                patient.setCreatedBy(createdBy);
                patient.setUpdatedAt(TimeZone.toCurrentZone(updatedAt));
                patient.setUpdatedBy(updatedBY);
            }
        } catch (Exception e) {
            Logger.getLogger(PatientDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return patient;
    }

    @Override
    public List<Patient> getAllPatients() {
        
        List<Patient> patients = new ArrayList<>();
        ResultSet rs = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery("select * from patient");
            while (rs.next()) {
                int pt_id = rs.getInt("pt_id");
                String name = rs.getString("pt_name");
                int address_id = rs.getInt("address_id");
                String ins_pr = rs.getString("INS_PR");
                java.util.Date createdAt = new java.util.Date(rs.getTimestamp("created_at").getTime());
                String createdBy = rs.getString("created_by");
                Timestamp updatedTs = rs.getTimestamp("updated_at");
                java.util.Date updatedAt = updatedTs == null ? null : new java.util.Date(updatedTs.getTime());
                String updatedBY = rs.getString("updated_by");

                // get the address
                Address address = adao.getAddressById(address_id);

                Patient patient = new Patient(pt_id, name, address, ins_pr);
                patient.setCreatedAt(TimeZone.toCurrentZone(createdAt));
                patient.setCreatedBy(createdBy);
                patient.setUpdatedAt(TimeZone.toCurrentZone(updatedAt));
                patient.setUpdatedBy(updatedBY);
                
                patients.add(patient);
            }
        } catch (Exception e) {
            Logger.getLogger(PatientDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return patients;
    }

    @Override
    public int getMaxPatientID() {
        int max = 0;
        ResultSet rs = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery("select max(pt_id) from patient");
            while (rs.next()) {
                max = rs.getInt("max(pt_id)");
            }
        } catch (Exception e) {
            Logger.getLogger(PatientDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return max;
    }

}
