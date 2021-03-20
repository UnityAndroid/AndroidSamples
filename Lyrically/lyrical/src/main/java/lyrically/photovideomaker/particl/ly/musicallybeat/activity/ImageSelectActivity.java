package lyrically.photovideomaker.particl.ly.musicallybeat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.AdmobAds;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.ConstantFilePaths;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

import com.squareup.picasso.Picasso;
import com.unity3d.player.UnityPlayer;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;

public class ImageSelectActivity extends AppCompatActivity {

    private static final String TAG = ImageSelectActivity.class.getSimpleName();

    String final_image_path = "";
    ImageView IvBackground, IvBack, IvDone, IvDelAll;
    TextView TvCounter;
    public RecyclerView albumRecycle, imageRecycle, cropImageRecycle;
    public ArrayList<String> albums = new ArrayList<>();
    public ArrayList<String> albumpaths = new ArrayList<>();
    public ArrayList<String> albumimages = new ArrayList<>();
    public ArrayList<String> images = new ArrayList<>();
    public Context context;
    DatabaseManager manager;
    public int album_position = 0, imgWidth = 0;
    Cursor SelImages;

    LinearLayout noimages;
    AlbumAdapter albumAdapter;
    ImageAdapter imageAdapter;
    CropImageAdapter cropImageAdapter;
    boolean first = false;


