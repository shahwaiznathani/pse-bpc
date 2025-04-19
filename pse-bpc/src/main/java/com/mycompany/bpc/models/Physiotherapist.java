package com.mycompany.bpc.models;

import java.util.List;

/**
 *
 * @author shahwaizshaban
 */
public class Physiotherapist extends Member{
    private List<String> expertise;

    public Physiotherapist(Long id, String fullName, String address, String phoneNumber, List<String> expertise) {
        super(id, fullName, address, phoneNumber);
        this.expertise = expertise;
    }

    public List<String> getAllExpertise() {
        return expertise;
    }
}
