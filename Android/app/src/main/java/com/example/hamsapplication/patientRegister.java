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




public class patientRegister extends AppCompatActivity{

    public EditText firstName;
    public EditText lastName;
    public EditText email;
    public EditText password;
    public EditText phoneNumber;
    public EditText address;
    public EditText healthCardNumber;
    public Button registerButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_register);

        registerButton = (Button) findViewById(R.id.registerPatient);

        firstName = (EditText) findViewById(R.id.patientFirstName);
        lastName = (EditText) findViewById(R.id.patientLastName);
        email = (EditText) findViewById(R.id.patientEmailUsername);
        password = (EditText) findViewById(R.id.patientAccountPassword);
        phoneNumber = (EditText) findViewById(R.id.patientPhoneNumber);
        address = (EditText) findViewById(R.id.patientAddress);
        healthCardNumber = (EditText) findViewById(R.id.patientHealthCardNumber);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");


        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { //storing the information in all the fields
                String userFirstName = firstName.getText().toString();
                String userLastName = lastName.getText().toString();
                String userEmail = email.getText().toString();
                String userPassword = password.getText().toString();
                String userPhoneNumber = phoneNumber.getText().toString();
                String userAddress = address.getText().toString();
                String userHealthCardNumber = healthCardNumber.getText().toString();

                //checking if the fields are blank or not
                if (userEmail.equals("") || userFirstName.equals("") || userLastName.equals("") || userPassword.equals("") || userPhoneNumber.equals("") || userAddress.equals("") || userHealthCardNumber.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please complete all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                generalInformation.hasAccount(userEmail, new generalInformation.AccountCheckCallback() { //checking if an accounts exists with the email/username
                    @Override
                    public void onAccountCheckResult(boolean accountExists) {
                        if (accountExists){ //letting the user know the account is in use
                            Log.d("Account exists", "true");
                            Toast.makeText(getApplicationContext(), "This email is already in use", Toast.LENGTH_SHORT).show();
                        } else { //creating the account
                            Log.d("Account does not exist", "false");
                            patientInformation patient = new patientInformation(userEmail, userPassword, userFirstName, userLastName, userPhoneNumber, userAddress, userHealthCardNumber, 0, 2);
                            patient.addToCollection();
                            Intent intent = new Intent(patientRegister.this, createdAccount.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}
