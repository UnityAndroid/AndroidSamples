package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.AndroidPlugin;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public InterstitialAd interstitial;
    public com.facebook.ads.InterstitialAd interstitialFB;
    public AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        List<String> testDeviceIds = Arrays.asList("9F2AB2DCAC4D42E0B6524C91D6879DA1",
                "757F0A0BF7195987EA23BDE2F9386957", "03CA849F77548D9C0088C3F295F759D7");
        RequestConfiguration configuration =
                new RequestConfiguration.Builder().setTestDeviceIds(testDeviceIds).build();
        MobileAds.setRequestConfiguration(configuration);

        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("fce85859-c3a6-41d0-b4c7-167f0a593461");

        if (AndroidPlugin.adsUnitySplashInterstitial.equalsIgnoreCase("Google")) {
            interstitial = new InterstitialAd(MainActivity.this);
            interstitial.setAdUnitId(AndroidPlugin.adMob_interstitial_id);
            adRequest = new AdRequest.Builder().build();
            interstitial.loadAd(adRequest);
            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    interstitial.show();
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    AdMobLoadAds.getInstance().loadInterstitialAds(MainActivity.this);

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    AdMobLoadAds.getInstance().loadInterstitialAds(MainActivity.this);

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }

                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }
            });
        } else if (AndroidPlugin.adsUnitySplashInterstitial.equalsIgnoreCase("Facebook")) {
            interstitialFB = new com.facebook.ads.InterstitialAd(MainActivity.this, AndroidPlugin.fb_interstitial_id);
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial ad displayed callback
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    AdMobLoadAds.getInstance().loadInterstitialAds(MainActivity.this);

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    AdMobLoadAds.getInstance().loadInterstitialAds(MainActivity.this);

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    finish();
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    interstitialFB.show();
                }

                @Override
                public void onAdClicked(Ad ad) {
                    // Ad clicked callback
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    // Ad impression logged callback
                }
            };

            interstitialFB.loadAd(interstitialFB.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        } else {

            AdMobLoadAds.getInstance().loadInterstitialAds(MainActivity.this);

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }
    }

}
