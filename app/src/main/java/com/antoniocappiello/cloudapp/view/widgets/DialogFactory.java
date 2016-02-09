package com.antoniocappiello.cloudapp.view.widgets;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.antoniocappiello.cloudapp.R;
import com.antoniocappiello.cloudapp.view.list.ItemListActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DialogFactory {
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

    public static AlertDialog getAddItemDialog(Activity activity) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
        View dialogView = activity.getLayoutInflater().inflate(R.layout.add_item_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        dialogBuilder.setTitle(activity.getString(R.string.add_new_item));
        dialogBuilder.setPositiveButton(activity.getString(R.string.save), (dialog, whichButton) -> {

        });
        dialogBuilder.setNegativeButton(activity.getString(R.string.cancel), (dialog, whichButton) -> {
            dialog.dismiss();
        });
        return dialogBuilder.create();
    }
}
