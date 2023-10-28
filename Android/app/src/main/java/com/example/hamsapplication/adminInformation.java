package com.example.hamsapplication;

public class adminInformation extends generalInformation {
    protected adminInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, int registrationStatus, int accountType) {
        super(username, password, firstName, lastName, phoneNumber, address, registrationStatus, accountType);
    }
}
