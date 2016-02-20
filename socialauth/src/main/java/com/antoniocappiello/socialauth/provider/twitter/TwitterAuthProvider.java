/*
 * Created by Antonio Cappiello on 2/20/16 12:32 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 2/18/16 5:40 PM
 */

package com.antoniocappiello.socialauth.provider.twitter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.antoniocappiello.socialauth.Constants;
import com.antoniocappiello.socialauth.OAuthTokenHandler;
import com.antoniocappiello.socialauth.model.Account;
import com.antoniocappiello.socialauth.provider.AuthProvider;
import com.antoniocappiello.socialauth.provider.AuthProviderBuilder;
import com.antoniocappiello.socialauth.provider.AuthProviderType;

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

            String oauthToken = data.getStringExtra(Constants.OAUTH_TOKEN);
            String ouathTokenSecret = data.getStringExtra(Constants.OAUTH_TOKEN_SECRET);
            String userId = data.getStringExtra(Constants.USER_ID);
            Account account = new Account(userId, userId, userId + Constants.NO_TWITTER_EMAIL, "");

            Map<String, String> options = new HashMap<>();
            options.put(Constants.OAUTH_TOKEN, oauthToken);
            options.put(Constants.OAUTH_TOKEN_SECRET, ouathTokenSecret);
            options.put(Constants.USER_ID, userId);

            mOAuthTokenHandler.onOAuthSuccess(options, account);
        } else if (resultCode == TwitterActions.USER_ERROR) {
            mOAuthTokenHandler.onOAuthFailure("USER_ERROR: " + data.getStringExtra("error"));
        } else if (resultCode == TwitterActions.PROVIDER_ERROR) {
            mOAuthTokenHandler.onOAuthFailure("PROVIDER_ERROR: " + data.getStringExtra("error"));

        }
    }

    public static class Builder extends AuthProviderBuilder {
        @Override
        public TwitterAuthProvider build() {
            if(mActivity == null || mSignInView == null || mOAuthTokenHandler == null){
                throwIllegalStateException();
            }
            return new TwitterAuthProvider(this);
        }
    }
}