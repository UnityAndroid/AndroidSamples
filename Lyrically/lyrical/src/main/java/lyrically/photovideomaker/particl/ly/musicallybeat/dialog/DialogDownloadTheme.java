package lyrically.photovideomaker.particl.ly.musicallybeat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DialogDownloadTheme extends Dialog {

    private static final String TAG = DialogDownloadTheme.class.getSimpleName();

    Context context;
    int id, Theme_Id;
    String sound_url, image_url, tname, filename, oname, lyrics;
    DatabaseManager manager;
    String soundfile, imagefile;

    LottieAnimationView IvLoader;
    TextView TvPercent, TvProcessing;
    SeekBar SbPercent;


    DownloadThemeTask task;

    public DialogDownloadTheme(Context context, int id, String soundurl, String imageurl, String tname, String oname, int Theme_Id, String lyrics) {
        super(context);
        this.context = context;
        this.id = id;
        this.oname = oname;
        this.tname = tname;
        this.sound_url = soundurl;
        this.image_url = imageurl;
        this.lyrics = lyrics;
        this.Theme_Id = Theme_Id;
        filename = String.valueOf(System.currentTimeMillis());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_download_theme);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        getIDs();
        setEvents();
        doWork();

    }

    private void getIDs() {

        SbPercent = findViewById(R.id.download_progress);
        TvPercent = findViewById(R.id.percent_text);
        TvProcessing = findViewById(R.id.text_processing);
        IvLoader = findViewById(R.id.dialog_download_loader);
    }

    private void setEvents() {
    }

    private void doWork() {
        manager = new DatabaseManager(context);
        TvProcessing.setText("Your Theme is Currently Processing...");
        task = new DownloadThemeTask();
        task.execute();
    }


    private class DownloadThemeTask extends AsyncTask<String, Integer, String> {

        public DownloadThemeTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... sUrl) {
            if (!manager.checksongurl(sound_url)) {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(sound_url);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Accept-Encoding", "identity");
                    connection.connect();

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Server returned HTTP " + connection.getResponseCode()
                                + " " + connection.getResponseMessage();
                    }

                    int fileLength = connection.getContentLength();

                    soundfile = AndroidPlugin.GetSoundPath() + filename + ".mp3";

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
                    Log.e(TAG, e.toString());
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
            return String.valueOf(id);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            TvPercent.setText("" + values[0] + "%");
            SbPercent.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(final String result) {
            if (!result.equalsIgnoreCase("-1")) {
                soundfile = AndroidPlugin.GetSoundPath() + filename + ".mp3";
                imagefile = AndroidPlugin.GetImagePath() + filename + ".png";

                Glide.with(context).asBitmap().load(image_url).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {

                            Bitmap bitmap = Bitmap.createScaledBitmap(resource, 720, 1080, false);
                            FileOutputStream out = new FileOutputStream(new File(imagefile));
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance

                            if (!manager.checksongurl(sound_url)) {
                                manager.insertdownloads(id, soundfile, imagefile, tname.replaceAll("'", "''"), oname, image_url, sound_url, lyrics);
                                Cursor cursor = null;
                                try {
                                    cursor = manager.featchSongByUrl(sound_url);
                                    if (cursor.moveToFirst()) {
                                        manager.insertsongdownloads(cursor.getInt(cursor.getColumnIndex("id")), soundfile, sound_url, cursor.getInt(cursor.getColumnIndex("Cat_Id")), lyrics);
                                    }
                                } finally {
                                    if (cursor != null)
                                        cursor.close();
                                }
                            } else {
                                Cursor cursor = null;
                                try {
                                    cursor = manager.getdownloadedsoundfromurl(sound_url);
                                    if (cursor.moveToFirst()) {
                                        manager.insertdownloads(id, cursor.getString(cursor.getColumnIndex("sound")), imagefile, tname.replaceAll("'", "''"), oname, image_url, sound_url, lyrics);
                                    }else{
                                        manager.insertdownloads(id, soundfile, imagefile, tname.replaceAll("'", "''"), oname, image_url, sound_url, lyrics);
                                    }
                                } finally {
                                    if (cursor != null)
                                        cursor.close();
                                }
                            }

                            Cursor download = manager.getdownloads(Integer.parseInt(result));
                            if (download.moveToFirst()) {
                                try {
                                    JSONObject jsonAdd = new JSONObject();
                                    jsonAdd.put("themeid", String.valueOf(download.getInt(0)));
                                    jsonAdd.put("sound", download.getString(1));
                                    jsonAdd.put("image", download.getString(2));

                                    manager.setcurrenttheme(id, soundfile, imagefile, tname, oname, Theme_Id);
                                    dismiss();

                                } catch (JSONException e) {
                                    Log.e(TAG, e.toString());
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e(TAG, e.toString());
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

            } else {
                dismiss();
                Log.e(TAG, "Download Theme Failed");

                AndroidPlugin.showToast(context, "Donwload Theme Failed");
            }
        }
    }


}
