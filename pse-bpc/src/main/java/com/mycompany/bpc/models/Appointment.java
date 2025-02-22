package com.mycompany.bpc.models;

import java.time.LocalDateTime;

/**
 *
 * @author shahwaizshaban
 */
public class Appointment {
    private LocalDateTime dateTime;
    private Physiotherapist physiotherapist;
    private Patient patient;
    private String status;

    // Constructor, Getters, Setters
    public Appointment(LocalDateTime dateTime, Physiotherapist physiotherapist, Patient patient, String status) {
        this.dateTime = dateTime;
        this.physiotherapist = physiotherapist;
        this.patient = patient;
        this.status = status;
    }

    public void book() {
        // Implement booking logic
    }

    public void cancel() {
        // Implement cancel logic
    }

    public void attend() {
        // Implement attend logic
    }

    public void reschedule(LocalDateTime newDateTime) {
        this.dateTime = newDateTime;
    }

    // Getters and Setters
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public Physiotherapist getPhysiotherapist() { return physiotherapist; }
    public void setPhysiotherapist(Physiotherapist physiotherapist) { this.physiotherapist = physiotherapist; }

    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
