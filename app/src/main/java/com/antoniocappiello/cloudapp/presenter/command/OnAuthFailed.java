package com.antoniocappiello.cloudapp.presenter.command;

import android.app.Activity;
import android.widget.Toast;

public class OnAuthFailed implements Command{

    private final Activity mActivity;

    public OnAuthFailed(Activity activity) {
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
