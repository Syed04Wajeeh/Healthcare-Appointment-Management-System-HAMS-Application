package com.example.hamsapplication;

public class Appointment {
    boolean approved, past;
    String username;
    int time, date;

    public Appointment(String username, int date, int time, boolean past, boolean approved){
        this.username = username;
        this.date = date;
        this.time = time;
        this.past = past;
        this.approved = approved;
    }
}