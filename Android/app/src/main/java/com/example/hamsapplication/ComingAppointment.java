package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.ArrayList;

public class ComingAppointment extends AppCompatActivity {
    Button back;
    Button refresh;
    TableLayout layout;

    // this method loops through the database and creates
    //a two dimensional ArrayList, storing each objects' information in a different array, and then storing
    //more specific information in the subsequent indexes
    public ArrayList<ArrayList<String>> populateArray(){
        ArrayList<ArrayList<String>> allInformation = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ArrayList<String>> allInformation = new ArrayList<>();

                int i = 0; //outer list index
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String uniqueID = snapshot.getKey(); // Get the Firebase ID
                    generalInformation user = snapshot.getValue(generalInformation.class);

                    allInformation.add(new ArrayList<String>());
                    allInformation.get(i).add(0, uniqueID); //add unique firebase id to the first elem in Arraylist
                    patientInformation patUser = snapshot.getValue((patientInformation.class));

                    allInformation.get(i).add(String.valueOf(patUser.registrationStatus));//index [i][1]
                    allInformation.get(i).add(String.valueOf(patUser.accountType));// index [i][2]
                    allInformation.get(i).add(patUser.username);
                    allInformation.get(i).add(patUser.firstName);
                    allInformation.get(i).add(patUser.lastName);
                    allInformation.get(i).add(patUser.address);
                    allInformation.get(i).add(patUser.phoneNumber);
                    allInformation.get(i).add(patUser.healthNumber);
                i++; // Move to the next row in the ArrayList
            }
            populateTable(allInformation, layout); //using this updated ArrayList, fill the layouts
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
            return allInformation;
    }

    public void populateTable(ArrayList<ArrayList<String>> masterInformation, TableLayout layout){

        //clear both layouts
        layout.removeAllViews();

        //add INBOX and REJECTED text to the layouts
        TableRow newRow = new TableRow(this);
        TextView inboxText = new TextView(this);
        inboxText.setText("INBOX\n");
        newRow.addView(inboxText);
        layout.addView(newRow);

        for (ArrayList<String> list: masterInformation) {// loop through each obj
            if(list.size() > 1) { //create a string to display all user info
                //makes sure it doesnt iterate through admin object!
                String concat = "";
                for (int i = 3; i < list.size(); i++) {
                    concat = concat + list.get(i) + " ";
                }

                if (list.get(1).equals("0")) {//if object is of unchecked registration status (not accepted or rejected yet)

                    TableRow row = new TableRow(this);
                    TextView text = new TextView(this);
                    Button button = new Button(this);
                    Button button1 = new Button(this);


                    text.setText(concat);
                    button.setText("REJECT");
                    button.setBackgroundColor(Color.RED);
                    button1.setText("ACCEPT");
                    button1.setBackgroundColor(Color.GREEN);

                    //add views to row, add row to layout
                    row.addView(button);
                    row.addView(button1);
                    row.addView(text);
                    layout.addView(row);

                    button.setOnClickListener(new View.OnClickListener() { //reject appointment
                        @Override
                        public void onClick(View view) {
                            String userId = list.get(0);
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            userRef.child("registrationStatus").setValue(2);
                            button.setEnabled(false);
                            button1.setEnabled(false);
                            layout.removeView(row);
                        }
                    });
                    button1.setOnClickListener(new View.OnClickListener() { //accept appointment
                        @Override
                        public void onClick(View view) {
                            String userId = list.get(0);
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            userRef.child("registrationStatus").setValue(1);
                            button.setEnabled(false);
                            button1.setEnabled(false);
                            layout.removeView(row);
                        }
                    });
                } else if (list.get(1).equals("1")) {// if object is already accepted

                    TableRow row = new TableRow(this);
                    TextView text = new TextView(this);
                    Button button = new Button(this);
                    text.setText(concat);

                    button.setText("ACCEPT");
                    button.setBackgroundColor(Color.GREEN);


                    text.setId(View.generateViewId());
                    button.setId(View.generateViewId());

                    row.addView(button);
                    row.addView(text);
                    button.setOnClickListener(new View.OnClickListener() { //reject appointment
                        @Override
                        public void onClick(View view) {
                            String userId = list.get(0);
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            userRef.child("registrationStatus").setValue(2);
                            button.setEnabled(false);
                            layout.removeView(row);
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_appointment);

        layout  = (TableLayout) findViewById(R.id.PastAppointmentView);
        back = (Button) findViewById(R.id.backButton);




        populateArray(); //populate the layouts

        back.setOnClickListener(new View.OnClickListener(){ //go back to doctor homepage
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ComingAppointment.this, WelcomeScreenPatient.class);
                startActivity(intent);
            }
        });
    }
}