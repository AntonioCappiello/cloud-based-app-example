/*
 * Created by Antonio Cappiello on 2/20/16 12:32 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 2/20/16 12:31 PM
 */

package com.antoniocappiello.socialauth.provider.google;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.antoniocappiello.socialauth.provider.AuthProvider;
import com.antoniocappiello.socialauth.provider.AuthProviderBuilder;
import com.antoniocappiello.socialauth.provider.AuthProviderType;
import com.antoniocappiello.socialauth.OAuthTokenHandler;
import com.antoniocappiello.socialauth.model.Account;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.orhanobut.logger.Logger;

public class GoogleAuthProvider implements AuthProvider, GoogleApiClient.ConnectionCallbacks{

    public static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1111;
    private final AuthProviderType mAuthProviderType = AuthProviderType.GOOGLE;

    private final Activity mActivity;
    private final View mViewSignInWithGoogle;
    private GoogleApiClient mGoogleApiClient;
    private final OAuthTokenHandler mOAuthTokenHandler;
    private final GoogleApiClient.ConnectionCallbacks mGoogleConnectionCallback;
    private final GoogleApiClient.OnConnectionFailedListener mGoogleOnConnectionFailed;

    public GoogleAuthProvider(Builder builder) {
        mActivity = builder.mActivity;
        mViewSignInWithGoogle = builder.mSignInView;
        mGoogleConnectionCallback = builder.mConnectionCallback;
        mOAuthTokenHandler = builder.mOAuthTokenHandler;
        mGoogleOnConnectionFailed = builder.mOnConnectionFailedListener;

        setSignInView(mViewSignInWithGoogle);
        initGoogleApiClient();
    }

    private void initGoogleApiClient() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .enableAutoManage((FragmentActivity) mActivity , mGoogleOnConnectionFailed)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void setSignInView(View signInView) {
        signInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                mActivity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        handleSignInResult(result);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Logger.d("handleSignInResult:" + result.isSuccess() + "\n" + result.getStatus().getStatusMessage());
        if (result != null && result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();

            String userName = acct.getDisplayName();
            String userEmail = acct.getEmail();
            String id = acct.getId();

            Account account = new Account(id, userName, userEmail, "");

            Logger.d("email " + acct.getEmail() + "\n name " + acct.getDisplayName() + "\n all " + acct.toString());

            GoogleOAuthTask googleOAuthTask = new GoogleOAuthTask();
            googleOAuthTask.setContext(mActivity);
            googleOAuthTask.setHandler(mOAuthTokenHandler, account);
            googleOAuthTask.execute(acct.getEmail());
        }
    }

    public void logout() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Logger.d(status.toString());
                    }
                });
    }

    @Override
    public AuthProviderType getType() {
        return mAuthProviderType;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mGoogleConnectionCallback.onConnected(bundle);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleConnectionCallback.onConnectionSuspended(i);
    }


    public static class Builder extends AuthProviderBuilder {
        public GoogleAuthProvider build() {
            if(mActivity == null || mSignInView == null || mOAuthTokenHandler == null || mConnectionCallback == null || mOnConnectionFailedListener == null){
                throwIllegalStateException();
            }
            return new GoogleAuthProvider(this);
        }
    }
}
