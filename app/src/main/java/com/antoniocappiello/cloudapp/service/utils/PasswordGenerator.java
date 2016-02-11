package com.antoniocappiello.cloudapp.service.utils;

import java.math.BigInteger;
import java.security.SecureRandom;

public class PasswordGenerator {

    public static String generateRandomPassword() {
        SecureRandom mRandom = new SecureRandom();
        return new BigInteger(130, mRandom).toString(32);
    }
}
