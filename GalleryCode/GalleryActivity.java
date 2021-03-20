package com.codexial.photomodule.activities;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.codexial.photomodule.MyApplication;
import com.codexial.photomodule.R;
import com.codexial.photomodule.adapters.AlbumAdapter;
import com.codexial.photomodule.adapters.AlbumImageAdapter;
import com.codexial.photomodule.adapters.AlbumImageSelectedAdapter;
import com.codexial.photomodule.models.FolderData;
import com.codexial.photomodule.models.ImageData;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;

public class GalleryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView rvSelectedImage;
    private LinearLayout llDataLoader;
    private ArrayList<FolderData> folderData;
    private ArrayList<ImageData> imagesData, selectedImagesList;
    private AppCompatTextView tv_count, tv_title;
    private RecyclerView rvAlbum, rvAlbumImage;

    private AlbumAdapter albumAdapter;
    private AlbumImageAdapter albumImageAdapter;
    private AlbumImageSelectedAdapter albumImageSelectedAdapter;

    public static void copy(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        folderData = new ArrayList<>();
        imagesData = new ArrayList<>();
        selectedImagesList = new ArrayList<>();

        rvAlbum = findViewById(R.id.rvAlbum);
        rvAlbumImage = findViewById(R.id.rvAlbumImage);
        rvSelectedImage = findViewById(R.id.rvSelectedImage);
        LinearLayout llBottomLayout = findViewById(R.id.llBottomLayout);
        llDataLoader = findViewById(R.id.llDataLoader);

        tv_count = findViewById(R.id.tvImageCounter);
        tv_count.setText(String.format("Selected Images(%s/%s)", selectedImagesList.size(), 9));
        tv_title = findViewById(R.id.tvTitle);
        tv_title.setText(R.string.gallery);

        if (MyApplication.typeClick.equalsIgnoreCase("edit") || MyApplication.typeClick.equalsIgnoreCase("gridChange")) {
            llBottomLayout.setVisibility(View.GONE);
        } else {
            llBottomLayout.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.ivBack).setOnClickListener(view -> onBackPressed());

        findViewById(R.id.tvNext).setOnClickListener(view -> {
            if (selectedImagesList.size() >= 2) {
                startCrop();
            } else {
                Toast.makeText(getApplicationContext(), "Select minimum 2 photos", Toast.LENGTH_SHORT).show();
            }
        });

        albumAdapter = new AlbumAdapter(this, folderData);
        rvAlbum.setAdapter(albumAdapter);
        albumAdapter.setOnItemClickListener(folderData -> {
            tv_title.setText(new File(folderData.getFolderName()).getName());
            rvAlbumImage.setVisibility(View.VISIBLE);
            rvAlbum.setVisibility(View.GONE);
            imagesData.clear();
            ArrayList<ImageData> tempImageData = folderData.getImageData();
            Collections.sort(tempImageData);
            imagesData.addAll(tempImageData);
            albumImageAdapter.notifyDataSetChanged();
        });

        albumImageAdapter = new AlbumImageAdapter(this, imagesData);
        rvAlbumImage.setAdapter(albumImageAdapter);
        albumImageAdapter.setOnItemClickListener((imageData, position) -> {
            if (MyApplication.typeClick.equalsIgnoreCase("edit")) {
                Intent intent = new Intent(getApplicationContext(), EditImageActivity.class);
                intent.putExtra("Single_Image_Select", imageData.getImagePath());
                startActivity(intent);
            } else if (MyApplication.typeClick.equalsIgnoreCase("gridChange")) {
                CollageMakerActivity.getInstance().replaceCurrentPiece(imageData.getImagePath());
                finish();
            } else {
                if (selectedImagesList.size() >= 9) {
                    Toast.makeText(getApplicationContext(), "Maximum 9 photos selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                imageData.setSelected(true);
                albumImageAdapter.notifyItemChanged(position, imageData);

                selectedImagesList.add(imageData);
                albumImageSelectedAdapter.notifyDataSetChanged();
                rvSelectedImage.smoothScrollToPosition(selectedImagesList.size());

                tv_count.setText(String.format("Selected Images(%s/%s)", selectedImagesList.size(), 9));
            }
        });

        albumImageSelectedAdapter = new AlbumImageSelectedAdapter(this, selectedImagesList);
        rvSelectedImage.setAdapter(albumImageSelectedAdapter);
        albumImageSelectedAdapter.setOnDeleteClickListener((imageData, position) -> {
            selectedImagesList.remove(position);
            tv_count.setText(String.format("Selected Images(%s/%s)", selectedImagesList.size(), 9));

            if (!hasImageSelectAlready(imageData)) {
                imageData.setSelected(false);
                int pos = getImageSelectAlready(imageData);
                if (pos != -1) {
                    imagesData.set(pos, imageData);
                    albumImageAdapter.notifyItemChanged(pos);
                }
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean hasImageSelectAlready(ImageData imageData) {
        for (int i = 0; i < selectedImagesList.size(); i++) {
            if (imageData.getImagePath().equalsIgnoreCase(selectedImagesList.get(i).getImagePath())) {
                return true;
            }
        }

        return false;
    }

    private int getImageSelectAlready(ImageData imageData) {
        for (int i = 0; i < imagesData.size(); i++) {
            if (imageData.getImagePath().equalsIgnoreCase(imagesData.get(i).getImagePath())) {
                return i;
            }
        }

        return -1;
    }

    private void startCrop() {
        ArrayList<String> selectedImagePath = new ArrayList<>();
        for (ImageData imageData : selectedImagesList) {
            selectedImagePath.add(imageData.getImagePath());
        }
        if (MyApplication.typeClick.equalsIgnoreCase("grid")) {
            Intent intent = new Intent(this, CollageMakerActivity.class);
            intent.putExtra("selectedImages", new Gson().toJson(selectedImagePath));
            startActivity(intent);
            finish();
        } else if (MyApplication.typeClick.equalsIgnoreCase("freestyle")) {
            Intent intent = new Intent(this, FreeStyleActivity.class);
            intent.putExtra("selectedImages", new Gson().toJson(selectedImagePath));
            startActivity(intent);
        } else if (MyApplication.typeClick.equalsIgnoreCase("picsew")) {
            Intent intent = new Intent(this, PicSewActivity.class);
            intent.putExtra("selectedImages", new Gson().toJson(selectedImagePath));
            startActivity(intent);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        llDataLoader.setVisibility(View.VISIBLE);

        String[] projection = {
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.DATE_ADDED,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE
        };

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

        Uri queryUri = MediaStore.Files.getContentUri("external");

        return new CursorLoader(
                this,
                queryUri,
                projection,
                selection,
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                folderData.clear();
                int int_position = 0;
                String absolutePathOfImage;
                int column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                boolean boolean_folder = false;
                while (cursor.moveToNext()) {
                    try {
                        absolutePathOfImage = cursor.getString(column_index_data);
                        File folderPath = new File(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))).getParentFile();
                        for (int i = 0; i < folderData.size(); i++) {
                            assert folderPath != null;
                            if (folderData.get(i).getFolderName().equals(folderPath.getAbsolutePath())) {
                                boolean_folder = true;
                                int_position = i;
                                break;
                            } else {
                                boolean_folder = false;
                            }
                        }
                        if (boolean_folder) {
                            ImageData imageData1 = new ImageData();
                            imageData1.setImagePath(absolutePathOfImage);
                            imageData1.setDate(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)));
                            imageData1.setSelected(false);
                            imageData1.setCount(0);
                            ArrayList<ImageData> imageData = new ArrayList<>(folderData.get(int_position).getImageData());
                            imageData.add(imageData1);
                            folderData.get(int_position).setImageData(imageData);
                        } else {
                            ArrayList<ImageData> imageData = new ArrayList<>();
                            ImageData imageData1 = new ImageData();
                            imageData1.setImagePath(absolutePathOfImage);
                            imageData1.setDate(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)));
                            imageData1.setSelected(false);
                            imageData1.setCount(0);
                            imageData.add(imageData1);
                            FolderData obj_model = new FolderData();
                            if (folderPath != null) {
                                obj_model.setFolderName(folderPath.getAbsolutePath());
                            }
                            obj_model.setDate(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)));
                            obj_model.setImageData(imageData);
                            folderData.add(obj_model);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Collections.sort(folderData);
                tv_title.setText(R.string.gallery);
                rvAlbumImage.setVisibility(View.GONE);
                rvAlbum.setVisibility(View.VISIBLE);
                llDataLoader.setVisibility(View.GONE);
                albumAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void onBackPressed() {
        if (rvAlbum.getVisibility() == View.VISIBLE) {
            super.onBackPressed();
            finish();
        } else {
            try {
                tv_title.setText(R.string.gallery);
                rvAlbumImage.setVisibility(View.GONE);
                rvAlbum.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}