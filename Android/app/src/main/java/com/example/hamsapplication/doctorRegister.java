package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class doctorRegister extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText email;
    private EditText password;
    private EditText phoneNumber;
    private EditText address;
    private EditText employeeNumber;
    private EditText specialties;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);

        registerButton = (Button) findViewById(R.id.register);

        firstName = (EditText) findViewById(R.id.doctorFirstName);
        lastName = (EditText) findViewById(R.id.doctorLastName);
        email = (EditText) findViewById(R.id.doctorEmailUsername);
        password = (EditText) findViewById(R.id.doctorAccountPassword);
        phoneNumber = (EditText) findViewById(R.id.doctorPhoneNumber);
        address = (EditText) findViewById(R.id.doctorAddress);
        employeeNumber = (EditText) findViewById(R.id.doctorNumber);
        specialties = (EditText) findViewById(R.id.doctorSpecialty);


        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String userFirstName = firstName.getText().toString();
                String userLastName = lastName.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userPhoneNumber = phoneNumber.getText().toString();
                String userAddress = address.getText().toString();
                String userEmployeeNumber = address.getText().toString();
                String userSpecialties = address.getText().toString();

                doctorInformation doctor = new doctorInformation(userFirstName, userLastName, userEmail,
                        userPassword, userPhoneNumber, userAddress, userEmployeeNumber, userSpecialties);
                generalInformation.addToCollection(doctor);
            }
        });
    }
}