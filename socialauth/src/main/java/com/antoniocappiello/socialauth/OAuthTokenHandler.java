/*
 * Created by Antonio Cappiello on 2/20/16 12:32 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 2/18/16 5:40 PM
 */

package com.antoniocappiello.socialauth;

import com.antoniocappiello.socialauth.model.Account;
import com.antoniocappiello.socialauth.provider.AuthProviderType;
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
