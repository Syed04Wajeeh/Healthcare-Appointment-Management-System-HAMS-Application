package com.example.hamsapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminInformation extends GeneralInformation {


    protected AdminInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, int registrationStatus, int accountType) {
        super(username, password, firstName, lastName, phoneNumber, address, registrationStatus, accountType);
    }

    @Override
    public void addToCollection() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        this.encryptPassword();

        //push admin object to database
        String adminId = myRef.push().getKey();
        myRef.child(adminId).setValue(this);
    }
}
