package com.antoniocappiello.cloudapp.service.auth;

import android.content.Intent;

import com.antoniocappiello.cloudapp.service.auth.google.GoogleAuthProvider;

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
