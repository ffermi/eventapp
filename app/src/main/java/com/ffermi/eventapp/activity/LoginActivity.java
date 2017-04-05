package com.ffermi.eventapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.ffermi.eventapp.R;
import com.ffermi.eventapp.service.PersistUserService;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "_LoginActivity";

    private ProgressDialog progressDialog;

    // Facebook
    private CallbackManager mCallbackManager;
    private LoginManager mLoginManager;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initializeFacebookAuth();
        initializeFirebaseAuth();
    }

    private void initializeFacebookAuth() {
        final Button loginButton = (Button) findViewById(R.id.facebook_login_button);

        mCallbackManager = CallbackManager.Factory.create();
        mLoginManager = LoginManager.getInstance();

        mLoginManager.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:login succeeded");
                handleFacebookAccessToken(AccessToken.getCurrentAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:login cancelled");
                progressDialog.dismiss();

                Snackbar.make(loginButton, "Login cancelled.", Snackbar.LENGTH_LONG)
                        .setAction("Retry", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                login(loginButton);
                            }
                        }).show();
            }

            @Override
            public void onError(FacebookException error) {
                Log.w(TAG, "facebook:login error");
                progressDialog.dismiss();
            }
        });

    }

    private void initializeFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // detect user
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "firebase:onAuthStateChanged: user signed in " + user.getUid());
                    startService(new Intent(LoginActivity.this, PersistUserService.class));
                } else {
                    Log.d(TAG, "firebase:onAuthStateChanged: user signed out.");
                }
            }
        };
    }

    private void handleFacebookAccessToken(final AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.d(TAG, "firebase:signInWithCredential: " + task.getException());
                            progressDialog.dismiss();
                        } else {
                            Intent intent = new Intent(
                                    LoginActivity.this, PrivateAreaActivity.class);

                            // Forbid back-navigation to this activity.
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            //start activity
                            startActivity(intent);
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    /**
     * On Login button click.
     */
    public void login (View view) {
        progressDialog = ProgressDialog.show(this, "Wait", "Logging in...", true, false);
        mLoginManager.logInWithReadPermissions(this,
                Arrays.asList("email", "public_profile", "user_friends"));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
