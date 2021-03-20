package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;


public class App extends Application {

    private static App instance = null;
    public static final String TAG = App.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/merriweather.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

    }

    public static App getInstance() {
        return instance;
    }
}
