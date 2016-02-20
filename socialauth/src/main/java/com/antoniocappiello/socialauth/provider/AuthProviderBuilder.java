/*
 * Created by Antonio Cappiello on 2/20/16 12:40 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 2/20/16 12:33 PM
 */

package com.antoniocappiello.socialauth.provider;

import android.app.Activity;
import android.view.View;

import com.antoniocappiello.socialauth.OAuthTokenHandler;
import com.google.android.gms.common.api.GoogleApiClient;

public abstract class AuthProviderBuilder {
    public Activity mActivity;
    public View mSignInView;
    public OAuthTokenHandler mOAuthTokenHandler;
    public GoogleApiClient.ConnectionCallbacks mConnectionCallback;
    public GoogleApiClient.OnConnectionFailedListener mOnConnectionFailedListener;

    public AuthProviderBuilder activity(Activity activity) {
        mActivity = activity;
        return this;
    }

    public AuthProviderBuilder signInView(View signInView) {
        mSignInView = signInView;
        return this;
    }

    public AuthProviderBuilder oAuthTokenHandler(OAuthTokenHandler oAuthTokenHandler) {
        mOAuthTokenHandler = oAuthTokenHandler;
        return this;
    }

    public AuthProviderBuilder connectionCallback(GoogleApiClient.ConnectionCallbacks connectionCallback) {
        mConnectionCallback = connectionCallback;
        return this;
    }

    public AuthProviderBuilder onConnectionFailedListener(GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        mOnConnectionFailedListener = onConnectionFailedListener;
        return this;
    }

    public void throwIllegalStateException() {
        throw new IllegalStateException("Builder was not initialized correctly");
    }

    public abstract AuthProvider build();
}
