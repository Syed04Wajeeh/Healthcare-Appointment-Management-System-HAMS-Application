package com.example.hamsapplication;

public class patientInformation extends generalInformation {
    public String healthNumber;
    protected patientInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, String healthNumber, int registrationStatus) {
        super(username, password, firstName, lastName, phoneNumber, address, registrationStatus);
        this.healthNumber = healthNumber;
    }
}