    int margin, margin2, imgwidth, albumwidth;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }
    public DialogLoader dialogLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        context = this;
        manager = new DatabaseManager(context);
        dialogLoader = new DialogLoader(this, "Loading Please Wait");
        margin = Constants.getPixelInteger(context, 2);
        margin2 = Constants.getPixelInteger(context, 10);

        getIDs();
        setEvents();
        doWork();

    }

    private void getIDs() {
        albumRecycle = findViewById(R.id.album_recycler);
        imageRecycle = findViewById(R.id.image_recycler);
        cropImageRecycle = findViewById(R.id.selected_images_recycler);
        IvBackground = findViewById(R.id.image_select_background);
        IvBack = findViewById(R.id.img_select_back_button);
        IvDone = findViewById(R.id.gallery_done);
        IvDelAll = findViewById(R.id.gallery_delete_all);
        TvCounter = findViewById(R.id.select_count);
        noimages=findViewById(R.id.no_images_ll);
    }

    private void setEvents() {

        IvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        IvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                first = true;
                croptask();
            }
        });

        IvDelAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.removecropimages();
                refreshSelectedImages();
            }
        });
    }

    public void showLoader() {
        if (dialogLoader != null) {
            if (dialogLoader.isShowing())
                dialogLoader.dismiss();
        }
        dialogLoader.show();

    }

    public void hideLoader() {
        if (dialogLoader != null)
            if (dialogLoader.isShowing())
                dialogLoader.dismiss();

    }
    @Override
    public void onBackPressed() {
        UnityPlayer.UnitySendMessage("SelectImage", "OnNotChangeInImage", "hello");

        super.onBackPressed();
        finish();
    }

    private void doWork() {
        showLoader();
        GlideImageLoader.SetImageResource(context, IvBackground, R.drawable.gallery_background, null);
        GlideImageLoader.SetImageResource(context, IvBack, R.drawable.all_back_button_black, null);
        imgWidth = (Constants.getScreenWidth(context) - Constants.getPixelInteger(context, 20)) / 3;
        manager.removecropimages();
        setCropRecycle();
        imageRecycle.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imageRecycle.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                imgwidth = (imageRecycle.getWidth() - (margin2 * 3)) / 2;
                albumwidth = (albumRecycle.getWidth() - (margin2 * 2));
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new GetBuckets().execute();
                    }
                }, 200);
            }
        });

    }

    public void setCropRecycle() {

        checkImages();

        if (SelImages != null)
            SelImages.close();
        SelImages = manager.getcropimages();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        cropImageRecycle.setLayoutManager(linearLayoutManager);
        cropImageAdapter = new CropImageAdapter();
        cropImageRecycle.setAdapter(cropImageAdapter);

    }

    public void checkImages() {
        Cursor cropimages = manager.getcropimages();
        TvCounter.setText(cropimages.getCount() + "/6 Selected");
        if (cropimages.getCount() > 0) {
            IvDone.setVisibility(View.VISIBLE);
            IvDelAll.setVisibility(View.VISIBLE);
        } else {
            IvDone.setVisibility(View.GONE);
            IvDelAll.setVisibility(View.INVISIBLE);
        }
        cropimages.close();
    }


    private class GetBuckets extends AsyncTask<String, Integer, String> {
        public GetBuckets() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... sUrl) {

            Cursor cursor = ImageSelectActivity.this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_display_name", "bucket_display_name", "bucket_id"}, (String) null, (String[]) null, (String) null);
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            String string = cursor.getString(cursor.getColumnIndexOrThrow("_display_name"));
                            String folder = cursor.getString(cursor.getColumnIndexOrThrow("bucket_display_name"));
                            String datapath = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                            String folderpaths = datapath.substring(0, datapath.lastIndexOf(folder + "/")) + folder + "/";
                            if (!ImageSelectActivity.this.albumpaths.contains(folderpaths)) {
                                ImageSelectActivity.this.albumimages.add(datapath);
                                ImageSelectActivity.this.albumpaths.add(folderpaths);
                                ImageSelectActivity.this.albums.add(folder);
                            }
                        } while (cursor.moveToNext());
                    }
                    if (cursor == null) {
                        return "hello";
                    }
                    cursor.close();
                    return "hello";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "hello";
                }


              /*  Cursor cursor = ImageSelectActivity.this.context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_display_name", "bucket_display_name", "bucket_id"}, (String) null, (String[]) null, (String) null);

            Uri allImagesuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID};
            Cursor cursor = context.getContentResolver().query(allImagesuri, projection, null, null, null);
            try {
                if (cursor.moveToFirst()) {
                    do {
                        // imageFolder folds = new imageFolder();
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME));
                        String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                        String datapath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));

                        //String folderpaths =  datapath.replace(name,"");
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
            return "hello";*/
        }

        @Override
        protected void onPostExecute(final String result) {
            setAlbums();
        }
    }

    public void setAlbums() {
        if (albumpaths.size() > 0) {
            album_position = 0;
        }


        albumAdapter = new AlbumAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        albumRecycle.setLayoutManager(linearLayoutManager);
        albumRecycle.setAdapter(albumAdapter);

        imageAdapter = new ImageAdapter();
        GridLayoutManager manager1 = new GridLayoutManager(context, 2);
        imageRecycle.setLayoutManager(manager1);
        imageRecycle.setAdapter(imageAdapter);

        if (albumpaths.size() > 0) {
            album_position = 0;
            noimages.setVisibility(View.GONE);
            RefreshAlBum();
        }else{
            noimages.setVisibility(View.VISIBLE);
            hideLoader();
            checkImages();
        }

    }

    public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Holder> {

        public AlbumAdapter() {
        }


        public class Holder extends RecyclerView.ViewHolder {
            TextView album_name;
            ImageView album_image;
            CardView album_card;

            public Holder(@NonNull View itemView) {
                super(itemView);
                album_name = itemView.findViewById(R.id.album_name);
                album_image = itemView.findViewById(R.id.album_image);
                album_card = itemView.findViewById(R.id.album_card);
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Holder(LayoutInflater.from(context).inflate(R.layout.item_gallery_album, viewGroup, false));
        }

        @Override
        public int getItemCount() {
            return albums.size();
        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {

            if (albums.get(position) != null) {
                holder.album_name.setText(albums.get(position).toString());
            } else {
                holder.album_name.setText("Images");
            }

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(albumwidth, albumwidth);
            lp.setMargins(margin2, 0, margin2, margin2);
            holder.album_card.setLayoutParams(lp);
            holder.album_name.setSelected(true);

            GlideImageLoader.SetImageUrl(context, holder.album_image, albumimages.get(position), DiskCacheStrategy.ALL);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (album_position != holder.getAdapterPosition()) {
                        notifyItemChanged(album_position);
                        album_position = position;
                        notifyItemChanged(album_position);
                        RefreshAlBum();
                    }
                }
            });
        }
    }

    public void RefreshAlBum() {
        new GetImagesFromBucket().execute(albumpaths.get(album_position));
    }

    private class GetImagesFromBucket extends AsyncTask<String, Integer, String> {

        public GetImagesFromBucket() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... sUrl) {
            ImageSelectActivity.this.images.clear();
            ContentResolver contentResolver = ImageSelectActivity.this.getContentResolver();
            Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_data", "_display_name"}, "_data like ? ", new String[]{"%" + sUrl[0] + "%"}, "date_added DESC");
            try {
                if (cursor.moveToFirst()) {
                    do {
                        ImageSelectActivity.this.images.add(cursor.getString(cursor.getColumnIndexOrThrow("_data")));
                    } while (cursor.moveToNext());
                }
                if (cursor == null) {
                    return "hello";
                }
                cursor.close();
                return "hello";
            } catch (Exception e) {
                e.printStackTrace();
                return "hello";
            }


       /* Uri allVideosuri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME};
            String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";

            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", new String[]{"%" + sUrl[0] + "%"}, orderBy);
                if (cursor.moveToFirst()) {
                    do {
                        images.add(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)));
                    } while (cursor.moveToNext());
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return "hello";*/
        }

        @Override
        protected void onPostExecute(final String result) {
            imageAdapter.notifyDataSetChanged();
            imageRecycle.scrollToPosition(0);
            hideLoader();
        }
    }


    public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.Holder> {
        private boolean on_attach = true;

        public ImageAdapter() {
            manager.removeanims();
        }

        public class Holder extends RecyclerView.ViewHolder {
            ImageView IvImage, IvTick;
            CardView CvMain;
            TextView TvCount;
            RelativeLayout RlCount;

            public Holder(@NonNull View itemView) {
                super(itemView);
                IvImage = itemView.findViewById(R.id.gallery_image);
                CvMain = itemView.findViewById(R.id.gallery_card);
                RlCount = itemView.findViewById(R.id.counter_rl);
                TvCount = itemView.findViewById(R.id.image_counter);
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Holder(LayoutInflater.from(context).inflate(R.layout.item_gallery_image, viewGroup, false));
        }

        @Override
        public int getItemCount() {
            return images.size();
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {

            RelativeLayout.LayoutParams lpp = new RelativeLayout.LayoutParams(imgwidth, imgwidth);
            if (position % 2 == 0) {
                lpp.setMargins(0, 0, 0, margin2);
            } else {
                lpp.setMargins(0, 0, margin2, margin2);
            }
            holder.CvMain.setLayoutParams(lpp);

            GlideImageLoader.SetImageUrl(context, holder.IvImage, images.get(position), null);

            if (manager.checkcropimages(images.get(position))) {
                holder.RlCount.setVisibility(View.VISIBLE);
                holder.TvCount.setText(manager.checkcropimageposition(images.get(position)) + "");
            } else {
                holder.RlCount.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cursor cropimages = manager.getcropimages();
                    if (cropimages.getCount() < 6) {
                        if (!manager.checkcropimages(images.get(position))) {
                            manager.insertcropimages(images.get(position), "", 0);
                            refreshSelectedImages();
                        }
                    } else {
                        if (!manager.checkcropimages(images.get(position))) {
                            manager.removelastcropimages();
                            manager.insertcropimages(images.get(position), "", 0);
                            refreshSelectedImages();
                        }
                    }
                    cropimages.close();
                }
            });

        }

    }

    public void refreshSelectedImages() {
        checkImages();

        if (SelImages != null)
            SelImages.close();
        SelImages = manager.getcropimages();
        cropImageAdapter.notifyDataSetChanged();
        imageAdapter.notifyDataSetChanged();
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
            return new Holder(LayoutInflater.from(context).inflate(R.layout.item_gallery_selected_image, parent, false));
        }

        @Override
        public int getItemCount() {
            return SelImages.getCount();
        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {
            SelImages.moveToPosition(position);

            GlideImageLoader.SetImageResource(context, holder.imgdelete, R.drawable.gallery_remove, null);
            GlideImageLoader.SetImageUrl(context, holder.imageView, SelImages.getString(SelImages.getColumnIndex("ipath")), null);
            holder.imgdelete.bringToFront();

            holder.imgdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SelImages.moveToPosition(holder.getAdapterPosition());
                    manager.removeselectedcropimages(SelImages.getInt(SelImages.getColumnIndex("id")));
                    refreshSelectedImages();
                }
            });
        }

    }

    public String getfilepath() {
        return ConstantFilePaths.GetCroppedPath(context) + System.currentTimeMillis() + ".png";
    }


    public void croptask() {
        if (first) {
            first = false;
            if (SelImages.moveToFirst()) {
                if (manager.checkcropedpimages(SelImages.getInt(0))) {
                    Uri res_url = Uri.fromFile(new File((SelImages.getString(1))));
                    UCrop.Options options = new UCrop.Options();
                    options.setToolbarColor(Color.parseColor("#0E0E0E"));
                    options.setToolbarWidgetColor(Color.parseColor("#FFFFFF"));

                    UCrop.of(res_url, Uri.parse(getfilepath()))
                            .withAspectRatio(9, 16)
                            .withOptions(options)
                            .start(ImageSelectActivity.this);
                } else {
                    croptask();
                }
            }
        } else {
            if (SelImages.moveToNext()) {
                if (manager.checkcropedpimages(SelImages.getInt(0))) {
                    Uri res_url = Uri.fromFile(new File((SelImages.getString(1))));
                    UCrop.Options options = new UCrop.Options();
                    options.setToolbarColor(Color.parseColor("#0E0E0E"));
                    options.setToolbarWidgetColor(Color.parseColor("#FFFFFF"));
                    UCrop.of(res_url, Uri.parse(getfilepath()))
                            .withAspectRatio(9, 16)
                            .withOptions(options)
                            .start(ImageSelectActivity.this);
                } else {
                    croptask();
                }
            } else {
                if (SelImages != null)
                    SelImages.close();

                SelImages = manager.getcropimages();
                Constants.android = false;
                if (SelImages.getCount() == 1) {
                    SelImages.moveToFirst();
                    if (manager.getGoogleAdStatus() == 1) {
                        AdmobAds.getInstance().displayInterstitial(context, new AdmobAds.MyCallback() {
                            @Override
                            public void callbackCall() {
                                UnityPlayer.UnitySendMessage("SelectImage", "GetSelectedImgPath", SelImages.getString(2));
                                finish();
                            }
                        });
                    } else if (manager.getFacebookAdStatus() == 1) {
                        AdmobAds.getInstance().displayfbInterstitial(context, new AdmobAds.MyCallback() {
                            @Override
                            public void callbackCall() {
                                UnityPlayer.UnitySendMessage("SelectImage", "GetSelectedImgPath", SelImages.getString(2));
                                finish();
                            }
                        });
                    } else {
                        UnityPlayer.UnitySendMessage("SelectImage", "GetSelectedImgPath", SelImages.getString(2));
                        finish();
                    }

                } else {
                    SelImages.moveToFirst();
                    do {
                        final_image_path = final_image_path.concat(SelImages.getString(2));
                        final_image_path = final_image_path.concat("?");
                    } while (SelImages.moveToNext());

                    final_image_path = final_image_path.substring(0, final_image_path.length() - 1);

                    if (manager.getGoogleAdStatus() == 1) {
                        AdmobAds.getInstance().displayInterstitial(context, new AdmobAds.MyCallback() {
                            @Override
                            public void callbackCall() {
                                UnityPlayer.UnitySendMessage("SelectImage", "GetSelectedMultipleImagePath", final_image_path);
                                finish();
                            }
                        });
                    } else if (manager.getFacebookAdStatus() == 1) {
                        AdmobAds.getInstance().displayfbInterstitial(context, new AdmobAds.MyCallback() {
                            @Override
                            public void callbackCall() {
                                UnityPlayer.UnitySendMessage("SelectImage", "GetSelectedMultipleImagePath", final_image_path);
                                finish();
                            }
                        });
                    } else {
                        UnityPlayer.UnitySendMessage("SelectImage", "GetSelectedMultipleImagePath", final_image_path);
                        finish();
                    }

                }

            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (SelImages != null)
            SelImages.close();

        GlideImageLoader.endGlideProcess(getApplicationContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            manager.updatecropimages(SelImages.getInt(0), UCrop.getOutput(data).toString().replaceAll("file://", ""));
            croptask();
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
        }
    }

}
