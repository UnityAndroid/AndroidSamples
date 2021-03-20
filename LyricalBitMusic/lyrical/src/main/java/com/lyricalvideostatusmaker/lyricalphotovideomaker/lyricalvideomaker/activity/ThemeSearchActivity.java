package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity;

import android.app.Activity;
import android.content.Context;
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
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
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
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.ads.NativeAdLayout;

import org.json.JSONException;
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
import java.util.Collections;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.AndroidPlugin;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model.ThemeModel;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.nativetemplates.TemplateView;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.Constants;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.DatabaseManager;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.GlideImageLoader;

public class ThemeSearchActivity extends AppCompatActivity {

    public Context context;
    public ImageView back;

    public ProgressBar progressBar;
    public boolean end = false;
    public RecyclerView SongsRecycler;
    public Cursor songs;
    public DatabaseManager manager;
    public String soundfile = "";
    public ThemeThumbnailAdapter themeThumbnailAdapter;
    public int pos = 0;
    public int position = 1;
    public MediaPlayer mediaPlayer;
    public String sound = "";
    public ArrayList<ThemeModel> songsArrayList = new ArrayList<ThemeModel>();
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

        typeface = Typeface.createFromAsset(getAssets(), "fonts/merriweather_bold.ttf");
        typeface1 = Typeface.createFromAsset(getAssets(), "fonts/merriweather.ttf");

        back = (ImageView) findViewById(R.id.song_select_back);

        LinearLayout creation_banner = (LinearLayout) findViewById(R.id.creation_banner);
        AdMobLoadAds.getInstance().loadBanner(ThemeSearchActivity.this, creation_banner);

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
            ActivityCompat.requestPermissions(ThemeSearchActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
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
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        SongsRecycler = (RecyclerView) findViewById(R.id.recycler_songs_popular);
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

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm);

        Collections.reverse(songsArrayList);
        Collections.shuffle(songsArrayList);

        themeThumbnailAdapter = new ThemeThumbnailAdapter(ThemeSearchActivity.this, songsArrayList, margin);
        //GridLayoutManager mDatabaseManager = new GridLayoutManager(getApplicationContext(), 2);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        SongsRecycler.setLayoutManager(staggeredGridLayoutManager);
        SongsRecycler.setAdapter(themeThumbnailAdapter);

