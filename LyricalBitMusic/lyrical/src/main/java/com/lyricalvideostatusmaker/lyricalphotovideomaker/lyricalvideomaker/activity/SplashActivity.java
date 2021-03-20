package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.AndroidPlugin;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AndroidPlugin.setContext(this);
        AndroidPlugin.LuanchApp(this);
    }
}
