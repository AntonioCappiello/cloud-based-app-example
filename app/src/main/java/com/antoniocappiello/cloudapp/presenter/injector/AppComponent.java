/*
 * Created by Antonio Cappiello on 1/13/16 9:31 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 1/13/16 6:05 PM
 */

package com.antoniocappiello.cloudapp.presenter.injector;

import android.content.Context;

import com.antoniocappiello.cloudapp.presenter.backend.BackendAdapter;
import com.antoniocappiello.cloudapp.view.BaseActivity;
import com.antoniocappiello.cloudapp.view.list.ItemListActivity;
import com.antoniocappiello.cloudapp.view.login.CreateAccountActivity;
import com.antoniocappiello.cloudapp.view.login.LoginActivity;
import com.antoniocappiello.cloudapp.view.list.AddItemDialogFragment;

import javax.inject.Singleton;

import dagger.Component;

@AppScope
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    void inject(BaseActivity activity);
    void inject(CreateAccountActivity activity);
    void inject(ItemListActivity activity);
    void inject(LoginActivity activity);
    void inject(AddItemDialogFragment addItemDialogFragment);

    @AppScope
    Context appContext();

    @Singleton
    BackendAdapter backendAdapter();

}