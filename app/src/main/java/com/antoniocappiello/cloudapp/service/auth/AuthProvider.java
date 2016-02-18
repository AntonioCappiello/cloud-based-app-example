package com.antoniocappiello.cloudapp.service.auth;

import android.content.Intent;

public interface AuthProvider {

    void onActivityResult(int requestCode, int resultCode, Intent data);
    void logout();
    AuthProviderType getType();
}
