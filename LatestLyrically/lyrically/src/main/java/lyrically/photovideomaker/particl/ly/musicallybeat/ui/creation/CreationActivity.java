package lyrically.photovideomaker.particl.ly.musicallybeat.ui.creation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.ui.video.VideoActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ScreenUtils;

public class CreationActivity extends AppCompatActivity {

    ImageView backImageView;
    Context context;
    MyApplication application;

    ImageView imageViewBack;
    TextView textViewTitle, textViewDone;

    RecyclerView recyclerView;
    CreationAdapter creationAdapter;
    ConstraintLayout nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);
        context = this;
        if (getIntent().hasExtra("filepath")) {
            gotoVideo(getIntent().getStringExtra("filepath"));
        }
        preWork();
        getIDs();
        setEvents();
        postWork();
    }

    public void preWork() {
        application = MyApplication.getInstance();
        context = this;
    }

    public void getIDs() {
        imageViewBack = findViewById(R.id.tool_left_icon);
        textViewTitle = findViewById(R.id.tool_title);
        textViewDone = findViewById(R.id.tool_right_icon);
        backImageView = findViewById(R.id.background);

        recyclerView = findViewById(R.id.recycler_view_creation);
        nodata = findViewById(R.id.no_data);

        textViewTitle.setText(context.getResources().getString(R.string.title_creation));
        textViewDone.setVisibility(View.GONE);
    }

    public void setEvents() {
        imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    public void postWork() {
        Glide.with(context).load(R.drawable.all_background).into(backImageView);

        ViewOutlineProvider mViewOutlineProvider2 = new ViewOutlineProvider() {
            @Override
            public void getOutline(final View view, final Outline outline) {

                int left = 0;
                int top = 0;
                int right = view.getWidth();
                int bottom = view.getHeight();
                float cornerRadiusDP = 1f;
                int cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerRadiusDP, context.getResources().getDisplayMetrics());

                outline.setRoundRect(left, top, right, bottom, cornerRadius);
            }
        };

        int widthint = (ScreenUtils.getScreenWidth(context) - ScreenUtils.convertDPItoINT(context, 60)) / 2;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthint, widthint + (widthint / 2));
        params.setMargins(0, 0, ScreenUtils.convertDPItoINT(context, 20), ScreenUtils.convertDPItoINT(context, 20));

        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setHasFixedSize(true);

        creationAdapter = new CreationAdapter(context, params, mViewOutlineProvider2);
        recyclerView.setAdapter(creationAdapter);

        if (creationAdapter.getItemCount() > 0) {
            nodata.setVisibility(View.GONE);
        }

    }

    public void gotoVideo(String filepath) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("filepath", filepath);
        startActivityForResult(intent, AndroidPlugin.VIDEO_ACTIVITY);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "YES");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AndroidPlugin.VIDEO_ACTIVITY) {
                String message = data.getStringExtra("MESSAGE");
                assert message != null;
                if (message.equalsIgnoreCase("NO")) {
                    onBackPressed();
                }
            }
        }
    }
}