package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private Button patientRegister;
    private Button doctorRegister;
    private String adminUser = "admin";
    private String adminPass = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = findViewById(R.id.usernameInput);
        password = findViewById(R.id.passwordInput);

        login = (Button) findViewById(R.id.loginButton);
        patientRegister = (Button)findViewById(R.id.patientRegisterButton);
        doctorRegister = (Button) findViewById(R.id.doctorRegisterButton);

        if (!generalInformation.hasAccount("admin")) {
            generalInformation admin = new generalInformation("admin", "pass", null, null, null, null);
            generalInformation.addToCollection(admin);
        }

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String usernameField = username.getText().toString().toLowerCase();
                String passwordField = password.getText().toString();
                if (generalInformation.hasAccount(usernameField)){
                    generalInformation currentAccount = generalInformation.searchForAccount(usernameField);
                    if (currentAccount.username == usernameField && currentAccount.password == passwordField){
                        Intent intent = new Intent(login.this, welcomeScreen.class);
                        startActivity(intent);
                    }
                }

            }
        });
        patientRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, patientRegister.class);
                startActivity(intent);
            }
        });
        doctorRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, doctorRegister.class);
                startActivity(intent);
            }
        });
    }
}