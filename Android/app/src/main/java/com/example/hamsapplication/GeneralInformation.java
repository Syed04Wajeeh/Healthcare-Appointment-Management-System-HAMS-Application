package com.example.hamsapplication;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GeneralInformation {

    //nested interface callback to ensure asynchronous data retrieval from database was completed
    //this nested interface is used to check if an account exists
    public interface AccountCheckCallback {
        void onAccountCheckResult(boolean accountExists);
    }

    //nested interface callback to ensure asynchronous data retrieval from database was completed
    //this nested interface is used to return the generalinformation object stored in the database with the correlating username
    public interface AccountSearchCallback {
        void onAccountSearchResult(GeneralInformation user);
    }

    public String username;
    public String password;
    public String firstName;
    public String lastName;
    public String phoneNumber;
    public String address;
    public int registrationStatus; //0(not looked at), 1(accepted), 2(denied)
    public int accountType;
    public GeneralInformation(){ //empty constructor
    }

    public GeneralInformation(String username, String password, String firstName, String lastName, String phoneNumber, String address, int registrationStatus, int accountType){
        this.username = username.toLowerCase();
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.registrationStatus = registrationStatus;
        this.accountType = accountType; //1 is admin, 2 is patient, 3 is doctor
    }

    static public void hasAccount(final String desiredUsername, final AccountCheckCallback callback){ // if an account exists with the desiredUsername, accountExists = true
        final boolean[] accountExists = {false};

        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) { // looping through database
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            GeneralInformation user = snapshot.getValue(GeneralInformation.class);
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



    static public void searchForAccount(final String desiredUsername, final AccountSearchCallback callback) { // returns a generalinformation object with the same name as desiredUsername
        FirebaseDatabase.getInstance().getReference().child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GeneralInformation user = snapshot.getValue(GeneralInformation.class);
                    if (user != null && user.username.equals(desiredUsername)) {
                        // Username was found
                        callback.onAccountSearchResult(user);
                        return;
                    }
                }
                // User was not found, callback null
                callback.onAccountSearchResult(null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onAccountSearchResult(null);
            }
        });
    }


    public void addToCollection(){ //method to be overridden by subclasses
    }

    public void encryptPassword(){ //encrypts the password of the object called upon
        AESCrypt crypt = new AESCrypt();
        try {
            String encrypted = crypt.encrypt(this.password);
            this.password = encrypted;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String decryptPassword(){//decrypts and returns password of object called upon
        AESCrypt crypt = new AESCrypt();
        try {
            return crypt.decrypt(this.password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
