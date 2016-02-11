package com.antoniocappiello.cloudapp.service.utils;


public class EmailEncoder {

    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

}
