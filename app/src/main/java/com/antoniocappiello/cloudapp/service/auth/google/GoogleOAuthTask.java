package com.antoniocappiello.cloudapp.service.auth.google;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.antoniocappiello.cloudapp.service.auth.OAuthTokenHandler;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

public class GoogleOAuthTask extends AsyncTask<String, Integer, String> {
    private final String TAG = "GoogleOAuthTask";

    private Context mContext;
    private OAuthTokenHandler mHandler;

    protected String doInBackground(String... emails) {
        String token = "";

        try {
            token = GoogleAuthUtil.getToken(mContext, emails[0], "oauth2:profile email");
            // since we're immediately exchanging this token for a Firebase JWT token, we don't need to store it
            GoogleAuthUtil.clearToken(mContext, token);
        } catch (UserRecoverableAuthException e) {
            Log.e(TAG, "Error getting token", e);
        } catch (GoogleAuthException e) {
            Log.e(TAG, "Error getting token", e);
        } catch (java.io.IOException e) {
            Log.e(TAG, "Error getting token", e);
        }
        if (!token.equals("")) return token;
        else return "";
    }

    public void setContext(Context context) {
        mContext = context;
    }
    public void setHandler(OAuthTokenHandler handler) { mHandler = handler; }

    protected void onPostExecute(String token) {
        if (token.equals("")) {
            mHandler.onOAuthFailure("Fetching OAuth token from Google failed");
        } else {
            mHandler.onOAuthSuccess(token);
        }
    }
}