package database.dao.impl;

import database.conn.DbConnection;
import database.dao.APTtypeDAO;
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
import model.APTType;

public class APTtypeDAOImpl implements APTtypeDAO {

    // get the database connection
    private static Connection conn = DbConnection.getConnection();

    @Override
    public int addAPTtype(APTType aptt) {
        Integer row = null;
        try {
            String addQuery = "insert into APTtype values(?,?,?,?,null,null)";
            PreparedStatement smt = conn.prepareStatement(addQuery);
            smt.setInt(1, aptt.getId());
            smt.setString(2, aptt.getDescription());

            smt.setTimestamp(3, new Timestamp(TimeZone.toMainOfficeZone(aptt.getCreatedAt()).getTime()));
            smt.setString(4, aptt.getCreatedBy());
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(APTtypeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public int updateAPTtype(APTType aptt) {
        Integer row = null;
        try {
            String updateQuery = "update APTtype set description=?,updated_at=?,updated_by=? where APTtype_id=?";
            PreparedStatement smt = conn.prepareStatement(updateQuery);
            smt.setString(1, aptt.getDescription());

            smt.setTimestamp(2, new Timestamp(TimeZone.toMainOfficeZone(aptt.getUpdatedAt()).getTime()));
            smt.setString(3, aptt.getUpdatedBy());
            
            smt.setInt(4, aptt.getId());
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(APTtypeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public int deleteAPTtypeById(int id) {
        Integer row = null;
        try {
            String deleteQuery = "delete from APTtype where APTtype_id=?";
            PreparedStatement smt = conn.prepareStatement(deleteQuery);
            smt.setInt(1, id);
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(APTtypeDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public APTType getAPTtypeById(int id) {
        APTType aptt = null;
        ResultSet rs = null;
        try {
            String getQuery = "select * from APTtype where APTtype_id=?";
            PreparedStatement smt = conn.prepareStatement(getQuery);
            smt.setInt(1, id);
            rs = smt.executeQuery();
            while (rs.next()) {
                String description = rs.getString("description");

                java.util.Date createdAt = new java.util.Date(rs.getTimestamp("created_at").getTime());
                String createdBy = rs.getString("created_by");
                Timestamp updatedTs = rs.getTimestamp("updated_at");
                java.util.Date updatedAt = updatedTs == null ? null : new java.util.Date(updatedTs.getTime());
                String updatedBY = rs.getString("updated_by");

                aptt = new APTType(id, description);
                aptt.setCreatedAt(TimeZone.toCurrentZone(createdAt));
                aptt.setCreatedBy(createdBy);
                aptt.setUpdatedAt(TimeZone.toCurrentZone(updatedAt));
                aptt.setUpdatedBy(updatedBY);
            }
        } catch (Exception e) {
            Logger.getLogger(APTtypeDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return aptt;
    }

    @Override
    public int getMaxAPTtypeID() {
        int max = 0;
        ResultSet rs = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery("select max(APTtype_id) from APTtype");
            while (rs.next()) {
                max = rs.getInt("max(APTtype_id)");
            }
        } catch (Exception e) {
            Logger.getLogger(APTtypeDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return max;
    }

    @Override
    public List<APTType> getAllAPTTypes() {
        List<APTType> aptts = new ArrayList<>();
        ResultSet rs = null;
        try {
            String getQuery = "select * from APTtype";
            PreparedStatement smt = conn.prepareStatement(getQuery);
            rs = smt.executeQuery();
            while (rs.next()) {
                
                int id = rs.getInt("APTtype_id");
                String description = rs.getString("description");

                java.util.Date createdAt = new java.util.Date(rs.getTimestamp("created_at").getTime());
                String createdBy = rs.getString("created_by");
                Timestamp updatedTs = rs.getTimestamp("updated_at");
                java.util.Date updatedAt = updatedTs == null ? null : new java.util.Date(updatedTs.getTime());
                String updatedBY = rs.getString("updated_by");

                APTType aptt = new APTType(id, description);
                aptt.setCreatedAt(TimeZone.toCurrentZone(createdAt));
                aptt.setCreatedBy(createdBy);
                aptt.setUpdatedAt(TimeZone.toCurrentZone(updatedAt));
                aptt.setUpdatedBy(updatedBY);
                
                aptts.add(aptt);
            }
        } catch (Exception e) {
            Logger.getLogger(APTtypeDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return aptts;
    }

}
