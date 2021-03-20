package lyrically.photovideomaker.particl.ly.musicallybeat.ui.iarrange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import java.util.Collections;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ArrangeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ItemMoveCallback.ItemTouchHelperAdapter {

    MyApplication application;
    Context context;
    ViewOutlineProvider outlineProvider;

    FrameLayout.LayoutParams params;

    public ArrangeAdapter(Context context, ViewOutlineProvider outlineProvider, FrameLayout.LayoutParams params) {
        this.context = context;
        this.params = params;
        application = MyApplication.getInstance();

        this.outlineProvider = outlineProvider;

        application.tempImages.clear();
        application.tempImages.addAll(application.appTheme.cropImages);
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition) {

        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(application.tempImages, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(application.tempImages, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(ViewHolder holder) {
    }

    @Override
    public void onRowClear(ViewHolder holder) {
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewFolderImage;
        private ConstraintLayout cardViewFolder;
        ImageView move, remove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewFolderImage = itemView.findViewById(R.id.image_main);
            cardViewFolder = itemView.findViewById(R.id.image_card);

            move = itemView.findViewById(R.id.image_move);
            remove = itemView.findViewById(R.id.image_remove);

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_arrange, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.cardViewFolder.setLayoutParams(params);

        holder1.cardViewFolder.setOutlineProvider(outlineProvider);
        holder1.cardViewFolder.setClipToOutline(true);

        holder1.move.setVisibility(View.VISIBLE);
        holder1.remove.setVisibility(View.VISIBLE);
        String path = application.tempImages.get(position).getImagepath();

        Glide.with(context).load("file://" + path)
                .placeholder(R.drawable.app_video_center_bg)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder1.imageViewFolderImage);

        holder1.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = holder1.getLayoutPosition();
                application.tempImages.remove(pos);
                notifyItemRemoved(pos);
            }
        });

        holder1.move.setOnClickListener(v -> {
            onRowSelected(holder1);
        });

    }

    @Override
    public int getItemCount() {
        return application.tempImages.size();
    }
}
