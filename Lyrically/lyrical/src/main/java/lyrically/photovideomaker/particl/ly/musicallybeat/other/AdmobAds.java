package lyrically.photovideomaker.particl.ly.musicallybeat.other;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import java.util.ArrayList;
import java.util.List;

public class AdmobAds {

    /*public static String bannerid = "ca-app-pub-1275180398540464/8178153598";
    public static String interid = "ca-app-pub-1275180398540464/2038452351";
    public static String rewardid = "ca-app-pub-1275180398540464/1485247572";
    public static String nativeid = "ca-app-pub-1275180398540464/2925826911";
*/

    public static String bannerid = "/419163168/lyrically.photovideomaker.particl.ly.musicallybeat.Banner";
    public static String interid = "/419163168/lyrically.photovideomaker.particl.ly.musicallybeat.Interstitial";
    public static String rewardid = "/419163168/lyrically.photovideomaker.particl.ly.musicallybeat.Rewarded";
    public static String nativeid = "/419163168/lyrically.photovideomaker.particl.ly.musicallybeat.Native";

    public static String fbbannerid = "2651800455108570_2651802285108387";
    public static String fbinterid = "2651800455108570_2651802195108396";
    public static String fbnative = "2651800455108570_2651801945108421";

    public static String TAG = AdmobAds.getInstance().getClass().getName();
    private static AdmobAds mInstance;
    public InterstitialAd interstitial;

    AdView adViewGoogle;
    MyCallback myCallback;
    public static boolean ads = false;

    private static com.facebook.ads.AdView adView;
    private com.facebook.ads.InterstitialAd interstitialAd;
    private NativeAd nativeAd;

    private RewardedVideoAd mRewardedVideoAd;
    public static int reward = 0;
    public static DatabaseManager manager;
    InterstitialAdListener interstitialAdListener;
    public static AdmobAds getInstance() {
        if (mInstance == null) {
            mInstance = new AdmobAds();
        }
        return mInstance;
    }

    public static void checkdb(Context context) {
        if (manager == null) {
            manager = new DatabaseManager(context);
        }
    }

