package com.antoniocappiello.cloudapp.service.utils;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.orhanobut.logger.Logger;

public class AppInfoUtils {

    public static boolean isFacebookAppIdSet(Activity activity) {
        String facebookAppId = "";
        try {
            ApplicationInfo ai = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            facebookAppId = bundle.getString("com.facebook.sdk.ApplicationId");
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            Logger.e(e.toString());
        }
        if (facebookAppId == null) {
            Logger.e("Missing Facebook Application ID, is it set in your AndroidManifest.xml?");
            return false;
        }
        if (facebookAppId.compareTo("") == 0) {
            Logger.e("Invalid Facebook Application ID, is it set in your res/values/strings.xml?");
            return false;
        }
        return true;
    }
}
