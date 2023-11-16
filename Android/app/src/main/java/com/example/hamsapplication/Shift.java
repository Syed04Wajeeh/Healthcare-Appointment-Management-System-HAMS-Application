package com.example.hamsapplication;

public class Shift {
    public int day, month, year, startHour, startMinute, endHour, endMinute;

    public Shift(int day, int month, int year, int startHour, int startMinute, int endHour, int endMinute) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.startHour = startHour;
        this.startMinute = startMinute;
        this.endHour = endHour;
        this.endMinute = endMinute;
    }
}