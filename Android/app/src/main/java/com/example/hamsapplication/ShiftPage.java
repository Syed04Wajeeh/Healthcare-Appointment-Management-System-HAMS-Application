package com.example.hamsapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TimePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ShiftPage extends AppCompatActivity {

    Button timeStartButton;
    Button timeEndButton;
    Button dateButton;
    Button addShiftButton;
    Button refresh;
    Button back;
    TextView startTime;
    TextView endTime;
    private DatePickerDialog datePickerDialog;
    TableLayout layout;

    int startHour, startMinute, endHour, endMinute, shiftDay, shiftMonth, shiftYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);

        //all elements on the layout
        layout = (TableLayout) findViewById(R.id.shiftTable);

        timeStartButton = (Button) findViewById(R.id.startTimeButton);
        timeEndButton = (Button) findViewById(R.id.endTimeButton);
        
        addShiftButton = (Button) findViewById(R.id.addShiftButton);
        startTime = (TextView) findViewById(R.id.startTimeView);
        endTime = (TextView) findViewById(R.id.endTimeView);

        refresh = (Button) findViewById(R.id.refreshButton);
        back = (Button) findViewById(R.id.shiftBackButton);
        
        initDatePicker();
        dateButton = (Button) findViewById(R.id.dateButton);
        dateButton.setText("JAN 01 2000");

        //calendar instance for dates
        Calendar cal = Calendar.getInstance();
        int thisDay = cal.get(Calendar.DAY_OF_MONTH);
        int thisYear = cal.get(Calendar.YEAR);
        int thisMonth = cal.get(Calendar.MONTH) + 1;

        CurrentUser.getID(new CurrentUser.OnDataReceivedListener() {//get current logged in userID
            @Override
            public void onDataReceived(String uniqueID) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Shifts").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Shift> allShifts = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//loop through all shifts in the current user object
                            String ID = snapshot.getKey();
                            Shift tempShift = snapshot.getValue(Shift.class);
                            tempShift.setID(ID);
                            allShifts.add(tempShift);//add all shifts to array
                        }
                        populateTable(allShifts, layout, uniqueID);//load all elements on display
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        refresh.setOnClickListener(new View.OnClickListener() {//refresh the page
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShiftPage.this, ShiftPage.class);
                startActivity(intent);
                refresh.setEnabled(false);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {//go back to welcome screen
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShiftPage.this, WelcomeScreenDoctor.class);
                startActivity(intent);
                refresh.setEnabled(false);
            }
        });
        addShiftButton.setOnClickListener(new View.OnClickListener() {//button to add a shift, uses start time, end time, and date fields from other buttons

            @Override
            public void onClick(View view) { //button that checks for shifts

                Shift shift = new Shift(shiftDay, shiftMonth, shiftYear, startHour, startMinute, endHour, endMinute);
                if (startMinute == 0 || startMinute == 30){//verify 30 minute increments
                    if(endMinute == 0 || endMinute == 30){
                        float floatStartMin = startMinute;
                        float floatEndMin = endMinute;
                        float startTimer = startHour + (floatStartMin / 60);
                        float endTimer = endHour + (floatEndMin / 60);
                        if (startTimer < endTimer){ //verify the shift ends after it starts
                            if(shift.year >= thisYear){//verify year is not in past
                                if(shift.month >= thisMonth || shift.year > thisYear){//verify month is not in past
                                    Log.d(String.valueOf(shift.day), String.valueOf(thisDay));//verify day is not in past
                                    if(thisDay <= shift.day  || shift.month > thisMonth){//verify day is not in past
                                        CurrentUser.getID(new CurrentUser.OnDataReceivedListener() {//get current user ID
                                            @Override
                                            public void onDataReceived(String uniqueID) {
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Shifts").addListenerForSingleValueEvent(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        ArrayList<Shift> allShifts = new ArrayList<>();
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//add all shifts to array
                                                            Shift tempShift = snapshot.getValue(Shift.class);
                                                            allShifts.add(tempShift);
                                                        }
                                                        if (allShifts.size() == 0){//if there are no current shifts, no problem, simply add this shift
                                                            doctorInformation.addShift(shift);
                                                            Toast.makeText(getApplicationContext(), "Shift added, Refresh page", Toast.LENGTH_SHORT).show();
                                                        }else{
                                                            boolean send = true;
                                                            for(Shift tempShift : allShifts){
                                                                if(shift.year == tempShift.year || shift.month == tempShift.month || shift.day == tempShift.day){//if the shift we are trying to add is on same day as another shift
                                                                    if(shift.calcStartTime <= tempShift.calcStartTime && shift.calcEndTime >= tempShift.calcStartTime){//and it overlaps with said shift
                                                                        Toast.makeText(getApplicationContext(), "You cannot have overlapping shifts", Toast.LENGTH_SHORT).show();
                                                                        send = false;//do not allow the shift to be added
                                                                    }else if(shift.calcEndTime >= tempShift.calcEndTime && shift.calcStartTime <= tempShift.calcEndTime){//second case overlap check
                                                                        Toast.makeText(getApplicationContext(), "You cannot have overlapping shifts", Toast.LENGTH_SHORT).show();
                                                                        send = false;//do not allow the shift to be added
                                                                    }
                                                                }
                                                            }
                                                            if (send){
                                                                doctorInformation.addShift(shift);//if shift passed all checks, add
                                                                Toast.makeText(getApplicationContext(), "Shift added, Refresh page", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    }
                                                });
                                            }
                                        });

                                    }else{
                                        Toast.makeText(getApplicationContext(), "You cannot make a shift in the past (day)", Toast.LENGTH_SHORT).show(); //error messages
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "You cannot make a shift in the past (month)", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "You cannot make a shift in the past (year)", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getApplicationContext(), "Invalid Times: your end time cannot be before your start time", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Shifts must end at xx:00 or xx:30", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Shifts must start at xx:00 or xx:30", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void populateTable(ArrayList<Shift> allShifts, TableLayout layout, String uniqueID){//this method populates the layout

        layout.removeAllViews();//clear views

        if(allShifts.size() == 0){

        }else{
            for (Shift tempShift : allShifts){ //loop through all shifts

                //create a string with all information to display
                String concat = makeDateString(tempShift.day, tempShift.month, tempShift.year) + "  " + tempShift.startHour + ":" + tempShift.startMinute + "-" + tempShift.endHour + ":" + tempShift.endMinute;

                //create views needed
                TableRow row = new TableRow(this);
                TextView text = new TextView(this);
                Button button = new Button(this);

                //set text and button properties
                text.setText(concat);
                button.setText("CANCEL");
                button.setBackgroundColor(Color.RED);

                //add to layout
                row.addView(button);
                row.addView(text);
                layout.addView(row);

                button.setOnClickListener(new View.OnClickListener() {//button to delete shift
                    @Override
                    public void onClick(View view) {
                        Log.d(uniqueID, tempShift.getID());
                        if (FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Appointment").equals(null)){
                            FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Shifts").child(tempShift.getID()).setValue(null);
                            button.setEnabled(false);
                            layout.removeView(row);
                        }else{
                            Toast.makeText(getApplicationContext(),"You cannot delete your shift, please cancel all appointments in this shift!",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
    public static String getTodaysDate() { //method that returns a string with today's date
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {//Date dialog picker
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
                shiftDay = day;
                shiftMonth = month;
                shiftYear = year;
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    public static String makeDateString(int day, int month, int year) {//returns a string with date
        return getMonthFormat(month) + " " + day + " " + year;
    }

    public static String getMonthFormat(int month) {//associate int from 1-12 with months of the year
        if (month == 1){
            return "JAN";
        }if (month == 2){
            return "FEB";
        }if (month == 3){
            return "MAR";
        }if (month == 4){
            return "APR";
        }if (month == 5){
            return "MAY";
        }if (month == 6){
            return "JUN";
        }if (month == 7){
            return "JUL";
        }if (month == 8){
            return "AUG";
        }if (month == 9){
            return "SEP";
        }if (month == 10){
            return "OCT";
        }if (month == 11){
            return "NOV";
        }if (month == 12){
            return "DEC";
        }
        return"JAN";
    }

    public void popStartTimePicker(View view){//time picker dialog
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                startHour = selectedHour;
                startMinute = selectedMinute;
                startTime.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute));

            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, startHour, startMinute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void popEndTimePicker(View view) {//button for time picker dialog
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                endHour = selectedHour;
                endMinute = selectedMinute;
                endTime.setText(String.format(Locale.getDefault(), "%02d:%02d", endHour, endMinute));

            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, endHour, endMinute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }//displays date picker
}