    public void loadintertialads(final Context context) {
        checkdb(context);
        if (manager.getGoogleAdStatus() == 1) {
            interstitial = new InterstitialAd(context);
            interstitial.setAdUnitId(interid);
            interstitial.loadAd(requestAd());
            interstitial.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    Log.e("error google ad inter", errorCode + " error");
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                    interstitial.loadAd(requestAd());
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }
            });
        }
    }

    public void displayInterstitial(Context context, MyCallback _myCallback) {
        this.myCallback = _myCallback;
        checkdb(context);
        if (manager.getGoogleAdStatus() == 1 && interstitial != null) {
            if (interstitial != null) {
                if (interstitial.isLoaded()) {
                    interstitial.show();
                } else if (!interstitial.isLoading()) {
                    try {
                        // interstitial.loadAd(adRequest);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
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
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        }
    }


    public interface MyCallback {
        void callbackCall();
    }


    public void loadRewardVideoAd(Context context) {

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
        mRewardedVideoAd.loadAd(rewardid, requestAd());

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {

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
                mRewardedVideoAd.loadAd(rewardid, requestAd());

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
                Log.e("error google reward", i + " ");

            }

            @Override
            public void onRewardedVideoCompleted() {

            }
        });
    }

    public void showRewardAd(Context context, MyCallback _myCallback) {
        this.myCallback = _myCallback;
        if (mRewardedVideoAd != null) {
            if (mRewardedVideoAd.isLoaded()) {
                mRewardedVideoAd.show();
            } else {
                reward = 2;
                if (myCallback != null) {
                    myCallback.callbackCall();
                    myCallback = null;
                }
            }
        } else {
            reward = 2;
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        }
    }

    private AdRequest requestAd() {
        AdRequest.Builder builder = new AdRequest.Builder();
        for (String addTestDevice : this.device_test_id) {
            builder.addTestDevice(addTestDevice);
        }
        return builder.build();
    }

    private final String[] device_test_id = new String[]{"1CA0FAC6AFA6404CF0E06EFA876A1039"};


    public void initbanner(Context context) {
        checkdb(context);
        if (manager.getGoogleAdStatus() == 1) {
            adViewGoogle = new AdView(context);
            adViewGoogle.setAdSize(AdSize.SMART_BANNER);
            adViewGoogle.setAdUnitId(bannerid);
        }
    }

    public void loadBanner(Context context, LinearLayout linearLayout) {
        checkdb(context);
        if (manager.getGoogleAdStatus() == 1 ) {
            if (adViewGoogle != null) {
                if (adViewGoogle.getParent() != null) {
                    ((ViewGroup) adViewGoogle.getParent()).removeView(adViewGoogle);
                }

                linearLayout.addView(adViewGoogle);
                adViewGoogle.loadAd(requestAd());
                adViewGoogle.setAdListener(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(int i) {
                        super.onAdFailedToLoad(i);
                        Log.e("error ad google banner", i + " error");
                    }

                    @Override
                    public void onAdLeftApplication() {
                        super.onAdLeftApplication();
                    }

                    @Override
                    public void onAdOpened() {
                        super.onAdOpened();
                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
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
            }
        }
    }


    public void loadfbintertialads(final Context context) {
        checkdb(context);
        if (manager.getFacebookAdStatus() == 1 ) {
            interstitialAd = new com.facebook.ads.InterstitialAd(context, fbinterid);
            InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
                @Override
                public void onInterstitialDisplayed(Ad ad) {
                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                   if (myCallback != null) {
                        myCallback.callbackCall();
                        myCallback = null;
                    }
                    loadfbintertialads(context);
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                }

                @Override
                public void onAdLoaded(Ad ad) {
                }

                @Override
                public void onAdClicked(Ad ad) {
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                }
            };
            interstitialAd.loadAd(interstitialAd.buildLoadAdConfig().withAdListener(interstitialAdListener).build());
        }
    }

    public void displayfbInterstitial(Context context, MyCallback _myCallback) {
        this.myCallback = _myCallback;
        checkdb(context);
        if (manager.getFacebookAdStatus() == 1) {
            if (interstitialAd != null) {
                if (interstitialAd.isAdLoaded()) {
                    interstitialAd.show();
                } else {
                    try {
                        //  interstitialAd.loadAd();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
            if (myCallback != null) {
                myCallback.callbackCall();
                myCallback = null;
            }
        }
    }


    public void initfbbanner(Context context) {
        checkdb(context);
        if (manager.getFacebookAdStatus() == 1) {
            adView = new com.facebook.ads.AdView(context, fbbannerid, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        }
    }

    public void loadfbbanner(Context context, LinearLayout ll) {
        checkdb(context);
        if (manager.getFacebookAdStatus() == 1) {
            if (adView != null) {
                if (adView.getParent() != null) {
                    ((ViewGroup) adView.getParent()).removeView(adView);
                }
                ll.addView(adView);
                adView.loadAd();
            }
        }
    }


    public void loadfbNativeAd(final Context activity, final NativeAdLayout nativeAdLayout) {
        checkdb(activity);
        if (manager.getFacebookAdStatus() == 1) {
            nativeAd = new NativeAd(activity, fbnative);
            NativeAdListener adListener=new NativeAdListener() {
                @Override
                public void onMediaDownloaded(Ad ad) {
                    Log.e(TAG, "Native ad finished downloading all assets.");
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
                }

                @Override
                public void onAdLoaded(Ad ad) {
                    Log.d(TAG, "Native ad is loaded and ready to be displayed!");
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                    inflateAd(activity, nativeAdLayout, nativeAd);
                }

                @Override
                public void onAdClicked(Ad ad) {
                    Log.d(TAG, "Native ad clicked!");
                }

                @Override
                public void onLoggingImpression(Ad ad) {
                    Log.d(TAG, "Native ad impression logged!");
                }
            };

            nativeAd.loadAd(nativeAd.buildLoadAdConfig().withAdListener(adListener).build());
        }
    }

    private void inflateAd(Context activity, NativeAdLayout nativeAdLayout, NativeAd nativeAd) {
        nativeAd.unregisterView();
        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(activity);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.ad_fb_unified, nativeAdLayout, false);
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

       /* Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/merriweather_bold.ttf");
        nativeAdCallToAction.setTypeface(tf, Typeface.BOLD);
*/
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


}
