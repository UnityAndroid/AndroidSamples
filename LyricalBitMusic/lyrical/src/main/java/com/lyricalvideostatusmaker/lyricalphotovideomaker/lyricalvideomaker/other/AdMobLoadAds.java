package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import java.util.ArrayList;
import java.util.List;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.AndroidPlugin;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;

public class AdMobLoadAds {

    public static AdMobLoadAds mInstance;
    public InterstitialAd interstitial;
    public com.facebook.ads.InterstitialAd interstitialFB;
    public AdRequest adRequest;
    public MyCallback myCallback;
    public boolean adsInterstitialType = false;
    public boolean adsBannerType = false;
    public static boolean adsNativeType = false;
    public RewardedVideoAd mRewardedVideoAd;
    public static int reward = 0;
    public AlertDialog alertDialog;

    public static AdMobLoadAds getInstance() {
        if (mInstance == null) {
            mInstance = new AdMobLoadAds();
        }
        return mInstance;
    }

    public interface MyCallback {
        void callbackCall();
    }

    public void loadInterstitialAds(final Activity context) {
        if (AndroidPlugin.adsInterstitial.equalsIgnoreCase("Google")) {
            loadAdMobInterstitialAds(context);
        } else if (AndroidPlugin.adsInterstitial.equalsIgnoreCase("Facebook")) {
            loadFacebookInterstitialAds(context);
        } else if (AndroidPlugin.adsInterstitial.equalsIgnoreCase("Both")) {
            loadFacebookInterstitialAds(context);
        } else {

        }
    }

    public void loadAdMobInterstitialAds(final Activity context) {
        interstitial = new InterstitialAd(context);
        interstitial.setAdUnitId(AndroidPlugin.adMob_interstitial_id);
        adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
                adsInterstitialType = false;
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
                if (!AndroidPlugin.isAdsLastShow) {
                    if (AndroidPlugin.adsInterstitial.equalsIgnoreCase("Google")) {
                        loadAdMobInterstitialAds(context);
                    } else if (AndroidPlugin.adsInterstitial.equalsIgnoreCase("Facebook")) {
                        loadFacebookInterstitialAds(context);
                    } else if (AndroidPlugin.adsInterstitial.equalsIgnoreCase("Both")) {
                        adsInterstitialType = false;
                        loadFacebookInterstitialAds(context);
                    } else {

                    }
                }
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
    }

    public void loadFacebookInterstitialAds(final Activity context) {
        interstitialFB = new com.facebook.ads.InterstitialAd(context, AndroidPlugin.fb_interstitial_id);
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
                if (!AndroidPlugin.isAdsLastShow) {
                    if (AndroidPlugin.adsInterstitial.equalsIgnoreCase("Google")) {
                        loadAdMobInterstitialAds(context);
                    } else if (AndroidPlugin.adsInterstitial.equalsIgnoreCase("Facebook")) {
                        loadFacebookInterstitialAds(context);
                    } else if (AndroidPlugin.adsInterstitial.equalsIgnoreCase("Both")) {
                        adsInterstitialType = true;
                        loadAdMobInterstitialAds(context);
                    } else {

                    }
                }
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                AndroidPlugin.adsInterstitial = "Google";
                adsInterstitialType = true;
                loadAdMobInterstitialAds(context);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                // Show the ad
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
    }

