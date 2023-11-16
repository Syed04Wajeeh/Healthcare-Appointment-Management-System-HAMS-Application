package com.example.hamsapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Locale;

public class ShiftPage extends AppCompatActivity {

    Button timeStartButton;
    Button timeEndButton;

    int startHour, startMinute, endHour, endMinute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shifts);
        timeStartButton = (Button) findViewById(R.id.addShiftButton);


    }
    public void popTimePicker(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                startHour = selectedHour;
                startMinute = selectedMinute;
                timeStartButton.setText(String.format(Locale.getDefault(), "%02d:%02d", startHour, startMinute));
                Log.d("minute is", String.valueOf(startMinute));

            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, startHour, startMinute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}