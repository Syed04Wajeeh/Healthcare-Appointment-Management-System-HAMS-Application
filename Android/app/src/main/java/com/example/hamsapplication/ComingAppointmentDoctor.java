package com.example.hamsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ComingAppointmentDoctor extends AppCompatActivity {
    Button back;
    Button refresh;
    TableLayout layout;
    Switch accept;
    Context context;

    //this function loads all coming appointments into the layout
    public void populateTable(ArrayList<Appointment> allAppointments, TableLayout layout, String uniqueID){

        layout.removeAllViews(); //remove all old elements

        if(allAppointments.size() == 0){
            Log.d("Array size", "0");
        }else {
            for(Appointment tempAppointment : allAppointments){ //loop through coming appointments

                Log.d(tempAppointment.ID, tempAppointment.patientID);


                //concatenate all information to be displayed
                FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            if(snapshot.getKey().equals(tempAppointment.patientID) ){
                                PatientInformation tempPatient = snapshot.getValue(PatientInformation.class);
                                String patientConcat = tempPatient.firstName + " " + tempPatient.lastName + ", " + tempPatient.username + ", " + tempPatient.address + ", " + tempPatient.phoneNumber + ", " + tempPatient.healthNumber;
                                String appointmentConcat = ShiftPage.makeDateString(tempAppointment.day, tempAppointment.month, tempAppointment.year) + ", " + tempAppointment.startHour + ":" + tempAppointment.startMinute;
                                String concat = appointmentConcat + "   " + patientConcat;
                                Log.d(concat, tempAppointment.patientID);
                                //create all elements
                                TableRow newRow = new TableRow(context);
                                TextView inboxText = new TextView(context);
                                Button button = new Button(context);

                                button.setText("REJECT");
                                button.setBackgroundColor(Color.RED);

                                newRow.addView(button);

                                button.setOnClickListener(new View.OnClickListener() { //this is the reject button for all appointments, including accepted ones
                                    @Override
                                    public void onClick(View view) {
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Appointments").child(tempAppointment.ID).child("status").setValue(-1);
                                        button.setEnabled(false);
                                        layout.removeView(newRow);
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(tempAppointment.patientID).child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                                                for (DataSnapshot snapshots : dataSnapshots.getChildren()) {
                                                    String IDT = snapshots.getKey();
                                                    Appointment temporAppointment  = snapshots.getValue(Appointment.class);
                                                    temporAppointment.ID = IDT;
                                                    if (temporAppointment.doctorID.equals(tempAppointment.doctorID) && temporAppointment.patientID.equals(tempAppointment.patientID)
                                                    && temporAppointment.startTime == tempAppointment.startTime && temporAppointment.day == tempAppointment.day && temporAppointment.month == tempAppointment.month
                                                    && temporAppointment.year == tempAppointment.year){
                                                        FirebaseDatabase.getInstance().getReference().child("Users").child(tempAppointment.patientID).child("Appointments").child(temporAppointment.ID).child("status").setValue(-1);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }
                                });

                                if (tempAppointment.status == 0){ //accept button is only created if not already accepted
                                    Button button1 = new Button(context);
                                    button1.setText("ACCEPT");
                                    button1.setBackgroundColor(Color.GREEN);
                                    newRow.addView(button1);

                                    button1.setOnClickListener(new View.OnClickListener() { //button created to accept appointment
                                        @Override
                                        public void onClick(View view) {
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Appointments").child(tempAppointment.ID).child("status").setValue(1);
                                            button1.setEnabled(false);
                                            FirebaseDatabase.getInstance().getReference().child("Users").child(tempAppointment.patientID).child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                                                    for (DataSnapshot snapshots : dataSnapshots.getChildren()) {
                                                        String IDT = snapshots.getKey();
                                                        Appointment temporAppointment  = snapshots.getValue(Appointment.class);
                                                        temporAppointment.ID = IDT;
                                                        if (temporAppointment.doctorID.equals(tempAppointment.doctorID) && temporAppointment.patientID.equals(tempAppointment.patientID)
                                                                && temporAppointment.startTime == tempAppointment.startTime && temporAppointment.day == tempAppointment.day && temporAppointment.month == tempAppointment.month
                                                                && temporAppointment.year == tempAppointment.year){
                                                            FirebaseDatabase.getInstance().getReference().child("Users").child(tempAppointment.patientID).child("Appointments").child(temporAppointment.ID).child("status").setValue(1);
                                                        }
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });
                                        }
                                    });
                                }

                                //upload the info to the layout row
                                inboxText.setText(concat);
                                newRow.addView(inboxText);
                                layout.addView(newRow);

                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });



            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_appointment);

        layout  = (TableLayout) findViewById(R.id.docComingAppt);
        back = (Button) findViewById(R.id.backButton);
        refresh = (Button)findViewById(R.id.RefreshAppointment);
        accept = (Switch)findViewById(R.id.acceptSwitch);
        context = this;

        CurrentUser.getID(new CurrentUser.OnDataReceivedListener() { //get the current user ID
            @Override
            public void onDataReceived(String uniqueID) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Appointment> allComingAppointments = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //Loop through all appointments stored in this doctor
                            String ID = snapshot.getKey();

                            Appointment tempAppointment = snapshot.getValue(Appointment.class);

                            if (tempAppointment.status == 0 || tempAppointment.status == 1){ // check if current appointment is accepted or not looked at
                                if(!tempAppointment.isPast()) {//check if appointment is past
                                    if (accept.isChecked()){//if the autoaccept is on, accept all appointment requests
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Appointments").child(ID).child("status").setValue(1);
                                    }
                                    //set appointment ID and add to the array of coming appointments
                                    tempAppointment.ID = (ID);

                                    allComingAppointments.add(tempAppointment);
                                }
                            }

                        }

                        populateTable(allComingAppointments, layout, uniqueID); //populate the layout with this array
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener(){ //go back to doctor homepage
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        refresh.setOnClickListener(new View.OnClickListener(){//refresh the page
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComingAppointmentDoctor.this, ComingAppointmentDoctor.class);
                startActivity(intent);
                finish();
            }
        });
    }
}