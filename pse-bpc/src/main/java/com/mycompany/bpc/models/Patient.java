package com.mycompany.bpc.models;

/**
 *
 * @author shahwaizshaban
 */
public class Patient extends Member{
    public Patient(String id, String fullName, String address, String phoneNumber) {
        super(id, fullName, address, phoneNumber);
    }

    // Methods for booking, canceling, and viewing appointments
    public void bookAppointment() {
        // Implement booking logic
    }

    public void cancelAppointment() {
        // Implement cancel logic
    }

    public void viewAppointments() {
        // Implement view appointments logic
    }
}
