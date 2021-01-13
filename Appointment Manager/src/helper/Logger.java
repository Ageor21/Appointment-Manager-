package helper;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import model.Counselor;

public class Logger {

    public static void successLogin(Counselor cr) {
        // Set true for append mode
        BufferedWriter writer = null;
        try {
            Date timeStamp = new Date();
            // Set true for append mode
            writer = new BufferedWriter(new FileWriter("log-sucess-login.txt", true));
            String info = String.format("%s successfully logged In\n", cr.getName());
            writer.write(timeStamp + ": " + info);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void errorLogin(String errorMsg, String userName, String password, String pin) {
        BufferedWriter writer = null;
        try {
            Date timeStamp = new Date();
            // Set true for append mode
            writer = new BufferedWriter(new FileWriter("log-error-login.txt", true));
            String info = String.format("username=%s password=%s pin=%s error=%s\n", userName, password, pin, errorMsg);
            writer.write(timeStamp + ": " + info);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
