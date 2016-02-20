/*
 * Created by Antonio Cappiello on 2/20/16 12:38 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 2/7/16 12:44 AM
 */

package com.antoniocappiello.socialauth.model;

public class Account {

    String id;
    String userName;
    String userEmail;
    String password;

    public Account(String id, String userName, String userEmail, String password) {
        this.id = id;
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPassword() {
        return password;
    }
}
