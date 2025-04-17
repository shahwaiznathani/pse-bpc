package com.mycompany.bpc.models;

import java.time.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author shahwaizshaban
 */
public class Treatment {
    private String id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Long appointmentDuration;
    private Long physiotherapistId;
    private String expertise;
    private String name;
    private String status;
    private static final AtomicLong appointmentCounter = new AtomicLong(0);

    public Treatment(LocalDate appointmentDate, LocalTime appointmentTime, Long appointmentDuration, Long physiotherapistId, String status, String name, String expertise) {
        this.id = "T-" + appointmentCounter.incrementAndGet();
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentDuration = appointmentDuration;
        this.physiotherapistId = physiotherapistId;
        this.status = status;
        this.name = name;
        this.expertise = expertise;
    }

    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public Long getAppointmentDuration() {
        return appointmentDuration;
    }

    public Long getPhysiotherapistId() {
        return physiotherapistId;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
    public String getExpertise() {
        return expertise;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
