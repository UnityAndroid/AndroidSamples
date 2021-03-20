package lyrically.photovideomaker.particl.ly.musicallybeat.extras;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.view.View;

import lyrically.photovideomaker.particl.ly.musicallybeat.util.ConstantUtils;

import java.io.File;
import java.io.FileOutputStream;

public class ViewToImage {
    Context context;
    ActionListeners listeners;
    String folderName = "DevelopersFolder";
    String fileName = "myFile";
    View view;
    Bitmap bitmap = null;
    String filePath = null;

    public ViewToImage(Context context, View view) {
        this.context = context;
        this.view = view;
        this.convert();
    }

    public ViewToImage(Context context, View view, ActionListeners listeners) {
        this.context = context;
        this.listeners = listeners;
        this.view = view;
        this.convert();
    }

    public ViewToImage(Context context, View view, String folderName, String fileName, ActionListeners listeners) {
        this.context = context;
        this.listeners = listeners;
        this.folderName = folderName;
        this.fileName = fileName;
        this.view = view;
        this.convert();
    }

    public ViewToImage(Context context, View view, String fileName, ActionListeners listeners) {
        this.context = context;
        this.listeners = listeners;
        this.fileName = fileName;
        this.view = view;
        this.convert();
    }

    private void convert() {
        Bitmap bitmap = this.getBitmapFromView(this.view, this.view.getWidth(), this.view.getHeight());
        if (this.fileName.equals("myFile")) {
            this.saveTheImage(bitmap, (String) null);
        } else {
            this.saveTheImage(bitmap, this.fileName);
        }

    }

    private Bitmap getBitmapFromView(View view, int width, int height) {
        this.bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas mCanvas = new Canvas(this.bitmap);
        view.layout(0, 0, view.getLayoutParams().width, view.getLayoutParams().height);
        view.draw(mCanvas);
        return this.bitmap;
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
            finalBitmap.compress(CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            this.filePath = ConstantUtils.GetCroppedImagePath(context) + fileName;
            if (this.listeners != null) {
                this.listeners.convertedWithSuccess(this.bitmap, this.filePath);
            }
        } catch (Exception var9) {
            var9.printStackTrace();
            if (this.listeners != null) {
                this.listeners.convertedWithError(var9.getMessage());
            }
        }

    }
}

