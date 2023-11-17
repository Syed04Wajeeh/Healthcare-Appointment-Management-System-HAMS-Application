package com.example.hamsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class PastAppointment extends AppCompatActivity {
    Button back;
    TableLayout layout;



    public void populateTable(ArrayList<Appointment> allAppointments, TableLayout layout){

        layout.removeAllViews();

        if(allAppointments.size() == 0){
        }else {
            for(Appointment tempAppointment : allAppointments){
                patientInformation tempPatient = tempAppointment.patient;
                String patientConcat = tempPatient.firstName + " " + tempPatient.lastName + ", " + tempPatient.username + ", " + tempPatient.address + ", " + tempPatient.phoneNumber + ", " + tempPatient.healthNumber;
                String appointmentConcat = ShiftPage.makeDateString(tempAppointment.day, tempAppointment.month, tempAppointment.year) + ", " + tempAppointment.startHour + ":" + tempAppointment.startMinute + "-" + tempAppointment.endHour + ":" + tempAppointment.endMinute;
                String concat = appointmentConcat + "   " + patientConcat;
                TableRow newRow = new TableRow(this);
                TextView inboxText = new TextView(this);
                inboxText.setText(concat);
                newRow.addView(inboxText);
                layout.addView(newRow);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_appointment);

        layout  = (TableLayout) findViewById(R.id.PastAppointmentView);
        back = (Button) findViewById(R.id.backButton);

        CurrentUser.getID(new CurrentUser.OnDataReceivedListener() {
            @Override
            public void onDataReceived(String uniqueID) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Appointment> allPastAppointments = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String ID = snapshot.getKey();
                            Appointment tempAppointment = snapshot.getValue(Appointment.class);
                            if (tempAppointment.past){
                                tempAppointment.setID(ID);
                                allPastAppointments.add(tempAppointment);
                            }

                        }
                        populateTable(allPastAppointments, layout);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PastAppointment.this, WelcomeScreenDoctor.class);
                startActivity(intent);
            }
        });
    }
}