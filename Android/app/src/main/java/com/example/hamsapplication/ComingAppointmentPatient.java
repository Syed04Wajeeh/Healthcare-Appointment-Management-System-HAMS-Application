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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ComingAppointmentPatient extends AppCompatActivity {

    Button back;
    TableLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_appointment_patient);

        Log.d("1", "1");
        back = (Button) findViewById(R.id.backButtonComingPatient);
        layout = (TableLayout) findViewById(R.id.patComingAppt);

        Context context = this;

        CurrentUser.getID(new CurrentUser.OnDataReceivedListener() { //get the current user ID
            @Override
            public void onDataReceived(String patientID) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(patientID).child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Appointment> allComingAppointments = new ArrayList<>();
                        Log.d("2", "2");
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //Loop through all appointments stored in this doctor
                            String ID = snapshot.getKey();
                            Log.d("1", ID);
                            Appointment tempAppointment = snapshot.getValue(Appointment.class);

                                if(!tempAppointment.isPast()) {//check if appointment is past
                                    if(tempAppointment.status != 2){
                                        Log.d("1", "made it to array add");
                                        tempAppointment.ID = (ID);
                                        allComingAppointments.add(tempAppointment);
                                    }
                                }
                        }

                        for (Appointment appt : allComingAppointments){
                            Log.d("3", "3");
                            TableRow newRow = new TableRow(context);
                            TextView text = new TextView(context);
                            Button button = new Button(context);

                            Double time = (double) appt.startTime;
                            String start = (String.valueOf((int) Math.floor(time)) + ":" + String.valueOf((int)((Double)(time -  Math.floor(time)) * 60)));
                            Double endTime = time + .5;
                            String end = (String.valueOf((int) Math.floor(endTime)) + ":" + String.valueOf((int)((Double)(endTime -  Math.floor(endTime)) * 60)));
                            Log.d(start, end);
                            String date = ShiftPage.makeDateString(appt.day, appt.month, appt.year);
                            text.setText( "  " + date + ", " + start + "-" + end + "    ");

                            if (appt.status == 0){
                                button.setText("Pending");
                                button.setBackgroundColor(Color.YELLOW);
                            }else if(appt.status == 1){
                                button.setText("Accepted");
                                button.setBackgroundColor(Color.GREEN);


                            }else if(appt.status == -1){
                                button.setText("Rejected");
                                button.setBackgroundColor(Color.RED);
                            }

                            Log.d("4", "4");
                            newRow.addView(button);
                            newRow.addView(text);

                            if(appt.status == 1){
                                Button button1 = new Button(context);
                                button1.setText("CANCEL");
                                button1.setBackgroundColor(Color.RED);
                                button1.setOnClickListener(new View.OnClickListener(){
                                    @Override
                                    public void onClick(View view) {
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(patientID).child("Appointments").child(appt.ID).child("status").setValue(-2);
                                        button.setEnabled(false);
                                        layout.removeView(newRow);
                                        FirebaseDatabase.getInstance().getReference().child("Users").child(appt.doctorID).child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshots) {
                                                for (DataSnapshot snapshots : dataSnapshots.getChildren()) {
                                                    String IDT = snapshots.getKey();
                                                    Appointment temporAppointment  = snapshots.getValue(Appointment.class);
                                                    temporAppointment.ID = IDT;
                                                    if (temporAppointment.doctorID.equals(appt.doctorID) && temporAppointment.patientID.equals(appt.patientID)
                                                            && temporAppointment.startTime == appt.startTime && temporAppointment.day == appt.day && temporAppointment.month == appt.month
                                                            && temporAppointment.year == appt.year){
                                                        FirebaseDatabase.getInstance().getReference().child("Users").child(appt.doctorID).child("Appointments").child(temporAppointment.ID).child("status").setValue(-2);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                });
                                newRow.addView(button1);


                            }

                            layout.addView(newRow);
                        }
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
    }
}