package com.antoniocappiello.cloudapp.service.auth;

import com.antoniocappiello.cloudapp.model.Account;
import com.antoniocappiello.cloudapp.service.backend.BackendAdapter;
import com.orhanobut.logger.Logger;

import java.util.Map;

public class OAuthTokenHandler {
    private final BackendAdapter mBackendAdapter;
    private final AuthProviderType mAuthProviderType;

    public OAuthTokenHandler(AuthProviderType authProviderType, BackendAdapter backendAdapter) {
        mAuthProviderType = authProviderType;
        mBackendAdapter = backendAdapter;
    }

    public void onOAuthSuccess(String token, Account account) {
        Logger.d("onOAuthSuccess");
        mBackendAdapter.authenticateWithOAuthToken(mAuthProviderType, token, account);
    }

    public void onOAuthFailure(String errorMessage) {
        Logger.d("onOAuthFailure " + errorMessage);
    }

    public void onOAuthSuccess(Map<String, String> options, Account account) {
        Logger.d("onOAuthSuccess");
        mBackendAdapter.authenticateWithOAuthToken(mAuthProviderType, options, account);
    }
}
