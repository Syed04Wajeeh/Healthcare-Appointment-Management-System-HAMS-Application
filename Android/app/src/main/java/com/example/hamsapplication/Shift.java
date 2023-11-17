package com.example.hamsapplication;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Shift {//class for shifts
    public int day, month, year, startHour, startMinute, endHour, endMinute;
    public float calcStartTime, calcEndTime;

    private String ID;

    public Shift() {
    }

    public Shift(int day, int month, int year, int startHour, int startMinute, int endHour, int endMinute) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
        this.calcStartTime = startHour + ((float)startMinute/60);
        this.calcEndTime = endHour + ((float)endMinute/60);
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }
}