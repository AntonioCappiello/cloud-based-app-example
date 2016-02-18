package com.antoniocappiello.cloudapp.service.auth.provider.twitter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.antoniocappiello.cloudapp.Constants;
import com.antoniocappiello.cloudapp.model.Account;
import com.antoniocappiello.cloudapp.service.auth.AuthProvider;
import com.antoniocappiello.cloudapp.service.auth.AuthProviderBuilder;
import com.antoniocappiello.cloudapp.service.auth.AuthProviderType;
import com.antoniocappiello.cloudapp.service.auth.OAuthTokenHandler;

import java.util.HashMap;
import java.util.Map;

public class TwitterAuthProvider implements AuthProvider {

    public static final AuthProviderType authProviderType = AuthProviderType.TWITTER;
    private final Activity mActivity;
    private final OAuthTokenHandler mOAuthTokenHandler;
    private final View mSignInView;

    public TwitterAuthProvider(Builder builder) {
        mActivity = builder.mActivity;
        mSignInView = builder.mSignInView;
        mOAuthTokenHandler = builder.mOAuthTokenHandler;

        setSignInView(mSignInView);
    }

    private void setSignInView(View signInView) {
        signInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivityForResult(new Intent(mActivity, TwitterPromptActivity.class), TwitterActions.REQUEST);
            }
        });
    }

    @Override
    public void logout() {
        // We don't store auth state in this handler, so no need to logout
    }

    @Override
    public AuthProviderType getType() {
        return authProviderType;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == TwitterActions.SUCCESS) {
            Map<String, String> options = new HashMap<>();
            options.put("oauth_token", data.getStringExtra("oauth_token"));
            options.put("oauth_token_secret", data.getStringExtra("oauth_token_secret"));
            options.put("user_id", data.getStringExtra("user_id"));

            Account account = new Account(data.getStringExtra("user_id"), data.getStringExtra("user_id") + Constants.NO_TWITTER_EMAIL, "");
            mOAuthTokenHandler.onOAuthSuccess(options, account);
        } else if (resultCode == TwitterActions.USER_ERROR) {
            mOAuthTokenHandler.onOAuthFailure("USER_ERROR: " + data.getStringExtra("error"));
        } else if (resultCode == TwitterActions.PROVIDER_ERROR) {
            mOAuthTokenHandler.onOAuthFailure("PROVIDER_ERROR: " + data.getStringExtra("error"));

        }
    }

    public static class Builder extends AuthProviderBuilder{
        @Override
        public TwitterAuthProvider build() {
            if(mActivity == null || mSignInView == null || mOAuthTokenHandler == null){
                throwIllegalStateException();
            }
            return new TwitterAuthProvider(this);
        }
    }
}