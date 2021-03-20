package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;

public class ImageViewerActivity extends AppCompatActivity {

    private final String TAG = "ImageViewer";
    String atype = "";
    public int count = 6;
    ImageView imageView;
    String image_path = "";
    public List<Observer> observers;
    String package_name = "";
    String path = "";
    String type = "";

    Button btnDownload;
    Button btnFacebook;
    Button btnInstagram;
    Button btnShare;
    Button btnWhatsApp;
    LinearLayout llDownload;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_wa_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        toolbar.setTitle("Share Image");

        this.imageView = (ImageView) findViewById(R.id.imageView);
        this.observers = new ArrayList();

        Intent intent = getIntent();
        if (intent != null) {
            image_path = intent.getStringExtra("image");
            type = intent.getStringExtra("type");
            atype = intent.getStringExtra("atype");
            if (image_path != null) {
                Glide.with(getApplicationContext()).load(this.image_path).into(this.imageView);
            }
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
                copyFileOrDirectory(image_path, path);
            }
        });

        btnFacebook = (Button) findViewById(R.id.btnFacebook);
        btnFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String packgeName = "com.facebook.katana";
                String packgeName2 = "com.facebook.lite";
                if (Config.isPackageInstalled(ImageViewerActivity.this, packgeName)) {
                    Intent facebookIntent = new Intent("android.intent.action.SEND");
                    facebookIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    facebookIntent.setType("image/jpeg");
                    facebookIntent.setPackage(packgeName);
                    facebookIntent.putExtra("android.intent.extra.STREAM", Config.getFileUri(getApplicationContext(), new File(image_path)));
                    startActivity(facebookIntent);
                } else if (Config.isPackageInstalled(ImageViewerActivity.this, packgeName2)) {
                    Intent facebookIntent2 = new Intent("android.intent.action.SEND");
                    facebookIntent2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    facebookIntent2.setType("image/jpeg");
                    facebookIntent2.setPackage(packgeName2);
                    facebookIntent2.putExtra("android.intent.extra.STREAM", Config.getFileUri(getApplicationContext(), new File(image_path)));
                    startActivity(facebookIntent2);
                } else {
                    Toast.makeText(ImageViewerActivity.this, "Facebook app is not installed", Toast.LENGTH_SHORT).show();
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
                    instagramIntent.setType("image/*");
                    instagramIntent.putExtra("android.intent.extra.TEXT", "");
                    instagramIntent.putExtra("android.intent.extra.STREAM", Config.getFileUri(getApplicationContext(), new File(image_path)));
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
                if (Config.isPackageInstalled(ImageViewerActivity.this, packageName)) {
                    Intent whatsappIntent = new Intent("android.intent.action.SEND");
                    whatsappIntent.setType("image/*");
                    whatsappIntent.putExtra("android.intent.extra.TEXT", "");
                    whatsappIntent.putExtra("android.intent.extra.STREAM", Config.getFileUri(getApplicationContext(), new File(image_path)));
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
                share.setType("image/*");
                share.putExtra("android.intent.extra.STREAM", Config.getFileUri(getApplicationContext(), new File(image_path)));
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
            Toast.makeText(getApplicationContext(), "Picture Saved", Toast.LENGTH_SHORT).show();
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


    public void showBanner() {
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.adBanner);
        adContainer.removeAllViews();
        AdMobLoadAds.getInstance().loadBanner(ImageViewerActivity.this, adContainer);
    }
}
