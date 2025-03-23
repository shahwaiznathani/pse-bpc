package com.mycompany.bpc.models;

/**
 *
 * @author shahwaizshaban
 */
public class Member {
    private Long id;
    private String password;
    private String fullName;
    private String address;
    private String phoneNumber;

    public Member(Long id, String fullName, String address, String phoneNumber, String password) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Member(){}

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
}
