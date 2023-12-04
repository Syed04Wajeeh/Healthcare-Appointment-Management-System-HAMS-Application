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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Iterator;


public class BookAppointmentPatientRowPage extends AppCompatActivity {
    String docID;
    String patID;
    String specialty;

    Button back;
    TextView text;
    TableLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment_patient_row_page);

        Context context = this;
        Bundle extras = getIntent().getExtras();
        patID = extras.getString("pat");
        docID = extras.getString("doc");
        specialty = extras.getString("specialty");

        back = (Button) findViewById(R.id.backButtonBookPage);
        text = (TextView) findViewById(R.id.textView17);
        layout = (TableLayout) findViewById(R.id.shiftsAvailTable);

        Log.d("1", "11");

        FirebaseDatabase.getInstance().getReference().child("Users").child(docID).child("Shifts").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Shift> allShifts = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//loop through all shifts in the current user object
                    String ID = snapshot.getKey();
                    Shift tempShift = snapshot.getValue(Shift.class);
                    tempShift.ID = ID;
                    allShifts.add(tempShift);//add all shifts to array
                    FirebaseDatabase.getInstance().getReference().child("Users").child(docID).child("Shifts").child(tempShift.ID).child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapShots) {
                            ArrayList<Appointment> allAppointments = new ArrayList<>();

                            for (DataSnapshot snapshot : snapShots.getChildren()) {//loop through all appt in the current user object
                                String ID = snapshot.getKey();
                                Appointment tempAppointment = snapshot.getValue(Appointment.class);
                                if (tempAppointment.day == tempShift.day && tempAppointment.month == tempShift.month && tempAppointment.year == tempShift.year){
                                    tempAppointment.ID = ID;

                                    allAppointments.add(tempAppointment);//add all appt to array
                                }

                            }

                            Log.d("here", "here");
                                double currentTime = (double) tempShift.calcStartTime;
                                ArrayList<Double> timeSlots = new ArrayList<>();
                                while(currentTime != tempShift.calcEndTime) {
                                    Log.d("time", "slot");

                                    timeSlots.add(currentTime);
                                    currentTime = currentTime + (Double) 0.50;
                                }

                            for(Appointment appt: allAppointments){
                                Log.d("appt", "appt");

                                Iterator<Double> itr = timeSlots.iterator();
                                while (itr.hasNext()){
                                    Log.d("error", "error");

                                    if (appt.startTime == itr.next()){
                                        itr.remove();
                                    }
                                }
                            }

                                if(timeSlots.size() == 0){
                                    text.setText("No Appointments Available");
                                }else{
                                    text.setText("Available Appointments");

                                }
                                for (Double time:timeSlots){
                                    TableRow row = new TableRow(context);
                                    TextView text = new TextView(context);
                                    Button button = new Button(context);

                                    //set text and button properties


                                    String start = (String.valueOf((int) Math.floor(time)) + ":" + String.valueOf((int)((Double)(time -  Math.floor(time)) * 60)));
                                    Double endTime = time + .5;
                                    String end = (String.valueOf((int) Math.floor(endTime)) + ":" + String.valueOf((int)((Double)(endTime -  Math.floor(endTime)) * 60)));
                                    Log.d(start, end);
                                    String date = ShiftPage.makeDateString(tempShift.day, tempShift.month, tempShift.year);
                                    text.setText( "  " + date + ", " + start + "-" + end);
                                    button.setText("Book Appointment");
                                    button.setBackgroundColor(Color.GREEN);

                                    //add to layout
                                    row.addView(button);
                                    row.addView(text);


                                    layout.addView(row);

                                    button.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Appointment appointment = new Appointment(patID, docID, tempShift.day, tempShift.month, tempShift.year, (int) Math.floor(time), (int)((time - Math.floor(time)) * 60));
                                            patientInformation.addAppointmentToPatient(appointment);
                                            doctorInformation.addAppointmentToDoctor(appointment, tempShift.ID);
                                            Toast.makeText(getApplicationContext(), "You have successfully booked this timeslot", Toast.LENGTH_SHORT).show();
                                            finish();
                                        }
                                    });
                                }

                        }



                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
                @Override
                public void onCancelled (@NonNull DatabaseError error){

                }

        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
