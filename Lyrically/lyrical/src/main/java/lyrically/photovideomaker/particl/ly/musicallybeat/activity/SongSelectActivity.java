package lyrically.photovideomaker.particl.ly.musicallybeat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.fragment.PopularFragment;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

public class SongSelectActivity extends AppCompatActivity {

    ImageView background, backbtn;
    public Context context;
    PopularFragment popularFragment;
    public boolean pop = true;

    public DialogLoader dialogLoader;
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
        setContentView(R.layout.activity_song_select);
        context = this;
        dialogLoader = new DialogLoader(this, "Loading Please Wait");

        getIDs();
        setEvents();
        doWork();

    }

    private void getIDs() {
        backbtn = findViewById(R.id.song_select_back);
        background = findViewById(R.id.song_select_background);

    }

    private void setEvents() {
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

       /* popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pop) {
                    pop = true;
                    poprl.setVisibility(View.VISIBLE);
                    filerl.setVisibility(View.GONE);
                    loadfragment(popularFragment);
                }
            }
        });

        filemanager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pop) {
                    pop = false;
                    poprl.setVisibility(View.GONE);
                    filerl.setVisibility(View.VISIBLE);
                    loadfragment(fileManagerFragment);
                }
            }
        });*/
    }

    private void doWork() {

        showLoader();
        GlideImageLoader.SetImageResource(context, background, R.drawable.song_background, null);
        GlideImageLoader.SetImageResource(context, backbtn, R.drawable.all_back_button, null);

        popularFragment = new PopularFragment();
        //   fileManagerFragment = new FileManagerFragment();

        //   poprl.setVisibility(View.VISIBLE);
        //   filerl.setVisibility(View.GONE);

        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            AndroidPlugin.UnityCall("PemissionHandler", "GetPermission", "Good Morning");
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoader();
                    loadfragment(popularFragment);
                }
            }, 200);
        }
    }

    public void loadfragment(Fragment fragment) {
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_container, fragment);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (popularFragment.songs != null)
            popularFragment.songs.close();

        GlideImageLoader.endGlideProcess(getApplicationContext());

    }
}
