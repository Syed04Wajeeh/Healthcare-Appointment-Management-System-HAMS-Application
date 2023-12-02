package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeScreenPatient extends AppCompatActivity {

    Button logOut;
    Button pastAppointment;
    Button comingAppointment;
    Button bookAppointment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen_patient); //setting layout for the patient screen

        CurrentUser.getID(new CurrentUser.OnDataReceivedListener() {
            @Override
            public void onDataReceived(String uniqueID) {
                Log.d(uniqueID, uniqueID);
            }
        });

        logOut = (Button) findViewById(R.id.logOff2);
        comingAppointment = (Button) findViewById(R.id.comingPatientAppointments);
        pastAppointment = (Button) findViewById(R.id.pastPatientAppointment);
        bookAppointment = (Button) findViewById(R.id.findAppointment);




        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //logout button that takes you to the login page
                Intent intent = new Intent(WelcomeScreenPatient.this, login.class);
                startActivity(intent);
                logOut.setEnabled(false);
            }
        });

        comingAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //logout button that takes you to the login page
                Intent intent = new Intent(WelcomeScreenPatient.this, ComingAppointmentPatient.class);
                startActivity(intent);
                comingAppointment.setEnabled(false);
            }
        });

        pastAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //logout button that takes you to the login page
                Intent intent = new Intent(WelcomeScreenPatient.this, PastAppointmentPatient.class);
                startActivity(intent);
                pastAppointment.setEnabled(false);
            }
        });

        bookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //logout button that takes you to the login page
                Intent intent = new Intent(WelcomeScreenPatient.this, BookAppointmentPatient.class);
                startActivity(intent);
                bookAppointment.setEnabled(false);
            }
        });
    }
}