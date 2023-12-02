package com.example.hamsapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class patientInformation extends generalInformation {
    public String healthNumber;
    protected patientInformation(){
    }

    //setter method
    protected patientInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, String healthNumber, int registrationStatus, int accountType) {
        super(username, password, firstName, lastName, phoneNumber, address, registrationStatus, accountType);
        this.healthNumber = healthNumber;
    }

    public static void addAppointmentToPatient(Appointment appointment){//same as above, for appointments
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();

        DatabaseReference userRef = rootRef.child("Users").child(appointment.patientID).child("Appointments");
        String appointmentId = userRef.push().getKey();
        userRef.child(appointmentId).setValue(appointment);

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
