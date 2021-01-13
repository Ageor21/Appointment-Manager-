package database.dao.impl;

import database.conn.DbConnection;
import database.dao.CounselorDAO;
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
import model.Counselor;

public class CounselorDAOImpl implements CounselorDAO {

    // get the database connection
    private static Connection conn = DbConnection.getConnection();

    @Override
    public int addCounselor(Counselor counselor) {
        Integer row = null;
        try {
            String addQuery = "insert into counselor values(?,?,?,?,?,?,null,null)";
            PreparedStatement smt = conn.prepareStatement(addQuery);
            smt.setInt(1, counselor.getId());
            smt.setString(2, counselor.getName());
            smt.setString(3, counselor.getPassword());
            smt.setInt(4, counselor.getPin());
            smt.setTimestamp(5, new Timestamp(TimeZone.toMainOfficeZone(counselor.getCreatedAt()).getTime()));
            smt.setString(6, counselor.getCreatedBy());
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(CounselorDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public int updateCounselor(Counselor counselor) {
        Integer row = null;
        try {
            String updateQuery = "update counselor set c_name=?,c_password=?,c_pin=?,"
                    + "updated_at=?,updated_by=? where c_id=?";
            PreparedStatement smt = conn.prepareStatement(updateQuery);
            smt.setString(1, counselor.getName());
            smt.setString(2, counselor.getPassword());
            smt.setInt(3, counselor.getPin());
            smt.setTimestamp(4, new Timestamp(TimeZone.toMainOfficeZone(counselor.getUpdatedAt()).getTime()));
            smt.setString(5, counselor.getUpdatedBy());
            smt.setInt(6, counselor.getId());
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(CounselorDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public int deleteCounselorById(int id) {
        Integer row = null;
        try {
            String deleteQuery = "delete from counselor where c_id=?";
            PreparedStatement smt = conn.prepareStatement(deleteQuery);
            smt.setInt(1, id);
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(CounselorDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public Counselor getCounselorById(int id) {
        Counselor counselor = null;
        ResultSet rs = null;
        try {
            String getQuery = "select * from counselor where c_id=?";
            PreparedStatement smt = conn.prepareStatement(getQuery);
            smt.setInt(1, id);
            rs = smt.executeQuery();
            while (rs.next()) {
                String name = rs.getString("c_name");
                String password = rs.getString("c_password");
                int pin = rs.getInt("c_pin");
                java.util.Date createdAt = new java.util.Date(rs.getTimestamp("created_at").getTime());
                String createdBy = rs.getString("created_by");
                Timestamp updatedTs = rs.getTimestamp("updated_at");
                java.util.Date updatedAt = updatedTs == null ? null : new java.util.Date(updatedTs.getTime());
                String updatedBY = rs.getString("updated_by");

                counselor = new Counselor(id, name, password, pin);
                counselor.setCreatedAt(TimeZone.toCurrentZone(createdAt));
                counselor.setCreatedBy(createdBy);
                counselor.setUpdatedAt(TimeZone.toCurrentZone(updatedAt));
                counselor.setUpdatedBy(updatedBY);
            }
        } catch (Exception e) {
            Logger.getLogger(CounselorDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return counselor;
    }

    @Override
    public List<Counselor> getAllCounselors() {
        List<Counselor> counselors = new ArrayList<>();
        ResultSet rs = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery("select * from counselor");
            while (rs.next()) {
                int id = rs.getInt("c_id");
                String name = rs.getString("c_name");
                String password = rs.getString("c_password");
                int pin = rs.getInt("c_pin");
                java.util.Date createdAt = new java.util.Date(rs.getTimestamp("created_at").getTime());
                String createdBy = rs.getString("created_by");
                Timestamp updatedTs = rs.getTimestamp("updated_at");
                java.util.Date updatedAt = updatedTs == null ? null : new java.util.Date(updatedTs.getTime());
                String updatedBY = rs.getString("updated_by");

                Counselor c = new Counselor(id, name, password, pin);
                c.setCreatedAt(TimeZone.toCurrentZone(createdAt));
                c.setCreatedBy(createdBy);
                c.setUpdatedAt(TimeZone.toCurrentZone(updatedAt));
                c.setUpdatedBy(updatedBY);
                counselors.add(c);
            }
        } catch (Exception e) {
            Logger.getLogger(CounselorDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return counselors;
    }

    @Override
    public int getMaxCounselorID() {
        int max = 0;
        ResultSet rs = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery("select max(c_id) from counselor");
            while (rs.next()) {
                max = rs.getInt("max(c_id)");
            }
        } catch (Exception e) {
            Logger.getLogger(CounselorDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return max;
    }

}
