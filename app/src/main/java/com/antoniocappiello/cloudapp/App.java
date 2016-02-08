package com.antoniocappiello.cloudapp;

import android.app.Application;
import android.content.ContextWrapper;

import com.antoniocappiello.cloudapp.presenter.injector.AppComponent;
import com.antoniocappiello.cloudapp.presenter.injector.AppModule;
import com.antoniocappiello.cloudapp.presenter.injector.DaggerAppComponent;
import com.firebase.client.Firebase;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;
import com.pixplicity.easyprefs.library.Prefs;

public class App extends Application{

    private AppComponent appComponent;

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
                .setMethodCount(2)
                .hideThreadInfo()
                .setLogLevel(BuildConfig.DEBUG ? LogLevel.FULL : LogLevel.NONE);
    }

    private void initDependencyInjection() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent appComponent() {
        return appComponent;
    }

}
