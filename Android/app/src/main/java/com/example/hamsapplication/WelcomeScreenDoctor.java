package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeScreenDoctor extends AppCompatActivity {

    Button logOut;
    Button shift;
    Button pastAppointment;
    Button comingAppointment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen_doctor); //setting layout for the doctor screen

        logOut = (Button) findViewById(R.id.logOff);
        shift = (Button) findViewById(R.id.shiftButton);
        pastAppointment = (Button) findViewById(R.id.pastAppointmentButton);
        comingAppointment = (Button) findViewById(R.id.comingAppointmentButton);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //logout button that takes you to the login page
                Intent intent = new Intent(WelcomeScreenDoctor.this, login.class);
                startActivity(intent);
                logOut.setEnabled(false);
            }
        });

        shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //button that takes you to the shifts page
                Intent intent = new Intent(WelcomeScreenDoctor.this, ShiftPage.class);
                startActivity(intent);
                shift.setEnabled(false);
            }
        });
        pastAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //button that takes you to the past appointments page
                Intent intent = new Intent(WelcomeScreenDoctor.this, PastAppointment.class);
                startActivity(intent);
                pastAppointment.setEnabled(false);
            }
        });
        comingAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //button that takes you to the coming appointments page
                Intent intent = new Intent(WelcomeScreenDoctor.this, ComingAppointment.class);
                startActivity(intent);
                comingAppointment.setEnabled(false);
            }
        });
    }
}