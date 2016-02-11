/*
 * Created by Antonio Cappiello on 1/13/16 9:31 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 1/13/16 6:05 PM
 */

package com.antoniocappiello.cloudapp.service.injector;

import android.content.Context;

import com.antoniocappiello.cloudapp.service.backend.BackendAdapter;
import com.antoniocappiello.cloudapp.service.backend.FirebaseBackend;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class BackendModule {

    @Provides
    @Singleton
    public BackendAdapter provideBackendAdapter(Context context) {
        return new FirebaseBackend(context);
    }

}