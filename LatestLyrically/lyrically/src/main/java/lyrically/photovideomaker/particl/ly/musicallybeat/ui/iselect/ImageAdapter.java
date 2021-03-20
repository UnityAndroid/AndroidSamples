package lyrically.photovideomaker.particl.ly.musicallybeat.ui.iselect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.CropImage;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import io.reactivex.Observable;

public class ImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> images = new ArrayList<>();
    Context context;
    ViewOutlineProvider outlineProvider;

    FrameLayout.LayoutParams params;

    public ImageAdapter(Context context, FrameLayout.LayoutParams params, ViewOutlineProvider outlineProvider) {
        this.context = context;
        this.params = params;
        this.outlineProvider = outlineProvider;
    }

    public void setImages(List<String> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewFolderImage;
        private ConstraintLayout cardViewFolder;
        TextView imageCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFolderImage = itemView.findViewById(R.id.image_main);
            cardViewFolder = itemView.findViewById(R.id.image_card);

            imageCount = itemView.findViewById(R.id.text_crop_count);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder holder1 = (ViewHolder) holder;

        holder1.cardViewFolder.setLayoutParams(params);
        int pad = ScreenUtils.convertDPItoINT(context, 2);
        holder1.cardViewFolder.setPadding(pad, pad, pad, pad);
        holder1.cardViewFolder.setOutlineProvider(outlineProvider);
        holder1.cardViewFolder.setClipToOutline(true);

        String path = images.get(position);

        holder1.imageCount.bringToFront();
        checkCropCount(holder1.imageCount, path);

        Glide.with(context).load("file://" + path)
                .placeholder(R.drawable.app_video_center_bg)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder1.imageViewFolderImage);

        holder.itemView.setOnClickListener(view -> {
            String path1 = images.get(holder.getLayoutPosition());
            CropImage cropImage = new CropImage(MyApplication.getInstance().appTheme.cropImages.size() + 1, path1, "", 0, 0);
            ((IselectActivity) context).setCropImages(position, cropImage);
        });
    }

    public void checkCropCount(TextView textView, String path) {
        Observable.fromIterable(MyApplication.getInstance().tempImages)
                .filter(cropImage -> {
                    return cropImage.getImagepath().equalsIgnoreCase(path);
                }).toList()
                .subscribe(cropImages -> {
                    if (cropImages.size() > 0) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText(cropImages.size() + "");
                    } else
                        textView.setVisibility(View.GONE);
                });


    }


    @Override
    public int getItemCount() {
        return images.size();
    }
}


