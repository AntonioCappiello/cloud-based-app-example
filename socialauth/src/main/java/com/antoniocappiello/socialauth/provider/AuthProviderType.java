/*
 * Created by Antonio Cappiello on 2/20/16 12:40 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 2/20/16 12:33 PM
 */

package com.antoniocappiello.socialauth.provider;

public enum AuthProviderType {
    GOOGLE("google"),
    FACEBOOK("facebook"),
    TWITTER("twitter"),
    EMAIL_AND_PASSWORD("password");

    String providerName;

    AuthProviderType(String name) {
        this.providerName = name;
    }

    public String getProviderName() {
        return providerName;
    }
}
