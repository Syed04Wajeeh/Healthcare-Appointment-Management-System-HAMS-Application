package com.example.hamsapplication;

public class patientInformation extends generalInformation {
    protected String healthNumber;
    protected patientInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, String healthNumber) {
        super(username, password, firstName, lastName, phoneNumber, address);
        this.healthNumber = healthNumber;
    }
}
