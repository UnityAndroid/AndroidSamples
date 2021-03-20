package lyrically.photovideomaker.particl.ly.musicallybeat.other;

import android.content.Context;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.ArrayList;

import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.MoreAppsModel;

public class Constants {


    public static String cpack = "lyrically.photovideomaker.particl.ly.musicallybeat";
    public static String BaseUrl = "http://inoventic.online/lyrically/";
    public static String VideoApi = BaseUrl + "VideoCatgoryList.php";
    public static String SongApi = BaseUrl + "SoundCategoryList.php";
    public static String TokenApi = BaseUrl + "Get_More_App.php";
    public static String ParticleApi = BaseUrl + "ParticleCategoryList.php";

    public static boolean isbitFirst = true;
    public static ArrayList<MoreAppsModel> moreAppsModels = new ArrayList<>();
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
