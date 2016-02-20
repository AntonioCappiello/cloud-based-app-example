/*
 * Created by Antonio Cappiello on 2/20/16 12:40 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 2/20/16 12:33 PM
 */

package com.antoniocappiello.socialauth.provider;

import android.content.Intent;

public interface AuthProvider {

    void onActivityResult(int requestCode, int resultCode, Intent data);
    void logout();
    AuthProviderType getType();
}
