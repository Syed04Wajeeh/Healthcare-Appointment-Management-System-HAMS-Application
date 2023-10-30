package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class welcomeScreenAdmin extends AppCompatActivity{

    Button logOut;
    Button inbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen_admin); //setting layout for the admin screen

        logOut = (Button) findViewById(R.id.logOff);
        inbox = (Button) findViewById(R.id.inbox);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //logout button that takes you to the login page
                Intent intent = new Intent(welcomeScreenAdmin.this, login.class);
                startActivity(intent);
                logOut.setEnabled(false);
            }
        });
        inbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //inbox button that takes you to the inbox page
                Intent intent = new Intent(welcomeScreenAdmin.this, Inbox.class);
                startActivity(intent);
                inbox.setEnabled(false);
            }
        });
    }
}