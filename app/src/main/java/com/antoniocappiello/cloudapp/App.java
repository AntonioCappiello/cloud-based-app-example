package com.antoniocappiello.cloudapp;

import android.app.Application;
import android.content.ContextWrapper;

import com.antoniocappiello.cloudapp.service.injector.AppModule;
import com.antoniocappiello.cloudapp.service.injector.BackendModule;
import com.antoniocappiello.cloudapp.service.injector.DaggerModuleInjector;
import com.antoniocappiello.cloudapp.service.injector.ModuleInjector;
import com.firebase.client.Firebase;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.pixplicity.easyprefs.library.Prefs;

public class App extends Application{

    private ModuleInjector mModuleInjector;

    @Override
    public void onCreate() {
        super.onCreate();

        initLogger();
        initSharedPreferences();
        initDependencyInjection();

        Firebase.setAndroidContext(this);

    }

    private void initSharedPreferences() {
        new Prefs.Builder()
                .setContext(this)
                .setMode(ContextWrapper.MODE_PRIVATE)
                .setPrefsName(getPackageName())
                .setUseDefaultSharedPreference(true)
                .build();
    }

    private void initLogger() {
        Logger.init()
                .setMethodCount(4)
                .hideThreadInfo()
                .setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
    }

    private void initDependencyInjection() {
        mModuleInjector = DaggerModuleInjector.builder()
                .appModule(new AppModule(this))
                .backendModule(new BackendModule())
                .build();
    }

    public ModuleInjector appComponent() {
        return mModuleInjector;
    }

}