        edtSearchSong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                themeThumbnailAdapter.getFilter().filter(query);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void showProgressView() {
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideProgressView() {
        progressBar.setVisibility(View.GONE);
    }

    public class ThemeThumbnailAdapter extends RecyclerView.Adapter<ThemeThumbnailAdapter.ViewHolder> implements Filterable {

        public boolean on_attach = true;
        public Cursor cursor;
        public int margin;
        public Activity context;
        public DatabaseManager manager;
        public ArrayList<ThemeModel> arrayList, mFilteredList;
        public ThemeModel themeModel;
        public ArrayList<AsyncTask> asyntask = new ArrayList<AsyncTask>();
        public ArrayList<AsyncTask> asyntaskCount = new ArrayList<AsyncTask>();

        public ThemeThumbnailAdapter(Activity context, ArrayList<ThemeModel> songsArrayList, int margin) {
            manager = new DatabaseManager(context);
            this.margin = margin;
            this.context = context;
            this.arrayList = songsArrayList;
            this.mFilteredList = songsArrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme_new, parent, false));
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
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
            themeModel = (ThemeModel) arrayList.get(position);

            viewHolder.CardMain.startAnimation(AnimationUtils.loadAnimation(context, R.anim.card_animation));

            viewHolder.txt_progress.setTypeface(typeface, Typeface.BOLD);
            viewHolder.themename.setTypeface(typeface, Typeface.BOLD);

            viewHolder.themename.setSelected(true);
            viewHolder.themename.setText(themeModel.getTheme_Name());

            switch (position % 6) {
                case 0:
                    viewHolder.CardMiddle.setCardBackgroundColor(ContextCompat.getColor(context, R.color.home_cat_1));
                    //viewHolder.RlText.setCardBackgroundColor(ContextCompat.getColor(context, R.color.home_cat_1));
                    break;
                case 1:
                    viewHolder.CardMiddle.setCardBackgroundColor(ContextCompat.getColor(context, R.color.home_cat_2));
                    //viewHolder.RlText.setCardBackgroundColor(ContextCompat.getColor(context, R.color.home_cat_2));
                    break;
                case 2:
                    viewHolder.CardMiddle.setCardBackgroundColor(ContextCompat.getColor(context, R.color.home_cat_3));
                    //viewHolder.RlText.setCardBackgroundColor(ContextCompat.getColor(context, R.color.home_cat_3));
                    break;
                case 3:
                    viewHolder.CardMiddle.setCardBackgroundColor(ContextCompat.getColor(context, R.color.home_cat_4));
                    //viewHolder.RlText.setCardBackgroundColor(ContextCompat.getColor(context, R.color.home_cat_4));
                    break;
                case 4:
                    viewHolder.CardMiddle.setCardBackgroundColor(ContextCompat.getColor(context, R.color.home_cat_5));
                    //viewHolder.RlText.setCardBackgroundColor(ContextCompat.getColor(context, R.color.home_cat_5));
                    break;
                case 5:
                    viewHolder.CardMiddle.setCardBackgroundColor(ContextCompat.getColor(context, R.color.home_cat_6));
                    //viewHolder.RlText.setCardBackgroundColor(ContextCompat.getColor(context, R.color.home_cat_6));
                    break;
            }

            Glide.with(context).load(themeModel.getThumnail_Small()).apply(new RequestOptions().override(180, 320)).into(viewHolder.ivthumb);


            if (manager.checktheme(themeModel.Id)) {
                Cursor download = manager.getdownloads(themeModel.Id);
                if (download.moveToFirst()) {
                    if (!manager.checkthemeimageurl(download.getInt(0), themeModel.getThumnail_Big()) || !manager.checkthemesongurl(download.getInt(0), themeModel.getSoundFile())) {
                        GlideImageLoader.SetImageResource(context, viewHolder.ivdownload, R.drawable.theme_download_icon, null);
                    } else {
                        GlideImageLoader.SetImageResource(context, viewHolder.ivdownload, R.drawable.theme_use_icon, null);
                    }
                } else {
                    GlideImageLoader.SetImageResource(context, viewHolder.ivdownload, R.drawable.theme_download_icon, null);
                }
                download.close();
            } else {
                GlideImageLoader.SetImageResource(context, viewHolder.ivdownload, R.drawable.theme_download_icon, null);
            }

            viewHolder.CardMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    themeModel = (ThemeModel) arrayList.get(position);
                    if (manager.checktheme(themeModel.getId())) {
                        Cursor download = manager.getdownloads(themeModel.getId());
                        if (download.moveToFirst()) {
                            if (!manager.checkthemeimageurl(download.getInt(0), themeModel.getThumnail_Big())
                                    || !manager.checkthemesongurl(download.getInt(0), themeModel.getSoundFile())) {
                                viewHolder.ivdownload.setVisibility(View.GONE);

                                DownloadTheme(themeModel, viewHolder);

                            } else {

                                manager.setcurrenttheme(download.getInt(0), download.getString(1), download.getString(2),
                                        download.getString(3).replaceAll("'", "''"), download.getString(4), themeModel.getTheme_Id());

                                if (AndroidPlugin.isAdsShow) {
                                    AdMobLoadAds.getInstance().displayInterstitial(context, new AdMobLoadAds.MyCallback() {
                                        @Override
                                        public void callbackCall() {
                                            AndroidPlugin.isAdsShow = false;
                                            AndroidPlugin.getTheme();

                                            Intent intent = new Intent();
                                            intent.putExtra("MESSAGE", "YES");
                                            setResult(102, intent);
                                            finish();
                                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                        }
                                    });
                                } else {
                                    AndroidPlugin.isAdsShow = true;
                                    AndroidPlugin.getTheme();
                                    Intent intent = new Intent();
                                    intent.putExtra("MESSAGE", "YES");
                                    setResult(102, intent);
                                    finish();
                                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                }
                            }
                        } else {
                            DownloadTheme(themeModel, viewHolder);
                        }
                        download.close();
                    } else {
                        DownloadTheme(themeModel, viewHolder);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
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
                        for (ThemeModel songName : mFilteredList) {
                            if (songName.getTheme_Name().toLowerCase().toString().contains(charString)) {
                                filteredList.add(songName);
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

        public class ViewHolder extends RecyclerView.ViewHolder {

            private CardView CardMain, CardMiddle;
            private RelativeLayout RelativeName;
            private ImageView ivdownload, ivthumb;
            private TextView themename, txt_progress;
            private ProgressBar ivprogress;
            private RelativeLayout llDownload;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ivdownload = itemView.findViewById(R.id.theme_download_image);
                ivthumb = itemView.findViewById(R.id.theme_thumb_image);
                themename = itemView.findViewById(R.id.theme_video_name);
                CardMain = itemView.findViewById(R.id.theme_root_card);
                CardMiddle = itemView.findViewById(R.id.theme_middle_card);
                ivprogress = itemView.findViewById(R.id.imageProgress);
                txt_progress = itemView.findViewById(R.id.txt_progress);
                llDownload = itemView.findViewById(R.id.llDownload);
            }
        }

        public void DownloadTheme(final ThemeModel theme, final ViewHolder holder) {
            if (asyntask.size() == 0) {
                holder.llDownload.setVisibility(View.VISIBLE);
                holder.ivdownload.setVisibility(View.GONE);
                asyntask.add(new DownloadThemeTask(theme, holder).execute());
            } else {
                AndroidPlugin.showToast(context, "Please Wait Until,\nAnother Theme Download Finished!!");
            }
        }

        private class DownloadThemeTask extends AsyncTask<String, Integer, String> {

            ViewHolder viewHolder;
            ThemeModel theme;
            String soundfile, imagefile, filename;

            public DownloadThemeTask(final ThemeModel theme, final ViewHolder viewHolder) {
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
                viewHolder.txt_progress.setText("" + values[0]);
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

                                Cursor download = null;
                                try {
                                    download = manager.getdownloads(Integer.parseInt(result));
                                    if (download.moveToFirst()) {
                                        try {
                                            JSONObject jsonAdd = new JSONObject();
                                            jsonAdd.put("themeid", String.valueOf(download.getInt(0)));
                                            jsonAdd.put("sound", download.getString(1));
                                            jsonAdd.put("image", download.getString(2));

                                            manager.setcurrenttheme(theme.Id, soundfile, imagefile, theme.Theme_Name.replaceAll("'", "''"), theme.GameobjectName, theme.Theme_Id);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } finally {
                                    if (download != null)
                                        download.close();
                                }

                                viewHolder.llDownload.setVisibility(View.GONE);
                                viewHolder.ivdownload.setVisibility(View.VISIBLE);

                                if (manager.checktheme(theme.Id)) {
                                    Glide.with(context).load(R.drawable.theme_use_icon).apply(new RequestOptions().fitCenter()).into(viewHolder.ivdownload);
                                } else {
                                    Glide.with(context).load(R.drawable.theme_download_icon).apply(new RequestOptions().fitCenter()).into(viewHolder.ivdownload);
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
                    viewHolder.llDownload.setVisibility(View.GONE);
                    viewHolder.ivdownload.setVisibility(View.VISIBLE);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "NO");
        setResult(102, intent);
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (songs != null)
            songs.close();
        Glide.with(getApplicationContext()).pauseRequests();
    }

}
