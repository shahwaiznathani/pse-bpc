package com.mycompany.bpc.models;

/**
 *
 * @author shahwaizshaban
 */
public class Member {
    private final Long id;
    private final String fullName;
    private final String address;
    private final String phoneNumber;

    public Member(Long id, String fullName, String address, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() { return id; }

    public String getFullName() { return fullName; }

    public String getAddress() { return address; }

    public String getPhoneNumber() { return phoneNumber; }
}
