package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class patientRegister extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText phoneNumber;
    private EditText address;
    private EditText healthCardNumber;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        registerButton = (Button) findViewById(R.id.register);

        firstName = (EditText) findViewById(R.id.patientFirstName);
        lastName = (EditText) findViewById(R.id.patientLastName);
        email = (EditText) findViewById(R.id.patientEmailUsername);
        password = (EditText) findViewById(R.id.patientAccountPassword);
        phoneNumber = (EditText) findViewById(R.id.patientPhoneNumber);
        address = (EditText) findViewById(R.id.patientAddress);
        healthCardNumber = (EditText) findViewById(R.id.patientHealthCardNumber);

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String userFirstName = firstName.getText().toString();
                String userLastName = lastName.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userPhoneNumber = phoneNumber.getText().toString();
                String userAddress = address.getText().toString();
                String userHealthCardNumber = healthCardNumber.getText().toString();

                patientInformation patient = new patientInformation(userEmail, userPassword, userFirstName,
                        userLastName, userPhoneNumber, userAddress, userHealthCardNumber);
                generalInformation.addToCollection(patient);
                Intent intent = new Intent(patientRegister.this, createdAccount.class);
                startActivity(intent);
            }
        });
    }
}