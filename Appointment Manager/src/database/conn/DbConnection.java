package database.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConnection {

    private static Connection conn = null;
    private static final String USERNAME = "U07PFM";
    private static final String PASSWORD = "53689091362";

    public static Connection getConnection() {
        if (conn == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                String databaseURL = "jdbc:mysql://wgudb.ucertify.com:3306/U07PFM?useSSL=false";
                conn = DriverManager.getConnection(databaseURL, USERNAME, PASSWORD);
            } catch (Exception ex) {
                Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return conn;
    }
    
    public static void close() {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
