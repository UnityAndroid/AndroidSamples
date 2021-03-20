package lyrically.photovideomaker.particl.ly.musicallybeat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.giraffeplayer.GiraffePlayer;
import lyrically.photovideomaker.particl.ly.musicallybeat.giraffeplayer.GiraffePlayerActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.giraffeplayer.IjkVideoView;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.AdmobAds;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

import java.io.File;

import tv.danmaku.ijk.media.player.IMediaPlayer;

public class PlayerActivity extends AppCompatActivity {
    DatabaseManager manager;
    Context context;

    String filepath;

    int model = 0, creation = 0;
    boolean pause = false;
    LinearLayout rightll;
    RelativeLayout videobox;
    ProgressBar progressBar;
    IjkVideoView videoView;
    GiraffePlayer player;

    public DialogLoader dialogLoader;
    ImageView whatsapp, facebook, instagram, backbtn, moreShare, background, fullscreen;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        context = this;
        manager = new DatabaseManager(context);
        dialogLoader = new DialogLoader(this, "Loading Please Wait");
        if (getIntent() != null) {
            if (getIntent().hasExtra("filepath"))
                filepath = getIntent().getExtras().getString("filepath");


            if (getIntent().hasExtra("creation")) {
                creation = getIntent().getExtras().getInt("creation");
                if (creation == 1)
                    Constants.isbitFirst = true;
            }

        }
        getIDs();
        setEvents();
        doWork();

    }

    private void getIDs() {
        videobox = findViewById(R.id.app_video_box);
        videoView = findViewById(R.id.video_view);
        whatsapp = findViewById(R.id.ivVideoShareWhatsapp);
        facebook = findViewById(R.id.ivVideoShareFb);
        instagram = findViewById(R.id.ivVideoShareInsta);
        moreShare = findViewById(R.id.ivVideoShareMore);
        background = findViewById(R.id.player_background);
        backbtn = findViewById(R.id.player_back_button);
        fullscreen = findViewById(R.id.app_video_fullscreen);
        progressBar = findViewById(R.id.app_video_loading);
        rightll = findViewById(R.id.rightll);
    }

    private void setEvents() {

        fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                GiraffePlayerActivity.configPlayer(PlayerActivity.this).setScaleType(GiraffePlayer.SCALETYPE_FILLPARENT).play(filepath);
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideoWhatsApp();
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideoFacebook();
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideoInsta();
            }
        });

        moreShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideoMore();
            }
        });


    }

    private void doWork() {
        showLoader();
        Cursor cursor = null;
        try {
            cursor = manager.getcreationbyfilepath(filepath);
            if (cursor.moveToFirst()) {
                if (new File(cursor.getString(cursor.getColumnIndex("filepath"))).exists()) {
                } else {
                    manager.deletecreation(cursor.getString(cursor.getColumnIndex("filepath")));
                    onBackPressed();
                }
            } else {
                onBackPressed();
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }

        GlideImageLoader.SetImageResource(context, background, R.drawable.player_background, null);
        GlideImageLoader.SetImageResource(context, whatsapp, R.drawable.player_whatsapp, null);
        GlideImageLoader.SetImageResource(context, instagram, R.drawable.player_instagram, null);
        GlideImageLoader.SetImageResource(context, facebook, R.drawable.player_facebook, null);
        GlideImageLoader.SetImageResource(context, moreShare, R.drawable.player_more, null);
        GlideImageLoader.SetImageResource(context, backbtn, R.drawable.all_back_button, null);

        player = new GiraffePlayer(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setupvideo();
            }
        }, 200);

    }


    public void setupvideo() {

        if (manager.getGoogleAdStatus() == 1) {
            LinearLayout ll = findViewById(R.id.player_banner);
            AdmobAds.getInstance().loadBanner(this, ll);
        } else if (manager.getFacebookAdStatus() == 1) {
            LinearLayout ll = findViewById(R.id.player_banner);
            AdmobAds.getInstance().loadfbbanner(this, ll);
        }


        player.play(filepath);
        player.setScaleType(GiraffePlayer.SCALETYPE_FITXY);
        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {

                if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    if (progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.GONE);

                    videoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(videoView.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            videobox.setLayoutParams(lp);
                            videobox.setGravity(Gravity.CENTER);

                            RelativeLayout.LayoutParams lpp = new RelativeLayout.LayoutParams(videoView.getMeasuredWidth(), ViewGroup.LayoutParams.MATCH_PARENT);
                            rightll.setLayoutParams(lpp);

                            hideLoader();

                          /*  setPopinAnimation(whatsapp);
                            setPopinAnimation(instagram);
                            setPopinAnimation(facebook);
                            setPopinAnimation(moreShare);*/


                        }
                    });

                }
                return false;
            }
        });

    }

    private void setPopinAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
        animation.setDuration(500);
        viewToAnimate.startAnimation(animation);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!pause) {
            if (videoView != null) {
                videoView.pause();
                pause = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (pause) {
            if (videoView != null) {
                videoView.start();
                pause = false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }

        GlideImageLoader.endGlideProcess(getApplicationContext());

    }


    public void shareVideoWhatsApp() {
        Uri uri = FileProvider.getUriForFile(context, Constants.cpack + ".fileprovider", new File(filepath));
        String app_name = context.getResources().getString(R.string.app_name);

        Intent videoshare = new Intent(Intent.ACTION_SEND);
        videoshare.setType("video/*");
        videoshare.setPackage("com.whatsapp");
        String name = "App:" + app_name;
        videoshare.putExtra(Intent.EXTRA_SUBJECT, name);
        String text = app_name + "  : Lyrical Video Maker\n" +
                "https://play.google.com/store/apps/details?id=" + Constants.cpack + "\n";
        videoshare.putExtra(Intent.EXTRA_TEXT, text);
        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        videoshare.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(videoshare);
        } catch (ActivityNotFoundException unused) {
            AndroidPlugin.showToast(context, "Whatsapp have not been installed.");
        }

    }

    public void shareVideoFacebook() {
        Uri uri = FileProvider.getUriForFile(context, Constants.cpack + ".fileprovider", new File(filepath));
        String app_name = context.getResources().getString(R.string.app_name);

        Intent videoshare = new Intent(Intent.ACTION_SEND);
        videoshare.setType("video/*");
        videoshare.setPackage("com.facebook.katana");
        String name = "App:" + app_name;
        videoshare.putExtra(Intent.EXTRA_SUBJECT, name);
        String text = app_name + "  : Lyrical Video Maker\n" +
                "https://play.google.com/store/apps/details?id=" + Constants.cpack + "\n";
        videoshare.putExtra(Intent.EXTRA_TEXT, text);
        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        videoshare.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(videoshare);
        } catch (ActivityNotFoundException unused) {
            AndroidPlugin.showToast(context, "Facebook have not been installed.");

        }

    }


    public void shareVideoInsta() {
        Uri uri = FileProvider.getUriForFile(context, Constants.cpack + ".fileprovider", new File(filepath));
        String app_name = context.getResources().getString(R.string.app_name);
        Intent videoshare = new Intent(Intent.ACTION_SEND);
        videoshare.setType("video/*");
        videoshare.setPackage("com.instagram.android");
        String name = "App:" + app_name;
        videoshare.putExtra(Intent.EXTRA_SUBJECT, name);
        String text = app_name + "  : Lyrical Video Maker\n" +
                "https://play.google.com/store/apps/details?id=" + Constants.cpack + "\n";
        videoshare.putExtra(Intent.EXTRA_TEXT, text);
        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        videoshare.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(videoshare);
        } catch (ActivityNotFoundException unused) {
            AndroidPlugin.showToast(context, "Instagram have not been installed.");

        }

    }

    public void shareVideoMore() {

        Uri uri = FileProvider.getUriForFile(context, Constants.cpack + ".fileprovider", new File(filepath));
        String app_name = context.getResources().getString(R.string.app_name);
        Intent videoshare = new Intent(Intent.ACTION_SEND);
        videoshare.setType("video/*");
        String name = "App:" + app_name;
        videoshare.putExtra(Intent.EXTRA_SUBJECT, name);
        String text = app_name + "  : Lyrical Video Maker\n" +
                "https://play.google.com/store/apps/details?id=" + Constants.cpack + "\n";
        videoshare.putExtra(Intent.EXTRA_TEXT, text);
        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        videoshare.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(videoshare, "Share Video"));

    }


    @Override
    public void onBackPressed() {
        if (model == 1) {
            //  mainvideo.getPlayer().setDisplayModel(0);
        } else {
            startActivity(new Intent(context, CreationActivity.class));
            finish();
        }
    }


}
