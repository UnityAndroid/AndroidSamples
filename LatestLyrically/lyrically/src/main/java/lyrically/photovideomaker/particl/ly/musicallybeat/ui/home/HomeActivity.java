package lyrically.photovideomaker.particl.ly.musicallybeat.ui.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.AppTheme;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.Theme.ThemeActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.creation.CreationActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.PrefUtils;

public class HomeActivity extends AppCompatActivity {


    boolean check = false;
    boolean doubleBackToExitPressedOnce = false;

    Context context;
    MyApplication application;

    ImageView imageViewBack, imageViewHome, imageViewBanner;
    TextView textViewTitle;

    DrawerLayout drawer;
    boolean isOpen = false;

    ConstraintLayout slideShow, creation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        context = this;
        if (getIntent().hasExtra("filepath")) {
            startActivityForResult(new Intent(context, CreationActivity.class).putExtra("filepath", getIntent().getStringExtra("filepath")), AndroidPlugin.CREATION_ACTIVITY);
        }
        preWork();
        getIDs();
        setEvents();
        postWork();
    }

    public void preWork() {
        context = this;
        application = MyApplication.getInstance();
        application.appTheme = new AppTheme();
        MyApplication.getInstance().appTheme.sound_path = PrefUtils.getInstance(context).getStringData("sound_path");
        MyApplication.getInstance().appTheme.sound_name = PrefUtils.getInstance(context).getStringData("sound_name");
        if (application.isFirst) {
            application.isFirst = false;
           // AndroidPlugin.getMbitParticles();
        }
    }

    public void getIDs() {
        imageViewBack = findViewById(R.id.tool_left_icon);
        textViewTitle = findViewById(R.id.tool_title);
        imageViewHome = findViewById(R.id.tool_home);

        drawer = findViewById(R.id.home_drawer);
        imageViewBanner = findViewById(R.id.drawer_banner);

        slideShow = findViewById(R.id.button_slideshow);
        creation = findViewById(R.id.button_creation);
    }

    public void setEvents() {

        imageViewBack.setOnClickListener(v -> {
            if (!isOpen)
                drawer.openDrawer(GravityCompat.START);
            else
                drawer.closeDrawer(GravityCompat.START);
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                isOpen = true;
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                isOpen = false;
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        slideShow.setOnClickListener(v -> {
            startActivityForResult(new Intent(context, ThemeActivity.class), AndroidPlugin.THEME_ACTIVITY);
        });

        creation.setOnClickListener(view -> startActivityForResult(new Intent(context, CreationActivity.class), AndroidPlugin.CREATION_ACTIVITY));
    }

    public void postWork() {
        imageViewHome.setVisibility(View.GONE);
        Glide.with(context).load(R.drawable.drawer_banner).into(imageViewBanner);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AndroidPlugin.IMAGE_ACTIVITY || requestCode == AndroidPlugin.THEME_ACTIVITY) {
                String message = data.getStringExtra("MESSAGE");
                assert message != null;
                if (message.equalsIgnoreCase("YES")) {
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", "YES");
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent();
            intent.putExtra("MESSAGE", "NO");
            setResult(Activity.RESULT_OK, intent);
            finish();
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
    }
}