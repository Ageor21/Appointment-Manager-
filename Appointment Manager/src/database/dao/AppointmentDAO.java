package database.dao;

import model.Appointment;
import java.util.List;

public interface AppointmentDAO {

    public int addAppointment(Appointment appointment);

    public int updateAppointment(Appointment appointment);

    public int deleteAppointment(int id);

    public Appointment getAppointmentById(int id);

    public List<Appointment> getAllAppointments();

    public int getMaxAppointmentID();
}
