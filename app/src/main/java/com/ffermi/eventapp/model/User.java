package com.ffermi.eventapp.model;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

/**
 * User model
 */

@IgnoreExtraProperties
public class User {

    private String fbId;
    private String name;
    private String email;
    private String picture;

    public User() {}

    public User(String fbId, String name, String email, String picture) {
        this.fbId = fbId;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public User(FirebaseUser fu) {
        if (fu != null) {
            this.fbId = fu.getProviderData().get(1).getUid();
            this.name = fu.getDisplayName();
            this.email = fu.getEmail();
            this.picture = String.valueOf(fu.getPhotoUrl());
        }
    }

    @Exclude
    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}

