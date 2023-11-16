package com.example.hamsapplication;

public class Appointment {
    boolean past;
    String username;
    int time, date, status; //-1 is rejected, 0 is not looked at, 1 is accepted

    public Appointment(String username, int date, int time, boolean past, int status){
        this.username = username;
        this.date = date;
        this.time = time;
        this.past = past;
        this.status = status;
    }
}