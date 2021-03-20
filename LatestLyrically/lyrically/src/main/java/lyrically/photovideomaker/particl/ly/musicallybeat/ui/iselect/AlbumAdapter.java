package lyrically.photovideomaker.particl.ly.musicallybeat.ui.iselect;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.siyamed.shapeimageview.HexagonImageView;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.ImagesFolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ImagesFolder> folders;
    Context context;
    ViewOutlineProvider outlineProvider;
    ViewGroup.LayoutParams layoutParams;
    int pos = 0;

    public AlbumAdapter(Context context, List<ImagesFolder> folders, ViewOutlineProvider outlineProvider, ViewGroup.LayoutParams params) {
        this.folders = folders;
        this.context = context;
        this.outlineProvider = outlineProvider;
        this.layoutParams = params;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private HexagonImageView imageViewFolderImage, layer;
        CardView constraintLayoutMain;
        TextView textViewName, textViewSize;
        RelativeLayout linerl;
        FrameLayout.LayoutParams params;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageViewFolderImage = itemView.findViewById(R.id.image_view_folder_image);
            constraintLayoutMain = itemView.findViewById(R.id.card_view_image_folder);
            textViewName = itemView.findViewById(R.id.text_view_folder_name);
            layer = itemView.findViewById(R.id.layer);
            linerl = itemView.findViewById(R.id.rightline);
            // textViewSize = itemView.findViewById(R.id.album_size);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_album, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        ImagesFolder folder = folders.get(position);
        String path = folder.getFolder_images().get(0);

        holder1.constraintLayoutMain.setLayoutParams(layoutParams);

        if (position == pos) {
            holder1.layer.setVisibility(View.GONE);
            holder1.textViewName.setTextColor(Color.parseColor("#FFFFFF"));
            holder1.linerl.setVisibility(View.VISIBLE);
        } else {
            holder1.textViewName.setTextColor(Color.parseColor("#000000"));
            holder1.layer.setVisibility(View.GONE);
            holder1.linerl.setVisibility(View.GONE);
        }
        Glide.with(context).load("file://" + path)
                .placeholder(R.drawable.app_video_center_bg)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(holder1.imageViewFolderImage);

        holder1.textViewName.setText(folder.folder_name);
        //  holder1.textViewSize.setText(folder.folder_images.size() + " Photos");

        holder1.constraintLayoutMain.setOutlineProvider(outlineProvider);
        holder1.constraintLayoutMain.setClipToOutline(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos = position;
                ((IselectActivity) context).setImages(holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }
}
