package com.antoniocappiello.cloudapp.service.action;

import android.app.Activity;
import android.content.Intent;

import com.antoniocappiello.cloudapp.ui.screen.itemlist.ItemListActivity;

public class OnSignInSucceeded implements Action {

    private final Activity mActivity;

    public OnSignInSucceeded(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void execute() {
        Intent intent = new Intent(mActivity, ItemListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mActivity.startActivity(intent);
        mActivity.finish();
    }

    @Override
    public void execute(String message) {
        ;
    }
}
