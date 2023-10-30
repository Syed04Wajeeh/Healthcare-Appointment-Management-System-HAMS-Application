package com.example.hamsapplication;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class patientInformation extends generalInformation {
    public String healthNumber;
    protected patientInformation(){
    }

    //setter method
    protected patientInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, String healthNumber, int registrationStatus, int accountType) {
        super(username, password, firstName, lastName, phoneNumber, address, registrationStatus, accountType);
        this.healthNumber = healthNumber;
    }

    @Override
    public void addToCollection() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        this.encryptPassword(); //encrypting password

        String patientId = myRef.push().getKey(); //creating an id for the patient in the database
        myRef.child(patientId).setValue(this);
    }
}
