package com.mycompany.bpc.models;

/**
 *
 * @author shahwaizshaban
 */
public class Member {
    private String id;
    private String passwrod;
    private String fullName;
    private String address;
    private String phoneNumber;

    public Member(String id, String fullName, String address, String phoneNumber, String passwrod) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.passwrod = passwrod;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPasswrod() { return passwrod; }
    public void setPasswrod(String passwrod) { this.passwrod = passwrod; }
}
