package com.example.hamsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class PastAppointmentPatient extends AppCompatActivity {
    Button back;
    TableLayout layout;
    Context context;



    public void populateTable(ArrayList<Appointment> allAppointments, TableLayout layout){// populates the layout with past appointments

        layout.removeAllViews();

        if(allAppointments.size() == 0){
        }else {
            for(Appointment tempAppointment : allAppointments){ //loops through all past appointments

                FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            if(Objects.equals(snapshot.getKey(), tempAppointment.patientID)){
                                PatientInformation tempPatient = snapshot.getValue(PatientInformation.class);

                                //concatenate all information to be displayed
                                String patientConcat = tempPatient.firstName + " " + tempPatient.lastName + ", " + tempPatient.username + ", " + tempPatient.address + ", " + tempPatient.phoneNumber + ", " + tempPatient.healthNumber;
                                String appointmentConcat = ShiftPage.makeDateString(tempAppointment.day, tempAppointment.month, tempAppointment.year) + ", " + tempAppointment.startHour + ":" + tempAppointment.startMinute;
                                String concat = appointmentConcat + "   " + patientConcat;

                                //add views to the layout
                                TableRow newRow = new TableRow(context);
                                TextView inboxText = new TextView(context);
                                RatingBar ratingbar = new RatingBar(context);
                                Button button = new Button(context);

                                button.setText("RATE");
                                button.setBackgroundColor(Color.YELLOW);

                                inboxText.setText(concat);
                                newRow.addView(inboxText);
                                newRow.addView(ratingbar);
                                newRow.addView(button);
                                layout.addView(newRow);

                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        float getRating = ratingbar.getRating();
                                        button.setEnabled(false);
                                        ratingbar.setEnabled();
                                    }
                                });


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
        setContentView(R.layout.activity_past_appointment_patient);
        context = this;
        layout  = (TableLayout) findViewById(R.id.pastAppointmentTablePatient);
        back = (Button) findViewById(R.id.backButtonPastPatient);


        CurrentUser.getID(new CurrentUser.OnDataReceivedListener() {//obtain the current user's ID
            @Override
            public void onDataReceived(String uniqueID) {//uniqueID is the firebase ID
                FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Appointments").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Appointment> allPastAppointments = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//Loop through all appointments
                            String ID = snapshot.getKey();
                            Appointment tempAppointment = snapshot.getValue(Appointment.class);
                            if (tempAppointment.isPast()){//if the appointment is past, add it to the array
                                if(tempAppointment.status == 1){
                                    tempAppointment.ID = (ID);
                                    allPastAppointments.add(tempAppointment);
                                }

                            }

                        }
                        populateTable(allPastAppointments, layout);//display all elements
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener(){//button to go back to welcome screen
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}