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
import lyrically.photovideomaker.particl.ly.musicallybeat.model.SoundFile;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SoundStorageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    MyApplication application;
    private List<SoundFile> soundArrayList = new ArrayList<>();
    Context context;

    public SoundStorageAdapter(Context context) {
        this.context = context;
        application = MyApplication.getInstance();
    }

    public void setSoundArrayList(List<SoundFile> folders) {
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
        SoundFile folder = soundArrayList.get(position);

        holder1.textViewSoundName.setText(folder.file_name);

        holder1.progressBar.setVisibility(View.GONE);
        holder1.textViewUse.setVisibility(View.GONE);
        holder1.imageViewDownload.setVisibility(View.GONE);

        holder1.textViewUse.setVisibility(View.VISIBLE);
        holder1.imageViewDownload.setVisibility(View.GONE);

        ((SoundActivity) context).checkMusicIcon(folder.file_path, holder1.imageViewPlay, holder1.textViewUse);



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
            ((SoundActivity) context).SelectStorageMusic(soundArrayList.get(pos).file_path, soundArrayList.get(pos).file_name);

        });

        holder1.itemView.setOnClickListener(view -> {
            ((SoundActivity) context).playPauseSound(folder.file_path, folder.file_name);
        });
    }

    @Override
    public int getItemCount() {
        return soundArrayList.size();
    }

}
