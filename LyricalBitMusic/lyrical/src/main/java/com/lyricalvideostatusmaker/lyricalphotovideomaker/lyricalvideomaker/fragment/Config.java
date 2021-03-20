package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Parcelable;

import androidx.core.content.FileProvider;

import java.io.File;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.Constants;

public class Config {

    public static final String GBWhatsAppDirectoryPath = "/storage/emulated/0/GWAActivity/Media/.Statuses/";
    public static final String GBWhatsAppSaveStatus = "/storage/emulated/0/WhatsAppStatuses/Media/GWAActivity/";
    public static final String WhatsAppBusinessDirectoryPath = "/storage/emulated/0/WhatsApp Business/Media/.Statuses/";
    public static final String WhatsAppBusinessSaveStatus = "/storage/emulated/0/WhatsAppStatuses/Media/WhatsAppBusiness/";
    public static final String WhatsAppDirectoryPath = "/storage/emulated/0/WhatsApp/Media/.Statuses/";
    public static final String WhatsAppSaveStatus = "/storage/emulated/0/LyricalBitMusic/WhatsAppStatusSaver/";

    public static Parcelable getFileUri(Context context, File file) {
        return FileProvider.getUriForFile(context, Constants.cpack + ".fileprovider", file);
    }

    public static String getALLState(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE);
        return sharedPreferences.getString("ALL", "").length() > 0 ? sharedPreferences.getString("ALL", "") : "";
    }

    public static boolean isPackageInstalled(Context context, String targetPackage) {
        for (ApplicationInfo packageInfo : context.getPackageManager().getInstalledApplications(0)) {
            if (packageInfo.packageName.equals(targetPackage)) {
                return true;
            }
        }
        return false;
    }

}
