package database.dao;

import model.Patient;
import java.util.List;

public interface PatientDAO {

    public int addPatient(Patient patient);

    public int updatePatient(Patient patient);

    public int deletePatient(int id);

    public Patient getPatientById(int id);

    public List<Patient> getAllPatients();

    public int getMaxPatientID();
}
