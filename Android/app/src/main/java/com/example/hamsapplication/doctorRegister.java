package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class doctorRegister extends AppCompatActivity{

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

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");

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

                doctorInformation doctor = new doctorInformation(userEmail, userPassword, userFirstName, userLastName, userPhoneNumber, userAddress, userEmployeeNumber, userSpecialties, 0, 2);
                generalInformation.addToCollection(doctor);
                Intent intent = new Intent(doctorRegister.this, createdAccount.class);
                startActivity(intent);

                AESCrypt crypt = new AESCrypt();
                try {
                    String encrypted = crypt.encrypt(userPassword);
                    doctor.password = encrypted;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


                String patientId = myRef.push().getKey(); // Generate a unique key for the patient
                myRef.child(patientId).setValue(doctor);
            }
        });
    }
}