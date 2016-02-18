package com.antoniocappiello.cloudapp.service.auth;

import android.app.Activity;
import android.view.View;

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

    public AuthProviderBuilder oAuthTaskHandler(OAuthTokenHandler oAuthTokenHandler) {
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
