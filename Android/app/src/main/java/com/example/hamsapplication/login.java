package com.example.hamsapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class login extends AppCompatActivity{


    EditText username;
    TextView rejected;
    EditText password;
    Button login;
    Button patientRegister;
    Button doctorRegister;
    String adminUser = "admin";
    String adminPass = "pass";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        CurrentUser.username = "";

        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordInput);

        login = (Button) findViewById(R.id.loginButton);
        patientRegister = (Button)findViewById(R.id.patientRegisterButton);
        doctorRegister = (Button) findViewById(R.id.doctorRegisterButton);

        GeneralInformation.hasAccount(adminUser, new GeneralInformation.AccountCheckCallback() {
            @Override
            public void onAccountCheckResult(boolean accountExists) { //creating the admin account immediately if there isnt one that exists in the database
                if (!accountExists){
                    AdminInformation admin = new AdminInformation(adminUser, adminPass, null, null, null, null, 1, 1);

                    admin.addToCollection();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener(){ //login button
            @Override
            public void onClick(View view) {

                String usernameField = username.getText().toString();
                String passwordField = password.getText().toString();

                //checking the username and the password is blank
                if (usernameField.equals("") || passwordField.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter username and password",Toast.LENGTH_SHORT).show();
                    return;
                }

                //checking if the account exists or not
                GeneralInformation.hasAccount(usernameField, new GeneralInformation.AccountCheckCallback() {
                    @Override
                    public void onAccountCheckResult(boolean accountExists) {
                        if (accountExists){
                            GeneralInformation.searchForAccount(usernameField, new GeneralInformation.AccountSearchCallback() {
                                @Override
                                public void onAccountSearchResult(GeneralInformation user) {
                                    //checking if the user field is empty
                                    if (user != null) {
                                        //checking the username and the decrypted password matches with anything in the database
                                        if (user.username.equals(usernameField) && user.decryptPassword().equals(passwordField)) {
                                            //telling the user that their account has not been approved by the admin
                                            if (user.registrationStatus == 0) {
                                                Toast.makeText(getApplicationContext(), "Your registration has not been approved yet, please wait", Toast.LENGTH_SHORT).show();
                                                return;
                                                //telling the user that their account has been denied by the admin
                                            } else if (user.registrationStatus == 2) {
                                                Toast.makeText(getApplicationContext(), "Your registration has been denied, please contact the admin at 613-XXX-XXXX", Toast.LENGTH_SHORT).show();
                                                return;
                                                //telling the user that their account has not been approved by the admin
                                            } else if (user.registrationStatus == 1) {
                                                //setting the account as an admin account
                                                if (user.accountType == 1) {
                                                    CurrentUser.username = (user.username);
                                                    Log.d("admin", CurrentUser.username);
                                                    Intent intent = new Intent(login.this, WelcomeScreenAdmin.class);
                                                    startActivity(intent);
                                                    //setting the account as an patient account
                                                } else if (user.accountType == 2) {
                                                    CurrentUser.username = (user.username);
                                                    Log.d("patient", CurrentUser.username);
                                                    Intent intent = new Intent(login.this, WelcomeScreenPatient.class);
                                                    startActivity(intent);
                                                    //setting the account as an doctor account
                                                } else if (user.accountType == 3) {
                                                    CurrentUser.username = (user.username);
                                                    Log.d("doctor", CurrentUser.username);
                                                    Intent intent = new Intent(login.this, WelcomeScreenDoctor.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        }else{
                                            //telling the user that their account password is incorrect
                                            Toast.makeText(getApplicationContext(), "incorrect password", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        //telling the user that the inputted username is not in the database
                                        Toast.makeText(getApplicationContext(), "An account with this username could not be found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            //telling the user that the inputted username is not in the database
                            Toast.makeText(getApplicationContext(), "An account with this username could not be found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
        patientRegister.setOnClickListener(new View.OnClickListener(){ //taking the user to the doctor patient page
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, PatientRegister.class);
                startActivity(intent);
            }
        });
        doctorRegister.setOnClickListener(new View.OnClickListener(){ //taking the user to the doctor register page
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, DoctorRegister.class);
                startActivity(intent);
            }
        });

    }
}