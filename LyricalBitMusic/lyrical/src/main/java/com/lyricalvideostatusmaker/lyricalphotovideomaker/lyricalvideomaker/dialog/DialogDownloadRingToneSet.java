package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;

public class DialogDownloadRingToneSet extends Dialog {

    private static final String TAG = DialogDownloadRingToneSet.class.getSimpleName();
    Context context;
    String soundurl;
    File filename;

    private TextView progress;
    DownloadSongTask task;

    public DialogDownloadRingToneSet(Context context, String soundUrl) {
        super(context);
        this.context = context;
        this.soundurl = soundUrl;
        File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "Download");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        filename = new File(dir, "ringtone.mp3");
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
        task = new DownloadSongTask();
        task.execute(soundurl);
    }

    class DownloadSongTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();

                int lenghtOfFile = conexion.getContentLength();

                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(filename);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        protected void onProgressUpdate(String... strings) {
            progress.setText("" + strings[0] + "%");
        }

        @Override
        protected void onPostExecute(String unused) {
            dismiss();
        }
    }

}
