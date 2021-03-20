package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.AndroidPlugin;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity.HomeActivity;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model.ThemeModel;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.Constants;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.DatabaseManager;

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

public class DialogDownloadSong extends Dialog {

    private static final String TAG = DialogDownloadSong.class.getSimpleName();
    Context context;
    int id, catid;
    String soundurl, filename;
    String soundfile, lyrics;

    private TextView progress;

    DatabaseManager manager;
    DownloadSongTask task;

    public DialogDownloadSong(Context context, int id, String soundUrl, int catid, String lyrics) {
        super(context);
        this.context = context;
        this.id = id;
        this.catid = catid;
        this.lyrics = lyrics;
        this.soundurl = soundUrl;
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
        progress = findViewById(R.id.progress_download);
    }

    private void setEvents() {
    }

    private void doWork() {
        manager = new DatabaseManager(context);
        task = new DownloadSongTask();
        task.execute();

    }

    private class DownloadSongTask extends AsyncTask<String, Integer, String> {


        public DownloadSongTask() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(soundurl);
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
            return String.valueOf(id);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progress.setText("" + values[0] + "%");
        }

        @Override
        protected void onPostExecute(final String result) {
            if (!result.equalsIgnoreCase("-1")) {
                soundfile = AndroidPlugin.GetSoundPath() + filename + ".mp3";
                //manager.insertsongdownloads(id, soundfile.replaceAll("'", "''"), soundurl, catid, lyrics.replaceAll("'", "''"));
                dismiss();

                new HttpPostRequestCountSongs(id).execute(Constants. + "download/formate/json/");

            } else {
                dismiss();
                Log.e(TAG, "Download Sound Failed");
                AndroidPlugin.showToast(context, "Download Sound Failed");
            }
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
