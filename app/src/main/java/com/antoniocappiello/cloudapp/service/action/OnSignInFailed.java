package com.antoniocappiello.cloudapp.service.action;

import android.app.Activity;
import android.widget.Toast;

public class OnSignInFailed implements Action {

    private final Activity mActivity;

    public OnSignInFailed(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void execute() {
        ;
    }

    @Override
    public void execute(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();    }
}
