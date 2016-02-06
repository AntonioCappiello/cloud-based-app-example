package com.antoniocappiello.cloudapp.model;

public class Account {

    String userName;
    String userEmail;
    String password;

    public Account(String userName, String userEmail, String password) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
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
