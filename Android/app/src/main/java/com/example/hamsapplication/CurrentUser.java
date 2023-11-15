package com.example.hamsapplication;

public class CurrentUser {
    private String username;

    public CurrentUser(){
        this.username = "";
    }
    public void setUser(String str){
        this.username = str;
    }

    public String getUsername() {
        return username;
    }
}