package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class doctorRegister extends AppCompatActivity{

    private EditText firstName, lastName, email, password, phoneNumber, address, employeeNumber, specialties;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_register);

        registerButton = (Button) findViewById(R.id.registerDoctor);

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
            public void onClick(View view) { //stores all information in fields
                String userFirstName = firstName.getText().toString();
                String userLastName = lastName.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userPhoneNumber = phoneNumber.getText().toString();
                String userAddress = address.getText().toString();
                String userEmployeeNumber = employeeNumber.getText().toString();
                String userSpecialties = specialties.getText().toString();

                //verifies fields to be not blank
                if (userEmail.equals("") || userFirstName.equals("") || userLastName.equals("") || userPassword.equals("") || userPhoneNumber.equals("") || userAddress.equals("") || userEmployeeNumber.equals("") || userSpecialties.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                generalInformation.hasAccount(userEmail, new generalInformation.AccountCheckCallback() {//checks if an account already exists with username
                    @Override
                    public void onAccountCheckResult(boolean accountExists) {
                        if (accountExists){
                            Log.d("Account exists", "true");
                            Toast.makeText(getApplicationContext(), "This email is already in use", Toast.LENGTH_SHORT).show();
                        } else { //if account does not exist, create and send to database
                            Log.d("Account does not exist", "false");
                            doctorInformation doctor = new doctorInformation(userEmail, userPassword, userFirstName, userLastName, userPhoneNumber, userAddress, userEmployeeNumber, userSpecialties, 0, 3);
                            doctor.addToCollection();
                            Intent intent = new Intent(doctorRegister.this, createdAccount.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });
    }
}