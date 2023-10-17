package com.example.hamsapplication;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
public class login extends AppCompatActivity {

    EditText username;
    TextView result;
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

        result = (TextView)findViewById(R.id.textView5);

        if (!generalInformation.hasAccount("admin")) {
            generalInformation admin = new generalInformation("admin", "pass", null, null, null, null);
            generalInformation.addToCollection(admin);
        }

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                for(int i = 0; i < generalInformation.collection.size(); i ++){
                    Log.e("iter", generalInformation.collection.get(i).username);
                }
                String usernameField = username.getText().toString();
                String passwordField = password.getText().toString();

                //if (!generalInformation.collection.isEmpty()){
                    //result.setText("woooo");
                //}
                if (generalInformation.hasAccount(usernameField)){
                    Log.e("myTag", usernameField);
                    Log.e("myTag", "admin");
                }else{
                    Log.e("urTag", usernameField);
                    Log.e("urTag", "admin");
                }


                if (generalInformation.hasAccount(usernameField)) {
                    result.setText("weee");
                    generalInformation currentAccount = generalInformation.searchForAccount(usernameField);
                }
                if (  usernameField == "admin" &&  passwordField == "pass"){
                    result.setText("wahhhh");
                    Intent intent = new Intent(login.this, welcomeScreen.class);
                    startActivity(intent);
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