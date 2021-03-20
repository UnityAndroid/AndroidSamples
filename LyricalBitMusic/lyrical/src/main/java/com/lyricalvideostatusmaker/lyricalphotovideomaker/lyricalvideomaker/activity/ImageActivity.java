package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.unity3d.player.UnityPlayer;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.AndroidPlugin;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.Constants;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.DatabaseManager;

public class ImageActivity extends AppCompatActivity {

    public boolean iimage = false, first = false;
    public Cursor selImages, cropimages;
    public DatabaseManager manager;
    public ImageView img_check, back;
    public String imagepath = "", albumname = "";
    public RecyclerView albumRecycler;
    public RecyclerView imageRecycler;
    public Context context;
    public ArrayList<String> albums = new ArrayList<>();
    public ArrayList<String> albumpaths = new ArrayList<>();
    public ArrayList<String> albumimages = new ArrayList<>();
    public ArrayList<String> images = new ArrayList<>();
    public AlbumAdapter albumAdapter;
    public ImageAdapter imageAdapter;
    public DisplayMetrics dm;
    public int margin, margin2, imgwidth, album_position = 00;
    public RecyclerView recyclerView;
    public CropImageAdapter adapter;
    public int imageid = 0;
    public ImageView txtTitle;
    public Typeface typeface;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_image);

        context = this;

        dm = getResources().getDisplayMetrics();
        Constants.initializeDialog(context, "Fetching Images Plz Wait");
        margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, dm);
        margin2 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, dm);

        imgwidth = (dm.widthPixels - (margin * 6)) / 3;
        manager = new DatabaseManager(context);
        txtTitle = findViewById(R.id.txtTitle);
        albumRecycler = findViewById(R.id.album_recycler);
        imageRecycler = findViewById(R.id.image_recycler);
        recyclerView = findViewById(R.id.selected_images_recycler);
        LinearLayoutManager managerr = new LinearLayoutManager(context);
        managerr.setOrientation(RecyclerView.HORIZONTAL);
        recyclerView.setLayoutManager(managerr);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/merriweather.ttf");

        selImages = manager.getcropimages();
        adapter = new CropImageAdapter();
        recyclerView.setAdapter(adapter);

        img_check = findViewById(R.id.img_done);
        back = findViewById(R.id.img_select_back);
        manager.removeanims();

        img_check.setVisibility(View.GONE);


        img_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropimages = manager.getcropimages();
                first = true;
                croptask();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        LinearLayout creation_banner = (LinearLayout) findViewById(R.id.creation_banner);
        AdMobLoadAds.getInstance().loadBanner(ImageActivity.this, creation_banner);

        permission();
    }

    public void permission() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            new GetBuckets().execute();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permission();
                } else {
                    permission();
                }
                return;
            }
        }
    }

    private class GetBuckets extends AsyncTask<String, Integer, String> {
        public GetBuckets() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Constants.showLoader();
        }

        @Override
        protected void onPostExecute(final String result) {
            setAlbums();
        }

        @Override
        protected String doInBackground(String... sUrl) {

            Uri allImagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
            Cursor cursor = context.getContentResolver().query(allImagesuri, projection, null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                        String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                        String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                        String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/"));
                        folderpaths = folderpaths + folder + "/";
                        if (!albumpaths.contains(folderpaths)) {
                            albumimages.add(datapath);
                            albumpaths.add(folderpaths);
                            albums.add(folder);
                        }
                    } while (cursor.moveToNext());
                }
                if (cursor != null)
                    cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "hello";
        }

    }

    public void ShowAlbums() {
        iimage = false;
        imageRecycler.setVisibility(View.GONE);
        albumRecycler.setVisibility(View.VISIBLE);
    }

    public void ShowImages() {
        iimage = true;
        albumRecycler.setVisibility(View.GONE);
        imageRecycler.setVisibility(View.VISIBLE);
    }

    private class GetImagesFromBucket extends AsyncTask<String, Integer, String> {

        public GetImagesFromBucket() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(final String result) {
            manager.removeanims();
            imageAdapter.notifyDataSetChanged();
            imageRecycler.scrollToPosition(0);
            ShowImages();
        }

        @Override
        protected String doInBackground(String... sUrl) {
            images.clear();
            Uri allVideosuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME};
            String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";

            Cursor cursor = getContentResolver().query(allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + sUrl[0] + "%"}, orderBy);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        images.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                    } while (cursor.moveToNext());
                }
                if (cursor != null)
                    cursor.close();

            } catch (Exception e) {
                e.printStackTrace();
            }


            return "hello";
        }

    }

    public void checkimage() {
        if (selImages.getCount() > 0) {
            img_check.setVisibility(View.VISIBLE);
        } else {
            img_check.setVisibility(View.GONE);
        }
    }

    /*@Override
    public void onBackPressed() {
        UnityPlayer.UnitySendMessage("SelectImage", "OnNotChangeInImage", "hello");
        super.onBackPressed();
        finish();
    }*/

    @Override
    public void onBackPressed() {
        if (iimage) {
            ShowAlbums();
        } else {
            UnityPlayer.UnitySendMessage("SelectImage", "OnNotChangeInImage", "hello");
            super.onBackPressed();
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    public void setAlbums() {

        ShowAlbums();

        albumAdapter = new AlbumAdapter();
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        albumRecycler.setLayoutManager(manager);
        albumRecycler.setAdapter(albumAdapter);

        imageAdapter = new ImageAdapter();
        GridLayoutManager manager1 = new GridLayoutManager(context, 3);
        imageRecycler.setLayoutManager(manager1);
        imageRecycler.setAdapter(imageAdapter);

        Constants.hideLoader();
    }


    public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Holder> {

        public AlbumAdapter() {
        }


        public class Holder extends RecyclerView.ViewHolder {
            TextView album_name;
            ImageView album_image;

            public Holder(@NonNull View itemView) {
                super(itemView);
                album_name = itemView.findViewById(R.id.album_video_name);
                album_image = itemView.findViewById(R.id.album_thumb_image);
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Holder(LayoutInflater.from(context).inflate(R.layout.item_album_name, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int i) {

            holder.album_name.setTypeface(typeface, Typeface.NORMAL);

            if (albums.get(i) != null) {
                holder.album_name.setText(albums.get(i).toString());
            } else {
                holder.album_name.setText("Images");
            }

            Glide.with(context).load(albumimages.get(i)).into(holder.album_image);

            holder.album_name.setSelected(true);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = i;
                    albumname = albums.get(position);
                    notifyItemChanged(album_position);
                    album_position = position;
                    notifyItemChanged(album_position);
                    Refresh();
                }
            });
        }

        @Override
        public int getItemCount() {
            return albums.size();
        }
    }

    public void Refresh() {
        new GetImagesFromBucket().execute(albumpaths.get(album_position));
    }

    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Holder> {
        private boolean on_attach = true;
        long DURATION = 500;

        public ImageAdapter() {
        }


        public class Holder extends RecyclerView.ViewHolder {
            ImageView imageView;
            ImageView tick;
            CardView rl, midrl;

            public Holder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.album_image);
                rl = itemView.findViewById(R.id.gallery_rl);
            }
        }

        private void setAnimation(View itemView, int i) {
            manager.insertanims(i);
            if (!on_attach) {
                i = -1;
            }
            boolean isNotFirstItem = i == -1;
            i++;
            itemView.setAlpha(0.f);
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator animator = ObjectAnimator.ofFloat(itemView, "alpha", 0.f, 0.5f, 1.0f);
            ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
            animator.setDuration(500);
            animatorSet.play(animator);
            animator.start();
        }


        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Holder(LayoutInflater.from(context).inflate(R.layout.item_gallery, viewGroup, false));
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    on_attach = false;
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });

            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int i) {

            Glide.with(context).load(images.get(i)).into(holder.imageView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = i;
                    Cursor cropimages = manager.getcropimages();
                    if (cropimages.getCount() < 6) {
                        if (!manager.checkcropimages(images.get(pos))) {
                            manager.insertcropimages(images.get(pos), "", 0);
                            refresh();
                        }
                    } else {
                        if (!manager.checkcropimages(images.get(pos))) {
                            manager.removelastcropimages();
                            manager.insertcropimages(images.get(pos), "", 0);
                            refresh();
                        }
                    }
                    cropimages.close();
                }
            });

        }

        @Override
        public int getItemCount() {
            return images.size();
        }
    }


    public String getfilepath() {
        imagepath = AndroidPlugin.GetCroppedPath() + System.currentTimeMillis() + ".png";
        return imagepath;
    }

    public void refresh() {
        if (selImages != null)
            selImages.close();

        selImages = manager.getcropimages();
        checkimage();
        adapter.notifyDataSetChanged();
    }

    public class CropImageAdapter extends RecyclerView.Adapter<CropImageAdapter.Holder> {


        public CropImageAdapter() {
        }

        public class Holder extends RecyclerView.ViewHolder {
            ImageView imageView, imgdelete;

            public Holder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.selected_image);
                imgdelete = itemView.findViewById(R.id.img_delete);
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(context).inflate(R.layout.item_image, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {
            selImages.moveToPosition(position);

            Glide.with(context).load(R.drawable.close).into(holder.imgdelete);
            Glide.with(context).load(selImages.getString(1)).into(holder.imageView);

            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = position;
                    selImages.moveToPosition(pos);
                    manager.removeselectedcropimages(selImages.getInt(0));
                    refresh();
                }
            });
        }

        @Override
        public int getItemCount() {
            return selImages.getCount();
        }
    }

    public void croptask() {
        if (first) {
            first = false;
            if (cropimages.moveToFirst()) {
                if (manager.checkcropedpimages(cropimages.getInt(0))) {
                    imageid = cropimages.getInt(0);
                    Uri res_url = Uri.fromFile(new File((cropimages.getString(1))));
                    UCrop.Options options = new UCrop.Options();
                    options.setToolbarColor(Color.parseColor("#0E0E0E"));
                    options.setToolbarWidgetColor(Color.parseColor("#FFFFFF"));

                    UCrop.of(res_url, Uri.parse(getfilepath()))
                            .withAspectRatio(9, 16)
                            .withOptions(options)
                            .start(ImageActivity.this, imageid);
                } else {
                    croptask();
                }
            }
        } else {
            if (cropimages.moveToNext()) {
                if (manager.checkcropedpimages(cropimages.getInt(0))) {
                    imageid = cropimages.getInt(0);
                    Uri res_url = Uri.fromFile(new File((cropimages.getString(1))));
                    UCrop.Options options = new UCrop.Options();
                    options.setToolbarColor(Color.parseColor("#0E0E0E"));
                    options.setToolbarWidgetColor(Color.parseColor("#FFFFFF"));
                    UCrop.of(res_url, Uri.parse(getfilepath()))
                            .withAspectRatio(9, 16)
                            .withOptions(options)
                            .start(ImageActivity.this, imageid);
                } else {
                    croptask();
                }
            } else {
                if (cropimages != null)
                    cropimages.close();

                cropimages = manager.getcropimages();

                AdMobLoadAds.getInstance().displayInterstitial(ImageActivity.this, new AdMobLoadAds.MyCallback() {
                    @Override
                    public void callbackCall() {
                        if (cropimages.getCount() == 1) {
                            cropimages.moveToFirst();
                            UnityPlayer.UnitySendMessage("SelectImage", "GetSelectedImgPath", cropimages.getString(2));
                        } else {
                            cropimages.moveToFirst();
                            String imagepath = "";
                            do {
                                imagepath = imagepath.concat(cropimages.getString(2));
                                imagepath = imagepath.concat("?");
                            } while (cropimages.moveToNext());

                            imagepath = imagepath.substring(0, imagepath.length() - 1);
                            UnityPlayer.UnitySendMessage("SelectImage", "GetSelectedMultipleImagePath", imagepath);
                        }

                        onBackPressed();
                        finish();
                    }
                });

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (selImages != null)
            selImages.close();

        if (cropimages != null)
            cropimages.close();

        Glide.with(getApplicationContext()).pauseRequests();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            manager.updatecropimages(requestCode, UCrop.getOutput(data).toString().replaceAll("file://", ""));
            croptask();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

}
