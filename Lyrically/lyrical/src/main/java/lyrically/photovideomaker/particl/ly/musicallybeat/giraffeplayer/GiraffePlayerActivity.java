package lyrically.photovideomaker.particl.ly.musicallybeat.giraffeplayer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;

/**
 * Created by tcking on 15/10/27.
 */
public class GiraffePlayerActivity extends Activity {

    GiraffePlayer player;
    RelativeLayout mainrl;
    CardView videocard;
    IjkVideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.giraffe_player);
        Config config = getIntent().getParcelableExtra("config");
        mainrl = findViewById(R.id.app_video_box);
        videocard = findViewById(R.id.video_card);
        videoView=findViewById(R.id.video_view);

        RelativeLayout.LayoutParams lpp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        videocard.setLayoutParams(lpp);
        videocard.setRadius(0f);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mainrl.setLayoutParams(lp);
        mainrl.setGravity(Gravity.CENTER);

        RelativeLayout.LayoutParams vlp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        vlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        vlp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        vlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        vlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        videoView.setLayoutParams(vlp);


        if (config == null || TextUtils.isEmpty(config.url)) {
            Toast.makeText(this, R.string.giraffe_player_url_empty, Toast.LENGTH_SHORT).show();
        } else {
            player = new GiraffePlayer(this);
            player.setTitle(config.title);
            player.setDefaultRetryTime(config.defaultRetryTime);
            player.setFullScreenOnly(config.fullScreenOnly);
            //  player.setScaleType(GiraffePlayer.SCALETYPE_FITXY);
            player.setScaleType(TextUtils.isEmpty(config.scaleType) ? GiraffePlayer.SCALETYPE_FITPARENT : config.scaleType);
            player.setTitle(TextUtils.isEmpty(config.title) ? "" : config.title);
            player.setShowNavIcon(config.showNavIcon);
            player.play(config.url);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    /**
     * play video
     *
     * @param context
     * @param url     url,title
     */
    public static void play(Activity context, String... url) {
        Intent intent = new Intent(context, GiraffePlayerActivity.class);
        intent.putExtra("url", url[0]);
        if (url.length > 1) {
            intent.putExtra("title", url[1]);
        }
        context.startActivity(intent);
    }

    public static Config configPlayer(Activity activity) {
        return new Config(activity);
    }

    public static class Config implements Parcelable {

        private Activity activity;
        private String scaleType;
        private boolean fullScreenOnly;
        private long defaultRetryTime = 5 * 1000;
        private String title;
        private String url;
        private boolean showNavIcon = true;

        private static boolean debug = true;

        public Config debug(boolean debug) {
            Config.debug = debug;
            return this;
        }

        public static boolean isDebug() {
            return debug;
        }

        public Config setTitle(String title) {
            this.title = title;
            return this;
        }


        public Config(Activity activity) {
            this.activity = activity;
        }

        public void play(String url) {
            this.url = url;
            Intent intent = new Intent(activity, GiraffePlayerActivity.class);
            intent.putExtra("config", this);
            activity.startActivity(intent);
        }

        public Config setDefaultRetryTime(long defaultRetryTime) {
            this.defaultRetryTime = defaultRetryTime;
            return this;
        }

        public Config setScaleType(String scaleType) {
            this.scaleType = scaleType;
            return this;
        }

        public Config setFullScreenOnly(boolean fullScreenOnly) {
            this.fullScreenOnly = fullScreenOnly;
            return this;
        }

        private Config(Parcel in) {
            scaleType = in.readString();
            fullScreenOnly = in.readByte() != 0;
            defaultRetryTime = in.readLong();
            title = in.readString();
            url = in.readString();
            showNavIcon = in.readByte() != 0;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(scaleType);
            dest.writeByte((byte) (fullScreenOnly ? 1 : 0));
            dest.writeLong(defaultRetryTime);
            dest.writeString(title);
            dest.writeString(url);
            dest.writeByte((byte) (showNavIcon ? 1 : 0));
        }

        public static final Parcelable.Creator<Config> CREATOR = new Parcelable.Creator<Config>() {
            public Config createFromParcel(Parcel in) {
                return new Config(in);
            }

            public Config[] newArray(int size) {
                return new Config[size];
            }
        };
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }
}
