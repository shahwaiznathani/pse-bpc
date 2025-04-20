package com.mycompany.bpc.models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicLong;

/**
 *
 * @author shahwaizshaban
 */

public class Booking {
    private final String id;
    private LocalDate bookingDate;
    private LocalTime bookingTime;
    private Long bookingDuration;
    private Long physiotherapistId;
    private Long patientId;
    private String treatmentName;
    private String treatmentId;
    private String status;
    private static final AtomicLong bookingCounter = new AtomicLong(0);

    public Booking(LocalDate bookingDate, LocalTime bookingTime, Long bookingDuration, Long physiotherapistId, Long patientId, String status, String treatmentName, String treatmentId) {
        this.id = "B-" + bookingCounter.incrementAndGet();
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.bookingDuration = bookingDuration;
        this.physiotherapistId = physiotherapistId;
        this.patientId = patientId;
        this.status = status;
        this.treatmentName = treatmentName;
        this.treatmentId = treatmentId;
    }

    public String getId() { return id; }

    public LocalTime getBookingTime() {
        return bookingTime;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public Long getBookingDuration() {
        return bookingDuration;
    }

    public Long getPhysiotherapistId() {
        return physiotherapistId;
    }

    public Long getPatientId() {
        return patientId;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public String getTreatmentId() {
        return treatmentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBookingTime(LocalTime bookingTime) {
        this.bookingTime = bookingTime;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public void setPhysiotherapistId(Long physiotherapistId) {
        this.physiotherapistId = physiotherapistId;
    }

    public void setBookingDuration(Long bookingDuration) {
        this.bookingDuration = bookingDuration;
    }

    public void setTreatmentId(String treatmentId) {
        this.treatmentId = treatmentId;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    public void setPatientId(Long patientId){
        this.patientId = patientId;
    }
}
