package lyrically.photovideomaker.particl.ly.musicallybeat.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class ScreenUtils {

    public static int imgWidth = 720;
    public static int imgHeight = 1280;

    public static int nImgWidth = 1000;
    public static int nImgHeight = 1000;

    public static DisplayMetrics getDisplayMetrices(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static int convertDPItoINT(Context context, float dpi) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, getDisplayMetrices(context));
    }

    public static int getScreenWidth(Context context) {
        return getDisplayMetrices(context).widthPixels;
    }

    public static int getScreenHeight(Context context) {
        return getDisplayMetrices(context).heightPixels;
    }
}
