package com.example.hamsapplication;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//this class stores the username of the current user logged into the application
public class CurrentUser {
    public static String username = "";

    public interface OnDataReceivedListener {//callback interface
        void onDataReceived(String uniqueID);
    }
    public static void getID(final OnDataReceivedListener listener) {//this method is a callback for the ID of the username above
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference();

        rootRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String uniqueID = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    GeneralInformation user = snapshot.getValue(GeneralInformation.class);
                    if (user.username.equals(CurrentUser.username)) {
                        uniqueID = snapshot.getKey(); // Get the Firebase ID
                        break;
                    }
                }
                if (listener != null) {
                    listener.onDataReceived(uniqueID);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled
            }
        });
    }
}