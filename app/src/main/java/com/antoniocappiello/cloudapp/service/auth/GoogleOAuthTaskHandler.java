package com.antoniocappiello.cloudapp.service.auth;

import com.firebase.ui.auth.core.FirebaseLoginError;

public interface GoogleOAuthTaskHandler {
    public void onOAuthSuccess(String token);
    public void onOAuthFailure(FirebaseLoginError firebaseError);
}
