/*
 * Created by Antonio Cappiello on 2/20/16 12:39 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 2/20/16 12:39 PM
 */

package com.antoniocappiello.socialauth;

import com.antoniocappiello.socialauth.model.Account;
import com.antoniocappiello.socialauth.provider.AuthProviderType;

import java.util.Map;

public interface BackendAdapter {
    void authenticateWithOAuthToken(AuthProviderType authProviderType, String token, Account account);
    void authenticateWithOAuthToken(AuthProviderType authProviderType, Map<String, String> options, Account account);
}
