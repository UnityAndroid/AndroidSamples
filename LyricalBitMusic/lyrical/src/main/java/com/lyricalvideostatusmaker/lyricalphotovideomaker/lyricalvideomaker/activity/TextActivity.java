package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.unity3d.player.UnityPlayer;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.Constants;

public class TextActivity extends AppCompatActivity {

    public ImageView tick, back, pencil, txtTitle;
    public TextView text, txtMessage;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_text);

        context = this;
        text = findViewById(R.id.text_name);
        tick = findViewById(R.id.text_done);
        back = findViewById(R.id.text_select_back_button);
        pencil = findViewById(R.id.text);
        txtTitle = (ImageView) findViewById(R.id.txtTitle);
        txtMessage = (TextView) findViewById(R.id.txtMessage);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/merriweather.ttf");

        txtMessage.setTypeface(tf1, Typeface.NORMAL);
        text.setTypeface(tf1, Typeface.NORMAL);

        LinearLayout creation_banner = (LinearLayout) findViewById(R.id.creation_banner);
        AdMobLoadAds.getInstance().loadBanner(TextActivity.this, creation_banner);

        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString().length() > 0) {
                    Constants.android = false;
                    UnityPlayer.UnitySendMessage("SelectImage", "EditText", text.getText().toString());
                    onBackPressed();
                } else {
                    text.setError("Plz Enter Something");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.with(getApplicationContext()).pauseRequests();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
