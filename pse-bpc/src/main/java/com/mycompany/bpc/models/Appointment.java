package com.mycompany.bpc.models;

import java.time.*;

/**
 *
 * @author shahwaizshaban
 */
public class Appointment {
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Long appointmentDuration; // Duration in minutes
    private LocalDateTime bookingDate;
    private Long physiotherapistId;
    private Long patientId;
    private String treatment;
    private String status;

    // Constructor, Getters, Setters

    public Appointment(LocalDate appointmentDate, LocalTime appointmentTime, Long appointmentDuration, Long physiotherapistId, String status) {
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentDuration = appointmentDuration;
        this.physiotherapistId = physiotherapistId;
        this.status = status;
    }

    public Appointment() {}

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public Long getAppointmentDuration() {
        return appointmentDuration;
    }

    public LocalDateTime getBookingDate() {
        return bookingDate;
    }

    public Long getPhysiotherapistId() {
        return physiotherapistId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getTreatment() {
        return treatment;
    }

    public String getStatus() {
        return status;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public void setBookingDate(LocalDateTime bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAppointmentDuration(Long appointmentDuration) {
        this.appointmentDuration = appointmentDuration;
    }
}
