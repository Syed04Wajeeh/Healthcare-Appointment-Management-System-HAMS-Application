package com.example.hamsapplication;

public class doctorInformation extends generalInformation {
    public String employeeNumber;
    public String specialties;
    protected doctorInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, String employeeNumber, String specialties, int registrationStatus) {
        super(username, password, firstName, lastName, phoneNumber, address, registrationStatus);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
    }
}
