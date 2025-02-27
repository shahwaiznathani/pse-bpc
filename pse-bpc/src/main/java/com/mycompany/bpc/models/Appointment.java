package com.mycompany.bpc.models;

import java.time.*;

/**
 *
 * @author shahwaizshaban
 */
public class Appointment {
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private LocalDateTime bookingDate;
    private String physiotherapistId;
    private String patientId;
    private String treatment;
    private String status;

    // Constructor, Getters, Setters
    public Appointment(LocalDate appointmentDate, LocalTime appointmentTime, LocalDateTime bookingDate, String physiotherapistId, String patientId, String treatment, String status) {
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.bookingDate = bookingDate;
        this.physiotherapistId = physiotherapistId;
        this.patientId = patientId;
        this.treatment = treatment;
        this.status = status;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public String getPhysiotherapistId() {
        return physiotherapistId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getStatus() {
        return status;
    }
}
