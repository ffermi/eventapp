package com.ffermi.eventapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.ffermi.eventapp.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * This activity checks if the user is already logged in and it redirects him
 * to the login or to the app respectively.
 */

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "_MainActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        checkLogin();
    }

    /**
     * Either redirect the user to login or to private area
     */
    private void checkLogin() {
        Intent intent;

        if (mAuth.getCurrentUser() != null) {
            intent = new Intent(MainActivity.this, PrivateAreaActivity.class);
            Log.d(TAG, "authenticated user.");
        } else {
            intent = new Intent(MainActivity.this, LoginActivity.class);
            Log.d(TAG, "nonauthenticated user");
        }

        // Forbid back-navigation to this activity.
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

}
