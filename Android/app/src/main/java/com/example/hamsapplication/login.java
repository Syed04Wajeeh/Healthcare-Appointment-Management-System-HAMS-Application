package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = findViewById(R.id.usernameInput);
        username = findViewById(R.id.password);
        login = findViewById(R.id.loginButton);
        login = findViewById(R.id.patientRegisterButton);


    }
}