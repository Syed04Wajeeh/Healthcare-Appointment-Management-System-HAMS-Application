package com.example.hamsapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class doctorInformation extends generalInformation {
    public String employeeNumber;
    public String specialties;

    protected doctorInformation(){

    }
    public doctorInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, String employeeNumber, String specialties, int registrationStatus, int accountType) {
        super(username, password, firstName, lastName, phoneNumber, address, registrationStatus, accountType);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;


        //this.shiftArray.add(new Shift(0, 0, 0, 0, 0,0, 0));
    }

    @Override
    public void addToCollection() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        this.encryptPassword();


        String doctorId = myRef.push().getKey(); // pushes doctor object to database
        myRef.child(doctorId).setValue(this);
    }

    public static void addShift(Shift shift){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();
        rootRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                String uniqueID;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    generalInformation user = snapshot.getValue(generalInformation.class);
                    if (user.username.equals(CurrentUser.username)){
                        uniqueID = snapshot.getKey(); // Get the Firebase ID
                        DatabaseReference userRef = rootRef.child("Users").child(uniqueID).child("Shifts");
                        String appointmentId = userRef.push().getKey();
                        userRef.child(appointmentId).setValue(shift);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //asd
            }
        });
    }

    public static void addAppointment(Appointment appointment){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();
        rootRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uniqueID;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    generalInformation user = snapshot.getValue(generalInformation.class);
                    if (user.username.equals(CurrentUser.username)){
                        uniqueID = snapshot.getKey(); // Get the Firebase ID
                        Log.d(uniqueID, uniqueID);
                        DatabaseReference userRef = rootRef.child("Users").child(uniqueID).child("Appointments");
                        String appointmentId = userRef.push().getKey();
                        userRef.child(appointmentId).setValue(appointment);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //asd
            }
        });
    }
}
