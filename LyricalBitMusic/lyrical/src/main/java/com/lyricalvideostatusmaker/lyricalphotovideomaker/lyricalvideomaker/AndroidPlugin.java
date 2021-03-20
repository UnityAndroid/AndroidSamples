package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.App;
import com.unity3d.player.UnityPlayer;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity.HomeActivity;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity.ImageActivity;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity.MainActivity;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity.PlayerActivity;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity.SongSelectActivity;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity.TextActivity;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model.FirebaseToken;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.ConstantFilePaths;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.Constants;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.DatabaseManager;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.HttpHandler;


public class AndroidPlugin {

    private static final String TAG = AndroidPlugin.class.getSimpleName();

    public static AndroidPlugin instance;
    public static Context context;
    public static DatabaseManager mDatabaseManager;

    public static String android_id = "";
    public static String token = "";
    public static String getLatestVersion = "";
    public static String adsBanner = "None";
    public static String adsInterstitial = "None";
    public static String adsNative = "None";
    public static String adsRewarded = "None";
    public static String adsUnitySplashInterstitial = "None";
    public static String adsUnityBanner = "n";
    public static String adsUnityInterstitial = "n";
    public static String adsUnityRewarded = "n";

    public static String adMob_banner_id = "";
    public static String adMob_interstitial_id = "";
    public static String adMob_native_id = "";
    public static String adMob_rewarded_id = "";

    public static String fb_banner_id = "";
    public static String fb_interstitial_id = "";
    public static String fb_native_id = "";
    public static String fb_native_banner_id = "";

    public static boolean isAdsShow = true;
    public static boolean isFolder = true;
    public static boolean isAdsLastShow = false;

    public static int tabpos = 0;
    public static int recpos = 0;

    public AndroidPlugin() {
        instance = this;
    }

    public static AndroidPlugin instance() {
        if (instance == null) {
            instance = new AndroidPlugin();
        }
        return instance;
    }

    public static void setContext(Context contex) {
        context = contex;
        FirebaseApp.initializeApp(context);
        mDatabaseManager = new DatabaseManager(context);
    }

    public static String getDirectoryDCIM() {
        return ConstantFilePaths.getDirectoryDCIM(context);
    }

    public static String GetSoundPath() {
        return ConstantFilePaths.GetSoundPath(context);
    }

    public static String GetRingTonePath() {
        return ConstantFilePaths.GetMainRingPath(context);
    }

    public static String GetCroppedVideoPath() {
        return ConstantFilePaths.GetCroppedVideoPath(context);
    }

    public static String GetImagePath() {
        return ConstantFilePaths.GetImagePath(context);
    }

    public static String GetCroppedPath() {
        return ConstantFilePaths.GetCroppedPath(context);
    }

    public static String GetParticlePath() {
        return ConstantFilePaths.GetParticlePath(context);
    }

    public static String GetParticleThumbnailPath() {
        return ConstantFilePaths.GetParticleThumbnailPath(context);
    }

    public static String GetMainVideoPath() {
        return ConstantFilePaths.GetMainVideoPath(context);
    }

    public static void UnityCall(String ObjectName, String Methodname, String Data) {
        UnityPlayer.UnitySendMessage(ObjectName, Methodname, Data);
    }

    public static void ShowErrorLog(String Message) {
        Log.e("errorLog", Message);
    }

    public static void ShowDebugLog(String Message) {
        Log.d("debugLog", Message);
    }

    public static void CallGallery(Context context) {
        Constants.android = true;
        Intent intent = new Intent(context, ImageActivity.class);
        context.startActivity(intent);
    }

