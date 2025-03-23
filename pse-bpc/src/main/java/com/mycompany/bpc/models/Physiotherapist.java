package com.mycompany.bpc.models;

import java.util.List;

/**
 *
 * @author shahwaizshaban
 */
public class Physiotherapist extends Member{
    private List<Expertise> expertise;

    public Physiotherapist(Long id, String fullName, String address, String phoneNumber, String password, List<Expertise> expertise) {
        super(id, fullName, address, phoneNumber, password);
        this.expertise = expertise;
    }

    public Physiotherapist(){}

    public void addTreatment(String treatment, String expertiseName) {
        for (Expertise exp : expertise) {
            if (exp.getName().equalsIgnoreCase(expertiseName)) {
                exp.addTreatment(treatment);
                return;
            }
        }
        System.out.println("Expertise not found: " + expertiseName);
    }

    public List<Expertise> getAllExpertise() {
        return expertise;
    }

}
