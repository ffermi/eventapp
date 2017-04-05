package com.ffermi.eventapp.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ffermi.eventapp.R;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PrivateAreaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private Toolbar mToolbar;

    private ImageView mSideImage;
    private TextView mSideUserName;
    private TextView mSideUserEmail;

    private FragmentManager mFragmentManager;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_area);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.my_events);
        setSupportActionBar(mToolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // get user info
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        // get side textViews
        View sideNavHeader = navigationView.getHeaderView(0);
        mSideImage = (ImageView) sideNavHeader.findViewById(R.id.side_picture);
        mSideUserName = (TextView) sideNavHeader.findViewById(R.id.side_name);
        mSideUserEmail = (TextView) sideNavHeader.findViewById(R.id.side_email);

        updateUI();

        // TODO: manage fragments && fix side checked()
        mFragmentManager = getFragmentManager();

        // Floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                startActivity(new Intent(PrivateAreaActivity.this, NewEventActivity.class));
            }
        });
    }

    private void updateUI() {
        Glide.with(this).load(String.valueOf(mUser.getPhotoUrl())).fitCenter().into(mSideImage);
        mSideUserName.setText(String.valueOf(mUser.getDisplayName()));
        mSideUserEmail.setText(String.valueOf(mUser.getEmail()));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Load requested fragment / activity
        FragmentTransaction fragmentTransaction;
        switch (id) {
            case R.id.nav_my_ev:
                mToolbar.setTitle(R.string.my_events);
                break;

            case R.id.nav_nearby_events:
                mToolbar.setTitle(R.string.nearby_events);
                break;

            case R.id.nav_popular_ev:
                mToolbar.setTitle(R.string.popular_events);
                break;

            case R.id.nav_notifications:
                mToolbar.setTitle(R.string.notifications);
                //startActivity(new Intent(PrivateAreaActivity.this, NotificationsActivity.class));
                break;

            default:
                mToolbar.setTitle(R.string.app_name);
                break;
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        LoginManager.getInstance().logOut();
        FirebaseAuth.getInstance().signOut();

        startActivity(new Intent(PrivateAreaActivity.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dots_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;

            case R.id.action_logout:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        // Close the navDrawer if it is opened
        if(mDrawer.isDrawerOpen(GravityCompat.START)){
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (mFragmentManager.getBackStackEntryCount() > 0) {
                mFragmentManager.popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDrawer.removeDrawerListener(mToggle);
    }

}
