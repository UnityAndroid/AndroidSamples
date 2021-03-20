package lyrically.photovideomaker.particl.ly.musicallybeat.ui.particle;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ParticleEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ParticleResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.Error;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.OnDownloadListener;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.OnProgressListener;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.PRDownloader;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.Progress;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.downloader.Status;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ConstantUtils;
import com.unity3d.player.UnityPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

public class ParticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ParticleViewModel particleViewModel;
    MyApplication application;
    private ArrayList<ParticleResponse.Particle> particles = new ArrayList<>();
    Context context;
    ViewOutlineProvider outlineProvider;
    FrameLayout.LayoutParams params;
    ParticleResponse.ParticleCategory category;

    public ParticleAdapter(Context context, FrameLayout.LayoutParams params, ViewOutlineProvider outlineProvider, ParticleResponse.ParticleCategory category) {
        this.context = context;
        application = MyApplication.getInstance();
        this.params = params;
        this.category = category;
        this.outlineProvider = outlineProvider;
        particleViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(ParticleViewModel.class);
    }

    public void setParticleArrayList(ArrayList<ParticleResponse.Particle> folders) {
        this.particles = folders;
        notifyDataSetChanged();
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {

        ProgressBar progressBar;
        ImageView imageViewDownload;
        ImageView imageViewThumb;
        private ConstraintLayout cardViewFolder;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.particle_progressBar);
            imageViewThumb = itemView.findViewById(R.id.image_main);
            imageViewDownload = itemView.findViewById(R.id.download_particle);
            cardViewFolder = itemView.findViewById(R.id.image_card);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_particle, parent, false);
        return new ViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder1 holder1 = (ViewHolder1) holder;
        ParticleResponse.Particle folder = particles.get(position);

        holder1.cardViewFolder.setLayoutParams(params);

        holder1.cardViewFolder.setOutlineProvider(outlineProvider);
        holder1.cardViewFolder.setClipToOutline(true);

        holder1.progressBar.setVisibility(View.GONE);
        holder1.imageViewDownload.setVisibility(View.GONE);

        if (PRDownloader.getStatus(folder.id) == Status.RUNNING) {
            holder1.imageViewDownload.setVisibility(View.GONE);
            holder1.progressBar.setVisibility(View.VISIBLE);
        } else {
            if (particleViewModel.checkParticle(folder.id) != null) {
                holder1.imageViewDownload.setVisibility(View.GONE);
            } else {
                holder1.imageViewDownload.setVisibility(View.VISIBLE);
            }
        }

        String path = folder.thumb_url;

        Glide.with(context).load(path)
                .placeholder(R.drawable.app_video_center_bg)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder1.imageViewThumb);

        holder1.itemView.setOnClickListener(view -> {
            if (particleViewModel.checkParticle(folder.id) != null) {
                ((ParticleActivity) context).onBackPressed();
            } else {
                holder1.progressBar.setProgress(0);
                holder1.progressBar.setVisibility(View.VISIBLE);
                holder1.imageViewDownload.setVisibility(View.GONE);
                DownloadParticleTask(folder, holder1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return particles.size();
    }

    public void DownloadParticleTask(ParticleResponse.Particle particle, ViewHolder1 holder1) {

        String filename = String.valueOf(System.currentTimeMillis());
        PRDownloader.download(particle.particle_url, ConstantUtils.GetParticlePath(context), filename + ".unity3d")
                .setTag(particle.id)
                .build()
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        holder1.progressBar.setProgress((int) (((double) progress.currentBytes / progress.totalBytes) * 100.0) / 2);
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        DownloadThumbTask(particle, holder1, filename);
                    }

                    @Override
                    public void onError(Error error) {
                        holder1.progressBar.setVisibility(View.GONE);
                        holder1.imageViewDownload.setVisibility(View.VISIBLE);
                        ConstantUtils.showErrorLog(error.toString());
                        ConstantUtils.showToast(context, "Particle Download Failed");
                    }
                });
    }

    public void DownloadThumbTask(ParticleResponse.Particle particle, ViewHolder1 holder, String name) {

        PRDownloader.download(particle.thumb_url, ConstantUtils.GetImagePath(context), name + ".jpg")
                .setTag(particle.id)
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

                        String imagefile, particlefile;
                        imagefile = ConstantUtils.GetImagePath(context) + name + ".jpg";
                        particlefile = ConstantUtils.GetParticlePath(context) + name + ".unity3d";

                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        intent.setData(Uri.fromFile(new File(imagefile)));
                        context.sendBroadcast(intent);

                        ((ParticleActivity) context).setDownload();

                        ParticleEntity entity = new ParticleEntity(particle.id, particle.category_id, particle.theme_name, particle.prefab_name, particle.thumb_url, imagefile, particle.particle_url, particlefile);
                        particleViewModel.insert(entity);

                        holder.progressBar.setVisibility(View.GONE);
                        holder.imageViewDownload.setVisibility(View.GONE);

                        getMbitParticle(entity);

                    }

                    @Override
                    public void onError(Error error) {
                        holder.progressBar.setVisibility(View.GONE);
                        holder.imageViewDownload.setVisibility(View.VISIBLE);
                        ConstantUtils.showErrorLog(error.toString());
                        ConstantUtils.showToast(context, "Particle Download Failed");
                    }
                });
    }


    public void getMbitParticle(ParticleEntity entity) {

        JSONObject jsonAdd = new JSONObject();
        try {
            jsonAdd.put("name", "Magically");

            JSONArray catarray = new JSONArray();

            JSONObject object = new JSONObject();
            object.put("ParticleCatName", category.category_name);
            object.put("ParticalCatImg", category.icon_url);

            JSONArray partarray = new JSONArray();
            JSONObject object1 = new JSONObject();

            object1.put("UniqueIDNo", entity.getId());
            object1.put("BundlePath", entity.getParticle_path());
            object1.put("DecryptedBundlePath", entity.getParticle_path());
            object1.put("ImgPath", entity.getThumb_path());
            object1.put("ThemeName", entity.getTheme_name());
            object1.put("prefbName", entity.getPrefab_name());

            partarray.put(object1);

            object.put("ParticalInfo", partarray);
            catarray.put(object);

            jsonAdd.put("ParticalDetails", catarray);

        } catch (JSONException e) {
            ConstantUtils.showErrorLog(e.toString());
        } finally {
            UnityPlayer.UnitySendMessage("LoadJsonData", "LoadJson", jsonAdd.toString());
        }
    }


}
