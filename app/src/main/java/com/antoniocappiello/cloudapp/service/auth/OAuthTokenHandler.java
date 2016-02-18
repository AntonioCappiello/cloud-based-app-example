package com.antoniocappiello.cloudapp.service.auth;

import com.antoniocappiello.cloudapp.service.backend.BackendAdapter;
import com.orhanobut.logger.Logger;

public class OAuthTokenHandler {
    private final BackendAdapter mBackendAdapter;
    private final AuthProviderType mAuthProviderType;

    public OAuthTokenHandler(AuthProviderType authProviderType, BackendAdapter backendAdapter) {
        mAuthProviderType = authProviderType;
        mBackendAdapter = backendAdapter;
    }

    public void onOAuthSuccess(String token) {
        Logger.d("onOAuthSuccess");
        mBackendAdapter.authenticateWithOAuthToken(mAuthProviderType, token);
    }

    public void onOAuthFailure(String errorMessage) {
        Logger.d("onOAuthFailure " + errorMessage);
    }
}
