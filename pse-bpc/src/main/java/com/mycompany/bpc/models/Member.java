package com.mycompany.bpc.models;

/**
 *
 * @author shahwaizshaban
 */
public class Member {
    private Long id;
    private String fullName;
    private String address;
    private String phoneNumber;

    public Member(Long id, String fullName, String address, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Member(){}

    public Long getId() { return id; }

    public String getFullName() { return fullName; }

    public String getAddress() { return address; }

    public String getPhoneNumber() { return phoneNumber; }
}
