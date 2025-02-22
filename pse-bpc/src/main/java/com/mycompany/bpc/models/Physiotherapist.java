package com.mycompany.bpc.models;

import java.util.List;

/**
 *
 * @author shahwaizshaban
 */
public class Physiotherapist extends Member{
    private List<String> expertise;
    private List<Treatment> treatmentsOffered;

    public Physiotherapist(String id, String fullName, String address, String phoneNumber, List<String> expertise) {
        super(id, fullName, address, phoneNumber);
        this.expertise = expertise;
    }

    // Methods for managing treatments and timetable
    public void addTreatment(Treatment treatment) {
        treatmentsOffered.add(treatment);
    }

    public void removeTreatment(Treatment treatment) {
        treatmentsOffered.remove(treatment);
    }

    public void viewTimetable() {
        // Implement timetable logic
    }

    // Getters and Setters for expertise and treatmentsOffered
    public List<String> getExpertise() { return expertise; }
    public void setExpertise(List<String> expertise) { this.expertise = expertise; }

    public List<Treatment> getTreatmentsOffered() { return treatmentsOffered; }
    public void setTreatmentsOffered(List<Treatment> treatmentsOffered) { this.treatmentsOffered = treatmentsOffered; }
}
