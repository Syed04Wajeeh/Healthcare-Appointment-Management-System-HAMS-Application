package com.example.hamsapplication;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Shift {
    public int day, month, year, startHour, startMinute, endHour, endMinute;
    public float calcStartTime, calcEndTime;

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

    public interface OnDataReceivedListener {
        void onDataReceived(String uniqueID);
    }
    public static void getID(final Shift desiredShift, final CurrentUser.OnDataReceivedListener listener) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();

        rootRef.child("Users").child("Shifts").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uniqueID = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Shift tempShift = snapshot.getValue(Shift.class);
                    if (tempShift.calcStartTime == desiredShift.calcStartTime && tempShift.calcEndTime == desiredShift.calcEndTime && tempShift.day == desiredShift.day && tempShift.month == desiredShift.month && tempShift.year == desiredShift.year) {
                        uniqueID = snapshot.getKey(); // Get the Firebase ID
                        break;
                    }
                }
                if (listener != null) {
                    listener.onDataReceived(uniqueID);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
}