package com.example.hamsapplication;
import android.util.Log;

import java.util.ArrayList;
public class generalInformation {
    protected static ArrayList<generalInformation> collection = new ArrayList<>();
    public static int currentTypeOf;
    //0 = admin, 1 = patient, 2 = doctor
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String phoneNumber;
    protected String address;
    private int registrationStatus;
    // 0 is not processed, 1 is accepted, 2 is rejected

    protected generalInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, int registrationStatus){
        this.username = username.toLowerCase();
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.registrationStatus = registrationStatus;
    }

    public void setStatus(int newStatus){
        this.registrationStatus = newStatus;
    }

    public int getStatus(){
        return this.registrationStatus;
    }

    static public boolean hasAccount(String user){
        boolean result = false;
        if (!collection.isEmpty()) {
            for (generalInformation users : collection) {
                if (users.username.equals(user)) {
                    result = true;
                    break;
                } else {
                    result = false;
                }
            }
        } else {
            result = false;
        }
        return result;
    }
    static public void addToCollection(generalInformation information){
        if (!hasAccount(information.username)){
            collection.add(information);
        }
    }

    static public generalInformation searchForAccount(String username) {
        if (!collection.isEmpty() && !username.equals(null)) {
            for (generalInformation users : collection) {
                if (users.username.equals(username)) {
                    return users;
                }
            }
        }
        return null;
    }
}
