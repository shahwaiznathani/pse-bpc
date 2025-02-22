package com.mycompany.bpc.models;

import java.util.List;

/**
 *
 * @author shahwaizshaban
 */
public class BookingSystem {
    private List<Physiotherapist> physiotherapists;
    private List<Patient> patients;
    private List<Appointment> appointments;

    // Constructor, Getters, Setters
    public BookingSystem(List<Physiotherapist> physiotherapists, List<Patient> patients, List<Appointment> appointments) {
        this.physiotherapists = physiotherapists;
        this.patients = patients;
        this.appointments = appointments;
    }

    // Methods to manage the system
    public void addPatient(Patient patient) {
        patients.add(patient);
    }

    public void removePatient(Patient patient) {
        patients.remove(patient);
    }

    public void searchByExpertise(String expertise) {
        // Implement search logic
    }

    public void searchByPhysiotherapist(Physiotherapist physiotherapist) {
        // Implement search logic
    }

    public void bookAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public void cancelAppointment(Appointment appointment) {
        appointments.remove(appointment);
    }

    public void generateReport() {
        // Implement report generation logic
    }

    // Getters and Setters
    public List<Physiotherapist> getPhysiotherapists() { return physiotherapists; }
    public void setPhysiotherapists(List<Physiotherapist> physiotherapists) { this.physiotherapists = physiotherapists; }

    public List<Patient> getPatients() { return patients; }
    public void setPatients(List<Patient> patients) { this.patients = patients; }

    public List<Appointment> getAppointments() { return appointments; }
    public void setAppointments(List<Appointment> appointments) { this.appointments = appointments; }
}
