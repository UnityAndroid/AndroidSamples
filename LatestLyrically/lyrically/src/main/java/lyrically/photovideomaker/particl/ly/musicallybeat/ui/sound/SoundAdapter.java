package lyrically.photovideomaker.particl.ly.musicallybeat.ui.sound;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.SoundEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.SoundResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.Error;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.OnDownloadListener;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.OnProgressListener;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.PRDownloader;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.Progress;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.Status;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ConstantUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

public class SoundAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    SoundViewModel soundViewModel;
    MyApplication application;
    private List<SoundResponse.Sound> soundArrayList = new ArrayList<>();
    Context context;

    public SoundAdapter(Context context) {
        this.context = context;
        application = MyApplication.getInstance();
        soundViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(SoundViewModel.class);
    }

    public void setSoundArrayList(List<SoundResponse.Sound> folders) {
        this.soundArrayList = folders;
        notifyDataSetChanged();
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {

        private TextView textViewSoundName;

        ProgressBar progressBar;
        TextView textViewUse;

        ImageView imageViewPlay;
        ImageView imageViewDownload;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            textViewSoundName = itemView.findViewById(R.id.sound_text_name);
            progressBar = itemView.findViewById(R.id.sound_progressBar);

            textViewUse = itemView.findViewById(R.id.sound_text_use);

            imageViewPlay = itemView.findViewById(R.id.sound_image_play);
            imageViewDownload = itemView.findViewById(R.id.sound_image_download);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sound, parent, false);
        return new ViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder1 holder1 = (ViewHolder1) holder;
        SoundResponse.Sound folder = soundArrayList.get(position);

        holder1.textViewSoundName.setText(folder.sound_name);

        holder1.progressBar.setVisibility(View.GONE);
        holder1.textViewUse.setVisibility(View.GONE);
        holder1.imageViewDownload.setVisibility(View.GONE);

        if (PRDownloader.getStatus(folder.id) == Status.RUNNING) {

            holder1.textViewUse.setVisibility(View.GONE);
            holder1.imageViewDownload.setVisibility(View.GONE);
            holder1.progressBar.setVisibility(View.VISIBLE);

        } else {
            if (soundViewModel.checkSound(folder.sound_full_url) != null) {
                holder1.textViewUse.setVisibility(View.VISIBLE);
                holder1.imageViewDownload.setVisibility(View.GONE);

                ((SoundActivity) context).checkMusicIcon(soundViewModel.checkSound(folder.sound_full_url).getSound_path(), holder1.imageViewPlay,holder1.textViewUse);

            } else {
                holder1.textViewUse.setVisibility(View.GONE);
                holder1.imageViewDownload.setVisibility(View.VISIBLE);
                ((SoundActivity) context).checkMusicIcon(folder.sound_full_url, holder1.imageViewPlay,holder1.textViewUse);
            }
        }


       /* holder1.imageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder1.textViewUse.getVisibility() == View.VISIBLE) {
                    ((SoundActivity) context).playPauseSound(holder1.getLayoutPosition(), application.getDatabaseManager().GetMusicFileByUrl(soundArrayList.get(holder1.getLayoutPosition()).sound_full_url), soundArrayList.get(holder1.getLayoutPosition()).sound_full_url,soundArrayList.get(holder1.getLayoutPosition()).sound_name);
                } else {
                    holder1.progressBar.setProgress(0);
                    holder1.progressBar.setVisibility(View.VISIBLE);
                    holder1.textViewUse.setVisibility(View.GONE);
                    holder1.imageViewDownload.setVisibility(View.GONE);

                    DownloadSoundTask(folder, holder1.progressBar, position);
                }
            }
        });*/

        holder1.textViewUse.setOnClickListener(view -> {
            int pos = holder1.getLayoutPosition();
            ((SoundActivity) context).SelectMusic(soundViewModel.GetMusicFileByUrl(soundArrayList.get(pos).sound_full_url), soundArrayList.get(pos).sound_name,soundArrayList.get(pos).lyrics);
        });

        holder1.itemView.setOnClickListener(view -> {
            if (soundViewModel.checkSound(folder.sound_full_url) != null) {
                ((SoundActivity) context).playPauseSound(soundViewModel.GetMusicFileByUrl(soundArrayList.get(holder1.getLayoutPosition()).sound_full_url), soundArrayList.get(holder1.getLayoutPosition()).sound_name);
            } else {
                holder1.progressBar.setProgress(0);
                holder1.progressBar.setVisibility(View.VISIBLE);

                holder1.textViewUse.setVisibility(View.GONE);
                holder1.imageViewDownload.setVisibility(View.GONE);

                DownloadSoundTask(folder, holder1, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return soundArrayList.size();
    }

    public void DownloadSoundTask(SoundResponse.Sound sound, ViewHolder1 holder1, int position) {

        String filename = String.valueOf(System.currentTimeMillis());
        PRDownloader.download(sound.sound_full_url, ConstantUtils.GetSoundPath(context), filename + ".mp3")
                .setTag(sound.id)
                .build()
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        holder1.progressBar.setProgress((int) (((double) progress.currentBytes / progress.totalBytes) * 100.0));
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        String soundfile = ConstantUtils.GetSoundPath(context) + filename + ".mp3";
                        SoundEntity soundEntity = new SoundEntity(sound.sound_full_url, soundfile, sound.lyrics);
                        soundViewModel.insert(soundEntity);

                        holder1.progressBar.setVisibility(View.GONE);

                        holder1.textViewUse.setVisibility(View.VISIBLE);
                        holder1.imageViewDownload.setVisibility(View.GONE);

                    }

                    @Override
                    public void onError(Error error) {
                        ConstantUtils.showToast(context, "Sound Download Failed");
                        holder1.progressBar.setVisibility(View.GONE);

                        holder1.textViewUse.setVisibility(View.GONE);
                        holder1.imageViewDownload.setVisibility(View.VISIBLE);

                    }
                });
    }



   /* public class DownloadSoundTask extends AsyncTask<Sound, Integer, String> {

        String soundfile = "";
        ViewHolder holder;
        Sound sound;
        int position;

        public DownloadSoundTask(ViewHolder holder, int position) {
            this.holder = holder;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(Sound... Sounds) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            this.sound = Sounds[0];
            try {
                URL url = new URL(Sounds[0].sound_full_url);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Accept-Encoding", "identity");
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                int fileLength = connection.getContentLength();
                soundfile = ConstantUtils.GetSoundPath(context) + System.currentTimeMillis() + ".mp3";
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
                ConstantUtils.showErrorLog(e.toString());
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
            return String.valueOf(Sounds[0].sound_name);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            holder.progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            holder.progressBar.setVisibility(View.GONE);
            holder.textViewUse.setVisibility(View.VISIBLE);

            if (!s.equalsIgnoreCase("-1")) {
                holder.textViewUse.setText("Use");
                databaseManager.addMusic(sound.sound_full_url, soundfile, sound.lyrics);
                notifyItemChanged(position);
            } else {
                Log.e(Application.class.getName(), "Download Sound Failed");
                Toast.makeText(context, "Download Sound Failed", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}
