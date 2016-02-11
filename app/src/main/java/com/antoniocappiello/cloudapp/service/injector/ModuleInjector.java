/*
 * Created by Antonio Cappiello on 1/13/16 9:31 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 1/13/16 6:05 PM
 */

package com.antoniocappiello.cloudapp.service.injector;

import android.content.Context;

import com.antoniocappiello.cloudapp.service.backend.BackendAdapter;
import com.antoniocappiello.cloudapp.ui.screen.BaseActivity;
import com.antoniocappiello.cloudapp.ui.screen.itemlist.AddItemDialogFragment;
import com.antoniocappiello.cloudapp.ui.screen.itemlist.EditItemDialogFragment;
import com.antoniocappiello.cloudapp.ui.screen.itemlist.ItemListActivity;
import com.antoniocappiello.cloudapp.ui.screen.login.CreateAccountActivity;
import com.antoniocappiello.cloudapp.ui.screen.login.LoginActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, BackendModule.class})
public interface ModuleInjector {

    void inject(BaseActivity activity);
    void inject(CreateAccountActivity activity);
    void inject(ItemListActivity activity);
    void inject(LoginActivity activity);
    void inject(AddItemDialogFragment addItemDialogFragment);
    void inject(EditItemDialogFragment editItemDialogFragment);

    Context appContext();

    @Singleton
    BackendAdapter backendAdapter();

}