package com.example.hamsapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreatedAccount extends AppCompatActivity {
    Button homeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_account);

        homeButton = (Button) findViewById(R.id.homeButton);



        homeButton.setOnClickListener(new View.OnClickListener(){ // button to go back to login
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreatedAccount.this, login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}