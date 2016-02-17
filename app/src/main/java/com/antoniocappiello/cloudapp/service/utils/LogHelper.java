package com.antoniocappiello.cloudapp.service.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;

import com.orhanobut.logger.Logger;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LogHelper {

    public static void logHashKey(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    "com.antoniocappiello.cloudapp",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Logger.d("KeyHash:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
