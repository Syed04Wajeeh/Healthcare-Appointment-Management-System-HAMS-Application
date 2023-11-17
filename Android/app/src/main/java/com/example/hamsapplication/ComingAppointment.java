package com.example.hamsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ComingAppointment extends AppCompatActivity {
    Button back;
    Button refresh;
    TableLayout layout;

    //this function loads all coming appointments into the layout
    public void populateTable(ArrayList<Appointment> allAppointments, TableLayout layout, String uniqueID){

        layout.removeAllViews(); //remove all old elements

        if(allAppointments.size() == 0){
        }else {
            for(Appointment tempAppointment : allAppointments){ //loop through coming appointments

                //concatenate all information to be displayed
                patientInformation tempPatient = tempAppointment.patient;
                String patientConcat = tempPatient.firstName + " " + tempPatient.lastName + ", " + tempPatient.username + ", " + tempPatient.address + ", " + tempPatient.phoneNumber + ", " + tempPatient.healthNumber;
                String appointmentConcat = ShiftPage.makeDateString(tempAppointment.day, tempAppointment.month, tempAppointment.year) + ", " + tempAppointment.startHour + ":" + tempAppointment.startMinute + "-" + tempAppointment.endHour + ":" + tempAppointment.endMinute;
                String concat = appointmentConcat + "   " + patientConcat;

                //create all elements
                TableRow newRow = new TableRow(this);
                TextView inboxText = new TextView(this);
                Button button = new Button(this);

                button.setText("REJECT");
                button.setBackgroundColor(Color.RED);

                newRow.addView(button);

                button.setOnClickListener(new View.OnClickListener() { //this is the reject button for all appointments, including accepted ones
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Appointment").child(tempAppointment.getID()).child("status").setValue(-1);
                        button.setEnabled(false);
                        layout.removeView(newRow);
                    }
                });

                if (tempAppointment.status == 0){ //accept button is only created if not already accepted
                    Button button1 = new Button(this);
                    button1.setText("ACCEPT");
                    button1.setBackgroundColor(Color.GREEN);
                    newRow.addView(button1);

                    button1.setOnClickListener(new View.OnClickListener() { //button created to accept appointment
                        @Override
                        public void onClick(View view) {
                            FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Appointment").child(tempAppointment.getID()).child("status").setValue(1);
                            button1.setEnabled(false);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_appointment);

        layout  = (TableLayout) findViewById(R.id.IncomingAppointmentView);
        back = (Button) findViewById(R.id.backButton);
        refresh = (Button)findViewById(R.id.RefreshAppointment);

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

                                    //set appointment ID and add to the array of coming appointments
                                    tempAppointment.setID(ID);
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
                Intent intent = new Intent(ComingAppointment.this, WelcomeScreenDoctor.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener(){//refresh the page
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComingAppointment.this, ComingAppointment.class);
                startActivity(intent);
            }
        });
    }
}