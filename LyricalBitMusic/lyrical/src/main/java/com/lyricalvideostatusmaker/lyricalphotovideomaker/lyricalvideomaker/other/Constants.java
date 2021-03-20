package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other;

import android.content.Context;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.dialog.DialogLoader;

public class Constants {

    public static DialogLoader dialogLoader;

    public static String cpack = "com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker";

    public static String BaseURL = "https://google.com/service/";
    public static String AllSongs = BaseURL + "categoryList/formate/json/";
    public static String AppToken = BaseURL + "token/formate/json/";
    public static String ParticleList = BaseURL + "particleList/formate/json/";
    public static String SliderList = BaseURL + "sliderList/formate/json/";
    public static String Ads = BaseURL + "ads/formate/json/";

    public static int tclickcount = 3;
    public static String weburl = "";
    public static String songname = "";
    public static boolean isFirst = true;
    public static int tabpos = 0;
    public static int recpos = 0;
    public boolean on_attach = true;

    public static boolean android = false;

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getPixelInteger(Context context, int dps) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dps, dm);
        return dp;
    }

    public static void initializeDialog(Context context, String msg) {
        dialogLoader = new DialogLoader(context, msg);
    }

    public static void showLoader() {
        dialogLoader.show();
    }

    public static void hideLoader() {
        if (dialogLoader != null)
            if (dialogLoader.isShowing())
                dialogLoader.dismiss();
    }


    // Http Demo

    public static class HttpPostDemo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(params[0], "GET", null);
            return jsonStr;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {

            } else {

            }
        }
    }

    // Demo Methods

    private void demoMethods() {
        getIDs();
        setEvents();
        doWork();
    }

    private void getIDs() {
    }

    private void setEvents() {
    }

    private void doWork() {
    }


}
