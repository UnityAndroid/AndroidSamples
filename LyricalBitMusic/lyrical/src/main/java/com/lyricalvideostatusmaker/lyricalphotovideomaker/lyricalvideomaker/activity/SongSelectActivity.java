package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.AndroidPlugin;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.dialog.DialogDownloadSong;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model.CategoryModel;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model.ThemeModel;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.Constants;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.DatabaseManager;

public class SongSelectActivity extends AppCompatActivity {

    public Context context;
    public ImageView back, song_select_search, txtTitle;

    public ProgressBar progressBar;
    public boolean end = false;
    public RecyclerView SongsRecycler, AlbumsRecycler;
    public DatabaseManager manager;
    public String soundfile = "";
    public String current_sound = "", final_sound = "";
    public SongsAdapter songsAdapter;
    public AlbumAdapter albumAdapter;
    public int pos = 0;
    public int position = 1;
    public MediaPlayer mediaPlayer;
    public String sound = "";
    public ArrayList<ThemeModel> songsArrayList = new ArrayList<ThemeModel>();
    public ArrayList<CategoryModel> categoryArrayList = new ArrayList<CategoryModel>();
    public int album_position = 0;
    public String albumname = "";
    public Typeface typeface, typeface1;
    public int cat_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_select);

        context = SongSelectActivity.this;
        back = (ImageView) findViewById(R.id.song_select_back);
        txtTitle = (ImageView) findViewById(R.id.txtTitle);
        song_select_search = (ImageView) findViewById(R.id.song_select_search);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/merriweather_bold.ttf");
        typeface1 = Typeface.createFromAsset(getAssets(), "fonts/merriweather.ttf");

        LinearLayout creation_banner = (LinearLayout) findViewById(R.id.creation_banner);
        AdMobLoadAds.getInstance().loadBanner(SongSelectActivity.this, creation_banner);

        permission();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        song_select_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SongSelectActivity.this, SongSelectSearchActivity.class);
                startActivityForResult(intent, 102);
            }
        });

        loadSongs();
    }


    public void permission() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SongSelectActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            loadSongs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permission();
                } else {
                    permission();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            try {
                String message = data.getStringExtra("MESSAGE");
                if (message.equalsIgnoreCase("YES")) {
                    onBackPressed();
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadSongs() {
        manager = new DatabaseManager(context);
        progressBar = findViewById(R.id.progressBar);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        SongsRecycler = findViewById(R.id.recycler_songs_popular);
        AlbumsRecycler = findViewById(R.id.recycler_songs_albums_popular);
        setTabView();
    }

    public void setTabView() {
        categoryArrayList.clear();
        Cursor cats = manager.featchCatagarytable();
        if (cats.moveToFirst()) {
            do {
                categoryArrayList.add(new CategoryModel(cats.getInt(cats.getColumnIndex("Id")),
                        cats.getString(cats.getColumnIndex("Cat_Name")),
                        cats.getString(cats.getColumnIndex("Cat_img")), false));
            } while (cats.moveToNext());
        }
        if (cats != null) {
            cats.close();
        }

        albumAdapter = new AlbumAdapter(SongSelectActivity.this, categoryArrayList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        AlbumsRecycler.setLayoutManager(manager);
        AlbumsRecycler.setAdapter(albumAdapter);
        cat_id = categoryArrayList.get(0).Cat_Id;

        setRecycle(categoryArrayList.get(0).Cat_Id);
    }

    public void setRecycle(int cId) {
        songsArrayList = new ArrayList<ThemeModel>();

        Cursor themeCursor = manager.featchThemetable(cId);
        if (themeCursor.moveToFirst()) {
            do {
                songsArrayList.add(new ThemeModel(themeCursor.getInt(themeCursor.getColumnIndex("Id")),
                        themeCursor.getInt(themeCursor.getColumnIndex("Cat_Id")), themeCursor.getString(themeCursor.getColumnIndex("Theme_Name")),
                        themeCursor.getString(themeCursor.getColumnIndex("Thumnail_Big")), themeCursor.getString(themeCursor.getColumnIndex("Thumnail_Small")),
                        themeCursor.getString(themeCursor.getColumnIndex("SoundFile")), themeCursor.getString(themeCursor.getColumnIndex("sound_size")),
                        themeCursor.getString(themeCursor.getColumnIndex("GameobjectName")), themeCursor.getInt(themeCursor.getColumnIndex("Theme_Id")),
                        themeCursor.getString(themeCursor.getColumnIndex("lyrics")), 1));
            } while (themeCursor.moveToNext());
        }
        if (themeCursor != null) {
            themeCursor.close();
        }

        songsAdapter = new SongsAdapter(SongSelectActivity.this, songsArrayList);
        SongsRecycler.setLayoutManager(new LinearLayoutManager(context));
        SongsRecycler.setAdapter(songsAdapter);

        SongsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1)) {
                    if (!end) {
                        end = true;
                        showProgressView();
                        new HttpPostRequestSMoreVideos().execute(Constants. + "moreVideos/formate/json/");
                    }
                }
            }
        });

    }

    public void setRecycleData(int cId) {
        songsArrayList.clear();
        Cursor themeCursor = manager.featchThemetable(cId);
        if (themeCursor.moveToFirst()) {
            do {
                songsArrayList.add(new ThemeModel(themeCursor.getInt(themeCursor.getColumnIndex("Id")),
                        themeCursor.getInt(themeCursor.getColumnIndex("Cat_Id")), themeCursor.getString(themeCursor.getColumnIndex("Theme_Name")),
                        themeCursor.getString(themeCursor.getColumnIndex("Thumnail_Big")), themeCursor.getString(themeCursor.getColumnIndex("Thumnail_Small")),
                        themeCursor.getString(themeCursor.getColumnIndex("SoundFile")), themeCursor.getString(themeCursor.getColumnIndex("sound_size")),
                        themeCursor.getString(themeCursor.getColumnIndex("GameobjectName")), themeCursor.getInt(themeCursor.getColumnIndex("Theme_Id")),
                        themeCursor.getString(themeCursor.getColumnIndex("lyrics")), 1));
            } while (themeCursor.moveToNext());
        }

        if (themeCursor != null) {
            themeCursor.close();
        }
        songsAdapter.notifyDataSetChanged();
    }


    public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Holder> {

        public ArrayList<CategoryModel> categoryList;
        public Activity context;

        public AlbumAdapter(Activity context, ArrayList<CategoryModel> categoryArrayList) {
            this.context = context;
            this.categoryList = categoryArrayList;
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView album_name;
            RelativeLayout border, llMain;

            public Holder(@NonNull View itemView) {
                super(itemView);
                album_name = itemView.findViewById(R.id.music_tab_name);
                border = itemView.findViewById(R.id.music_tab_indicator);
                llMain = itemView.findViewById(R.id.llMain);
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Holder(LayoutInflater.from(context).inflate(R.layout.item_song_category, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int i) {
            holder.album_name.setTypeface(typeface1, Typeface.NORMAL);

            holder.album_name.setText(categoryList.get(i).getName());
            holder.album_name.setSelected(true);
            switch (i % 4) {
                case 0:
                    holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.music_tab_1));
                    break;
                case 1:
                    holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.music_tab_2));
                    break;
                case 2:
                    holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.music_tab_3));
                    break;
                case 3:
                    holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.music_tab_4));
                    break;
            }

            if (i == album_position) {
                holder.border.setVisibility(View.VISIBLE);
            } else {
                holder.border.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = i;
                    if (position != album_position) {
                        cat_id = categoryList.get(i).Cat_Id;
                        albumname = categoryList.get(position).Name;
                        notifyItemChanged(album_position);
                        album_position = position;
                        notifyItemChanged(album_position);
                        setRecycleData(cat_id);
                        SongsRecycler.scrollToPosition(0);
                        end = false;
                        notifyDataSetChanged();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }
    }

    public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.Holder> {

        private boolean on_attach = true;
        private Activity context;
        private ArrayList<ThemeModel> songsArrayList;
        public ArrayList<AsyncTask> asyntask = new ArrayList<AsyncTask>();
        public ArrayList<AsyncTask> asyntaskCount = new ArrayList<AsyncTask>();
        public ThemeModel themeModel;

        public SongsAdapter(Activity context, ArrayList<ThemeModel> songsArrayList) {
            this.context = context;
            this.songsArrayList = songsArrayList;
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView title, duration;
            ImageView use, play;
            LinearLayout item_back;
            private TextView txt_progress;
            private ProgressBar ivprogress;
            private LinearLayout llDownload;

            public Holder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.song_name);
                duration = itemView.findViewById(R.id.song_size);
                use = itemView.findViewById(R.id.download_icon);
                play = itemView.findViewById(R.id.play_icon);
                item_back = itemView.findViewById(R.id.song_item_linear);
                ivprogress = itemView.findViewById(R.id.theme_progress);
                txt_progress = itemView.findViewById(R.id.txt_progress);
                llDownload = itemView.findViewById(R.id.llDownload);
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Holder(LayoutInflater.from(context).inflate(R.layout.item_song, viewGroup, false));
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
        public void onBindViewHolder(@NonNull final Holder holder, final int i) {
            themeModel = (ThemeModel) songsArrayList.get(i);

            holder.title.setTypeface(typeface1, Typeface.NORMAL);
            holder.duration.setTypeface(typeface, Typeface.BOLD);

            holder.title.setText(themeModel.Theme_Name);
            holder.duration.setText(themeModel.sound_size);

            Cursor download = manager.getdownloads(themeModel.getId());
            if (download.moveToFirst()) {
                if (sound.equalsIgnoreCase(download.getString(download.getColumnIndex("sound")))) {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            Glide.with(context).load(R.drawable.song_pause_icon).apply(new RequestOptions().fitCenter()).into(holder.play);
                        } else {
                            Glide.with(context).load(R.drawable.song_play_icon).apply(new RequestOptions().fitCenter()).into(holder.play);
                        }
                    } else {
                        Glide.with(context).load(R.drawable.song_play_icon).apply(new RequestOptions().fitCenter()).into(holder.play);
                    }
                } else {
                    Glide.with(context).load(R.drawable.song_play_icon).apply(new RequestOptions().fitCenter()).into(holder.play);
                }
            } else {
                Glide.with(context).load(R.drawable.song_play_icon).apply(new RequestOptions().fitCenter()).into(holder.play);
            }
            download.close();

            if (manager.checkthemesongurl(themeModel.Id, themeModel.SoundFile)) {
                Glide.with(context).load(R.drawable.song_use_button).apply(new RequestOptions().fitCenter()).into(holder.use);
            } else {
                Glide.with(context).load(R.drawable.download).apply(new RequestOptions().fitCenter()).into(holder.use);
            }

            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    themeModel = (ThemeModel) songsArrayList.get(i);
                    soundfile = themeModel.SoundFile;

                    if (manager.checkthemesongurl(themeModel.Id, themeModel.SoundFile)) {
                        Cursor download = manager.getdownloads(themeModel.getId());
                        if (download.moveToFirst()) {
                            if (sound.equalsIgnoreCase(download.getString(download.getColumnIndex("sound")))) {
                                if (mediaPlayer != null) {
                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.pause();
                                    } else {
                                        mediaPlayer.start();
                                    }
                                } else {
                                    playsong(download.getString(download.getColumnIndex("sound")));
                                }
                            } else {
                                soundfile = themeModel.SoundFile;
                                playsong(download.getString(download.getColumnIndex("sound")));
                            }
                        }
                        download.close();
                        notifyDataSetChanged();
                    } else {

                        DownloadTheme(themeModel, holder);
                    }

                }
            });

            holder.title.setEllipsize(TextUtils.TruncateAt.MARQUEE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    themeModel = (ThemeModel) songsArrayList.get(i);
                    soundfile = themeModel.SoundFile;

                    if (manager.checkthemesongurl(themeModel.Id, themeModel.SoundFile)) {
                        Cursor download = manager.getdownloads(themeModel.getId());
                        if (download.moveToFirst()) {
                            if (sound.equalsIgnoreCase(download.getString(download.getColumnIndex("sound")))) {

                                if (mediaPlayer != null) {
                                    if (mediaPlayer.isPlaying()) {
                                        mediaPlayer.pause();
                                    } else {
                                        mediaPlayer.start();
                                    }
                                } else {
                                    playsong(download.getString(download.getColumnIndex("sound")));
                                }
                            } else {
                                soundfile = themeModel.SoundFile;
                                if (mediaPlayer != null) {
                                    mediaPlayer.release();
                                    mediaPlayer = null;
                                } else {
                                }
                                playsong(download.getString(download.getColumnIndex("sound")));
                            }
                        }
                        download.close();
                        notifyDataSetChanged();
                    } else {

                        DownloadTheme(themeModel, holder);
                    }

                }
            });

            holder.use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    themeModel = (ThemeModel) songsArrayList.get(i);
                    soundfile = themeModel.SoundFile;
                    try {
                        if (manager.checkthemesongurl(themeModel.Id, themeModel.SoundFile)) {
                            final Cursor download = manager.getdownloads(themeModel.getId());
                            if (download.moveToFirst()) {

                                final_sound = download.getString(download.getColumnIndex("sound"));
                                Constants.songname = themeModel.Theme_Name.replaceAll("'", "''");

                                final JSONObject jsonAdd = new JSONObject();
                                jsonAdd.put("soundpath", final_sound);
                                jsonAdd.put("lyrics", themeModel.lyrics.replaceAll("'", "''"));

                                AdMobLoadAds.getInstance().displayInterstitial(getApplicationContext(), new AdMobLoadAds.MyCallback() {
                                    @Override
                                    public void callbackCall() {
                                        AndroidPlugin.UnityCall("SelectMusic", "GetSelectedMusic", jsonAdd.toString());
                                        download.close();
                                        ((SongSelectActivity) context).onBackPressed();
                                        ((SongSelectActivity) context).finish();
                                    }
                                });
                            } else {
                                download.close();
                            }
                        } else {
                            DownloadTheme(themeModel, holder);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return songsArrayList.size();
        }

        public void DownloadTheme(final ThemeModel theme, final Holder holder) {

            if (asyntask.size() == 0) {
                holder.ivprogress.setVisibility(View.VISIBLE);
                holder.use.setVisibility(View.GONE);
                asyntask.add(new DownloadThemeTask(theme, holder).execute());
            } else {
                AndroidPlugin.showToast(context, "Please Wait Until,\nAnother Theme Download Finished!!");
            }
        }

        private class DownloadThemeTask extends AsyncTask<String, Integer, String> {

            Holder viewHolder;
            ThemeModel theme;
            String soundfile, imagefile, filename;

            public DownloadThemeTask(final ThemeModel theme, final Holder viewHolder) {
                this.theme = theme;
                this.viewHolder = viewHolder;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                filename = String.valueOf(System.currentTimeMillis());
            }


            @Override
            protected String doInBackground(String... sUrl) {
                if (!manager.checkthemesongurl(theme.Id, theme.SoundFile, theme.Thumnail_Big)) {
                    InputStream input = null;
                    OutputStream output = null;
                    HttpURLConnection connection = null;
                    try {
                        URL url = new URL(theme.SoundFile);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestProperty("Accept-Encoding", "identity");
                        connection.connect();

                        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            return "Server returned HTTP " + connection.getResponseCode()
                                    + " " + connection.getResponseMessage();
                        }

                        int fileLength = connection.getContentLength();

                        soundfile = AndroidPlugin.GetSoundPath() + filename + ".mp3";
                        imagefile = AndroidPlugin.GetImagePath() + filename + ".png";

                        input = connection.getInputStream();
                        output = new FileOutputStream(soundfile);


                        byte data[] = new byte[4096];
                        long total = 0;
                        int count;
                        while ((count = input.read(data)) != -1) {
                            if (isCancelled()) {
                                input.close();
                                return null;
                            }
                            total += count;
                            publishProgress((int) (total * 100 / fileLength));
                            output.write(data, 0, count);
                        }
                    } catch (Exception e) {
                        return "-1";
                    } finally {
                        try {
                            if (output != null)
                                output.close();
                            if (input != null)
                                input.close();
                        } catch (IOException ignored) {
                        }
                        if (connection != null)
                            connection.disconnect();
                    }
                } else {
                    publishProgress(100);
                }
                return String.valueOf(theme.Id);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                //viewHolder.txt_progress.setText("" + values[0]);
            }

            @Override
            protected void onPostExecute(final String result) {
                if (!result.equalsIgnoreCase("-1")) {
                    soundfile = AndroidPlugin.GetSoundPath() + filename + ".mp3";
                    imagefile = AndroidPlugin.GetImagePath() + filename + ".png";

                    Glide.with(context).asBitmap().load(theme.Thumnail_Big).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            try {

                                Bitmap bitmap = Bitmap.createScaledBitmap(resource, 720, 1080, false);
                                FileOutputStream out = new FileOutputStream(new File(imagefile));
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                                out.close();

                                manager.insertdownloads(theme.Id, soundfile, imagefile, theme.Theme_Name.replaceAll("'", "''"), theme.GameobjectName, theme.Thumnail_Big, theme.SoundFile, theme.lyrics.replaceAll("'", "''"));

                                //viewHolder.llDownload.setVisibility(View.GONE);
                                viewHolder.ivprogress.setVisibility(View.GONE);
                                //viewHolder.txt_progress.setVisibility(View.GONE);
                                viewHolder.use.setVisibility(View.VISIBLE);

                                if (manager.checktheme(theme.Id)) {
                                    Glide.with(context).load(R.drawable.song_use_button).apply(new RequestOptions().fitCenter()).into(viewHolder.use);
                                } else {
                                    Glide.with(context).load(R.drawable.download).apply(new RequestOptions().fitCenter()).into(viewHolder.use);
                                }

                                asyntaskCount.add(new HttpPostRequestCountSongs(theme.Id).execute(Constants. + "download/formate/json/"));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

                } else {
                    //viewHolder.llDownload.setVisibility(View.GONE);
                    viewHolder.ivprogress.setVisibility(View.GONE);
                    //viewHolder.txt_progress.setVisibility(View.GONE);
                    viewHolder.use.setVisibility(View.VISIBLE);
                    Glide.with(context).load(R.drawable.download).apply(new RequestOptions().fitCenter()).into(viewHolder.use);
                    AndroidPlugin.showToast(context, "Download Theme Failed");
                }
                asyntask.clear();
            }
        }

        public class HttpPostRequestCountSongs extends AsyncTask<String, Void, String> {
            public static final String REQUEST_METHOD = "POST";
            public static final int READ_TIMEOUT = 15000;
            public static final int CONNECTION_TIMEOUT = 15000;
            private int audioId = 0;

            public HttpPostRequestCountSongs(final int audioId) {
                this.audioId = audioId;
            }

            @Override
            protected String doInBackground(String... params) {
                String stringUrl = params[0];
                String result;
                String inputLine;
                try {
                    //Create a URL object holding our url
                    URL myUrl = new URL(stringUrl);
                    //Create a connection
                    HttpURLConnection connection = (HttpURLConnection)
                            myUrl.openConnection();
                    connection.setRequestProperty("Authorization", "Basic V0JpdE1hc3RlcjpNYXN0ZXJAV0JpdA==");

                    connection.setRequestMethod(REQUEST_METHOD);
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.setConnectTimeout(CONNECTION_TIMEOUT);


                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("audio_id", String.valueOf(audioId));
                    String query = builder.build().getEncodedQuery();
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();

                    connection.connect();
                    //Create a new InputStreamReader
                    InputStreamReader streamReader = new
                            InputStreamReader(connection.getInputStream());
                    //Create a new buffered reader and String Builder
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    //Check if the line we are reading is not null
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    //Close our InputStream and Buffered reader
                    reader.close();
                    streamReader.close();
                    //Set our result equal to our stringBuilder
                    result = stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    result = null;
                }
                return result;
            }

            @Override
            protected void onPostExecute(String resultt) {
                super.onPostExecute(resultt);
            }
        }
    }

    public class HttpPostRequestSMoreVideos extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        public HttpPostRequestSMoreVideos() {

        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                connection.setRequestProperty("Authorization", "Basic V0JpdE1hc3RlcjpNYXN0ZXJAV0JpdA==");

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("cat_id", String.valueOf(cat_id))
                        .appendQueryParameter("offset", String.valueOf(manager.getpage(cat_id)));

                String query = builder.build().getEncodedQuery();
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();

                Log.e("error", e.toString());
                result = null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String resultt) {
            super.onPostExecute(resultt);
            hideProgressView();
            if (resultt != null) {
                Log.e("resultt ", " : " + resultt.toString());
                try {
                    int oldSize = songsArrayList.size();
                    JSONObject object = new JSONObject(resultt);

                    if (object.getString("status").equalsIgnoreCase("1")) {

                        JSONArray themes = object.getJSONArray("videos");
                        for (int j = 0; j < themes.length(); j++) {
                            JSONObject object1 = themes.getJSONObject(j);

                            manager.insertthemetable(object1.getInt("Id"), object1.getInt("Cat_Id"), "0",
                                    object1.getString("Theme_Name").replaceAll("'", "''"),
                                    object1.getString("Thumbnail_Big"), object1.getString("Thumbnail_Small"),
                                    object1.getString("SoundFile"), object1.getString("SoundSize"),
                                    object1.getString("GameobjectName"), "0", object1.getString("Status"),
                                    "0", object1.getInt("Theme_Id"), 2,
                                    object1.getString("lyrics").replaceAll("'", "''"));

                            songsArrayList.add(new ThemeModel(object1.getInt("Id"),
                                    cat_id, object1.getString("Theme_Name").replaceAll("'", "''"),
                                    object1.getString("Thumbnail_Big"), object1.getString("Thumbnail_Small"),
                                    object1.getString("SoundFile"), object1.getString("SoundSize"),
                                    object1.getString("GameobjectName"), object1.getInt("Theme_Id"),
                                    object1.getString("lyrics").replaceAll("'", "''"), 1));
                        }

                        songsAdapter.notifyItemMoved(oldSize, songsArrayList.size());
                        manager.increpage(cat_id);
                        end = false;
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
        }
    }

    void showProgressView() {
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideProgressView() {
        progressBar.setVisibility(View.GONE);
    }

    public void playsong(String path) {
        sound = path;

        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setLooping(true);
            }

            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    albumAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.e("error", e.toString());
            AndroidPlugin.showToast(getApplicationContext(), "Error Playing Song");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                songsAdapter.notifyDataSetChanged();
            }
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.release();
        Glide.with(getApplicationContext()).pauseRequests();
    }
}


