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
    TextView startTime;
    TextView endTime;
    private DatePickerDialog datePickerDialog;
    TableLayout layout;
    

    int startHour, startMinute, endHour, endMinute, shiftDay, shiftMonth, shiftYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);

        layout = (TableLayout) findViewById(R.id.shiftTable);

        timeStartButton = (Button) findViewById(R.id.startTimeButton);
        timeEndButton = (Button) findViewById(R.id.endTimeButton);
        
        addShiftButton = (Button) findViewById(R.id.addShiftButton);
        startTime = (TextView) findViewById(R.id.startTimeView);
        endTime = (TextView) findViewById(R.id.endTimeView);
        
        initDatePicker();
        dateButton = (Button) findViewById(R.id.dateButton);
        dateButton.setText(getTodaysDate());

        Calendar cal = Calendar.getInstance();
        int thisDay = cal.get(Calendar.DAY_OF_MONTH);
        int thisYear = cal.get(Calendar.YEAR);
        int thisMonth = cal.get(Calendar.MONTH) + 1;

        CurrentUser.getID(new CurrentUser.OnDataReceivedListener() {
            @Override
            public void onDataReceived(String uniqueID) {
                FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Shifts").addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Shift> allShifts = new ArrayList<>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Shift tempShift = snapshot.getValue(Shift.class);
                            allShifts.add(tempShift);
                        }
                        populateTable(allShifts, layout, uniqueID);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        addShiftButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) { //button that checks for shifts

                Shift shift = new Shift(shiftDay, shiftMonth, shiftYear, startHour, startMinute, endHour, endMinute);
                if (startMinute == 0 || startMinute == 30){
                    if(endMinute == 0 || endMinute == 30){
                        float floatStartMin = startMinute;
                        float floatEndMin = endMinute;
                        float startTimer = startHour + (floatStartMin / 60);
                        float endTimer = endHour + (floatEndMin / 60);
                        if (startTimer < endTimer){
                            if(shift.year >= thisYear){
                                if(shift.month >= thisMonth || shift.year > thisYear){
                                    if(shift.day >= thisDay - 1 || shift.month > thisMonth){
                                        CurrentUser.getID(new CurrentUser.OnDataReceivedListener() {
                                            @Override
                                            public void onDataReceived(String uniqueID) {
                                                FirebaseDatabase.getInstance().getReference().child("Users").child(uniqueID).child("Shifts").addListenerForSingleValueEvent(new ValueEventListener() {

                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        ArrayList<Shift> allShifts = new ArrayList<>();
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            Shift tempShift = snapshot.getValue(Shift.class);
                                                            allShifts.add(tempShift);
                                                        }
                                                        if (allShifts.size() == 0){
                                                            doctorInformation.addShift(shift);
                                                        }else{
                                                            for(Shift tempShift : allShifts){
                                                                if(shift.year != tempShift.year || shift.month != tempShift.month || shift.day != tempShift.day){
                                                                    doctorInformation.addShift(shift);
                                                                }else if(shift.calcStartTime < tempShift.calcStartTime && shift.calcEndTime < tempShift.calcStartTime){
                                                                    doctorInformation.addShift(shift);
                                                                }else if(shift.calcEndTime > tempShift.calcEndTime && shift.calcStartTime > shift.calcEndTime){
                                                                    doctorInformation.addShift(shift);
                                                                }else{
                                                                    Toast.makeText(getApplicationContext(), "You cannot have overlapping shifts", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        }
                                                        populateTable(allShifts, layout, uniqueID);
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    }
                                                });
                                            }
                                        });

                                        }else{
                                        Toast.makeText(getApplicationContext(), "You cannot make a shift in the past", Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "You cannot make a shift in the past", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "You cannot make a shift in the past", Toast.LENGTH_SHORT).show();
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

    public void populateTable(ArrayList<Shift> allShifts, TableLayout layout, String uniqueID){

        layout.removeAllViews();

        if(allShifts.size() == 0){

        }else{
            for (Shift tempShift : allShifts){
                String concat = makeDateString(tempShift.day, tempShift.month, tempShift.year) + "  " + tempShift.startHour + ":" + tempShift.startMinute + "-" + tempShift.endHour + ":" + tempShift.endMinute;
                TableRow row = new TableRow(this);
                TextView text = new TextView(this);
                Button button = new Button(this);

                text.setText(concat);
                button.setText("CANCEL");
                button.setBackgroundColor(Color.RED);

                row.addView(button);
                row.addView(text);
                layout.addView(row);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Shift.getID(tempShift, new CurrentUser.OnDataReceivedListener() {
                            @Override
                            public void onDataReceived(String uniqueID) {
                                if(uniqueID == null){
                                    Log.d("UNIQUEID", "BNULLLL");
                                }

                                DatabaseReference shiftRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Shifts").child(uniqueID);
                                shiftRef.removeValue();
                                button.setEnabled(false);
                                layout.removeView(row);
                            }
                        });
                    }
                });
            }
        }

    }
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
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

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
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

    public void popStartTimePicker(View view){
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

    public void popEndTimePicker(View view) {
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
    }
}