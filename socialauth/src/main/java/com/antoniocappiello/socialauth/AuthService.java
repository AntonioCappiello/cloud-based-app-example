/*
 * Created by Antonio Cappiello on 2/20/16 12:32 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 2/18/16 5:40 PM
 */

package com.antoniocappiello.socialauth;

import android.content.Intent;

import com.antoniocappiello.socialauth.provider.AuthProvider;
import com.antoniocappiello.socialauth.provider.AuthProviderType;
import com.antoniocappiello.socialauth.provider.google.GoogleAuthProvider;

import java.util.HashMap;
import java.util.Map;

public class AuthService {

    Map<AuthProviderType, AuthProvider> mEnabledProvidersByType = new HashMap<>();

    public AuthService enableAuthProvider(AuthProvider authProvider) {
        if (!mEnabledProvidersByType.containsKey(authProvider.getType())) {
            mEnabledProvidersByType.put(authProvider.getType(), authProvider);
        }
        return this;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GoogleAuthProvider.GOOGLE_SIGN_IN_REQUEST_CODE) {
            mEnabledProvidersByType.get(AuthProviderType.GOOGLE)
                    .onActivityResult(requestCode, resultCode, data);
        }
        else {
            for(AuthProvider authProvider: mEnabledProvidersByType.values()){
                if(authProvider.getType() != AuthProviderType.GOOGLE) {
                    authProvider.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    public void logOut() {
        for(AuthProvider authProvider: mEnabledProvidersByType.values()){
            authProvider.logout();
        }
    }

}
