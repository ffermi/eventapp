package com.ffermi.eventapp.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.ffermi.eventapp.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PersistUserService extends IntentService {

    private static final String TAG = "_PersistUserService";

    private ValueEventListener mUserListener;
    private DatabaseReference mUserRef;

    /**
     * Creates an IntentService. Invoked by your subclass's constructor.
     */
    public PersistUserService() {
        super("PersistUserService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {

        // Log.d(TAG, "Thread " + Thread.currentThread().getName());

        final User user = new User(FirebaseAuth.getInstance().getCurrentUser());
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        mUserRef = databaseReference.child("users").child(user.getFbId());

        // Check if the user is in the database
        mUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Store user info in the database if he's logging in for the first time.
                if (!dataSnapshot.hasChildren()) {
                    mUserRef.setValue(user);
                    Log.d(TAG, "ValueEventListener.onDataChange USER STORED");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting user failed.
                Log.d(TAG,"ValueEventListener.onCancelled");
            }
        };

        mUserRef.addListenerForSingleValueEvent(mUserListener);
    }

    @Override
    public void onDestroy() {
        mUserRef.removeEventListener(mUserListener);
        Log.d(TAG,"DESTROYED");
        super.onDestroy();
    }
}
