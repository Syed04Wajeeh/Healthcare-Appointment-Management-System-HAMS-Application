package com.example.hamsapplication;

public class doctorInformation extends generalInformation {
    String employeeNumber;
    String specialties;
    protected doctorInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, String employeeNumber, String specialties) {
        super(username, password, firstName, lastName, phoneNumber, address);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
    }
}
