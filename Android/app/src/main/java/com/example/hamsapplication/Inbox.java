package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Inbox extends AppCompatActivity {
    Button back;
    Button refresh;
    TableLayout layout;
    TableLayout layoutRej;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        ArrayList<ArrayList<String>> allInformation = new ArrayList<>();

        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        int i = 0; // Initialize the outer list index
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String uniqueID = snapshot.getKey(); // Get the Firebase ID
                            generalInformation user = snapshot.getValue(generalInformation.class);
                            allInformation.add(new ArrayList<String>());
                            allInformation.get(i).add(0, uniqueID);
                            if (user.accountType == 3) {
                                Log.d("doctor", "DOCTOR");
                                doctorInformation docUser = snapshot.getValue((doctorInformation.class));
                                allInformation.get(i).add(String.valueOf(docUser.registrationStatus));
                                allInformation.get(i).add(String.valueOf(docUser.accountType));
                                allInformation.get(i).add(docUser.username);
                                allInformation.get(i).add(docUser.firstName);
                                allInformation.get(i).add(docUser.lastName);
                                allInformation.get(i).add(docUser.address);
                                allInformation.get(i).add(docUser.phoneNumber);
                                allInformation.get(i).add(docUser.employeeNumber);
                                allInformation.get(i).add(docUser.specialties);

                            } else if (user.accountType == 2) {
                                Log.d("patient", "aptient");
                                patientInformation patUser = snapshot.getValue((patientInformation.class));
                                allInformation.get(i).add(String.valueOf(patUser.registrationStatus));
                                allInformation.get(i).add(String.valueOf(patUser.accountType));
                                allInformation.get(i).add(patUser.username);
                                allInformation.get(i).add(patUser.firstName);
                                allInformation.get(i).add(patUser.lastName);
                                allInformation.get(i).add(patUser.address);
                                allInformation.get(i).add(patUser.phoneNumber);
                                allInformation.get(i).add(patUser.healthNumber);

                            }else{
                                Log.d("NOT WORKING", "NOT WORKING ASDIUAGR8W7FIVUD FBSUYFBESFJHSBDFJSD");
                            }
                            i++; // Move to the next row in the ArrayList
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        int i = 0;

        layout  = (TableLayout) findViewById(R.id.buttonContainerReq);
        layoutRej = (TableLayout) findViewById(R.id.buttonContainerRej);

        back = (Button) findViewById(R.id.backButton);
        refresh = (Button)findViewById(R.id.RefreshInbox);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inbox.this, welcomeScreenAdmin.class);
                startActivity(intent);
            }
        });

        refresh.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                int i = 0;
                for (ArrayList<String> list : allInformation){
                    for (String e : list){
                        Log.d(String.valueOf(i), e);
                    }

                }

            }
        });

        TableRow newRow = new TableRow(this);
        TextView inboxText = new TextView(this);
        inboxText.setText("INBOX\n");
        newRow.addView(inboxText);
        layout.addView(newRow);

        TableRow newRowr = new TableRow(this);
        TextView inboxTextr = new TextView(this);
        inboxTextr.setText("REJECTED\n");
        newRowr.addView(inboxTextr);
        layoutRej.addView(newRowr);

        for (generalInformation info : generalInformation.collection) {
            if (info.registrationStatus == 0){

                TableRow row = new TableRow(this);
                TextView text = new TextView(this);
                Button button = new Button(this);
                Button button1 = new Button(this);

                text.setText(info.username);
                //TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
                //text.setLayoutParams(params);
                button.setText("REJECT");
                button.setBackgroundColor(Color.RED);
                button1.setText("ACCEPT");
                button1.setBackgroundColor(Color.GREEN);

                row.addView(text);
                text.setId(View.generateViewId());
                button.setId(View.generateViewId());
                button1.setId(View.generateViewId());

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        info.registrationStatus = 2;
                        button.setEnabled(false);
                        button1.setEnabled(false);
                    }
                });
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        info.registrationStatus = 1;
                        button.setEnabled(false);
                        button1.setEnabled(false);
                    }
                });

                // Add the button to the container

                row.addView(button);
                row.addView(button1);
                layout.addView(row);

            }else if (info.registrationStatus == 2){
                TableRow row = new TableRow(this);
                TextView text = new TextView(this);
                Button button = new Button(this);
                text.setText(info.username);

                button.setText("ACCEPT");
                button.setBackgroundColor(Color.GREEN);

                row.addView(text);
                text.setId(View.generateViewId());
                button.setId(View.generateViewId());


                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        info.registrationStatus = 1;
                        button.setEnabled(false);
                    }
                });

                // Add the button to the container

                row.addView(button);
                layoutRej.addView(row);
            }

        }



    }
}