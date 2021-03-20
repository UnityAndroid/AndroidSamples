package lyrically.photovideomaker.particl.ly.musicallybeat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.fragment.HomeParticleFragment;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

public class ParticleActivity extends AppCompatActivity {
    ImageView IvBackground;
    Context context;
    HomeParticleFragment HfHome;
    DatabaseManager manager;
    int position = 0, tab_pos = 0;
    String category;

    public DialogLoader dialogLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particle);
        category = getIntent().getExtras().getString("category");
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
        IvBackground = findViewById(R.id.particle_background);
        HfHome = (HomeParticleFragment) getSupportFragmentManager().findFragmentById(R.id.particle_fragment);
    }

    private void setEvents() {
    }

    private void doWork() {
        showLoader();
        GlideImageLoader.SetImageResource(context, IvBackground, R.drawable.main_background, null);
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
            cursor = manager.featchparticleCatagarytable();
            if (cursor.moveToFirst()) {
                do {
                    Cursor check = null;
                    try {
                        check = manager.featchParticletable(cursor.getInt(cursor.getColumnIndex("category_id")));
                        if (check.moveToFirst()) {
                            if (category.equalsIgnoreCase(cursor.getString(cursor.getColumnIndex("category_name")))) {
                                tab_pos = position;
                            }
                            HfHome.addPage(cursor.getString(cursor.getColumnIndex("category_name")), cursor.getInt(cursor.getColumnIndex("category_id")), cursor.getString(cursor.getColumnIndex("icon_url")), cursor.getString(cursor.getColumnIndex("icon_url")));
                            position++;
                        }
                    } finally {
                        if (check != null)
                            check.close();
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }


        if (HfHome.themePagerAdapter.getCount() > 0)
            HfHome.tabLayout.setupWithViewPager(HfHome.viewPager);
        HfHome.setupTabLayout();
        hideLoader();
    }

}
