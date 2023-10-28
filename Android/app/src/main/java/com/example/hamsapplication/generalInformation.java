package com.example.hamsapplication;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class generalInformation {
    protected static ArrayList<generalInformation> collection = new ArrayList<>();
    public static int currentTypeOf;
    //0 = admin, 1 = patient, 2 = doctor
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String address;
    public int registrationStatus;
    public int accountType;

    // 0 is not processed, 1 is accepted, 2 is rejected

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");

    public  generalInformation(){
    }
    public generalInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, int registrationStatus, int accountType){
        this.username = username.toLowerCase();
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.registrationStatus = registrationStatus;
        this.accountType = accountType;
    }

    public void setStatus(int newStatus){
        this.registrationStatus = newStatus;
    }

    public int getStatus(){
        return this.registrationStatus;
    }

    public String getUsername(){return this.username; }

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