    public static void CallMain(Context context) {
        Constants.android = true;
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public static void CallSongs(Context context) {
        Constants.android = true;
        Intent intent = new Intent(context, SongSelectActivity.class);
        context.startActivity(intent);
    }

    public static void CallHome(Context context) {
        Constants.android = true;
        Intent intent = new Intent(context, HomeActivity.class);
        context.startActivity(intent);
    }

    public static void editInput(Context context) {
        Constants.android = true;
        Intent intent = new Intent(context, TextActivity.class);
        context.startActivity(intent);
    }

    public static String GetBannerType() {
        return adsUnityBanner;
    }

    public static String GetRewardType() {
        return adsUnityRewarded;
    }

    public static String GetRenderingInterAd() {
        return adsUnityInterstitial;
    }

    public static void showToast(Context context, String Message) {
        Toast.makeText(context, Message, Toast.LENGTH_LONG).show();
    }

    public static void LuanchApp(Context context) {
        new HttpPostRequestThemes().execute(Constants.AllSongs);
    }


    public static void CallTokenApi() {

        android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
                Log.e("token ", " : " + token);
                if (mDatabaseManager.gettoken().equalsIgnoreCase("")) {
                    mDatabaseManager.inserttoken(android_id, token);
                    new HttpPostRequestToken().execute(Constants.AppToken, android_id, token);
                } else if (!mDatabaseManager.gettoken().equalsIgnoreCase(token)) {
                    mDatabaseManager.inserttoken(android_id, token);
                    new HttpPostRequestToken().execute(Constants.AppToken, android_id, token);
                } else {
                    new HttpPostRequestParticle().execute(Constants.ParticleList);
                }
            }
        });

        FirebaseInstallations.getInstance().getToken(true).addOnSuccessListener(new OnSuccessListener<InstallationTokenResult>() {
            @Override
            public void onSuccess(InstallationTokenResult installationTokenResult) {
                token = installationTokenResult.getToken();
                Log.e("token 1", " : " + token);
                if (mDatabaseManager.gettoken().equalsIgnoreCase("")) {
                    mDatabaseManager.inserttoken(android_id, token);
                    new HttpPostRequestToken().execute(Constants.AppToken, android_id, token);
                } else if (!mDatabaseManager.gettoken().equalsIgnoreCase(token)) {
                    mDatabaseManager.inserttoken(android_id, token);
                    new HttpPostRequestToken().execute(Constants.AppToken, android_id, token);
                } else {
                    new HttpPostRequestParticle().execute(Constants.ParticleList);
                }
            }
        });
    }

    public static class HttpPostRequestThemes extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(params[0], "POST");
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String resultt) {
            super.onPostExecute(resultt);
            if (resultt != null) {
                try {
                    JSONObject jsonObject = new JSONObject(resultt);
                    JSONArray result = jsonObject.getJSONArray("data");

                    //Video
                    mDatabaseManager.removecatagorytable();
                    mDatabaseManager.removethemetable();

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);
                        //Video
                        mDatabaseManager.insertcatagorytable(object.getInt("Cat_Id"), object.getString("Category_Name"), object.getString("Icon"), 1, object.getString("Icon"));

                        JSONArray themes = object.getJSONArray("videos");
                        for (int j = 0; j < themes.length(); j++) {
                            JSONObject object1 = themes.getJSONObject(j);
                            //Video
                            mDatabaseManager.insertthemetable(object1.getInt("Id"), object.getInt("Cat_Id"), "0", object1.getString("Theme_Name").replaceAll("'", "''"), object1.getString("Thumbnail_Big"), object1.getString("Thumbnail_Small"), object1.getString("SoundFile"), object1.getString("SoundSize"), object1.getString("GameobjectName"), "0", object1.getString("Status"), "0", object1.getInt("Theme_Id"), 2, object1.getString("lyrics").replaceAll("'", "''"));
                        }
                    }

                    new HttpPostRequestSlider().execute(Constants.SliderList);

                } catch (JSONException e) {
                    UnityCall("Main Camera", "ShowNoInternetDialog", "Good Morning");
                }
            } else {
                UnityCall("Main Camera", "ShowNoInternetDialog", "Good Morning");
            }
        }
    }

    public static class HttpPostRequestSlider extends AsyncTask<String, Void, String> {

        public static final String REQUEST_METHOD = "POST";
        public static final int READ_TIMEOUT = 150000;
        public static final int CONNECTION_TIMEOUT = 150000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                URL myUrl = new URL(stringUrl);

                HttpURLConnection connection = (HttpURLConnection) myUrl.openConnection();
                connection.setRequestProperty("Authorization", "Basic V0JpdE1hc3RlcjpNYXN0ZXJAV0JpdA==");

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                connection.connect();
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                reader.close();
                streamReader.close();
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("error", e.toString());
                result = null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String resultt) {
            super.onPostExecute(resultt);
            if (resultt != null) {
                try {
                    JSONObject jsonObject = new JSONObject(resultt);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {

                        try {
                            JSONArray sliderJsonArray = jsonObject.getJSONArray("slider");
                            mDatabaseManager.removeSlider();
                            for (int i = 0; i < sliderJsonArray.length(); i++) {
                                JSONObject object = sliderJsonArray.getJSONObject(i);
                                mDatabaseManager.insertSlider(object.getInt("Slider_id"),
                                        object.getString("Title"), object.getString("Type"),
                                        object.getString("Icon"), object.getString("Name"),
                                        object.getString("status"));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //More Apps
                        try {
                            mDatabaseManager.removeAddMoreTable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        String imagePath = jsonObject.getString("imagePath");
                        JSONArray data = jsonObject.getJSONArray("application");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject1 = data.getJSONObject(i);
                            String app_id = jsonObject1.getString("app_id");
                            if (app_id.equalsIgnoreCase("1")) {

                                getLatestVersion = jsonObject1.getString("version");

                                adsBanner = jsonObject1.getString("banner");
                                adsNative = jsonObject1.getString("native");
                                adsInterstitial = jsonObject1.getString("interstitial");
                                adsRewarded = jsonObject1.getString("rewarded_video");

                                adsUnitySplashInterstitial = jsonObject1.getString("unity_splash_interstitial");

                                if (jsonObject1.getString("unity_banner").equalsIgnoreCase("Google")) {
                                    adsUnityBanner = "g";
                                } else if (jsonObject1.getString("unity_banner").equalsIgnoreCase("Facebook")) {
                                    adsUnityBanner = "f";
                                } else if (jsonObject1.getString("unity_banner").equalsIgnoreCase("None")) {
                                    adsUnityBanner = "n";
                                }

                                if (jsonObject1.getString("unity_interstitial").equalsIgnoreCase("Google")) {
                                    adsUnityInterstitial = "g";
                                } else if (jsonObject1.getString("unity_interstitial").equalsIgnoreCase("Facebook")) {
                                    adsUnityInterstitial = "f";
                                } else if (jsonObject1.getString("unity_interstitial").equalsIgnoreCase("None")) {
                                    adsUnityInterstitial = "n";
                                }

                                if (jsonObject1.getString("unity_rewarded_video").equalsIgnoreCase("Google")) {
                                    adsUnityRewarded = "g";
                                } else if (jsonObject1.getString("unity_rewarded_video").equalsIgnoreCase("Facebook")) {
                                    adsUnityRewarded = "f";
                                } else if (jsonObject1.getString("unity_rewarded_video").equalsIgnoreCase("None")) {
                                    adsUnityRewarded = "n";
                                }

                            } else {
                                String application = jsonObject1.getString("application");
                                String app_link = jsonObject1.getString("app_link");
                                String icon = imagePath + jsonObject1.getString("icon");

                                mDatabaseManager.insertAddMoreTable(i, application, app_link, icon);
                            }
                        }

                        try {
                            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                            String version = pInfo.versionName;
                            int verCode = pInfo.versionCode;
                            if (!getLatestVersion.equalsIgnoreCase(version)) {
                                UnityPlayer.UnitySendMessage("Main Camera", "ShowUpdateAvailableDialog", "Good Morning");
                            } else {
                                CallTokenApi();
                            }
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                            CallTokenApi();
                        }
                    } else {
                        CallTokenApi();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    UnityPlayer.UnitySendMessage("Main Camera", "ShowNoInternetDialog", "Good Morning");
                }
            } else {
                UnityPlayer.UnitySendMessage("Main Camera", "ShowNoInternetDialog", "Good Morning");
            }
        }
    }

    public static class HttpPostRequestToken extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            FirebaseToken token = new FirebaseToken(params[1], params[2]);
            String jsonStr = sh.makeServiceCall(params[0], "POST", token);
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                new HttpPostRequestParticle().execute(Constants.ParticleList);
            } else {
                UnityCall("Main Camera", "ShowNoInternetDialog", "Good Morning");
            }
        }
    }

    public static class HttpPostRequestParticle extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(params[0], "POST");
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String resultt) {
            super.onPostExecute(resultt);
            if (resultt != null) {
                try {
                    JSONObject jsonObject = new JSONObject(resultt);
                    JSONArray result = jsonObject.getJSONArray("data");

                    mDatabaseManager.removeparticletable();
                    mDatabaseManager.removesortparticletable();

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);
                        if (!object.getString("Prefb_Name").equalsIgnoreCase("")) {
                            mDatabaseManager.insertparticle(object.getInt("Particle_Id"),
                                    object.getInt("Cat_Id"),
                                    object.getString("Particle_Name").replaceAll("'", "''"),
                                    object.getString("Prefb_Name"), object.getString("Particle_Image"),
                                    object.getString("Particle_Preview_Image"),
                                    object.getString("Particle"), object.getInt("Lock"));
                        }

                        mDatabaseManager.insertSortParticle(object.getInt("Id"), object.getInt("Particle_Id"),
                                object.getString("Particle_Name").replaceAll("'", "''"), i + 1);

                    }

                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    if (!prefs.getBoolean("firstTime", false)) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("firstTime", true);
                        editor.apply();
                        readJson(context);
                    } else {
                        Constants.android = false;
                        UnityPlayer.UnitySendMessage("Main Camera", "LoadPreviewAndroidScreen", "0");
                    }
                } catch (Exception e) {
                    UnityCall("Main Camera", "ShowNoInternetDialog", "Good Morning");
                }
            } else {
                UnityCall("Main Camera", "ShowNoInternetDialog", "Good Morning");
            }
        }

    }


    public static void readJson(Context context) {

        String json = null;
        try {
            InputStream is = context.getAssets().open("ParticleJsonOnLoadAndroid.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

            JSONObject object = new JSONObject(json);

            JSONArray ParticalDetails = object.getJSONArray("ParticalDetails");
            JSONObject General = ParticalDetails.getJSONObject(0);

            JSONArray ParticalInfo = General.getJSONArray("ParticalInfo");
            for (int i = 0; i < ParticalInfo.length(); i++) {

                JSONObject particle = ParticalInfo.getJSONObject(i);

                copyFile(context, particle.getString("BundlePath"), 0);
                copyFile(context, particle.getString("ImgPath"), 1);

                ConstantFilePaths.randImage = GetParticleThumbnailPath() + particle.getString("ImgPath");
                mDatabaseManager.insertparticledownloads(particle.getInt("UniqueIDNo"), 10, particle.getString("ThemeName").replaceAll("'", "''"), particle.getString("prefbName"), GetParticleThumbnailPath() + particle.getString("ImgPath"), GetParticlePath() + particle.getString("BundlePath"), "", "");

            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            Constants.android = false;
            UnityPlayer.UnitySendMessage("Main Camera", "LoadPreviewAndroidScreen", "0");
        }
    }

    public static void copyFile(Context context, String filename, int filetype) {
        InputStream in = null;
        OutputStream out = null;
        String newFileName = "";
        try {

            in = context.getAssets().open("asset_bundle/" + filename);
            if (filetype == 0)
                newFileName = GetParticlePath() + filename;
            else
                newFileName = GetParticleThumbnailPath() + filename;

            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e(TAG + "error", e.toString());
        } finally {

        }
    }


    // Get Theme

    public static void getTheme() {

        Cursor download = mDatabaseManager.getcurrenttheme();
        JSONObject jsonAdd = new JSONObject();

        try {
            if (download.moveToFirst()) {
                Constants.songname = download.getString(3).replaceAll("'", "");
                Cursor theme = null;
                try {
                    theme = mDatabaseManager.getthemebyid(download.getInt(0));
                    if (theme.moveToFirst()) {
                        jsonAdd.put("TemplateId", theme.getString(theme.getColumnIndex("GameobjectName")));
                        jsonAdd.put("ThemeId", String.valueOf(theme.getInt(theme.getColumnIndex("Theme_Id"))));
                        jsonAdd.put("sound", download.getString(1));
                        jsonAdd.put("image", download.getString(2));
                        jsonAdd.put("lyricsContent", theme.getString(theme.getColumnIndex("lyrics")));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                } finally {
                    if (theme != null)
                        theme.close();
                }
            }
        } finally {
            if (download != null)
                download.close();

            mDatabaseManager.removecropimages();
            Constants.android = false;
            UnityCall("CategoryManagement", "OnLoadUserData", jsonAdd.toString());
        }
    }

    public static void downloadedParticle(String json) {
        Cursor data = null;
        Cursor theme = null;
        int uniqueId = 0;
        try {
            uniqueId = Integer.parseInt(json);
            theme = mDatabaseManager.featchParticleById(uniqueId);
            if (theme.moveToFirst()) {
                data = mDatabaseManager.getdownloadedparticlebyid(uniqueId);
                String BundlePath = theme.getString(theme.getColumnIndex("particle_url"));
                String ImagePath = theme.getString(theme.getColumnIndex("thumb_url"));
                String Particlefilename = BundlePath.substring(BundlePath.lastIndexOf("/") + 1);
                String Imagefilename = ImagePath.substring(ImagePath.lastIndexOf("/") + 1);

                if (data.moveToFirst()) {
                    mDatabaseManager.updateParticleThumbnailDownload(uniqueId, GetParticleThumbnailPath() + Imagefilename);
                } else {
                    if (new File(GetParticleThumbnailPath() + Imagefilename).exists()) {
                        mDatabaseManager.insertparticledownloads(uniqueId, theme.getInt(theme.getColumnIndex("category_id")), theme.getString(theme.getColumnIndex("theme_name")).replaceAll("'", "''"), theme.getString(theme.getColumnIndex("prefab_name")), GetParticleThumbnailPath() + Imagefilename, GetParticlePath() + Particlefilename, "", theme.getString(theme.getColumnIndex("particle_url")));
                    } else if (new File(GetParticlePath() + Particlefilename).exists()) {
                        mDatabaseManager.insertparticledownloads(uniqueId, theme.getInt(theme.getColumnIndex("category_id")), theme.getString(theme.getColumnIndex("theme_name")).replaceAll("'", "''"), theme.getString(theme.getColumnIndex("prefab_name")), theme.getString(theme.getColumnIndex("thumb_url")), GetParticlePath() + Particlefilename, "", "");
                    }
                }
            }
        } finally {
            if (data != null)
                data.close();
            if (theme != null)
                theme.close();
        }
    }


    public static void getParticles() {
        JSONObject jsonAdd = new JSONObject();
        try {
            JSONArray pdetails = new JSONArray();
            JSONArray odetails = new JSONArray();

            Cursor part = null;
            //Cursor down = null;
            Cursor sortpart = null;
            try {
                /*JSONArray catparts = new JSONArray();
                down = mDatabaseManager.getalldownloadedparticlebycategoryid(10);
                if (down.moveToFirst()) {
                    do {
                        JSONObject object = new JSONObject();
                        object.put("UniqueIDNo", down.getInt(down.getColumnIndex("id")));
                        object.put("BundlePath", down.getString(down.getColumnIndex("particle")));
                        object.put("ImgPath", down.getString(down.getColumnIndex("thumb")));
                        object.put("ThemeName", down.getString(down.getColumnIndex("theme_name")));
                        object.put("prefbName", down.getString(down.getColumnIndex("prefab_name")));
                        object.put("NewFlag", "1");
                        object.put("downloadWebUrl", "");

                        catparts.put(object);

                    } while (down.moveToNext());
                }*/

                part = mDatabaseManager.featchParticletable();
                if (part != null) {
                    if (part.moveToFirst()) {
                        do {
                            JSONObject object = new JSONObject();
                            object.put("UniqueIDNo", part.getInt(part.getColumnIndex("id")));

                            Cursor data = null;
                            data = mDatabaseManager.getdownloadedparticlebyid(part.getInt(part.getColumnIndex("id")));
                            if (data != null) {
                                if (data.moveToFirst()) {
                                    object.put("BundlePath", data.getString(data.getColumnIndex("particle")));
                                    object.put("ImgPath", data.getString(data.getColumnIndex("thumb")));
                                    object.put("downloadWebUrl", data.getString(data.getColumnIndex("particle_url")));
                                } else {
                                    String BundlePath = part.getString(part.getColumnIndex("particle_url"));
                                    String filename = BundlePath.substring(BundlePath.lastIndexOf("/") + 1);
                                    object.put("BundlePath", GetParticlePath() + filename);
                                    object.put("ImgPath", part.getString(part.getColumnIndex("thumb_url")));
                                    object.put("downloadWebUrl", part.getString(part.getColumnIndex("particle_url")));
                                }
                                data.close();
                            }

                            object.put("ThemeName", part.getString(part.getColumnIndex("theme_name")));
                            object.put("prefbName", part.getString(part.getColumnIndex("prefab_name")));
                            object.put("NewFlag", "1");

                            pdetails.put(object);
                        } while (part.moveToNext());
                    }
                }

                sortpart = mDatabaseManager.featchSortParticle();
                if (sortpart != null) {
                    if (sortpart.moveToFirst()) {
                        do {
                            JSONObject object = new JSONObject();
                            object.put("uniqueId", sortpart.getInt(sortpart.getColumnIndex("particle_id")));
                            object.put("priorityIndex", sortpart.getInt(sortpart.getColumnIndex("position")));
                            odetails.put(object);
                        } while (sortpart.moveToNext());
                    }
                }
            } finally {
                if (part != null)
                    part.close();

                if (sortpart != null)
                    sortpart.close();
            }

            jsonAdd.put("LyricsTemplates", pdetails);
            jsonAdd.put("orderList", odetails);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        } finally {
            Constants.android = false;
            UnityPlayer.UnitySendMessage("LoadJsonData", "LoadJson", jsonAdd.toString());
        }
    }

    public static void videoComplete(String filepath) {

        Cursor download = null;
        try {
            download = mDatabaseManager.getcurrenttheme();
            if (download.moveToFirst()) {
                RefereshVideoList(context, filepath);
                //  MP4Recorder.release();
                if (Constants.songname.equalsIgnoreCase("")) {
                    mDatabaseManager.insertcreation(download.getInt(0), download.getString(3).replaceAll("'", ""), filepath);
                } else {
                    mDatabaseManager.insertcreation(download.getInt(0), Constants.songname.replaceAll("'", ""), filepath);
                }

                UnityPlayer.UnitySendMessage("DIALOG", "OnStopCapturing", "ads");
            }
        } finally {
            if (download != null)
                download.close();
        }
    }

    public static void RefereshVideoList(Context context, String str) {
        try {
            MediaScannerConnection.scanFile(context, new String[]{str}, new String[]{"video/mp4"}, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void playerscreen(String[] values) {
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("filepath", values[0].toString());
        intent.putExtra("creation", 1);

        if (values[1].equalsIgnoreCase("ads")) {
            intent.putExtra("ads", 0);
        }
        context.startActivity(intent);
    }

    public void rateus() {
        Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + Constants.cpack));
        context.startActivity(rateIntent);
    }

    public void playerscreenrateus(String filepath) {
        rateus();
        Intent intent = new Intent(context, PlayerActivity.class);
        intent.putExtra("filepath", filepath);
        intent.putExtra("creation", 1);
        context.startActivity(intent);
    }

    public static void loadimage(Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.app_banner);
        String path = AndroidPlugin.GetImagePath() + "LyricalBitBanner.jpg";
        OutputStream out = null;
        File file = new File(path);
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}