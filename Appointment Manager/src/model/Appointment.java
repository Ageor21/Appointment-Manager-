package model;

import java.util.Date;

public class Appointment extends DataBasic {

    private int id;
    private Patient patient;
    private Counselor counselor;
    private APTType type;
    private String notes;
    private Date startDateTime;

    public Appointment() {
    }

    public Appointment(int id, Patient patient, Counselor counselor, APTType type, String notes, Date startDateTime) {
        this.id = id;
        this.patient = patient;
        this.counselor = counselor;
        this.type = type;
        this.notes = notes;
        this.startDateTime = startDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Counselor getCounselor() {
        return counselor;
    }

    public void setCounselor(Counselor counselor) {
        this.counselor = counselor;
    }

    public APTType getType() {
        return type;
    }

    public void setType(APTType type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

}
