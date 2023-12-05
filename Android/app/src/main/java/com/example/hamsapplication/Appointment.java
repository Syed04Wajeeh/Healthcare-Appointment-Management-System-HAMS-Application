package com.example.hamsapplication;

import android.util.Log;

import java.util.Calendar;

public class Appointment {
    public boolean past;
    public String patientID;
    public String doctorID;
    public float startTime;
    public int  day, month, year, status, startHour, startMinute; //-1 is rejected, 0 is not looked at, 1 is accepted

    public String ID;
    public Appointment() {
    }

    public Appointment(String patientID, String doctorID, int day, int month, int year,  int startHour, int startMinute) {
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.day = day;
        this.month = month;
        this.year = year;
        this.status = 0;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.startTime = startHour + ((float)startMinute/60); //holds start and end time with decimal value for minutes
        this.past = isPast();
    }


    public boolean isPast(){ //checks if this appointment has past or not
        Calendar cal = Calendar.getInstance();
        int currYear = cal.get(Calendar.YEAR);
        int currMonth = cal.get(Calendar.MONTH) + 1;
        int currDay = cal.get(Calendar.DAY_OF_MONTH);
        int currHour = cal.get(Calendar.HOUR_OF_DAY);
        Log.d(String.valueOf(currHour), String.valueOf(currDay));

        if(this.year < currYear){
            this.past = true;
            return true;
        }else if((this.year == currYear) && (this.month < currMonth)){
            this.past = true;
            return true;
        }else if(this.year == currYear && this.month == currMonth && this.day < currDay){
            this.past = true;
            return true;
        }else if(this.year == currYear && this.month == currMonth && this.day == currDay && this.startHour <= currHour){
            this.past = true;
            return true;
        }
        return false;
    }
}