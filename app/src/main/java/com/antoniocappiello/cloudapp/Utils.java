package com.antoniocappiello.cloudapp;


import java.math.BigInteger;
import java.security.SecureRandom;

public class Utils {

    /**
     * Encode user email replacing "." with ","
     * to be able to use it as a Firebase db key
     */
    public static String encodeEmail(String userEmail) {
        return userEmail.replace(".", ",");
    }

    public static String decodeEmail(String userEmail) {
        return userEmail.replace(",", ".");
    }

    public static String generateRandomPassword() {
        SecureRandom mRandom = new SecureRandom();
        return new BigInteger(130, mRandom).toString(32);
    }
}
