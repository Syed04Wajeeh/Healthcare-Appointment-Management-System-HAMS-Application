package com.example.hamsapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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

        rejected = (TextView)findViewById(R.id.rejectedPopup);

        generalInformation.addToCollection(new adminInformation("admin", "pass", null, null, null, null, 1));


        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                int currentType = 0;
                String usernameField = username.getText().toString();
                String passwordField = password.getText().toString();
                for(int i = 0; i < generalInformation.collection.size(); i++){
                }

                if (generalInformation.hasAccount(usernameField)) {
                    generalInformation currentAccount = generalInformation.searchForAccount(usernameField);
                    if (currentAccount instanceof adminInformation){
                        currentType = 0;
                    }else if(currentAccount instanceof patientInformation){
                        currentType = 1;
                    } else if (currentAccount instanceof doctorInformation) {
                        currentType = 2;
                    }

                    if (!currentAccount.password.equals(null)){
                        if (  usernameField.equals(currentAccount.username)  &&  passwordField.equals(currentAccount.password)) {
                            if (currentAccount.getStatus() == 0){
                                Toast.makeText(getApplicationContext(),"Your registration has not been approved yet, please wait",Toast.LENGTH_SHORT).show();
                                return;
                            } else if (currentAccount.getStatus() == 2){
                                Toast.makeText(getApplicationContext(),"Your registration has been denied, please contact the admin at 613-XXX-XXXX",Toast.LENGTH_SHORT).show();
                                return;
                            } else if (currentAccount.getStatus() == 1) {
                                if (currentType == 0){
                                    Intent intent = new Intent(login.this, welcomeScreenAdmin.class);
                                    startActivity(intent);
                                }else if(currentType == 1){
                                    Intent intent = new Intent(login.this, WelcomeScreenPatient.class);
                                    startActivity(intent);
                                }else if(currentType == 2){
                                    Intent intent = new Intent(login.this, WelcomeScreenDoctor.class);
                                    startActivity(intent);
                                }
                            }
                        }
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Unable to find an account\n registered with this username",Toast.LENGTH_SHORT).show();
                }

                login.setEnabled(false);
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