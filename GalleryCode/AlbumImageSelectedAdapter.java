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

public class AlbumImageSelectedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final ArrayList<ImageData> selectedImageList;

    private OnDeleteClickListener listener;

    public AlbumImageSelectedAdapter(Context context, ArrayList<ImageData> selectedImageList) {
        this.context = context;
        this.selectedImageList = selectedImageList;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.listener = listener;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.adapter_selected_photo_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Glide.with(context.getApplicationContext()).load(selectedImageList.get(position).getImagePath()).into(viewHolder.ivSelectPhotoList);
        viewHolder.ivRemovePhoto.setOnClickListener(v -> listener.onDeleteClick(selectedImageList.get(position), position));
    }

    @Override
    public int getItemCount() {
        return selectedImageList.size();
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

        AppCompatImageView ivSelectPhotoList, ivRemovePhoto;

        ViewHolder(final View itemView) {
            super(itemView);
            ivSelectPhotoList = itemView.findViewById(R.id.ivSelectPhotoList);
            ivRemovePhoto = itemView.findViewById(R.id.ivRemovePhoto);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(ImageData imageData, int position);
    }
}