    public void displayInterstitial(Context context, MyCallback _myCallback) {
        this.myCallback = _myCallback;
        if (AndroidPlugin.adsInterstitial.equalsIgnoreCase("Google")) {
            if (interstitial != null) {
                if (interstitial.isLoaded()) {
                    interstitial.show();
                } else {
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            } else {
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        } else if (AndroidPlugin.adsInterstitial.equalsIgnoreCase("Facebook")) {
            if (interstitialFB != null) {
                if (interstitialFB.isAdLoaded()) {
                    interstitialFB.show();
                } else {
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            } else {
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        } else if (AndroidPlugin.adsInterstitial.equalsIgnoreCase("Both")) {
            if (adsInterstitialType) {
                if (interstitial != null) {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                    } else {
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                    }
                } else {
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            } else {
                if (interstitialFB != null) {
                    if (interstitialFB.isAdLoaded()) {
                        interstitialFB.show();
                    } else {
                        if (myCallback != null) {
                            myCallback.callbackCall();
                            myCallback = null;
                        }
                    }
                } else {
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }
            }
        } else {
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        }
    }

    public void loadBanner(Activity activity, LinearLayout linearLayout) {
        if (AndroidPlugin.adsBanner.equalsIgnoreCase("Google")) {
            loadAdMobBanner(activity, linearLayout);
        } else if (AndroidPlugin.adsBanner.equalsIgnoreCase("Facebook")) {
            loadFacebookBanner(activity, linearLayout);
        } else if (AndroidPlugin.adsBanner.equalsIgnoreCase("Both")) {
            if (adsBannerType) {
                adsBannerType = false;
                loadAdMobBanner(activity, linearLayout);
            } else {
                adsBannerType = true;
                loadFacebookBanner(activity, linearLayout);
            }
        } else {

        }
    }

    public void loadAdMobBanner(final Activity activity, final LinearLayout linearLayout) {
        linearLayout.removeAllViews();
        final AdView adView = new AdView(activity);
        adView.setAdUnitId(AndroidPlugin.adMob_banner_id);
        linearLayout.addView(adView);
        AdSize adSize = getAdSize(activity);
        adView.setAdSize(adSize);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {
                adView.setVisibility(View.VISIBLE);
            }
        });

    }

    private AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public void loadFacebookBanner(final Activity activity, final LinearLayout linearLayout) {
        linearLayout.removeAllViews();
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(activity, AndroidPlugin.fb_banner_id,
                com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        linearLayout.addView(adView);

        com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
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

        // Request an ad
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());

    }

    public void loadFacebookNativeBanner(final Activity activity, final LinearLayout linearLayout) {
        linearLayout.removeAllViews();
        final NativeAdLayout mNativeBannerAdContainer = new NativeAdLayout(activity);
        linearLayout.addView(mNativeBannerAdContainer);
        LayoutInflater inflater = LayoutInflater.from(activity);
        final LinearLayout mAdView = (LinearLayout) inflater.inflate(R.layout.fb_native_banner_ad_unit,
                mNativeBannerAdContainer, false);

        final FrameLayout mAdChoicesContainer = mAdView.findViewById(R.id.ad_choices_container);

        final NativeBannerAd mNativeBannerAd = new NativeBannerAd(activity, AndroidPlugin.fb_native_banner_id);
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                loadAdMobBanner(activity, linearLayout);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Native ad is loaded and ready to be displayed
                if (mNativeBannerAd == null || mNativeBannerAd != ad) {
                    // Race condition, load() called again before last ad was displayed
                    return;
                }
                mNativeBannerAdContainer.addView(mAdView);
                // Unregister last ad
                mNativeBannerAd.unregisterView();

                AdOptionsView adOptionsView = new AdOptionsView(activity,
                        mNativeBannerAd, mNativeBannerAdContainer,
                        AdOptionsView.Orientation.HORIZONTAL, 10);
                mAdChoicesContainer.removeAllViews();
                mAdChoicesContainer.addView(adOptionsView);


                // Create native UI using the ad metadata.
                TextView nativeAdTitle = mAdView.findViewById(R.id.native_ad_title);
                TextView nativeAdSocialContext = mAdView.findViewById(R.id.native_ad_social_context);
                TextView sponsoredLabel = mAdView.findViewById(R.id.native_ad_sponsored_label);
                MediaView nativeAdIconView = mAdView.findViewById(R.id.native_icon_view);
                Button nativeAdCallToAction = mAdView.findViewById(R.id.native_ad_call_to_action);

                Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/merriweather_bold.ttf");
                nativeAdCallToAction.setTypeface(tf, Typeface.BOLD);

                // Setting the Text
                nativeAdCallToAction.setText(mNativeBannerAd.getAdCallToAction());
                nativeAdCallToAction.setVisibility(
                        mNativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                nativeAdTitle.setText(mNativeBannerAd.getAdvertiserName());
                nativeAdSocialContext.setText(mNativeBannerAd.getAdSocialContext());

                // You can use the following to specify the clickable areas.
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdCallToAction);
                clickableViews.add(nativeAdIconView);
                clickableViews.add(nativeAdSocialContext);
                clickableViews.add(nativeAdTitle);

                mNativeBannerAd.registerViewForInteraction(mNativeBannerAdContainer, nativeAdIconView, clickableViews);

                sponsoredLabel.setText(R.string.sponsored);

                mNativeBannerAd.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
                            int i = view.getId();
                            if (i == R.id.native_ad_call_to_action) {
                                Log.d("", "Call to action button clicked");
                            } else if (i == R.id.native_icon_view) {
                                Log.d("", "Main image clicked");
                            } else {
                                Log.d("", "Other ad component clicked");
                            }
                        }
                        return false;
                    }
                });
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
            }
        };

        // Request an ad
        mNativeBannerAd.loadAd(mNativeBannerAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());
    }

    public void loadNativeAds(final Activity activity, final NativeAdLayout nativeAdLayout) {

        final NativeAd nativeAd = new NativeAd(activity, AndroidPlugin.fb_native_id);
        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(activity, nativeAdLayout, nativeAd);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
            }
        };

        // Request an ad
        nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build());

    }

    private void inflateAd(Activity activity, NativeAdLayout nativeAdLayout, NativeAd nativeAd) {

        nativeAd.unregisterView();
        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.fb_native, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(activity, nativeAd, nativeAdLayout,
                AdOptionsView.Orientation.VERTICAL, 6);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/merriweather_bold.ttf");
        nativeAdCallToAction.setTypeface(tf, Typeface.BOLD);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        clickableViews.add(nativeAdMedia);
        clickableViews.add(nativeAdSocialContext);
        clickableViews.add(nativeAdBody);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(adView, nativeAdMedia, nativeAdIcon, clickableViews);

    }

    public void loadRewardVideoAd(final Activity context, final MyCallback _myCallback) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.progressdialog, null);
        dialogBuilder.setView(dialogView);
        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        if (!((Activity) context).isFinishing()) {
            try {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                alertDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        this.myCallback = _myCallback;
        if (AndroidPlugin.adsRewarded.equalsIgnoreCase("Google")) {
            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
            AdRequest adRequest = new AdRequest.Builder().build();
            mRewardedVideoAd.loadAd(AndroidPlugin.adMob_rewarded_id, adRequest);

            mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                @Override
                public void onRewardedVideoAdLoaded() {
                    if (alertDialog != null) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                    mRewardedVideoAd.show();
                }

                @Override
                public void onRewardedVideoAdOpened() {

                }

                @Override
                public void onRewardedVideoStarted() {

                }

                @Override
                public void onRewardedVideoAdClosed() {
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }

                @Override
                public void onRewarded(RewardItem rewardItem) {
                    reward = 2;
                }

                @Override
                public void onRewardedVideoAdLeftApplication() {

                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i) {
                    if (alertDialog != null) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                    reward = 1;
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }

                @Override
                public void onRewardedVideoCompleted() {

                }
            });
        } else if (AndroidPlugin.adsRewarded.equalsIgnoreCase("Facebook")) {
            interstitialFB = new com.facebook.ads.InterstitialAd(context, AndroidPlugin.fb_interstitial_id);
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                    // Interstitial ad displayed callback
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    reward = 2;
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    if (alertDialog != null) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                    reward = 1;
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    if (alertDialog != null) {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
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
        }
    }
}
