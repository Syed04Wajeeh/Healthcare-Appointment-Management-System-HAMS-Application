package com.example.hamsapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class adminInformation extends generalInformation {


    protected adminInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, int registrationStatus, int accountType) {
        super(username, password, firstName, lastName, phoneNumber, address, registrationStatus, accountType);
    }

    @Override
    public void addToCollection() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        this.encryptPassword();

        String adminId = myRef.push().getKey(); // Generate a unique key for the patient
        myRef.child(adminId).setValue(this);
    }
}
