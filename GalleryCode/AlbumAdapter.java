package com.codexial.photomodule.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codexial.photomodule.R;
import com.codexial.photomodule.models.FolderData;
import com.codexial.photomodule.models.ImageData;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class AlbumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<FolderData> albumList;

    private OnItemClickListener listener;

    public AlbumAdapter(Context context, ArrayList<FolderData> albumList) {
        this.context = context;
        this.albumList = albumList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_album_folder, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.@NotNull ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        ArrayList<ImageData> tempImageData = albumList.get(position).getImageData();
        Collections.sort(tempImageData);
        Glide.with(context.getApplicationContext()).load(tempImageData.get(0).getImagePath()).into(viewHolder.ivAlbumImage);
        final File file = new File(albumList.get(position).getFolderName());
        viewHolder.tvFolderName.setText(file.getName());
        viewHolder.tvImageCount.setText(String.valueOf(albumList.get(position).getImageData().size()));
        viewHolder.ivAlbumImage.setOnClickListener(v -> listener.onItemClick(albumList.get(position)));
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatImageView ivAlbumImage;
        AppCompatTextView tvFolderName, tvImageCount;

        ViewHolder(final View itemView) {
            super(itemView);
            ivAlbumImage = itemView.findViewById(R.id.ivAlbumImage);
            tvFolderName = itemView.findViewById(R.id.tvFolderName);
            tvImageCount = itemView.findViewById(R.id.tvImageCount);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(FolderData folderData);
    }
}