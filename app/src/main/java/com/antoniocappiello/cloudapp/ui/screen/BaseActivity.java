/*
 * Created by Antonio Cappiello on 2/8/16 8:19 PM
 * Copyright (c) 2016. All rights reserved.
 *
 * Last modified 2/8/16 8:19 PM
 */

package com.antoniocappiello.cloudapp.ui.screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.antoniocappiello.cloudapp.App;
import com.antoniocappiello.cloudapp.R;
import com.antoniocappiello.cloudapp.service.action.ShowLoginScreenAction;
import com.antoniocappiello.cloudapp.service.backend.BackendAdapter;
import com.antoniocappiello.cloudapp.ui.screen.login.CreateAccountActivity;
import com.antoniocappiello.cloudapp.ui.screen.login.LoginActivity;
import com.orhanobut.logger.Logger;

import javax.inject.Inject;

public class BaseActivity extends AppCompatActivity {

    @Inject
    BackendAdapter mBackendAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((App) getApplication()).appComponent().inject(this);
        if (!((this instanceof LoginActivity) || (this instanceof CreateAccountActivity))) {
            mBackendAdapter.addAuthStateListener(null, new ShowLoginScreenAction(this));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!((this instanceof LoginActivity) || (this instanceof CreateAccountActivity))) {
            mBackendAdapter.removeAuthStateListener();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.action_log_out:
                logOut();
        }
        return super.onOptionsItemSelected(item);
    }

    public void logOut() {
        Logger.d("Logging out");
        mBackendAdapter.logOut();
    }

}
