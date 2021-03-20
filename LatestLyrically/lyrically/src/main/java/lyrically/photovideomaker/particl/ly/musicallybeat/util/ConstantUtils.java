package lyrically.photovideomaker.particl.ly.musicallybeat.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class ConstantUtils {

    public static boolean isFolder = true;
    /*
        preWork();
        getIDs();
        setEvents();
        postWork();
    }
    public void preWork() { }

    public void getIDs() { }

    public void setEvents() { }

    public void postWork() { }

    */

    public static String errorLog = "errorLog";

    public static void showErrorLog(String msg) {
        Log.e(errorLog, msg);
    }

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static String GetSoundPath(Context context) {
        String spath = context.getApplicationInfo().dataDir + "/files/Sound/";
        if (!new File(spath).exists())
            new File(spath).mkdirs();

        return spath;
    }

    public static String GetImagePath(Context context) {
        String spath = context.getApplicationInfo().dataDir + "/files/Images/";
        if (!new File(spath).exists())
            new File(spath).mkdirs();

        return spath;
    }

    public static String GetCroppedImagePath(Context context) {
        String spath = Environment.getExternalStorageDirectory() + "/slideshow/files/CroppedImages/";
        //String spath = context.getApplicationInfo().dataDir + "/files/CroppedImages/";
        if (!new File(spath).exists())
            new File(spath).mkdirs();

        return spath;
    }

    public static String GetParticlePath(Context context) {
        String spath = context.getApplicationInfo().dataDir + "/asset_bundle/";
        if (!new File(spath).exists())
            new File(spath).mkdirs();
        return spath;
    }

    public static String GetParticleThumbnailPath(Context context) {
        String spath = context.getApplicationInfo().dataDir + "/asset_image/";
        if (!new File(spath).exists())
            new File(spath).mkdirs();

        return spath;
    }

    public static String GetMainVideoPath(Context context) {
        String spath = Environment.getExternalStorageDirectory() + "/slideshow/";
        File dir = new File(spath);
        if (!dir.exists()) {
            if (dir.mkdirs()) {
                isFolder = true;
            } else {
                isFolder = false;
                spath = Environment.getExternalStorageDirectory() + "/Android/Data/" + context.getPackageName() + "/files/Videos/";
                dir = new File(spath);
                if (!dir.exists()) {
                    if (dir.mkdirs()) {
                    } else {
                    }
                }
            }
        }
        return spath;
    }


}
