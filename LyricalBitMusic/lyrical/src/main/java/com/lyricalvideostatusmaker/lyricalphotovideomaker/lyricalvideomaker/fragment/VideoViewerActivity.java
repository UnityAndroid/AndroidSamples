package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;

public class VideoViewerActivity extends AppCompatActivity {

    private final String TAG = "VideoViewer";
    String atype = "";
    public int count = 6;
    public List<Observer> observers;
    String package_name = "";
    String path = "";
    public int position = 0;
    String type = "";
    String videoPath = "";
    VideoView video_view;

    Button btnDownload;
    Button btnFacebook;
    Button btnInstagram;
    Button btnShare;
    Button btnWhatsApp;
    LinearLayout llDownload;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_wa_video);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar.setTitle("Share Video");

        video_view = (VideoView) findViewById(R.id.video_view);
        observers = new ArrayList();

        Intent intent = getIntent();
        if (intent != null) {
            videoPath = intent.getStringExtra("video");
            type = intent.getStringExtra("type");
            atype = intent.getStringExtra("atype");
            video_view.setVideoPath(videoPath);
            video_view.start();
        }

        video_view.setMediaController(new MediaController(this));

        try {
            video_view.setOnPreparedListener(new OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    video_view.seekTo(position);
                    if (position == 0) {
                        video_view.start();
                    } else {
                        video_view.resume();
                    }
                }
            });
        } catch (Exception unused) {
            unused.printStackTrace();
        }

        if (type.equals("0")) {
            path = Config.WhatsAppSaveStatus;
            package_name = "com.whatsapp";
        } else if (type.equals("1")) {
            path = Config.GBWhatsAppSaveStatus;
            package_name = "com.gbwhatsapp";
        } else if (type.equals("2")) {
            path = Config.WhatsAppBusinessSaveStatus;
            package_name = "com.whatsapp.w4b";
        }

        llDownload = (LinearLayout) findViewById(R.id.llDownload);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                copyFileOrDirectory(videoPath, path);
            }
        });

        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String packgeName = "com.facebook.katana";
                String packgeName2 = "com.facebook.lite";
                if (Config.isPackageInstalled(getApplicationContext(), packgeName)) {
                    Intent facebookIntent = new Intent("android.intent.action.SEND");
                    facebookIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    facebookIntent.setType("*/*");
                    facebookIntent.setPackage(packgeName);
                    facebookIntent.putExtra("android.intent.extra.STREAM", Config.getFileUri(getApplicationContext(), new File(videoPath)));
                    startActivity(facebookIntent);
                } else if (Config.isPackageInstalled(getApplicationContext(), packgeName2)) {
                    Intent facebookIntent2 = new Intent("android.intent.action.SEND");
                    facebookIntent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    facebookIntent2.setType("*/*");
                    facebookIntent2.setPackage(packgeName2);
                    facebookIntent2.putExtra("android.intent.extra.STREAM", Config.getFileUri(getApplicationContext(), new File(videoPath)));
                    startActivity(facebookIntent2);
                } else {
                    Toast.makeText(getApplicationContext(), "Facebook app is not installed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnInstagram = (Button) findViewById(R.id.btnInstagram);
        btnInstagram.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageName2 = "com.instagram.android";
                if (Config.isPackageInstalled(getApplicationContext(), packageName2)) {
                    Intent instagramIntent = new Intent("android.intent.action.SEND");
                    instagramIntent.setType("video/*");
                    instagramIntent.putExtra("android.intent.extra.TEXT", "");
                    instagramIntent.putExtra("android.intent.extra.STREAM", Config.getFileUri(getApplicationContext(), new File(videoPath)));
                    instagramIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    instagramIntent.setPackage(packageName2);
                    startActivity(instagramIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Instagram app is not installed", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btnWhatsApp = (Button) findViewById(R.id.btnWhatsApp);
        btnWhatsApp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String packageName = "com.whatsapp";
                if (Config.isPackageInstalled(getApplicationContext(), packageName)) {
                    Intent whatsappIntent = new Intent("android.intent.action.SEND");
                    whatsappIntent.setType("*/*");
                    whatsappIntent.putExtra("android.intent.extra.TEXT", "");
                    whatsappIntent.putExtra("android.intent.extra.STREAM", Config.getFileUri(getApplicationContext(), new File(videoPath)));
                    whatsappIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    whatsappIntent.setPackage(packageName);
                    startActivity(whatsappIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Whatsapp app is not installed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnShare = (Button) findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent share = new Intent("android.intent.action.SEND");
                share.setType("video/*");
                share.putExtra("android.intent.extra.STREAM", Config.getFileUri(getApplicationContext(), new File(videoPath)));
                share.putExtra("android.intent.extra.TEXT", "");
                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(share, "Share Image"));
            }
        });


        if (atype.equals("0")) {
            llDownload.setVisibility(View.GONE);
        } else if (atype.equals("1")) {
            llDownload.setVisibility(View.VISIBLE);
        }

        showBanner();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("Position", video_view.getCurrentPosition());
        video_view.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        position = bundle.getInt("Position");
        video_view.seekTo(position);
    }

    public void copyFileOrDirectory(String str, String str2) {
        try {
            File file = new File(str);
            File file2 = new File(str2, file.getName());
            if (file.isDirectory()) {
                for (String file3 : file.list()) {
                    copyFileOrDirectory(new File(file, file3).getPath(), file2.getPath());
                }
                return;
            }
            copyFile(file, file2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFile(File file, File file2) throws IOException {
        if (!file2.getParentFile().exists()) {
            file2.getParentFile().mkdirs();
        }
        if (!file2.exists()) {
            file2.createNewFile();
        }
        try {
            FileChannel fileChannel2 = new FileInputStream(file).getChannel();
            FileChannel fileChannel = new FileOutputStream(file2).getChannel();
            fileChannel.transferFrom(fileChannel2, 0, fileChannel2.size());
            Toast.makeText(getApplicationContext(), "Video Saved", Toast.LENGTH_SHORT).show();
            if (fileChannel2 != null) {
                fileChannel2.close();
            }
            if (fileChannel != null) {
                fileChannel.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        video_view.start();
        super.onResume();
    }

    public void showBanner() {
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.adBanner);
        adContainer.removeAllViews();
        AdMobLoadAds.getInstance().loadBanner(VideoViewerActivity.this, adContainer);
    }


}
