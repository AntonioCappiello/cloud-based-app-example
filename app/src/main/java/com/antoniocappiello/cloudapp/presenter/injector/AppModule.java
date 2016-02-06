/*
 * Created by Antonio Cappiello on 1/13/16 9:31 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 1/13/16 6:05 PM
 */

package com.antoniocappiello.cloudapp.presenter.injector;

import android.content.Context;

import com.antoniocappiello.cloudapp.presenter.backend.BackendAdapter;
import com.antoniocappiello.cloudapp.presenter.backend.FirebaseBackendAdapter;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context.getApplicationContext();
    }

    @Provides
    @AppScope
    public Context provideAppContext() {
        return context;
    }

    @Provides
    public BackendAdapter provideBackendAdapter() {
        return new FirebaseBackendAdapter(context);
    }


}