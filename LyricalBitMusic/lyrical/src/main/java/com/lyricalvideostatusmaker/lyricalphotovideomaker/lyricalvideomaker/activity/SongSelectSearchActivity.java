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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model.ThemeModel;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.Constants;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.DatabaseManager;

public class SongSelectSearchActivity extends AppCompatActivity {

    public Context context;
    public ImageView back;

    public ProgressBar progressBar;
    public boolean end = false;
    public RecyclerView SongsRecycler;
    public Cursor songs;
    public DatabaseManager manager;
    public String soundfile = "";
    public SongsAdapter songsAdapter;
    public int pos = 0;
    public int position = 1;
    public MediaPlayer mediaPlayer;
    public String sound = "";
    public ArrayList<ThemeModel> songsArrayList = new ArrayList<ThemeModel>();
    public ArrayList<Integer> albumids = new ArrayList<>();
    public int album_position = 0, song_position = 0;
    public EditText edtSearchSong;
    public Typeface typeface, typeface1;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_song_select_all);

        context = this;

        back = (ImageView) findViewById(R.id.song_select_back);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/merriweather_bold.ttf");
        typeface1 = Typeface.createFromAsset(getAssets(), "fonts/merriweather.ttf");

        LinearLayout creation_banner = (LinearLayout) findViewById(R.id.creation_banner);
        AdMobLoadAds.getInstance().loadBanner(SongSelectSearchActivity.this, creation_banner);

        permission();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", "NO");
                setResult(102, intent);
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

    }

    public void permission() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SongSelectSearchActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
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

    public void loadSongs() {
        manager = new DatabaseManager(context);
        progressBar = findViewById(R.id.progressBar);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        SongsRecycler = findViewById(R.id.recycler_songs_popular);
        edtSearchSong = (EditText) findViewById(R.id.edtSearchSong);

        edtSearchSong.setTypeface(typeface, Typeface.BOLD);

        songs = manager.getThemes();
        if (songs.moveToFirst()) {
            do {
                if (songs.getInt(songs.getColumnIndex("Cat_Id")) == 1 || songs.getInt(songs.getColumnIndex("Cat_Id")) == 2) {

                } else {
                    songsArrayList.add(new ThemeModel(songs.getInt(songs.getColumnIndex("Id")),
                            songs.getInt(songs.getColumnIndex("Cat_Id")), songs.getString(songs.getColumnIndex("Theme_Name")),
                            songs.getString(songs.getColumnIndex("Thumnail_Big")), songs.getString(songs.getColumnIndex("Thumnail_Small")),
                            songs.getString(songs.getColumnIndex("SoundFile")), songs.getString(songs.getColumnIndex("sound_size")),
                            songs.getString(songs.getColumnIndex("GameobjectName")), songs.getInt(songs.getColumnIndex("Theme_Id")),
                            songs.getString(songs.getColumnIndex("lyrics")), 1));
                }
            } while (songs.moveToNext());
        }

        songsAdapter = new SongsAdapter(SongSelectSearchActivity.this, songsArrayList);
        SongsRecycler.setLayoutManager(new LinearLayoutManager(context));
        SongsRecycler.setAdapter(songsAdapter);

        edtSearchSong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                songsAdapter.getFilter().filter(query);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.Holder> implements Filterable {

        private boolean on_attach = true;
        private Activity context;
        private ArrayList<ThemeModel> songsArrayList;
        private ThemeModel themeModel;
        private ArrayList<ThemeModel> mFilteredList;
        public ArrayList<AsyncTask> asyntask = new ArrayList<AsyncTask>();
        public ArrayList<AsyncTask> asyntaskCount = new ArrayList<AsyncTask>();

        public SongsAdapter(Activity context, ArrayList<ThemeModel> songsArrayList) {
            this.context = context;
            this.songsArrayList = songsArrayList;
            this.mFilteredList = songsArrayList;
        }

        public class Holder extends RecyclerView.ViewHolder {
            private TextView title, duration;
            private ImageView use, play;
            private LinearLayout item_back;
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

                                String final_sound = download.getString(download.getColumnIndex("sound"));
                                Constants.songname = themeModel.Theme_Name.replaceAll("'", "''");

                                final JSONObject jsonAdd = new JSONObject();
                                jsonAdd.put("soundpath", final_sound);
                                jsonAdd.put("lyrics", themeModel.lyrics.replaceAll("'", "''"));

                                AdMobLoadAds.getInstance().displayInterstitial(getApplicationContext(), new AdMobLoadAds.MyCallback() {
                                    @Override
                                    public void callbackCall() {
                                        AndroidPlugin.UnityCall("SelectMusic", "GetSelectedMusic", jsonAdd.toString());
                                        download.close();
                                        Intent intent = new Intent();
                                        intent.putExtra("MESSAGE", "YES");
                                        setResult(102, intent);
                                        finish();
                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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

        @Override
        public Filter getFilter() {
            return new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence charSequence) {
                    String charString = charSequence.toString();
                    if (charString.isEmpty()) {
                        songsArrayList = mFilteredList;
                    } else {
                        ArrayList<ThemeModel> filteredList = new ArrayList<>();
                        for (ThemeModel themeModel : mFilteredList) {
                            if (themeModel.getTheme_Name().toLowerCase().toString().contains(charString)) {
                                filteredList.add(themeModel);
                            }
                        }
                        songsArrayList = filteredList;
                    }
                    FilterResults filterResults = new FilterResults();
                    filterResults.values = songsArrayList;
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                    songsArrayList = (ArrayList<ThemeModel>) filterResults.values;
                    notifyDataSetChanged();
                }
            };
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
                    viewHolder.ivprogress.setVisibility(View.GONE);
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
                    songsAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "NO");
        setResult(102, intent);
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

        if (songs != null)
            songs.close();
        Glide.with(getApplicationContext()).pauseRequests();
    }

}
