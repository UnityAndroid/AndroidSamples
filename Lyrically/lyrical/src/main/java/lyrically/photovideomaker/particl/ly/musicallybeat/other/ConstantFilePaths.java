package lyrically.photovideomaker.particl.ly.musicallybeat.other;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public class ConstantFilePaths {

    public static String randImage = "";
    public static boolean isFolder = true;

    public static String getDirectoryDCIM(Context context) {
        return GetMainVideoPath(context);
    }

    public static String GetSoundPath(Context context) {
        String spath = context.getApplicationInfo().dataDir + "/files/Sound/";
        if (!new File(spath).exists())
            new File(spath).mkdirs();

        return spath;
    }

    public static String GetCroppedVideoPath(Context context) {
        String spath = context.getApplicationInfo().dataDir + "/files/CroppedVideo/";
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

    public static String GetCroppedPath(Context context) {
        String spath = context.getApplicationInfo().dataDir + "/files/CroppedImages/";
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
        String spath = Environment.getExternalStorageDirectory() + "/Lyrically/";
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
        } else {

        }
        return spath;
    }

}
