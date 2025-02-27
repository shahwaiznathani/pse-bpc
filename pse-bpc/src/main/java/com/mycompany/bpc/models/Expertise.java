package com.mycompany.bpc.models;

import java.util.List;
/**
 *
 * @author shahwaizshaban
 */
public class Expertise {
    private String name;
    private List<String> treatments;

    public Expertise(String name, List<String> treatments) {
        this.name = name;
        this.treatments = treatments;
    }

    public String getName() {
        return name;
    }

    public void addTreatment(String treatment) {
        if (!treatments.contains(treatment)) {
            treatments.add(treatment);
            System.out.println("Treatment added: " + treatment);
        } else {
            System.out.println("Treatment already exists: " + treatment);
        }
    }


    public List<String> getTreatments() {
        return treatments;
    }
}
