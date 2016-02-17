package com.antoniocappiello.cloudapp.service.auth;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.antoniocappiello.cloudapp.service.backend.BackendAdapter;
import com.firebase.ui.auth.core.FirebaseLoginError;
import com.firebase.ui.auth.core.FirebaseOAuthToken;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.orhanobut.logger.Logger;

public class AuthProvider implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleOAuthTaskHandler {

    public static final int GOOGLE_SIGN_IN_REQUEST_CODE = 1111;

    private GoogleApiClient mGoogleApiClient;
    private Activity mActivity;
    private BackendAdapter mBackendAdapter;
    private FacebookAuthProvider mFacebookAuthProvider;

    public AuthProvider(Activity activity) {
        mActivity = activity;
    }

    public AuthProvider enableGoogleProvider() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .enableAutoManage((FragmentActivity) mActivity , this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        return this;
    }

    public AuthProvider setGoogleSignInView(View googleSignInView) {
        googleSignInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                mActivity.startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);
            }
        });
        return this;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Logger.e(connectionResult.toString());
    }

    @Override
    public void onConnected(final Bundle bundle) {
        Logger.d("onConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Logger.d("onConnectionSuspended");
    }

    @Override
    public void onOAuthSuccess(String token) {
        Logger.d("onOAuthSuccess");
        FirebaseOAuthToken firebaseOAuthToken = new FirebaseOAuthToken(
                "google",
                token);
        mBackendAdapter.authenticateRefWithOAuthFirebasetoken(firebaseOAuthToken);
    }

    @Override
    public void onOAuthFailure(FirebaseLoginError firebaseError) {
        Logger.d("onOAuthFailure");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AuthProvider.GOOGLE_SIGN_IN_REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else {
            mFacebookAuthProvider.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Logger.d("handleSignInResult:" + result.isSuccess() + "\n" + result.getStatus().getStatusMessage());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Logger.d("email " + acct.getEmail() + "\n name " + acct.getDisplayName() + "\n all " + acct.toString());

            mBackendAdapter.setCurrentUserEmail(personEmail);
            GoogleOAuthTask googleOAuthTask = new GoogleOAuthTask();
            googleOAuthTask.setContext(mActivity);
            googleOAuthTask.setHandler(this);
            googleOAuthTask.execute(acct.getEmail());
        }
    }

    public void logOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Logger.d(status.toString());
                    }
                });
        mFacebookAuthProvider.logout();
    }

    public AuthProvider setBackendAdapter(BackendAdapter backendAdapter) {
        mBackendAdapter = backendAdapter;
        return this;
    }

    public AuthProvider enableFacebookProvider() {
        mFacebookAuthProvider = new FacebookAuthProvider(mActivity, mBackendAdapter);
        return this;
    }

    public AuthProvider setFacebookSignInView(View facebookSignInView) {
        facebookSignInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFacebookAuthProvider.login(mActivity);
            }
        });
        return this;
    }
}
