package lyrically.photovideomaker.particl.ly.musicallybeat.ui.Theme;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.SoundEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ThemeEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ThemeResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.Error;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.OnDownloadListener;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.OnProgressListener;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.PRDownloader;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.Progress;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.Status;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ConstantUtils;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ScreenUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

public class ThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ThemeViewModel themeViewModel;

    private List<ThemeResponse.Theme> themeArrayList = new ArrayList<>();
    Context context;

    LayoutInflater inflter;
    ViewOutlineProvider outlineProvider;
    FrameLayout.LayoutParams params;

    MyApplication application;

    public ThemeAdapter(Context context, FrameLayout.LayoutParams params, ViewOutlineProvider outlineProvider) {
        this.context = context;
        inflter = (LayoutInflater.from(context.getApplicationContext()));
        themeViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ThemeViewModel.class);
        this.params = params;
        this.outlineProvider = outlineProvider;
        application = MyApplication.getInstance();
    }

    public void setThemeArrayList(List<ThemeResponse.Theme> folders) {
        this.themeArrayList = folders;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;
        ImageView imageViewDownload;
        ImageView imageViewThumb;
        private ConstraintLayout cardViewFolder;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.theme_progressBar);
            imageViewThumb = itemView.findViewById(R.id.image_main);
            imageViewDownload = itemView.findViewById(R.id.download_theme);
            cardViewFolder = itemView.findViewById(R.id.image_card);
            textView = itemView.findViewById(R.id.theme_video_name);
        }
    }


    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_theme, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder holder1 = (ViewHolder) holder;
        ThemeResponse.Theme folder = themeArrayList.get(position);

        if (position < 2) {
            params.topMargin = ScreenUtils.convertDPItoINT(context, 20);
        } else {
            params.topMargin = 0;
        }
        holder1.cardViewFolder.setLayoutParams(params);

        holder1.cardViewFolder.setOutlineProvider(outlineProvider);
        holder1.cardViewFolder.setClipToOutline(true);

        holder1.textView.setText(folder.Theme_Name);

        holder1.progressBar.setVisibility(View.GONE);
        holder1.imageViewDownload.setVisibility(View.GONE);

        if (PRDownloader.getStatus(folder.Id) == Status.RUNNING) {
            holder1.imageViewDownload.setVisibility(View.GONE);
            holder1.progressBar.setVisibility(View.VISIBLE);
        } else {
            if (themeViewModel.checkTheme(folder.Id) != null) {
                holder1.imageViewDownload.setVisibility(View.GONE);
            } else {
                holder1.imageViewDownload.setVisibility(View.VISIBLE);
            }
        }

        String path = folder.Thumnail_Big;

        Glide.with(context).load(path)
                .placeholder(R.drawable.app_video_center_bg)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder1.imageViewThumb);

        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeEntity entity = themeViewModel.checkTheme(folder.Id);
                if (entity != null) {
                    application.appTheme.sound_path = entity.getSound_path();
                    application.appTheme.sound_name = entity.getTheme_name();

                    application.appTheme.Theme_Id = folder.Theme_Id;
                    application.appTheme.GameobjectName = entity.getGameobjectName();
                    application.appTheme.image_file = entity.getThumb_path();

                    application.appTheme.cropImages.clear();

                    ((ThemeActivity) context).SelectTheme();

                } else {
                    holder1.progressBar.setProgress(0);
                    holder1.progressBar.setVisibility(View.VISIBLE);
                    holder1.imageViewDownload.setVisibility(View.GONE);

                    DownloadThemeTask(folder, holder1);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return themeArrayList.size();
    }

    public void DownloadThemeTask(ThemeResponse.Theme theme, ViewHolder holder) {
        SoundEntity soundEntity = themeViewModel.checkSound(theme.SoundFile);
        if (soundEntity == null) {
            String name = System.currentTimeMillis() + ".mp3";
            String soundfile = ConstantUtils.GetSoundPath(context) + name;
            PRDownloader.download(theme.SoundFile, ConstantUtils.GetSoundPath(context), name)
                    .setTag(theme.Id)
                    .build()
                    .setOnProgressListener(new OnProgressListener() {
                        @Override
                        public void onProgress(Progress progress) {
                            holder.progressBar.setProgress((int) (((double) progress.currentBytes / progress.totalBytes) * 100.0) / 2);
                        }
                    })
                    .start(new OnDownloadListener() {
                        @Override
                        public void onDownloadComplete() {
                            SoundEntity soundEntity1 = new SoundEntity(theme.SoundFile, soundfile, theme.lyrics);
                            themeViewModel.insertSound(soundEntity1);

                            DownloadThumbTask(theme, holder, soundEntity1);
                        }

                        @Override
                        public void onError(Error error) {
                            ConstantUtils.showToast(context, "Theme Download Failed");
                            holder.progressBar.setVisibility(View.GONE);
                            holder.imageViewDownload.setVisibility(View.VISIBLE);
                        }
                    });
        } else {
            holder.progressBar.setProgress(50);
            DownloadThumbTask(theme, holder, soundEntity);
        }
    }

    public void DownloadThumbTask(ThemeResponse.Theme theme, ViewHolder holder, SoundEntity soundEntity) {

        String name = System.currentTimeMillis() + ".jpg";
        String imagefile = ConstantUtils.GetImagePath(context) + name;

        PRDownloader.download(theme.Thumnail_Big, ConstantUtils.GetImagePath(context), name)
                .setTag(theme.Id)
                .build()
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        holder.progressBar.setProgress(50 + ((int) (((double) progress.currentBytes / progress.totalBytes) * 100.0) / 2));
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {

                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(new File(imagefile)));
                        context.sendBroadcast(intent);

                        ThemeEntity themeEntity = new ThemeEntity(theme.Id, theme.Theme_Name, String.valueOf(theme.GameobjectName), theme.Thumnail_Big, imagefile, theme.SoundFile, soundEntity.getSound_path(), theme.lyrics);
                        themeViewModel.insert(themeEntity);

                        holder.progressBar.setVisibility(View.GONE);
                        holder.imageViewDownload.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Error error) {
                        ConstantUtils.showToast(context, "Theme Download Failed");
                        holder.progressBar.setVisibility(View.GONE);
                        holder.imageViewDownload.setVisibility(View.VISIBLE);
                    }
                });
    }

}
