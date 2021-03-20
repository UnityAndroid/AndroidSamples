package lyrically.photovideomaker.particl.ly.musicallybeat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.AdmobAds;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.googleMaster;

import com.unity3d.player.UnityPlayer;

public class MainActivity extends AppCompatActivity {
    private ImageView Ivbackground, IvLyricalBack, IvCreationBack, IvShareBack, IvRateBack, IvTop;
    private ImageView IvLyrical, IvCreation, IvShare, IvRate;
    private RelativeLayout RlLyrical, RlCreation, RlShare, RlRate;
    private Context context;
    private int IntWidth;
    boolean check = false;
    boolean doubleBackToExitPressedOnce = false;
    DatabaseManager manager;

    CardView adrl;

    FrameLayout frameLayout;
    RelativeLayout ad_text_native;

    public DialogLoader dialogLoader;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;


        if (Constants.isbitFirst) {
            Constants.isbitFirst = false;
            AndroidPlugin.getParticles(context);
            AndroidPlugin.getbitParticles(context);
        }

        if (!AdmobAds.ads) {
            MobileAds.initialize(context, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            AdmobAds.getInstance().initbanner(context);
            AdmobAds.getInstance().loadintertialads(context);

            AdmobAds.getInstance().initfbbanner(context);
            AdmobAds.getInstance().loadfbintertialads(context);

            AdmobAds.ads = true;
        }

        dialogLoader = new DialogLoader(this, "Loading AD Please Wait");
        manager = new DatabaseManager(context);
        getIDs();
        setEvents();

        if (Constants.isFirst) {
            Constants.isFirst = false;
            if (manager.getGoogleAdStatus() == 1) {
                AdmobAds.getInstance().displayInterstitial(context, new AdmobAds.MyCallback() {
                    @Override
                    public void callbackCall() {
                        doWork();
                    }
                });
            } else if (manager.getFacebookAdStatus() == 1) {
                AdmobAds.getInstance().displayfbInterstitial(context, new AdmobAds.MyCallback() {
                    @Override
                    public void callbackCall() {
                        doWork();
                    }
                });
            } else {
                doWork();
            }
        } else {
            doWork();
        }
    }

    public void showLoader() {
        if (dialogLoader != null) {
            if (dialogLoader.isShowing())
                dialogLoader.dismiss();
        }
        dialogLoader.show();

    }

    public void hideLoader() {
        if (dialogLoader != null)
            if (dialogLoader.isShowing())
                dialogLoader.dismiss();

    }

    private void getIDs() {
        Ivbackground = findViewById(R.id.main_background);
        IvLyricalBack = findViewById(R.id.main_lyrical_background);
        IvCreationBack = findViewById(R.id.main_creation_background);
        IvShareBack = findViewById(R.id.main_share_background);
        IvRateBack = findViewById(R.id.main_rate_background);

        IvTop = findViewById(R.id.top_image);
        IvLyrical = findViewById(R.id.main_lyrical_icon);
        IvCreation = findViewById(R.id.main_creation_icon);
        IvShare = findViewById(R.id.main_share_icon);
        IvRate = findViewById(R.id.main_rate_icon);

        RlLyrical = findViewById(R.id.main_lyrical_button);
        RlCreation = findViewById(R.id.main_creation_button);
        RlShare = findViewById(R.id.main_share_button);
        RlRate = findViewById(R.id.main_rate_button);

        ad_text_native = (RelativeLayout) findViewById(R.id.adtext_native);
        adrl = findViewById(R.id.ad_rl);
        frameLayout = findViewById(R.id.fl_adplaceholder);
    }

