package com.example.hamsapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class doctorInformation extends generalInformation {
    public String employeeNumber;
    public String specialties;
    protected doctorInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, String employeeNumber, String specialties, int registrationStatus, int accountType) {
        super(username, password, firstName, lastName, phoneNumber, address, registrationStatus, accountType);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
    }

    @Override
    public void addToCollection() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        this.encryptPassword();


        String doctorId = myRef.push().getKey(); // Generate a unique key for the patient
        myRef.child(doctorId).setValue(this);
    }
}
