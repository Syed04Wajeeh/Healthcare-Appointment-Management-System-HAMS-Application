package com.example.hamsapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity{


    EditText username;
    TextView rejected;
    EditText password;
    Button login;
    Button patientRegister;
    Button doctorRegister;
    String adminUser = "admin";
    String adminPass = "password";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = (EditText) findViewById(R.id.usernameInput);
        password = (EditText) findViewById(R.id.passwordInput);

        login = (Button) findViewById(R.id.loginButton);
        patientRegister = (Button)findViewById(R.id.patientRegisterButton);
        doctorRegister = (Button) findViewById(R.id.doctorRegisterButton);



        //adminInformation admin = new adminInformation("admin", "pass", null, null, null, null, 1, 0);

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("Users");
        /*
        FirebaseDatabase.getInstance().getReference().child("Users")

                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            generalInformation user = snapshot.getValue(generalInformation.class);
                            Log.d("TESTING", String.valueOf(user.username));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        AESCrypt crypt = new AESCrypt();
        try {

        String encrypted = crypt.encrypt(admin.password);
            admin.password = encrypted;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String doctorId = myRef.push().getKey(); // Generate a unique key for the patient
        myRef.child(doctorId).setValue(admin);
        */

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String usernameField = username.getText().toString();
                String passwordField = password.getText().toString();

                if (usernameField.equals("") || passwordField.equals("")){
                    Toast.makeText(getApplicationContext(),"Please enter username and password",Toast.LENGTH_SHORT).show();
                    return;
                }

                generalInformation.hasAccount(usernameField, new generalInformation.AccountCheckCallback() {
                    @Override
                    public void onAccountCheckResult(boolean accountExists) {
                        if (accountExists){
                            generalInformation.searchForAccount(usernameField, new generalInformation.AccountSearchCallback() {
                                @Override
                                public void onAccountSearchResult(generalInformation user) {
                                    if (user != null) {
                                        if (user.username.equals(usernameField) && user.decryptPassword().equals(passwordField)) {
                                            Log.d("user and password match", "");
                                            if (user.registrationStatus == 0) {
                                                Log.d("registration not approved", "");
                                                Toast.makeText(getApplicationContext(), "Your registration has not been approved yet, please wait", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else if (user.registrationStatus == 2) {
                                                Log.d("registration denied", "");
                                                Toast.makeText(getApplicationContext(), "Your registration has been denied, please contact the admin at 613-XXX-XXXX", Toast.LENGTH_SHORT).show();
                                                return;
                                            } else if (user.registrationStatus == 1) {
                                                Log.d("reg approved", "");
                                                if (user.accountType == 1) {
                                                    Log.d("instance of admin", "");
                                                    Intent intent = new Intent(login.this, welcomeScreenAdmin.class);
                                                    startActivity(intent);
                                                } else if (user.accountType == 2) {
                                                    Log.d("instance of patient", "");
                                                    Intent intent = new Intent(login.this, WelcomeScreenPatient.class);
                                                    startActivity(intent);
                                                } else if (user.accountType == 3) {
                                                    Log.d("instance of doctor", "");
                                                    Intent intent = new Intent(login.this, WelcomeScreenDoctor.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        }else{
                                            Toast.makeText(getApplicationContext(), "incorrect password", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "An account with this username could not be found", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getApplicationContext(), "An account with this username could not be found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
        patientRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, patientRegister.class);
                startActivity(intent);
                patientRegister.setEnabled(false);
            }
        });
        doctorRegister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login.this, doctorRegister.class);
                startActivity(intent);
                doctorRegister.setEnabled(false);
            }
        });

    }
}