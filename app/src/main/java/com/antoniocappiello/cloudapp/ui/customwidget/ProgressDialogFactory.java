package com.antoniocappiello.cloudapp.ui.customwidget;

import android.app.ProgressDialog;
import android.content.Context;

import com.antoniocappiello.cloudapp.R;

public class ProgressDialogFactory {
    public static ProgressDialog getSignInProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getString(R.string.progress_dialog_loading));
        progressDialog.setMessage(context.getString(R.string.progress_dialog_authenticating_with_firebase));
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static ProgressDialog getSignUpProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(context.getString(R.string.progress_dialog_loading));
        progressDialog.setMessage(context.getString(R.string.progress_dialog_check_inbox));
        progressDialog.setCancelable(false);
        return progressDialog;
    }
}
