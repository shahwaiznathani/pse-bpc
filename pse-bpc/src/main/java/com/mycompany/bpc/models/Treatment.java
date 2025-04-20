package com.mycompany.bpc.models;

import java.time.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author shahwaizshaban
 */
public class Treatment {
    private final String id;
    private final LocalDate appointmentDate;
    private final LocalTime appointmentTime;
    private final Long appointmentDuration;
    private final Long physiotherapistId;
    private final String expertise;
    private final String name;
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
