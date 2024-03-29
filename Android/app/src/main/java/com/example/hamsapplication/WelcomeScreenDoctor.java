package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        CurrentUser.getID(new CurrentUser.OnDataReceivedListener() {
            @Override
            public void onDataReceived(String uniqueID) {
                Log.d(uniqueID, uniqueID);
            }
        });
        logOut = (Button) findViewById(R.id.logOff);
        shift = (Button) findViewById(R.id.shiftButton);
        pastAppointment = (Button) findViewById(R.id.pastAppointmentButton);
        comingAppointment = (Button) findViewById(R.id.comingAppointmentButton);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //logout button that takes you to the login page
                Intent intent = new Intent(WelcomeScreenDoctor.this, login.class);
                startActivity(intent);
            }
        });

        shift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //button that takes you to the shifts page
                Intent intent = new Intent(WelcomeScreenDoctor.this, ShiftPage.class);
                startActivity(intent);
            }
        });
        pastAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //button that takes you to the past appointments page
                Intent intent = new Intent(WelcomeScreenDoctor.this, PastAppointmentDoctor.class);
                startActivity(intent);
            }
        });
        comingAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //button that takes you to the coming appointments page
                Intent intent = new Intent(WelcomeScreenDoctor.this, ComingAppointmentDoctor.class);
                startActivity(intent);
            }
        });
    }
}