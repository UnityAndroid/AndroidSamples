package lyrically.photovideomaker.particl.ly.musicallybeat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AndroidPlugin.setContext(this);
        AndroidPlugin.LuanchApp(this);
    }
}
