package com.mycompany.bpc.models;
/**
 *
 * @author shahwaizshaban
 */
public class Patient extends Member{
    public Patient(Long id, String fullName, String address, String phoneNumber, String password) {
        super(id, fullName, address, phoneNumber, password);
    }
}
