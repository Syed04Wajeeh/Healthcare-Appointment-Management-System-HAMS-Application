package com.example.hamsapplication;

public class Appointment {
    public boolean past;
    public String username;
    public int time, date, status; //-1 is rejected, 0 is not looked at, 1 is accepted

    public Appointment(String username, int date, int time, boolean past, int status){
        this.username = username;
        this.date = date;
        this.time = time;
        this.past = past;
        this.status = status;
    }
}