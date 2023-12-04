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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookAppointmentPatient extends AppCompatActivity {

    Button back;
    TableLayout specialtyTable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment_patient);

        back = (Button) findViewById(R.id.backButtonSpecialty);
        specialtyTable = (TableLayout)  findViewById(R.id.specialtyTable);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //ArrayList<String> allSpecialties = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                CurrentUser.getID(new CurrentUser.OnDataReceivedListener() {
                    @Override
                    public void onDataReceived(String patID) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            GeneralInformation user = snapshot.getValue(GeneralInformation.class);

                            if (user.accountType == 3) { //if the current obj is of type doctor
                                DoctorInformation docUser = snapshot.getValue((DoctorInformation.class));
                                String docID = snapshot.getKey(); // Get the Firebase ID
                                String specialty = docUser.specialties;
                                addToTableSpecialties(specialty, docID, patID, specialtyTable);
                            }
                        }
                    }
                });


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void addToTableSpecialties(String specialty, String docID, String patID, TableLayout layout) {

            TableRow row = new TableRow(this);
            TextView text = new TextView(this);
            Button button = new Button(this);

            //set text and button properties
            text.setText( "     " + specialty );
            button.setText("View Available Appointments");
            button.setBackgroundColor(Color.GREEN);

            //add to layout
            row.addView(button);
            row.addView(text);


            layout.addView(row);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(BookAppointmentPatient.this, BookAppointmentPatientRowPage.class);
                    intent.putExtra("doc",docID);
                    intent.putExtra("pat",patID);
                    intent.putExtra("specialty",specialty);
                    startActivity(intent);
                    finish();
                }
            });
    }
}



