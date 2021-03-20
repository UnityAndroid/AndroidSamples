package lyrically.photovideomaker.particl.ly.musicallybeat.ui.iselect;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogSize;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.ActionListeners;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.ViewToImage;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.cropper.CropImageView;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.imagecropper.view.ImageCropView;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.CropImage;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.icrop.IcropActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ConstantUtils;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ScreenUtils;

import java.io.File;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class IselectActivity extends AppCompatActivity {

    Context context;
    MyApplication application;
    ImageView backImageView;
    ImageCropView imageCropView;

    ImageView imageViewBack, imageViewDone;
    TextView textViewTitle, textViewDone;

    RecyclerView recyclerViewAlbums;
    AlbumAdapter albumAdapter;

    RecyclerView recyclerViewImages;
    ImageAdapter imageAdapter;

    RecyclerView recyclerViewCrops;
    SImageAdapter sImageAdapter;
    TextView textViewCropCount;
    boolean isFolders = true;

    public String size = "S";
    public int sizeHeight = 1, sizeWidth = 1;
    public int auto = 0;

    ConstraintLayout nodata;

    int position = 0;
    RelativeLayout relativeLayout;
    ConstraintLayout imageconstraint;
    ImageView imageViewCrop, imageViewBlur;

    public Bitmap blurBitmap, normBitmap;
    public Bitmap mainBitmap;
    public String mainPath;
    public File mainFile;
    CardView dialogloder;
    TextView textView;
    CropImageView cropImageView, cropImageView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iselect);
        preWork();
        getIDs();
        setEvents();
        postWork();
    }

    public void preWork() {
        application = MyApplication.getInstance();
        context = this;
    }

    public void getIDs() {
        imageViewBack = findViewById(R.id.tool_left_icon);
        textViewTitle = findViewById(R.id.tool_title);
        textViewDone = findViewById(R.id.tool_right_icon);
        imageViewDone = findViewById(R.id.donebtn);
        recyclerViewAlbums = findViewById(R.id.recycler_view_imagefolders);
        recyclerViewImages = findViewById(R.id.recycler_view_images);

        recyclerViewCrops = findViewById(R.id.recycler_view_crop_images);
        textViewCropCount = findViewById(R.id.crop_count);
        dialogloder = findViewById(R.id.dialogloader2);

        nodata = findViewById(R.id.no_data);
        textViewTitle.setText(context.getResources().getText(R.string.title_iselect));
        textViewDone.setVisibility(View.GONE);

        cropImageView = findViewById(R.id.cropImageView);
        cropImageView2 = findViewById(R.id.cropImageView2);

        backImageView = findViewById(R.id.background);

        relativeLayout = findViewById(R.id.crop_image_rl);
        imageconstraint = findViewById(R.id.image_constraint);
        imageViewCrop = findViewById(R.id.crop_image_normal);
        imageViewBlur = findViewById(R.id.crop_image_blur);
        textView = findViewById(R.id.wait_text);
    }

    public void setEvents() {
        imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });

        imageViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSize dialogSize = new DialogSize(context);
                dialogSize.show();
            }
        });
    }


    public void gotoCrop(int auto) {
        if (auto == 0) {
            Intent intent = new Intent(context, IcropActivity.class);
            intent.putExtra("sizeHeight", sizeHeight);
            intent.putExtra("sizeWidth", sizeWidth);
            intent.putExtra("auto", auto);
            intent.putExtra("size", size);
            startActivityForResult(intent, AndroidPlugin.CROP_ACTIVITY);
        } else {

            iniatiecrop();
            showLoader();
            setMessage("Loading Photos Please Wait");
            startAutoCropping();
        }
    }

    public void showLoader() {
        dialogloder.bringToFront();
        dialogloder.setVisibility(View.VISIBLE);
    }

    public void hideLoader() {
        dialogloder.setVisibility(View.GONE);
    }

    public void setMessage(String data) {
        textView.setText(data);
    }

    public void postWork() {
        setAlbums();
    }


    public void setSize(int sizeWidth, int sizeHeight, String size) {
        this.sizeHeight = sizeHeight;
        this.sizeWidth = sizeWidth;
        this.size = size;
    }

    public void setAuto(int auto) {
        this.auto = auto;
    }

    public void setAlbums() {
        Glide.with(context).load(R.drawable.all_background).into(backImageView);

        recyclerViewAlbums.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewAlbums.setHasFixedSize(true);

        ViewOutlineProvider mViewOutlineProvider2 = new ViewOutlineProvider() {
            @Override
            public void getOutline(final View view, final Outline outline) {
                int left = 0;
                int top = 0;
                int right = view.getWidth();
                int bottom = view.getHeight();
                float cornerRadiusDP = 1f;
                int cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerRadiusDP, context.getResources().getDisplayMetrics());

                outline.setRoundRect(left, top, right, bottom, cornerRadius);
            }
        };

        int widthint1 = (ScreenUtils.getScreenWidth(context) - ScreenUtils.convertDPItoINT(context, 20)) / 3;
        FrameLayout.LayoutParams params1 = new FrameLayout.LayoutParams(widthint1, widthint1);
        params1.setMargins(0, 0, 0, ScreenUtils.convertDPItoINT(context, 10));


        int widthint = (ScreenUtils.getScreenWidth(context) - ScreenUtils.convertDPItoINT(context, 100)) / 3;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthint, widthint);
        params.setMargins(0, 0, ScreenUtils.convertDPItoINT(context, 10), ScreenUtils.convertDPItoINT(context, 10));

        albumAdapter = new AlbumAdapter(context, application.imageFolders, mViewOutlineProvider2, params1);
        recyclerViewAlbums.setAdapter(albumAdapter);

        if (albumAdapter.getItemCount() > 0) {
            nodata.setVisibility(View.GONE);
        }

        recyclerViewImages.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerViewImages.setHasFixedSize(true);

        imageAdapter = new ImageAdapter(context, params, mViewOutlineProvider2);
        recyclerViewImages.setAdapter(imageAdapter);
        imageAdapter.setImages(application.imageFolders.get(position).getFolder_images());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewCrops.setLayoutManager(linearLayoutManager);
        recyclerViewCrops.setHasFixedSize(true);

        sImageAdapter = new SImageAdapter(context, mViewOutlineProvider2);
        recyclerViewCrops.setAdapter(sImageAdapter);

        showAlbums();
        cropCount();
    }

    public void setImages(int position) {
        isFolders = false;
        imageAdapter.setImages(application.imageFolders.get(position).getFolder_images());
        albumAdapter.notifyDataSetChanged();
        // recyclerViewAlbums.setVisibility(View.GONE);
        //  recyclerViewImages.setVisibility(View.VISIBLE);
    }

    public void showAlbums() {
        isFolders = true;
        recyclerViewImages.setVisibility(View.VISIBLE);
        recyclerViewAlbums.setVisibility(View.VISIBLE);
    }

    public void cropCount() {
        textViewCropCount.setText("Selected Images (" + application.tempImages.size() + ")");
        if (application.tempImages.size() > 0)
            imageViewDone.setVisibility(View.VISIBLE);
        else
            imageViewDone.setVisibility(View.INVISIBLE);
    }

    public void setCropImages(int position, CropImage images) {
        Collections.reverse(application.tempImages);
        application.tempImages.add(images);
        Collections.reverse(application.tempImages);
        cropCount();
        imageAdapter.notifyItemChanged(position);
        sImageAdapter.notifyDataSetChanged();
    }

    public void removeCropImage(int position) {
        if (application.tempImages.size() > position) {
            application.tempImages.remove(position);
            sImageAdapter.notifyDataSetChanged();
            imageAdapter.notifyDataSetChanged();
            cropCount();
        }
    }

    @Override
    public void onBackPressed() {
        if (isFolders) {
            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_CANCELED, returnIntent);
            finish();
        } else
            showAlbums();
    }

    public String getImages() {
        List<CropImage> cropImages = MyApplication.getInstance().appTheme.cropImages;
        String final_image_path = "";
        for (int i = 0; i < cropImages.size(); i++) {
            final_image_path = final_image_path.concat(cropImages.get(i).getCroppath());
            final_image_path = final_image_path.concat("?");
        }
        final_image_path = final_image_path.substring(0, final_image_path.length() - 1);
        return final_image_path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AndroidPlugin.CROP_ACTIVITY) {
                String message = data.getStringExtra("MESSAGE");

                assert message != null;
                if (message.equalsIgnoreCase("YES")) {

                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", "YES");
                    intent.putExtra("IMAGECOUNT", MyApplication.getInstance().appTheme.cropImages.size());
                    intent.putExtra("final_image_path", getImages());

                    setResult(Activity.RESULT_OK, intent);
                    finish();

                }
            }

        }
    }


    public void iniatiecrop() {
        cropImageView.setAspectRatio(sizeWidth, sizeHeight);
        cropImageView.setFixedAspectRatio(true);

        if (size.equalsIgnoreCase("P") || size.equalsIgnoreCase("L"))
            cropImageView2.setAspectRatio(1, 1);
        else
            cropImageView2.setAspectRatio(9, 16);
        cropImageView2.setFixedAspectRatio(true);

        FrameLayout.LayoutParams lp;
        if (size.equalsIgnoreCase("S")) {
            lp = new FrameLayout.LayoutParams(1080, 1080);
        } else if (size.equalsIgnoreCase("P")) {
            lp = new FrameLayout.LayoutParams(720, 1280);
        } else {
            lp = new FrameLayout.LayoutParams(1280, 720);
        }
        relativeLayout.setLayoutParams(lp);

    }

    public void startAutoCropping() {

        if (position < application.tempImages.size()) {

            setMessage("Loading Photos.. " + (position + 1) + "/" + application.tempImages.size());
            textViewTitle.setText("Crop Images (" + (position + 1) + "/" + application.tempImages.size() + ")");

            String path = "file://" + application.tempImages.get(position).imagepath;
            ImageLoader.getInstance().loadImage(path.replaceAll(" ", "\\ "), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                    mainBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
                    getblurrybitmap(mainBitmap);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                    ConstantUtils.showErrorLog(failReason.getCause().getMessage() + "error");
                }
            });

        } else {
            hideLoader();
            doneCrop();
        }
    }

    public void getblurrybitmap(Bitmap bitmap) {
        cropImageView.clearImage();
        cropImageView.setOnCropImageCompleteListener((view, result) -> {
            blurBitmap = result.getBitmap();
            Glide.with(context).asBitmap().load(blurBitmap)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1)))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageViewBlur.setImageBitmap(resource);
                            setBitmap();
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });

        });

        cropImageView.setmOnSetImageBitmapCompleteListener(new CropImageView.OnSetImageBitmapCompleteListener() {
            @Override
            public void onSetImageUriComplete(CropImageView view, Bitmap bitmap, Exception error) {
                cropImageView.getCroppedImageAsync();
            }
        });

        imageViewCrop.setImageBitmap(bitmap);
        cropImageView.setImageBitmap(bitmap);
    }


    public void getfirstblurrybitmap() {
        cropImageView.clearImage();
        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                ConstantUtils.showErrorLog("check 2");
                if (!result.isSuccessful()) {
                    ConstantUtils.showErrorLog("err" + result.getError().getMessage());
                }
                blurBitmap = result.getBitmap();
                Glide.with(context).asBitmap().load(blurBitmap)
                        .apply(RequestOptions.bitmapTransform(new BlurTransformation(25, 1)))
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                ConstantUtils.showErrorLog("check 3");
                                imageViewBlur.setImageBitmap(resource);
                                getfirstnormalbitmap();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }

                        });

            }
        });

        cropImageView.setOnSetImageUriCompleteListener(new CropImageView.OnSetImageUriCompleteListener() {
            @Override
            public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
                ConstantUtils.showErrorLog("check 1");
                cropImageView.getCroppedImageAsync();
            }
        });

        cropImageView.setImageUriAsync(Uri.parse("file://" + application.tempImages.get(position).getImagepath()));
    }

    public void getfirstnormalbitmap() {
        cropImageView2.clearImage();
        cropImageView2.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                ConstantUtils.showErrorLog("check 5");
                normBitmap = result.getBitmap();
                imageViewCrop.setImageBitmap(normBitmap);
                setBitmap();
            }
        });

        cropImageView2.setOnSetImageUriCompleteListener(new CropImageView.OnSetImageUriCompleteListener() {
            @Override
            public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
                ConstantUtils.showErrorLog("check 4");
                cropImageView2.getCroppedImageAsync();
            }
        });

        cropImageView2.setImageUriAsync(Uri.parse("file://" + application.tempImages.get(position).getImagepath()));
    }

    public void setBitmap() {
        ConvertToFile();
    }

    @SuppressLint("CheckResult")
    public void ConvertToFile() {
        new ViewToImage(context, relativeLayout, new ActionListeners() {
            @Override
            public void convertedWithSuccess(Bitmap bitmap, String filePath) {
                application.tempImages.get(position).setCroppath(filePath);
                position++;
                startAutoCropping();
            }

            @Override
            public void convertedWithError(String error) {
                Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void doneCrop() {

        application.getCropImages().clear();

        for (int i = 0; i < application.tempImages.size(); i++) {
            CropImage image = application.tempImages.get(i);
            image.setPosition(i);
            application.getCropImages().add(image);
        }

        application.getTempImages().clear();
        application.appTheme.image_size = size;
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "YES");
        intent.putExtra("final_image_path", getImages());
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

}