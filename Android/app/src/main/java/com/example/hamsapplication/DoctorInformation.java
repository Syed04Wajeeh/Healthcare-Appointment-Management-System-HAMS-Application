package com.example.hamsapplication;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorInformation extends GeneralInformation {
    public String employeeNumber;
    public String specialties;

    protected DoctorInformation(){

    }
    public DoctorInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, String employeeNumber, String specialties, int registrationStatus, int accountType) {
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

    public static void addShift(Shift shift){//this function adds a Shift path if not existant in DB, then adds shift object
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();
        rootRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {

            public void onDataChange(DataSnapshot dataSnapshot) {
                String uniqueID;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GeneralInformation user = snapshot.getValue(GeneralInformation.class);
                    if (user.username.equals(CurrentUser.username)){
                        uniqueID = snapshot.getKey(); // Get the Firebase ID
                        DatabaseReference userRef = rootRef.child("Users").child(uniqueID).child("Shifts");
                        String appointmentId = userRef.push().getKey();//push to DB
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

    public static void addAppointmentToDoctor(Appointment appointment, String shiftID) {//same as above, for appointments
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();

        DatabaseReference userRef = rootRef.child("Users").child(appointment.doctorID).child("Appointments");
        String appointmentId = userRef.push().getKey();
        userRef.child(appointmentId).setValue(appointment);

        DatabaseReference userRef2 = rootRef.child("Users").child(appointment.doctorID).child("Shifts").child(shiftID).child("Appointments");
        appointmentId = userRef2.push().getKey();
        userRef2.child(appointmentId).setValue(appointment);

    }
}
