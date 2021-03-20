package lyrically.photovideomaker.particl.ly.musicallybeat.ui.video;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.CreationEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ScreenUtils;
import tv.danmaku.ijk.media.player.IMediaPlayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.extras.giraffeplayer.GiraffePlayer;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.giraffeplayer.IjkVideoView;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.creation.CreationViewModel;

import java.io.File;
import java.util.List;
import java.util.Objects;

public class VideoActivity extends AppCompatActivity {

    Context context;
    MyApplication application;

    ImageView backImageView;
    ImageView imageViewBack, imageViewHome, fullscreen;
    TextView textViewTitle, textViewDone;

    int model = 0;
    boolean pause = false;
    RelativeLayout videobox;
    ProgressBar progressBar;
    IjkVideoView videoView;
    GiraffePlayer player;

   /* SurfaceView videoSurface;
    MediaPlayer player;
    VideoControllerView controller;
    boolean isFullScreen = false;*/

    boolean isPause = false;
    String filepath;

    CardView toolbar;
    ConstraintLayout bottom, top;
    ConstraintLayout playerRelative;

    CreationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        preWork();
        getIDs();
        setEvents();
        postWork();
    }

    public void preWork() {
        context = this;
        if (getIntent() != null && getIntent().hasExtra("filepath")) {
            filepath = Objects.requireNonNull(getIntent().getExtras()).getString("filepath");
        }
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(CreationViewModel.class);
        application = MyApplication.getInstance();

    }

    public void getIDs() {
        videobox = findViewById(R.id.app_video_box);
        videoView = findViewById(R.id.video_view);


        imageViewBack = findViewById(R.id.tool_left_icon);
        textViewTitle = findViewById(R.id.tool_title);
        textViewDone = findViewById(R.id.tool_right_icon);
        imageViewHome = findViewById(R.id.tool_home);
      //  videoSurface = findViewById(R.id.videoSurface);

        backImageView = findViewById(R.id.background);

        fullscreen = findViewById(R.id.app_video_fullscreen);
        progressBar = findViewById(R.id.app_video_loading);

        toolbar = findViewById(R.id.tool_bar);
        bottom = findViewById(R.id.block_bottom);
        playerRelative = findViewById(R.id.player_relative);

        top = findViewById(R.id.top_constraint);

        textViewTitle.setText(context.getResources().getString(R.string.title_video));
        textViewDone.setVisibility(View.GONE);
        imageViewHome.setVisibility(View.GONE);


    }

    public void setEvents() {

        imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imageViewHome.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("MESSAGE", "NO");
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }

    public void postWork() {

        List<CreationEntity> entities = viewModel.getAllCreationbyFilepath(filepath);
        if (!new File(entities.get(0).file_path).exists()) {
            viewModel.deleteCreation(entities.get(0));
            onBackPressed();
        }
        Glide.with(context).load(R.drawable.all_background).into(backImageView);

        player = new GiraffePlayer(this);


       /* SurfaceHolder videoHolder = videoSurface.getHolder();
        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);
        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            player.setDataSource(this, Uri.parse(filepath));
            player.setOnPreparedListener(this);
        } catch (IllegalArgumentException | SecurityException | IllegalStateException | IOException e) {
            ConstantUtils.showErrorLog(e.toString());
            e.printStackTrace();
        }

        setNormalPlayer();*/

       setNormalPlayer();
       setupvideo();
    }

    public void setupvideo() {


        fullscreen.setVisibility(View.GONE);
        player.play(filepath);
        player.setScaleType(GiraffePlayer.SCALETYPE_FITXY);
        videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(IMediaPlayer mp, int what, int extra) {

                if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    if (progressBar.getVisibility() == View.VISIBLE)
                        progressBar.setVisibility(View.GONE);

                    videoView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(videoView.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
                            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                            videobox.setLayoutParams(lp);
                            videobox.setGravity(Gravity.CENTER);
                        }
                    });

                }
                return false;
            }
        });

    }



    public void setNormalPlayer() {

        bottom.setVisibility(View.VISIBLE);
        toolbar.setVisibility(View.VISIBLE);

        int margin = ScreenUtils.convertDPItoINT(context, 10);
        int width = ScreenUtils.getScreenWidth(context) - (margin * 2);

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(width, width);
        lp.startToStart = top.getId();
        lp.endToEnd = top.getId();
        lp.topToTop = top.getId();
        lp.topMargin = margin;

        playerRelative.setLayoutParams(lp);
    }
    /*

    public void setFullScreen() {
        isFullScreen = true;
        bottom.setVisibility(View.GONE);
        toolbar.setVisibility(View.GONE);

        ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, 0, 0, 0);
        playerRelative.setLayoutParams(lp);

    }

    @Override
    public void toggleFullScreen() {
        if (isFullScreen()) {
            setNormalPlayer();
        } else {
            setFullScreen();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.videoSurfaceContainer));
        player.start();
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    @Override
    public int getDuration() {
        return player.getDuration();
    }

    @Override
    public boolean isPlaying() {
        return player.isPlaying();
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        return isFullScreen;
    }
*/

    @Override
    protected void onPause() {
        super.onPause();
       /* if (!isPause) {
            if (player.isPlaying()) {
                if (player != null) {
                    player.pause();
                    isPause = true;
                }
            }
        }*/

        if (!pause) {
            if (videoView != null) {
                videoView.pause();
                pause = true;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    /*    if (isPause) {
            if (player != null) {
                player.start();
                isPause = false;
            }
        }*/

        if (pause) {
            if (videoView != null) {
                videoView.start();
                pause = false;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  if (player != null) {
            player.stop();
        }*/

        if (player != null) {
            player.onDestroy();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "YES");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


}