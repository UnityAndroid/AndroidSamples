package lyrically.photovideomaker.particl.ly.musicallybeat.ui.iselect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class SImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    MyApplication application;
    Context context;
    ViewOutlineProvider outlineProvider;

    public SImageAdapter(Context context, ViewOutlineProvider outlineProvider) {
        this.context = context;
        application = MyApplication.getInstance();
        this.outlineProvider = outlineProvider;
        application.tempImages.clear();
        application.tempImages.addAll(application.appTheme.cropImages);
        Collections.reverse(application.tempImages);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewFolderImage;
        private ImageView imageViewRemove;

        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewFolderImage = itemView.findViewById(R.id.image_view_selected_image);
            imageViewRemove = itemView.findViewById(R.id.image_view_remove);
            constraintLayout = itemView.findViewById(R.id.image_card);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_selected_image, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        ViewHolder holder1 = (ViewHolder) holder;
        holder1.imageViewRemove.bringToFront();

        holder1.constraintLayout.setOutlineProvider(outlineProvider);
        holder1.constraintLayout.setClipToOutline(true);

        String path = application.tempImages.get(position).getImagepath();

        Glide.with(context).load("file://" + path)
                .placeholder(R.drawable.app_video_center_bg)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder1.imageViewFolderImage);

        holder1.imageViewRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((IselectActivity) context).removeCropImage(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return application.tempImages.size();
    }
}
