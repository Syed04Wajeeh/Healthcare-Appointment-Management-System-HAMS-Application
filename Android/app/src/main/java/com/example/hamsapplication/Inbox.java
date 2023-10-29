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
    public ArrayList<ArrayList<String>> populateArray(){
        Log.d("1", "111111111111111111111111111111111111111111111111111111111111111111111111111");
        ArrayList<ArrayList<String>> allInformation = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("2", "22222222222222222222222222222222222222222222222222222222222222222222");
                ArrayList<ArrayList<String>> allInformation = new ArrayList<>();
                int i = 0; // Initialize the outer list index
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.d("3", "2333333333333333333333333333333333333333333333333333333333333333333333333333");
                    String uniqueID = snapshot.getKey(); // Get the Firebase ID
                    generalInformation user = snapshot.getValue(generalInformation.class);
                    allInformation.add(new ArrayList<String>());
                    allInformation.get(i).add(0, uniqueID);
                    if (user.accountType == 3) {
                        Log.d("doctor", "DOCTOR");
                        doctorInformation docUser = snapshot.getValue((doctorInformation.class));
                        allInformation.get(i).add(String.valueOf(docUser.registrationStatus)); //index [i][1]
                        allInformation.get(i).add(String.valueOf(docUser.accountType)); // index [i][2]
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
                        allInformation.get(i).add(String.valueOf(patUser.registrationStatus));//index [i][1]
                        allInformation.get(i).add(String.valueOf(patUser.accountType));// index [i][2]
                        allInformation.get(i).add(patUser.username);
                        allInformation.get(i).add(patUser.firstName);
                        allInformation.get(i).add(patUser.lastName);
                        allInformation.get(i).add(patUser.address);
                        allInformation.get(i).add(patUser.phoneNumber);
                        allInformation.get(i).add(patUser.healthNumber);

                    }else{
                    }
                    i++; // Move to the next row in the ArrayList
                }
                Log.d("2", "444444444444444444444444444444444444444444444444444444444444444444444");
                populateTable(allInformation, layout, layoutRej);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return allInformation;
    }

    public void populateTable(ArrayList<ArrayList<String>> masterInformation, TableLayout layout, TableLayout layoutRej){
        Log.d("2", "2453452345234624565475675678697857696879624755367356735663576456456");
        Log.d("LOGGIN" , String.valueOf(masterInformation.size()));
        layout.removeAllViews();
        layoutRej.removeAllViews();

        TableRow newRow = new TableRow(this);
        TextView inboxText = new TextView(this);
        inboxText.setText("INBOX\n");
        newRow.addView(inboxText);
        layout.addView(newRow);

        TableRow newRowR = new TableRow(this);
        TextView inboxTextR = new TextView(this);
        inboxTextR.setText("REJECTED\n");
        newRowR.addView(inboxTextR);
        layoutRej.addView(newRowR);

        for (ArrayList<String> list: masterInformation) {
            if(list.size() > 1) {
                Log.d("list size", String.valueOf(list.size()));
                Log.d("iD", list.get(0));
                String concat = "";
                for (int i = 3; i < list.size(); i++) {
                    concat = concat + list.get(i) + " ";

                }
                Log.d("TABLE POP", concat);
                if (list.get(1).equals("0")) {

                    TableRow row = new TableRow(this);
                    TextView text = new TextView(this);
                    Button button = new Button(this);
                    Button button1 = new Button(this);


                    text.setText(concat);
                    //TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
                    //text.setLayoutParams(params);
                    button.setText("REJECT");
                    button.setBackgroundColor(Color.RED);
                    button1.setText("ACCEPT");
                    button1.setBackgroundColor(Color.GREEN);

                    row.addView(button);
                    row.addView(button1);
                    row.addView(text);
                    layout.addView(row);

                    button.setOnClickListener(new View.OnClickListener() { //reject
                        @Override
                        public void onClick(View view) { //accept
                            String userId = list.get(0);
                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                            userRef.child("registrationStatus").setValue(2);
                            button.setEnabled(false);
                            button1.setEnabled(false);
                            layout.removeView(row);
                        }
                    });
                    button1.setOnClickListener(new View.OnClickListener() { //accept
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

                    // Add the button to the container


                } else if (list.get(1).equals("2")) {

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
                    layoutRej.addView(row);

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //info.registrationStatus = 1;
                            button.setEnabled(false);
                            layoutRej.removeView(row);
                        }
                    });

                    // Add the button to the container


                }
            }
        }


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        layout  = (TableLayout) findViewById(R.id.buttonContainerReq);
        layoutRej = (TableLayout) findViewById(R.id.buttonContainerRej);

        back = (Button) findViewById(R.id.backButton);
        refresh = (Button)findViewById(R.id.RefreshInbox);

        populateArray();

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
                Log.d("2", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                populateArray();

            }
        });

    }
}