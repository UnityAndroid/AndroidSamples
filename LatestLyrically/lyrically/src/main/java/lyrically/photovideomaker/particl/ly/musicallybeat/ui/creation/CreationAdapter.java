package lyrically.photovideomaker.particl.ly.musicallybeat.ui.creation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.CreationEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

public class CreationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CreationEntity> images = new ArrayList<>();
    Context context;
    ViewOutlineProvider outlineProvider;

    FrameLayout.LayoutParams params;
    CreationViewModel viewModel;

    public CreationAdapter(Context context, FrameLayout.LayoutParams params, ViewOutlineProvider outlineProvider) {
        this.context = context;
        this.params = params;
        this.outlineProvider = outlineProvider;
        viewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(CreationViewModel.class);
        images = viewModel.getAllCreations();
    }

    public void refresh() {

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewFolderImage, imageViewRemove, imageViewPlay;
        private ConstraintLayout cardViewFolder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFolderImage = itemView.findViewById(R.id.image_main);
            cardViewFolder = itemView.findViewById(R.id.image_card);
            imageViewRemove = itemView.findViewById(R.id.image_remove);
            imageViewPlay = itemView.findViewById(R.id.creation_play);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_creation, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder holder1 = (ViewHolder) holder;

        holder1.cardViewFolder.setLayoutParams(params);

        holder1.cardViewFolder.setOutlineProvider(outlineProvider);
        holder1.cardViewFolder.setClipToOutline(true);

        String path = images.get(position).file_path;

        Glide.with(context).load("file://" + path)
                .placeholder(R.drawable.app_video_center_bg)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder1.imageViewFolderImage);

        holder1.imageViewRemove.setOnClickListener(v -> {
            int pos = holder1.getLayoutPosition();
            if (new File(images.get(pos).file_path).exists()) {
                new File(images.get(pos).file_path).delete();
            }
            viewModel.deleteCreation(images.get(pos));
            images.remove(pos);
            notifyItemRemoved(pos);
        });
        holder1.imageViewPlay.setOnClickListener(v -> {
            int pos = holder1.getLayoutPosition();
            if (new File(images.get(pos).file_path).exists()) {
                ((CreationActivity) context).gotoVideo(images.get(pos).file_path);
            } else {
                viewModel.deleteCreation(images.get(pos));
                images.remove(pos);
                notifyItemRemoved(pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}



