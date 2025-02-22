package com.mycompany.bpc.models;

/**
 *
 * @author shahwaizshaban
 */
public class Treatment {
    private String name;
    private String areaOfExpertise;

    // Constructor, Getters, Setters
    public Treatment(String name, String areaOfExpertise) {
        this.name = name;
        this.areaOfExpertise = areaOfExpertise;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAreaOfExpertise() { return areaOfExpertise; }
    public void setAreaOfExpertise(String areaOfExpertise) { this.areaOfExpertise = areaOfExpertise; }
}
