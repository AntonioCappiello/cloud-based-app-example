package com.antoniocappiello.cloudapp.service.action;

import android.app.Activity;
import android.content.Intent;

import com.orhanobut.logger.Logger;

public class OpenEmailAppAction implements Action {

    private final Activity mActivity;

    public OpenEmailAppAction(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void execute() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        try {
            mActivity.startActivity(intent);
            mActivity.finish();
        } catch (android.content.ActivityNotFoundException ex) {
            Logger.e("User does not have any app to handle email");
        }
    }

    @Override
    public void execute(String message) {

    }
}
