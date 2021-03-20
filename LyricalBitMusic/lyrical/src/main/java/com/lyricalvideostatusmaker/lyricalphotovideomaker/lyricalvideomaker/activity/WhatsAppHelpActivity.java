package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;


public class WhatsAppHelpActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_wa_how_it_works);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar.setTitle((CharSequence) "How it works");

        showBanner();
    }

    public void showBanner() {
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.adBanner);
        adContainer.removeAllViews();
        AdMobLoadAds.getInstance().loadBanner(WhatsAppHelpActivity.this, adContainer);
    }

}
