package database.dao.impl;

import database.conn.DbConnection;
import database.dao.AddressDAO;
import helper.TimeZone;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Address;

public class AddressDAOImpl implements AddressDAO {

    // get the database connection
    private static Connection conn = DbConnection.getConnection();

    @Override
    public int addAddress(Address address) {
        Integer row = null;
        try {
            String addQuery = "insert into address values(?,?,?,?,?,?,?,?,?,null,null)";
            PreparedStatement smt = conn.prepareStatement(addQuery);
            smt.setInt(1, address.getId());
            smt.setString(2, address.getLine1());
            smt.setString(3, address.getLine2());
            smt.setString(4, address.getCity());
            smt.setString(5, address.getState());
            smt.setString(6, address.getPostalCode());
            smt.setString(7, address.getPhone());

            smt.setTimestamp(8, new Timestamp(TimeZone.toMainOfficeZone(address.getCreatedAt()).getTime()));
            smt.setString(9, address.getCreatedBy());
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(AddressDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public int updateAddress(Address address) {
        Integer row = null;
        try {
            String updateQuery = "update address set addressline_1=?,addressline_2=?,city=?,state=?,"
                    + "postal_code=?,phone=?,updated_at=?,updated_by=? where address_id=?";
            PreparedStatement smt = conn.prepareStatement(updateQuery);
            smt.setString(1, address.getLine1());
            smt.setString(2, address.getLine2());
            smt.setString(3, address.getCity());
            smt.setString(4, address.getState());
            smt.setString(5, address.getPostalCode());
            smt.setString(6, address.getPhone());
            
            smt.setTimestamp(7, new Timestamp(TimeZone.toMainOfficeZone(address.getUpdatedAt()).getTime()));
            smt.setString(8, address.getUpdatedBy());
            smt.setInt(9, address.getId());
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(AddressDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public int deleteAddressById(int id) {
        Integer row = null;
        try {
            String deleteQuery = "delete from address where address_id=?";
            PreparedStatement smt = conn.prepareStatement(deleteQuery);
            smt.setInt(1, id);
            row = smt.executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(AddressDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return row;
    }

    @Override
    public Address getAddressById(int id) {
        Address address = null;
        ResultSet rs = null;
        try {
            String getQuery = "select * from address where address_id=?";
            PreparedStatement smt = conn.prepareStatement(getQuery);
            smt.setInt(1, id);
            rs = smt.executeQuery();
            while (rs.next()) {
                String line1 = rs.getString("addressline_1");
                String line2 = rs.getString("addressline_2");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String postal_code = rs.getString("postal_code");
                String phone = rs.getString("phone");
                
                java.util.Date createdAt = new java.util.Date(rs.getTimestamp("created_at").getTime());
                String createdBy = rs.getString("created_by");
                Timestamp updatedTs = rs.getTimestamp("updated_at");
                java.util.Date updatedAt = updatedTs == null ? null : new java.util.Date(updatedTs.getTime());
                String updatedBY = rs.getString("updated_by");

                address = new Address(id, line1, line2, city, state, postal_code, phone);
                address.setCreatedAt(TimeZone.toCurrentZone(createdAt));
                address.setCreatedBy(createdBy);
                address.setUpdatedAt(TimeZone.toCurrentZone(updatedAt));
                address.setUpdatedBy(updatedBY);
            }
        } catch (Exception e) {
            Logger.getLogger(AddressDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return address;
    }

    @Override
    public int getMaxAddressID() {
        int max = 0;
        ResultSet rs = null;
        try {
            Statement st = conn.createStatement();
            rs = st.executeQuery("select max(address_id) from address");
            while (rs.next()) {
                max = rs.getInt("max(address_id)");
            }
        } catch (Exception e) {
            Logger.getLogger(AddressDAOImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return max;
    }

}
