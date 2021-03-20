package com.codexial.photomodule.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codexial.photomodule.R;
import com.codexial.photomodule.models.ImageData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class AlbumImageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<ImageData> albumImageList;

    private OnItemClickListener listener;

    public AlbumImageAdapter(Context context, ArrayList<ImageData> albumImageList) {
        this.context = context;
        this.albumImageList = albumImageList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_photo_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(context.getApplicationContext()).load(albumImageList.get(position).getImagePath()).into(viewHolder.ivSelectPhoto);

        if (albumImageList.get(position).getSelected()) {
            viewHolder.ivSelectedTick.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivSelectedTick.setVisibility(View.GONE);
        }

        viewHolder.ivSelectPhoto.setOnClickListener(v -> listener.onItemClick(albumImageList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return albumImageList.size();
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

        AppCompatImageView ivSelectPhoto, ivSelectedTick;

        ViewHolder(final View itemView) {
            super(itemView);
            ivSelectPhoto = itemView.findViewById(R.id.ivSelectPhoto);
            ivSelectedTick = itemView.findViewById(R.id.ivSelectedTick);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(ImageData imageData, int position);
    }
}
