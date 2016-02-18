package com.antoniocappiello.cloudapp.service.auth;

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
