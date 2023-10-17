package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class welcomeScreen extends AppCompatActivity{

    Button logOut;
    TextView youAreText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        logOut = (Button) findViewById(R.id.logOff);
        youAreText = (TextView) findViewById(R.id.youAreText);
        if (generalInformation.currentTypeOf == 0){
            youAreText.setText("You are a: Admin");
        }else if(generalInformation.currentTypeOf == 1){
            youAreText.setText("You are a: Patient");
        }else if(generalInformation.currentTypeOf == 2){
            youAreText.setText("You are a: Doctor");
        }



        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(welcomeScreen.this, login.class);
                startActivity(intent);
            }
        });
    }
}