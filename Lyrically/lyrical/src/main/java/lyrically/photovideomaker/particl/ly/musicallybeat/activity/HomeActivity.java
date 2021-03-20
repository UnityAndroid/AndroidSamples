package lyrically.photovideomaker.particl.ly.musicallybeat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.fragment.HomeFragment;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

public class HomeActivity extends AppCompatActivity {

    ImageView IvBackground, IvSearch;
    Context context;
    HomeFragment HfHome;
    DatabaseManager manager;

    public DialogLoader dialogLoader;
    ImageView IvBack;
    public static boolean isAttached = true;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        context = this;
        manager = new DatabaseManager(context);
        dialogLoader = new DialogLoader(this, "Loading Please Wait");
        getIDs();
        setEvents();
        doWork();
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
        IvBackground = findViewById(R.id.home_background);
        HfHome = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragment);
        IvBack = findViewById(R.id.home_back_button);
        IvSearch = findViewById(R.id.home_search_button);
    }

    private void setEvents() {
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        IvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivityForResult(intent, 102);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            String message = data.getStringExtra("MESSAGE");
            if (message.equalsIgnoreCase("YES")) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            } else {

            }
        }
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isAttached = false;
    }

    private void doWork() {
        showLoader();
        GlideImageLoader.SetImageResource(context, IvBackground, R.drawable.home_background, null);
        GlideImageLoader.SetImageResource(context, IvBack, R.drawable.all_back_button, null);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setTabs();
            }
        }, 200);
    }

    private void setTabs() {
        Cursor cursor = null;
        try {
            cursor = manager.featchCatagarytable();
            if (cursor.moveToFirst()) {
                do {
                    Cursor check = manager.featchThemetable(cursor.getInt(cursor.getColumnIndex("Id")));
                    if (check.moveToFirst()) {
                        HfHome.addPage(cursor.getString(cursor.getColumnIndex("Cat_Name")), cursor.getInt(cursor.getColumnIndex("Id")), cursor.getString(cursor.getColumnIndex("Cat_img")), cursor.getString(cursor.getColumnIndex("Back_img")));
                    }
                    check.close();
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        if (HfHome.themePagerAdapter.getCount() > 0)
            HfHome.TlHome.setupWithViewPager(HfHome.VpHome);
        HfHome.setupTabLayout();
        hideLoader();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideImageLoader.endGlideProcess(getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
