package lyrically.photovideomaker.particl.ly.musicallybeat.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

public class TextActivity extends AppCompatActivity {

    ImageView back, pencil, done_back;
    TextView text;
    RelativeLayout tick;
    Context context;
    ImageView background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        getIDs();
        setEvents();
        doWork();

    }

    private void getIDs() {
        text = findViewById(R.id.text_name);
        tick = findViewById(R.id.text_done_rl);
        pencil = findViewById(R.id.text_pencil);
        back = findViewById(R.id.text_select_back_button);
        background = findViewById(R.id.text_select_background);
    }

    private void setEvents() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        tick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text.getText().toString().length() > 0) {
                    Constants.android = false;
                    AndroidPlugin.UnityCall("SelectImage", "EditText", text.getText().toString());
                    onBackPressed();

                } else {
                    text.setError("Plz Enter Something");
                }
            }
        });
    }

    private void doWork() {
        GlideImageLoader.SetImageResource(context, background, R.drawable.main_background, null);
        GlideImageLoader.SetImageResource(context, back, R.drawable.all_back_button, null);
        GlideImageLoader.SetImageResource(context, pencil, R.drawable.text_pencil, null);
        GlideImageLoader.SetImageResource(context, done_back, R.drawable.album_normal_back, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideImageLoader.endGlideProcess(getApplicationContext());
    }

}
