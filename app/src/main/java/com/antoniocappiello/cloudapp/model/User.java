package com.antoniocappiello.cloudapp.model;

import java.util.HashMap;

public class User {
    private String name;
    private String email;
    private HashMap<String, Object> timestampJoined;
    private boolean emailConfirmed;

    public User() {
    }

    public User(String name, String email, HashMap<String, Object> timestampJoined) {
        this.name = name;
        this.email = email;
        this.timestampJoined = timestampJoined;
        this.emailConfirmed = false;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    public boolean isEmailConfirmed() {
        return emailConfirmed;
    }
}
