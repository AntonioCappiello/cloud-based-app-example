/*
 * Created by Antonio Cappiello on 2/8/16 8:35 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 2/8/16 8:27 PM
 */

package com.antoniocappiello.cloudapp.service.action;

import android.app.Activity;
import android.content.Intent;

import com.antoniocappiello.cloudapp.ui.screen.BaseActivity;
import com.antoniocappiello.cloudapp.ui.screen.login.LoginActivity;

public class ShowLoginScreenAction implements Action {

    Activity mActivity;

    public ShowLoginScreenAction(BaseActivity activity) {
        mActivity = activity;
    }

    @Override
    public void execute() {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    @Override
    public void execute(String message) {

    }
}
