/*
 * Created by Antonio Cappiello on 1/13/16 9:31 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 1/13/16 6:05 PM
 */

package com.antoniocappiello.cloudapp.presenter.injector;

import android.content.Context;

import com.antoniocappiello.cloudapp.presenter.backend.BackendAdapter;
import com.antoniocappiello.cloudapp.view.list.ItemListActivity;
import com.antoniocappiello.cloudapp.view.login.CreateAccountActivity;
import com.antoniocappiello.cloudapp.view.login.LoginActivity;

import dagger.Component;

@AppScope
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(LoginActivity baseActivity);
    void inject(CreateAccountActivity baseActivity);
    void inject(ItemListActivity baseActivity);

    @AppScope
    Context appContext();

    BackendAdapter backendAdapter();

}