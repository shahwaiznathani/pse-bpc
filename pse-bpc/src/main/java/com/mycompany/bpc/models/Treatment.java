package com.mycompany.bpc.models;

import java.time.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author shahwaizshaban
 */
public class Treatment {
    private Long id;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private Long appointmentDuration;
    private Long physiotherapistId;
    private String name;
    private String status;
    private static final AtomicLong appointmentCounter = new AtomicLong(3000000);

    public Treatment(LocalDate appointmentDate, LocalTime appointmentTime, Long appointmentDuration, Long physiotherapistId, String status, String name) {
        this.id = appointmentCounter.incrementAndGet();
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.appointmentDuration = appointmentDuration;
        this.physiotherapistId = physiotherapistId;
        this.status = status;
        this.name = name;
    }


    public Treatment() {}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

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

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAppointmentDuration(Long appointmentDuration) {
        this.appointmentDuration = appointmentDuration;
    }

    public static Long generateAppointmentID() {
        return appointmentCounter.incrementAndGet();
    }
}
