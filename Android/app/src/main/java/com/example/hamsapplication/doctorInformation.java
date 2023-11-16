package com.example.hamsapplication;

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
    public ArrayList<Appointment> appointmentArray;
    public ArrayList<Shift> shiftArray;
    protected doctorInformation(){

    }
    protected doctorInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, String employeeNumber, String specialties, int registrationStatus, int accountType) {
        super(username, password, firstName, lastName, phoneNumber, address, registrationStatus, accountType);
        this.employeeNumber = employeeNumber;
        this.specialties = specialties;
        this.appointmentArray = new ArrayList<>();
        this.shiftArray = new ArrayList<>();
        appointmentArray.add(new Appointment(null, 0, 0, true, 0));
        shiftArray.add(new Shift())
    }

    @Override
    public void addToCollection() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

        this.encryptPassword();


        String doctorId = myRef.push().getKey(); // pushes doctor object to database
        myRef.child(doctorId).setValue(this);
    }

    public void addShift(Shift shift){

    }

    public void addAppointment(Appointment appointment){
        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uniqueID;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    generalInformation user = snapshot.getValue(generalInformation.class);
                    if (user.username == CurrentUser.username){
                        uniqueID = snapshot.getKey(); // Get the Firebase ID
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child(appointmentArray);

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
