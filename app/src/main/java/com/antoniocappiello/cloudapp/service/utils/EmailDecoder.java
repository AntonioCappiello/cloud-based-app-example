package com.antoniocappiello.cloudapp.service.utils;


public class EmailDecoder {

    public static String decode(String userEmail) {
        return userEmail.replace(",", ".");
    }

}
