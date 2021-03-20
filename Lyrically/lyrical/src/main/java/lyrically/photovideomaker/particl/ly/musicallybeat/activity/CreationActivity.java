package lyrically.photovideomaker.particl.ly.musicallybeat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.fragment.CreationFragment;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

public class CreationActivity extends AppCompatActivity {

    public CreationFragment fragmentCreations;
    public ImageView IvBack;
    ImageView homebackground;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        getIDs();
        setEvents();

        fragmentCreations = (CreationFragment) this.getSupportFragmentManager().findFragmentById(R.id.fragmentCreations);

        GlideImageLoader.SetImageResource(this, homebackground, R.drawable.creation_background, null);
        GlideImageLoader.SetImageResource(this, IvBack, R.drawable.all_back_button, null);

    }


    private void getIDs() {
        homebackground = findViewById(R.id.creation_background);
        IvBack = findViewById(R.id.creation_back_button);
    }

    private void setEvents() {
        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CreationActivity.this, MainActivity.class));
        finish();
    }
}