    private void setEvents() {
        RlLyrical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.getFacebookAdStatus() == 1) {
                    showLoader();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AdmobAds.getInstance().displayfbInterstitial(MainActivity.this, new AdmobAds.MyCallback() {
                                @Override
                                public void callbackCall() {
                                    hideLoader();
                                    startActivity(new Intent(context, HomeActivity.class));
                                    finish();
                                }
                            });
                        }
                    }, 1000);

                } else {
                    startActivity(new Intent(context, HomeActivity.class));
                    finish();
                }
            }
        });

        RlCreation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (manager.getFacebookAdStatus() == 1) {
                    showLoader();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AdmobAds.getInstance().displayfbInterstitial(MainActivity.this, new AdmobAds.MyCallback() {
                                @Override
                                public void callbackCall() {
                                    hideLoader();
                                    startActivity(new Intent(context, CreationActivity.class));
                                    finish();
                                }
                            });
                        }
                    }, 1000);

                } else {
                    startActivity(new Intent(context, CreationActivity.class));
                    finish();
                }
            }
        });

        RlShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareapp();
            }
        });

        RlRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateus();
            }
        });
    }

    private void doWork() {
        GlideImageLoader.SetImageResource(context, Ivbackground, R.drawable.main_background, null);
        GlideImageLoader.SetImageResource(context, IvTop, R.drawable.main_guitar, null);

        GlideImageLoader.SetImageResource(context, IvLyricalBack, R.drawable.main_lyrical_background, null);
        GlideImageLoader.SetImageResource(context, IvCreationBack, R.drawable.main_creation_background, null);
        GlideImageLoader.SetImageResource(context, IvShareBack, R.drawable.main_share_background, null);
        GlideImageLoader.SetImageResource(context, IvRateBack, R.drawable.main_rate_background, null);

        GlideImageLoader.SetImageResource(context, IvLyrical, R.drawable.main_lyrical_icon, null);
        GlideImageLoader.SetImageResource(context, IvCreation, R.drawable.main_creation_icon, null);
        GlideImageLoader.SetImageResource(context, IvShare, R.drawable.main_share_icon, null);
        GlideImageLoader.SetImageResource(context, IvRate, R.drawable.main_rate_icon, null);

        IntWidth = (Constants.getScreenWidth(context) - Constants.getPixelInteger(context, 40)) / 2;


        adrl.setVisibility(View.VISIBLE);
        googleMaster.getInstance().showNative((MainActivity) context, frameLayout, ad_text_native);

        setMagic();

    }

    private void setPopinAnimation(View viewToAnimate) {

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
        animation.setDuration(500);
        viewToAnimate.startAnimation(animation);
    }

    public void setRate() {
        RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(IntWidth, IntWidth);
        //  lp4.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        //  lp4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        lp4.addRule(RelativeLayout.RIGHT_OF, R.id.main_share_button);
        lp4.addRule(RelativeLayout.BELOW, R.id.main_creation_button);
        RlRate.setLayoutParams(lp4);

        setPopinAnimation(RlLyrical);
        setPopinAnimation(RlCreation);
        setPopinAnimation(RlShare);
        setPopinAnimation(RlRate);
    }

    public void setShare() {
        int margin = Constants.getPixelInteger(context, 10);
        RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(IntWidth, IntWidth);
        lp3.addRule(RelativeLayout.BELOW, R.id.main_lyrical_button);
        RlShare.setLayoutParams(lp3);
        RlShare.setPadding(margin * 2, margin * 2, margin * 2, margin * 2);

        setRate();
    }

    public void setMagic() {
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(IntWidth, IntWidth);
        //  lp1.setMargins(AndroidPlugin.getPixelInteger(context, 10), 0, 0, 0);
        //  lp1.addRule(RelativeLayout.ABOVE, R.id.main_share_button);
        RlLyrical.setLayoutParams(lp1);

        setCreation();
    }

    public void setCreation() {
        int margin = Constants.getPixelInteger(context, 10);
        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(IntWidth, IntWidth);
        lp2.addRule(RelativeLayout.RIGHT_OF, R.id.main_lyrical_button);
        //  lp2.addRule(RelativeLayout.ABOVE, R.id.main_rate_button);
        // lp2.setMargins(0, 0, AndroidPlugin.getPixelInteger(context, 20), 0);
        RlCreation.setLayoutParams(lp2);
        RlCreation.setPadding(margin * 2, margin * 2, margin * 2, margin * 2);

        setShare();

    }

    @Override
    public void onBackPressed() {
  /*      if (!check) {
            if (doubleBackToExitPressedOnce) {
                UnityPlayer.UnitySendMessage("Category", "CloseGame", "Good Morning");
                finishAffinity();
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(context,
                    "Please click BACK again to exit",
                    Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);

            //  startActivity(new Intent(MainActivity.this, ExitActivity.class));
        } else {
            MainActivity.super.onBackPressed();
            MainActivity.this.finish();
        }*/

        startActivity(new Intent(context, MoreAppActivity.class));
    }


    public void shareapp() {
        String app_name = context.getResources().getString(R.string.app_name);

        String name = getResources().getString(R.string.app_name);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, name);
        String text = app_name + "  : Particle.maker Video Status Maker\n" +
                "https://play.google.com/store/apps/details?id=" + Constants.cpack + "\n";
        i.putExtra(Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(i, "choose one"));

    }

    public void rateus() {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + Constants.cpack));
        startActivity(rateIntent);
    }

}
