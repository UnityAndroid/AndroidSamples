package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.Calendar;

import cn.jzvd.JZVideoPlayerStandard;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.AndroidPlugin;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;

public class PlayerActivity extends AppCompatActivity {

    public LinearLayout rightll;
    public RelativeLayout surface;
    public String filepath;
    public Context context;
    public ImageView back;
    public ImageView whatsapp, facebook, instagram, youtube;
    public ImageView moreShare;
    public int model = 0;
    public ImageView IvDelete;
    public ImageView VidTitle;
    public JZVideoPlayerStandard jzVideoPlayerStandard;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        filepath = getIntent().getExtras().getString("filepath");

        context = PlayerActivity.this;

        VidTitle = findViewById(R.id.video_title);
        rightll = findViewById(R.id.rightll);
        IvDelete = findViewById(R.id.creation_delete_image);
        surface = findViewById(R.id.videoSurfaceContainer);
        jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        whatsapp = findViewById(R.id.ivVideoShareWhatsapp);
        facebook = findViewById(R.id.ivVideoShareFb);
        instagram = findViewById(R.id.ivVideoShareInsta);
        youtube = findViewById(R.id.ivVideoShareYouTube);
        moreShare = findViewById(R.id.ivVideoShareMore);
        back = findViewById(R.id.player_back_button);

        VidTitle.setSelected(true);
        rightll.setVisibility(View.VISIBLE);

