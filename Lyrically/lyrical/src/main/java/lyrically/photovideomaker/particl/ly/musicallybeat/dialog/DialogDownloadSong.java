package lyrically.photovideomaker.particl.ly.musicallybeat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DialogDownloadSong extends Dialog {

    private static final String TAG = DialogDownloadSong.class.getSimpleName();

    Context context;
    int id, catid;
    String soundurl, filename;
    String soundfile, lyrics;

    LottieAnimationView IvLoader;
    TextView TvPercent, TvProcessing;
    SeekBar SbPercent;

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
        SbPercent = findViewById(R.id.download_progress);
        TvPercent = findViewById(R.id.percent_text);
        TvProcessing = findViewById(R.id.text_processing);
        IvLoader = findViewById(R.id.dialog_download_loader);

    }

    private void setEvents() {
    }

    private void doWork() {
        manager = new DatabaseManager(context);
        TvProcessing.setText("Your Song is Currently Processing...");
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
            TvPercent.setText("" + values[0] + "%");
            SbPercent.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(final String result) {
            if (!result.equalsIgnoreCase("-1")) {
                soundfile = AndroidPlugin.GetSoundPath() + filename + ".mp3";
                manager.insertsongdownloads(id, soundfile, soundurl, catid, lyrics);

                dismiss();
                //  new HttpPostRequestCountSongs().execute("https://hamercode.xyz/WBitMaster/service/download/formate/json/");
            } else {
                dismiss();
                Log.e(TAG, "Download Sound Failed");

                AndroidPlugin.showToast(context,"Download Sound Failed");
            }
        }

    }


}
