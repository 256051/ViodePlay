package com.dsl.tvplayer;

import android.app.Application;
import android.graphics.Typeface;
import android.support.annotation.NonNull;

import es.dmoral.toasty.Toasty;


/**
 * Created by Administrator on 2018/3/24.
 */

public class App extends Application{
    private static App mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
    }

    public static App getInstance(){
        return mApp;
    }


}
