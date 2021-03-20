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

import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.ConstantFilePaths;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DialogDownloadParticle extends Dialog {

    private static final String TAG = DialogDownloadParticle.class.getSimpleName();
    Context context;

    DatabaseManager manager;
    public int id, category_id;
    public String theme_name, prefab_name, thumb_url, particle_url, file_name;

    LottieAnimationView IvLoader;
    TextView TvPercent, TvProcessing;
    SeekBar SbPercent;

    String thumbfile, particlefile;

    public DialogDownloadParticle(@NonNull Context context, int id, int category_id, String theme_name, String prefab_name, String thumb_url, String particle_url) {
        super(context);
        this.context = context;
        this.id = id;
        this.category_id = category_id;
        this.theme_name = theme_name;
        this.prefab_name = prefab_name;
        this.thumb_url = thumb_url;
        this.particle_url = particle_url;
        file_name = String.valueOf(System.currentTimeMillis());
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
        TvProcessing.setText("Your Particle is Currently Processing...");

    }

    private class DownloadParticleTask extends AsyncTask<String, Integer, String> {


        public DownloadParticleTask() {
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
                URL url = new URL(particle_url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept-Encoding", "identity");
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                particlefile = ConstantFilePaths.GetParticlePath(context) + file_name + ".unity3d";

                input = connection.getInputStream();
                output = new FileOutputStream(particlefile);


                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    // only if total length is known
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
                new DownloadThumbTask().execute();
            } else {
                dismiss();
                Log.e(TAG, "Download Particle Failed");

                AndroidPlugin.showToast(context,"Download Particle Failed");
            }
        }

    }



    private class DownloadThumbTask extends AsyncTask<String, Integer, String> {


        public DownloadThumbTask() {
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
                URL url = new URL(thumb_url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept-Encoding", "identity");
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();

                thumbfile = AndroidPlugin.GetImagePath() + file_name + ".png";

                input = connection.getInputStream();
                output = new FileOutputStream(thumbfile);


                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    // only if total length is known
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
                manager.insertparticledownloads(id, category_id, theme_name, prefab_name, thumbfile, particlefile, thumb_url, particle_url);
                dismiss();
            } else {
                dismiss();
                Log.e(TAG, "Download Particle Failed");

                AndroidPlugin.showToast(context,"Donwload Particle Failed");
            }
        }
    }



}
