package lyrically.photovideomaker.particl.ly.musicallybeat.ui.icrop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.imagecropper.view.ImageCropView;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.CropImage;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ConstantUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;

public class IcropActivity extends AppCompatActivity {

    ImageView backImageView;
    ImageCropView imageCropView;
    Context context;
    MyApplication application;

    ImageView imageViewBack;
    TextView textViewTitle, textViewDone;

    int position = 0, auto = 0;

    public int sizeHeight = 1, sizeWidth = 1;
    public String size = "";
    public DialogLoader dialogLoader;

    public void initializeDialog(Context context, String msg) {
        dialogLoader = new DialogLoader(context, msg);
    }

    public void showLoader() {
        dialogLoader.show();
    }

    public void hideLoader() {
        if (dialogLoader != null)
            if (dialogLoader.isShowing())
                dialogLoader.dismiss();
    }

    public void setMessage(String message) {
        dialogLoader.setText(message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icrop);

        preWork();
        getIDs();
        setEvents();
        postWork();

    }

    public void preWork() {

        if (getIntent() != null && getIntent().hasExtra("sizeHeight")) {
            sizeHeight = getIntent().getIntExtra("sizeHeight", 1);
            sizeWidth = getIntent().getIntExtra("sizeWidth", 1);
            auto = getIntent().getIntExtra("auto", 0);
            size = getIntent().getStringExtra("size");
        }

        application = MyApplication.getInstance();
        context = this;
        initializeDialog(context, "Loading Please Wait");

    }

    public void getIDs() {
        imageViewBack = findViewById(R.id.tool_left_icon);
        textViewTitle = findViewById(R.id.tool_title);
        textViewDone = findViewById(R.id.tool_right_icon);
        imageCropView = findViewById(R.id.imageCropView);
        backImageView = findViewById(R.id.background);
    }

    public void setEvents() {
        imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });

        textViewDone.setOnClickListener(v -> {
            showLoader();
            setMessage("Cropping Image " + (position + 1) + " Please Wait");
            saveTheImage(imageCropView.getCroppedImage(),"");
        });
    }

    @Override
    public void onBackPressed() {
        Collections.reverse(application.tempImages);
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void postWork() {
        Collections.reverse(application.tempImages);
        Glide.with(context).load(R.drawable.all_background).into(backImageView);
        textViewTitle.setText("Crop Images (" + (position + 1) + "/" + application.tempImages.size() + ")");

        if (auto == 1) {
            showLoader();
            setMessage("Cropping Images Please Wait");
            textViewDone.setVisibility(View.GONE);
        } else
            textViewDone.setVisibility(View.VISIBLE);

        startCropping();
    }

    public void startCropping() {
        if (auto == 0)
            startManualCropping();
        else {
            startAutoCropping();
        }
    }

    public void startManualCropping() {
        hideLoader();
        if (position < application.tempImages.size()) {
            imageCropView.setImageFilePath(application.tempImages.get(position).getImagepath());
            imageCropView.setAspectRatio(sizeWidth, sizeHeight);

            textViewTitle.setText("Crop Images (" + (position + 1) + "/" + application.tempImages.size() + ")");

        } else {
            doneCrop();
        }
    }

    public void startAutoCropping() {

        if (position < application.tempImages.size()) {

            setMessage("Cropping Image - " + (position + 1) + " Please Wait...");
            textViewTitle.setText("Crop Images (" + (position + 1) + "/" + application.tempImages.size() + ")");

            imageCropView.setImageFilePath(application.tempImages.get(position).getImagepath());
            imageCropView.setAspectRatio(sizeWidth, sizeHeight);

            saveTheImage(imageCropView.getCroppedImage(),"");

        } else {
            hideLoader();
            doneCrop();
        }
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
        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    private void saveTheImage(Bitmap finalBitmap, String fileName) {

        if (fileName == null) {
            fileName = System.currentTimeMillis() + ".jpg";
        } else {
            fileName = System.currentTimeMillis() + ".jpg";
        }

        File file = new File(ConstantUtils.GetCroppedImagePath(context), fileName);
        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            application.tempImages.get(position).croppath = file.getAbsolutePath();
            position++;
            startCropping();

        } catch (Exception var9) {
            var9.printStackTrace();

        }

    }

}