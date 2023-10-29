package com.example.hamsapplication;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class generalInformation {

    public interface AccountCheckCallback {
        void onAccountCheckResult(boolean accountExists);
    }

    public interface AccountSearchCallback {
        void onAccountSearchResult(generalInformation user);
    }
    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String address;
    public int registrationStatus; //0(not looked at), 1(accepted), 2(denied)
    public int accountType;
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
        this.accountType = accountType; //1 is admin, 2 is patient, 3 is doctor


    }

    static public void hasAccount(final String desiredUsername, final AccountCheckCallback callback){
        final boolean[] accountExists = {false};

        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            generalInformation user = snapshot.getValue(generalInformation.class);
                            if (user != null && user.username.equals(desiredUsername)) {
                                // Username exists in the database
                                accountExists[0] = true;
                                break; // Exit the loop since the account has been found
                            }
                        }
                        callback.onAccountCheckResult(accountExists[0]);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onAccountCheckResult(false);
                    }
                });

    }



    static public void searchForAccount(final String desiredUsername, final AccountSearchCallback callback) {
        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    generalInformation user = snapshot.getValue(generalInformation.class);
                    if (user != null && user.username.equals(desiredUsername)) {
                        // User with the desired username was found
                        callback.onAccountSearchResult(user);
                        return; // Exit the loop since the account has been found
                    }
                }
                // User with the desired username was not found
                callback.onAccountSearchResult(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onAccountSearchResult(null);
            }
        });
    }


    public void addToCollection(){
    } //MAKE THIS ABSTRACT LATER BTW

    public void encryptPassword(){
        AESCrypt crypt = new AESCrypt();
        try {
            String encrypted = crypt.encrypt(this.password);
            this.password = encrypted;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decryptPassword(){
        AESCrypt crypt = new AESCrypt();
        try {
            return crypt.decrypt(this.password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