        back.setOnClickListener(new View.OnClickListener() {
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

        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareVideoYouTube();
            }
        });

        LinearLayout creation_banner = (LinearLayout) findViewById(R.id.creation_banner);
        AdMobLoadAds.getInstance().loadBanner(PlayerActivity.this, creation_banner);

        setupvideo();

        IvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletevideo();
            }
        });

    }

    public void deletevideo() {
        new AlertDialog.Builder(context)
                .setTitle("Delete Creation")
                .setMessage("Are you sure you want to delete this creation?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(filepath);
                        final String where = MediaStore.MediaColumns.DATA + "=?";
                        final String[] selectionArgs = new String[]{
                                file.getAbsolutePath()
                        };
                        final ContentResolver contentResolver = getContentResolver();
                        final Uri filesUri = MediaStore.Files.getContentUri("external");

                        contentResolver.delete(filesUri, where, selectionArgs);
                        if (file.exists()) {
                            contentResolver.delete(filesUri, where, selectionArgs);
                        }

                        Uri uri1 = Uri.fromFile(file);
                        Intent scanFileIntent1 = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri1);
                        sendBroadcast(scanFileIntent1);

                        onBackPressed();
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.if_caution)
                .show();
    }

    private void setPopinAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
        animation.setDuration(1000);
        viewToAnimate.startAnimation(animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (jzVideoPlayerStandard != null) {
            jzVideoPlayerStandard.releaseAllVideos();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (jzVideoPlayerStandard != null) {
            jzVideoPlayerStandard.releaseAllVideos();
        }

        Glide.with(getApplicationContext()).pauseRequests();
    }

    public void setupvideo() {

        jzVideoPlayerStandard.setUp(filepath, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "");
        Glide.with(getApplicationContext()).load(filepath).apply(new RequestOptions().centerCrop()).into(jzVideoPlayerStandard.thumbImageView);
        jzVideoPlayerStandard.FULLSCREEN_ORIENTATION = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        jzVideoPlayerStandard.batteryLevel.setVisibility(View.GONE);
        jzVideoPlayerStandard.batteryTimeLayout.setVisibility(View.GONE);
        jzVideoPlayerStandard.startVideo();

        rightll.setVisibility(View.VISIBLE);

        setPopinAnimation(whatsapp);
        setPopinAnimation(instagram);
        setPopinAnimation(facebook);
        setPopinAnimation(moreShare);
        setPopinAnimation(youtube);
    }


    public void shareVideoWhatsApp() {
        Uri uri = FileProvider.getUriForFile(context, getPackageName() + ".fileprovider", new File(filepath));
        Intent videoshare = new Intent(Intent.ACTION_SEND);
        videoshare.setType("video/*");
        videoshare.setPackage("com.whatsapp");
        String name = "App: Lyrical Bit Music : Photo Video Status Maker";
        String text = "App: Lyrical Bit Music : Photo Video Status Maker" +
                "\n\n https://play.google.com/store/apps/details?id=" + getPackageName() + "\n";
        videoshare.putExtra(Intent.EXTRA_SUBJECT, name);
        videoshare.putExtra(Intent.EXTRA_TEXT, text);
        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        videoshare.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(videoshare);
        } catch (ActivityNotFoundException unused) {
            AndroidPlugin.showToast(getApplicationContext(), "Whatsapp have not been installed.");
        }

    }

    public void shareVideoFacebook() {
        Uri uri = FileProvider.getUriForFile(context, getPackageName() + ".fileprovider", new File(filepath));
        Intent videoshare = new Intent(Intent.ACTION_SEND);
        videoshare.setType("video/*");
        videoshare.setPackage("com.facebook.katana");
        String name = "App: Lyrical Bit Music : Photo Video Status Maker";
        videoshare.putExtra(Intent.EXTRA_SUBJECT, name);
        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        videoshare.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(videoshare);
        } catch (ActivityNotFoundException unused) {
            AndroidPlugin.showToast(getApplicationContext(), "Facebook have not been installed.");
        }
    }

    public void shareVideoInsta() {
        Uri uri = FileProvider.getUriForFile(context, getPackageName() + ".fileprovider", new File(filepath));
        Intent videoshare = new Intent(Intent.ACTION_SEND);
        videoshare.setType("video/*");
        videoshare.setPackage("com.instagram.android");
        String name = "App: Lyrical Bit Music : Photo Video Status Maker";
        videoshare.putExtra(Intent.EXTRA_SUBJECT, name);
        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        videoshare.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(videoshare);
        } catch (ActivityNotFoundException unused) {
            AndroidPlugin.showToast(getApplicationContext(), "Instagram have not been installed.");
        }
    }

    public void shareVideoYouTube() {
        Uri uri = FileProvider.getUriForFile(context, getPackageName() + ".fileprovider", new File(filepath));
        Intent videoshare = new Intent(Intent.ACTION_SEND);
        videoshare.setType("video/*");
        videoshare.setPackage("com.google.android.youtube");
        String name = "App: Lyrical Bit Music : Photo Video Status Maker" +
                "https://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n" +
                "Lyrical Bit Music: Photo Video Status Maker app, creates animated lyrics with particle effect video status from your photos within seconds. \n" +
                "\n" +
                "Lyrical Bit Music: Photo Video Status Maker app- DIFFERENT FROM OTHER LYRICAL APP. You can change lyrics animation and filters effect by your ways other lyrical app provide just permanent lyrics style.\n" +
                "\n" +
                "Prepare beautiful and creative lyrical video and share videos on any \n" +
                "platform with the Lyrical Bit Music: Photo Video Status Maker app.\n" +
                "\n" +
                "With its creative and easy video lyrics template, you can instantly make your life moment in video with animated lyrics style, photo filters with effect, different particles and spectolizer.\n" +
                "\n" +
                "Lyrical Bit Music: Photo Video Status Maker app which contains lyrics of large number of song like love status song, sad status song, festival status song, wedding status song, birthday status song, friendship status song, patriotic, religious status song and dialogues in Hindi, English, Tamil, Telugu, Kannada, Marathi, Gujarati, Punjabi, Malayalam, Bengali, Rajasthani, Bhojpuri.\n" +
                "\n" +
                "Lyrical Music Categories:\n" +
                "➔ Love Songs to make Love Lyrical Video Maker\n" +
                "➔ Hindi Songs to make Hindi Lyrical Video Maker\n" +
                "➔ English Songs to make English Lyrical Video Maker\n" +
                "➔ Bhojpuri Songs to make Bhojpuri Lyrical Video Maker\n" +
                "➔ Rajshthani Songs to make Rajshthani Lyrical Video Maker\n" +
                "➔ Gujarati Songs to make Gujarati Lyrical Video Maker\n" +
                "➔ Arabic Songs to make Arabic Lyrical Video Maker\n" +
                "➔ Malayalam Songs to make malayalam Lyrical Video Maker\n" +
                "➔ Kannad Songs to make Kannad Lyrical Video Maker\n" +
                "➔ Punjabi Songs to make Punjabi Lyrical Video Maker\n" +
                "➔ Tamil Songs to make Tamil Lyrical Video Maker\n" +
                "➔ Marathi Songs to make Marathi Lyrical Video Maker\n" +
                "➔ Telugu Songs to make Telugu Lyrical Video Maker\n" +
                "➔ Birthday Songs to make Birthday Lyrical Video Maker\n" +
                "➔ Dialogue Songs to make Dialogue Lyrical Video Maker\n" +
                "➔ Sad Songs to make Sad Lyrical Video Maker\n" +
                "➔ Old Songs to make Old Lyrical Video Maker\n" +
                "\n" +
                "#LyricsVideoEditingApp\n" +
                "#LyricalVideoStatus\n" +
                "#LyricalVideoKaiseBanaye";

        String text = "love status,romantic status video,love whatsapp video status,new whatsapp status video,couple romance love status video 2020,30sec whatsapp status,breakup status,Hindi WhatsApp status,Marathi WhatsApp status," +
                "Panjabi WhatsApp status,sad Love Status,Love Whatsapp status,Hindi Old Song Remix WhatsApp status,new sweet couple romance status,love beat video maker,Particle.ly Video Status Maker,Bit Video Status Maker,Lyrical Status Maker,Animation Video Maker,bit master video editing";

        String title = "App: Lyrical Bit Music : Photo Video Status Maker Romantic Love WhatsApp Status Video Maker " + Calendar.getInstance().get(Calendar.YEAR);

        videoshare.putExtra(Intent.EXTRA_TITLE, title);
        videoshare.putExtra(Intent.EXTRA_SUBJECT, name);
        videoshare.putExtra(Intent.EXTRA_TEXT, text);
        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        videoshare.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(videoshare);
        } catch (ActivityNotFoundException unused) {
            AndroidPlugin.showToast(getApplicationContext(), "YouTube have not been installed.");
        }
    }

    public void shareVideoMore() {

        Uri uri = FileProvider.getUriForFile(context, getPackageName() + ".fileprovider", new File(filepath));
        Intent videoshare = new Intent(Intent.ACTION_SEND);
        videoshare.setType("video/*");

        String name = "App: Lyrical Bit Music : Photo Video Status Maker";

        String text = "App: Lyrical Bit Music : Photo Video Status Maker" +
                "\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName() + "\n\n";

        String title = "App: Lyrical Bit Music : Photo Video Status Maker Romantic Love WhatsApp Status Video Maker " + Calendar.getInstance().get(Calendar.YEAR);

        videoshare.putExtra(Intent.EXTRA_TITLE, title);
        videoshare.putExtra(Intent.EXTRA_SUBJECT, name);
        videoshare.putExtra(Intent.EXTRA_TEXT, text);
        videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        videoshare.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(videoshare, "Share Video"));

    }

    @Override
    public void onBackPressed() {
        AndroidPlugin.recpos = 0;
        startActivity(new Intent(context, HomeActivity.class));
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}