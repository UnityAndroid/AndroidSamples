package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.AndroidPlugin;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.VideoItem;

public class CreationActivity extends AppCompatActivity {

    public ImageView IvBack, txtTitle;
    public RecyclerView recyclerView;
    public CreationAdapter adapter;
    public TextView TvNoCreations;
    public String creationsPath;
    public ArrayList<VideoItem> creationArrayList;
    public Typeface typeface, typeface1;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_creation);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        creationsPath = AndroidPlugin.getDirectoryDCIM();

        getIDs();
        setEvents();

        LinearLayout creation_banner = (LinearLayout) findViewById(R.id.creation_banner);
        AdMobLoadAds.getInstance().loadBanner(CreationActivity.this, creation_banner);
    }

    private void getIDs() {
        IvBack = (ImageView) findViewById(R.id.creation_back_button);
        recyclerView = (RecyclerView) findViewById(R.id.creation_recycler);
        TvNoCreations = (TextView) findViewById(R.id.creation_no_text);
        txtTitle = (ImageView) findViewById(R.id.txtTitle);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/merriweather_bold.ttf");
        typeface1 = Typeface.createFromAsset(getAssets(), "fonts/merriweather.ttf");

        TvNoCreations.setTypeface(typeface, Typeface.BOLD);
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
        Glide.with(getApplicationContext()).pauseRequests();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (AndroidPlugin.isFolder) {
                creationArrayList = getAllVideo("LyricalBitMusic");
            } else {
                try {
                    creationArrayList = new ArrayList<VideoItem>();
                    File dir = new File(creationsPath);
                    if (dir.isDirectory()) {
                        File[] listFiles = dir.listFiles();
                        for (int i = 0; i < listFiles.length; i++) {
                            VideoItem video = new VideoItem();
                            video.setDATA(listFiles[i].getAbsolutePath());
                            video.setDISPLAY_NAME(listFiles[i].getName());
                            creationArrayList.add(video);
                        }
                        Collections.sort(creationArrayList, new Comparator<VideoItem>() {
                            @Override
                            public int compare(VideoItem video, VideoItem video2) {
                                return video2.getDISPLAY_NAME().compareToIgnoreCase(video.getDISPLAY_NAME());
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (creationArrayList.size() == 0) {
                TvNoCreations.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                TvNoCreations.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            DisplayMetrics dm = getResources().getDisplayMetrics();
            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm);

            adapter = new CreationAdapter(CreationActivity.this, margin, creationArrayList);
            GridLayoutManager mDatabaseManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(mDatabaseManager);
            recyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<VideoItem> getAllVideo(String foldername) {
        Cursor cursor;
        String selection = MediaStore.Video.Media.BUCKET_DISPLAY_NAME + " =?";
        String[] selectionArgs = new String[]{foldername};
        String[] projection = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.DURATION
        };

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        cursor = getContentResolver().query(uri, projection, selection, selectionArgs,
                MediaStore.Video.Media.DATE_MODIFIED + " DESC");

        ArrayList<VideoItem> videoItems = new ArrayList<VideoItem>();
        VideoItem videoItem;
        while (cursor.moveToNext()) {
            videoItem = new VideoItem();
            videoItem.set_ID(cursor.getString(0));
            videoItem.setSIZE(size(cursor.getString(1)));
            videoItem.setDATE(date(cursor.getString(2)));
            videoItem.setDATA(cursor.getString(3));
            videoItem.setDISPLAY_NAME(cursor.getString(4));
            videoItem.setDURATION(duration(cursor.getString(5)));
            videoItems.add(videoItem);
        }
        return videoItems;
    }

    private String duration(String x) {
        int y = Integer.parseInt(x);

        long ho = TimeUnit.MILLISECONDS.toHours(y);
        long mo = TimeUnit.MILLISECONDS.toMinutes(y) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(y));
        long so = TimeUnit.MILLISECONDS.toSeconds(y) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(y));

        if (ho >= 1) return String.format("%02d:%02d:%02d", ho, mo, so);
        else return String.format("%02d:%02d", mo, so);
    }

    private String date(String x) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date(Long.parseLong(x) * 1000));
    }

    private String size(String x) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.FLOOR);
        double s = Double.parseDouble(x);
        s = s / 1024;
        if (s < 1024) {
            return df.format(s) + " KB";
        } else {
            s = s / 1024;
            if (s < 1024) {
                return df.format(s) + " MB";
            } else {
                s = s / 1024;
                return df.format(s) + " GB";
            }
        }
    }

    public class CreationAdapter extends RecyclerView.Adapter<CreationAdapter.Holder> {

        public boolean on_attach = true;
        public int margin;
        public Activity context;
        public ArrayList<VideoItem> creationArrayList;

        public CreationAdapter(Activity context, int margin, ArrayList<VideoItem> creationArrayList) {
            this.context = context;
            this.margin = margin;
            this.creationArrayList = creationArrayList;
        }

        public class Holder extends RecyclerView.ViewHolder {

            CardView CardMain, CardMiddle;
            ImageView ivdownload, ivthumb;
            TextView themename;

            public Holder(@NonNull View itemView) {
                super(itemView);
                ivdownload = itemView.findViewById(R.id.creation_play_image);
                ivthumb = itemView.findViewById(R.id.creation_thumb_image);
                themename = itemView.findViewById(R.id.creation_video_name);
                CardMain = itemView.findViewById(R.id.creation_root_card);
                CardMiddle = itemView.findViewById(R.id.creation_middle_card);
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(context).inflate(R.layout.item_creation, parent, false));
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    on_attach = false;
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
            super.onAttachedToRecyclerView(recyclerView);
        }


        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {

            holder.themename.setTypeface(typeface1, Typeface.NORMAL);

            holder.CardMain.startAnimation(AnimationUtils.loadAnimation(context, R.anim.card_animation));
            holder.themename.setSelected(true);

            Glide.with(context).load(creationArrayList.get(position).getDATA()).apply(new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)).into(holder.ivthumb);

            holder.themename.setText(creationArrayList.get(position).getDISPLAY_NAME());
            holder.themename.setSelected(true);

            holder.ivdownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdMobLoadAds.getInstance().displayInterstitial(CreationActivity.this, new AdMobLoadAds.MyCallback() {
                        @Override
                        public void callbackCall() {
                            File file = new File(creationArrayList.get(position).getDATA());
                            Intent intent = new Intent(context, PlayerViewActivity.class);
                            intent.putExtra("filepath", file.getAbsolutePath());
                            intent.putExtra("creation", 0);
                            intent.putExtra("ads", 0);
                            context.startActivity(intent);
                            ((CreationActivity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    });
                }
            });

            holder.CardMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AdMobLoadAds.getInstance().displayInterstitial(CreationActivity.this, new AdMobLoadAds.MyCallback() {
                        @Override
                        public void callbackCall() {
                            File file = new File(creationArrayList.get(position).getDATA());
                            Intent intent = new Intent(context, PlayerViewActivity.class);
                            intent.putExtra("filepath", file.getAbsolutePath());
                            intent.putExtra("creation", 0);
                            intent.putExtra("ads", 0);
                            context.startActivity(intent);
                            ((CreationActivity) context).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        }
                    });
                }
            });

        }

        @Override
        public int getItemCount() {
            return creationArrayList.size();
        }
    }


}