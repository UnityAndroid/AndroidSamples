package lyrically.photovideomaker.particl.ly.musicallybeat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import lyrically.photovideomaker.particl.ly.musicallybeat.activity.HomeActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.activity.ImageSelectActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.activity.MainActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.activity.ParticleActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.activity.PlayerActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.activity.SongSelectActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.activity.TextActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.FirebaseToken;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.MoreAppsModel;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.AdmobAds;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.ConstantFilePaths;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.HttpHandler;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.MySingleton;

import com.google.firebase.messaging.FirebaseMessaging;
import com.unity3d.player.UnityPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class AndroidPlugin {

    private static final String TAG = AndroidPlugin.class.getSimpleName();

    public static AndroidPlugin instance;
    public static Context context;

    public static DatabaseManager mDatabaseManager;

    public static String android_id = "";
    public static String token = "";
    public static CountDownTimer tm;

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
        Intent intent = new Intent(context, ImageSelectActivity.class);
        context.startActivity(intent);
    }

    public static void CallMain(Context context) {
        Constants.android = true;
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }


    public static void CallParticleStore(Context context, String name) {
        Constants.android = true;
        Intent intent = new Intent(context, ParticleActivity.class);
        intent.putExtra("category", name);
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


    public static void showToast(Context context, String Message) {
        Toast.makeText(context, Message, Toast.LENGTH_LONG).show();
    }

    public static void LuanchApp(Context context) {



        new HttpPostRequestThemes().execute(Constants.VideoApi);
    }


    public static class HttpPostRequestThemes extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(params[0], "POST", null);
            return jsonStr;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");
                    JSONArray adsJsonArray = jsonObject.getJSONArray("ads");

                    mDatabaseManager.removecatagorytable();
                    mDatabaseManager.removethemetable();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject category = data.getJSONObject(i);
                        mDatabaseManager.insertcatagorytable(category.getInt("Cat_Id"), category.getString("Category_Name"), category.getString("Icon"), 1, category.getString("background"));
                        JSONArray themes = category.getJSONArray("videos");
                        for (int j = 0; j < themes.length(); j++) {
                            JSONObject theme = themes.getJSONObject(j);
                            mDatabaseManager.insertthemetable(theme.getInt("Id"), category.getInt("Cat_Id"), "0", theme.getString("Theme_Name").replaceAll("'", "''"), theme.getString("Thumnail_Big"), theme.getString("Thumnail_Small"), theme.getString("SoundFile"), theme.getString("sound_size"), theme.getString("GameobjectName"), theme.getString("Is_Preimum"), theme.getString("Status"), "0", theme.getInt("Theme_Id"), 2, theme.getString("lyrics"));
                        }
                    }

                    for (int i = 0; i < adsJsonArray.length(); i++) {
                        JSONObject object = adsJsonArray.getJSONObject(i);
                        Constants.weburl = object.getString("url");
                        mDatabaseManager.insertads(object.getInt("id"), object.getString("appid"), object.getString("banner"), object.getString("inter"), object.getString("reward"), object.getString("gnative"), object.getString("fbanner"), object.getString("finter"), object.getString("fnative"), object.getInt("gstatus"), object.getInt("fstatus"));
                    }

                    CallTokenApi();

                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    UnityCall("Main Camera", "ShowNoInternetDialog", "Good Morning");
                }
            } else {
                UnityCall("Main Camera", "ShowNoInternetDialog", "Good Morning");
            }
        }
    }

    public static void CallTokenApi() {
       /* android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
*/
        android_id = UUID.randomUUID().toString();

        FirebaseMessaging.getInstance().subscribeToTopic("lyrical").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                token = instanceIdResult.getToken();
                if (mDatabaseManager.gettoken().equalsIgnoreCase("")) {
                    mDatabaseManager.inserttoken(android_id, token);
                    new HttpPostRequestToken().execute(Constants.TokenApi, android_id, token);
                } else if (!mDatabaseManager.gettoken().equalsIgnoreCase(token)) {
                    mDatabaseManager.inserttoken(android_id, token);
                    new HttpPostRequestToken().execute(Constants.TokenApi, android_id, token);
                } else {
                    new HttpPostRequestSongs().execute(Constants.SongApi);
                }
            }
        });
    }

    public static class HttpPostRequestToken extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            FirebaseToken token = new FirebaseToken(params[1], params[2]);
            String jsonStr = sh.makeServiceCall(params[0], "POST", token);
            return jsonStr;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                new HttpPostRequestSongs().execute(Constants.SongApi);
            } else {
                UnityCall("Main Camera", "ShowNoInternetDialog", "Good Morning");
            }
        }
    }

    public static class HttpPostRequestSongs extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(params[0], "POST", null);
            return jsonStr;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray data = jsonObject.getJSONArray("data");

                    mDatabaseManager.removesongcatagorytable();
                    mDatabaseManager.removesongtable();

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject songcategory = data.getJSONObject(i);
                        mDatabaseManager.insertSONGcatagorytable(songcategory.getInt("category_id"), songcategory.getString("category_name"), 1);
                        JSONArray songs = songcategory.getJSONArray("sounds");
                        for (int j = 0; j < songs.length(); j++) {
                            JSONObject song = songs.getJSONObject(j);
                            mDatabaseManager.insertsong(song.getInt("id"), songcategory.getInt("category_id"), song.getString("sound_name").replaceAll("'", "''"), song.getString("sound_full_url"), song.getString("sound_full_url"), song.getString("sound_size"), song.getString("lyrics"));
                        }
                    }


                    new HttpPostRequestParticle().execute(Constants.ParticleApi);

                } catch (JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    UnityCall("Main Camera", "ShowNoInternetDialog", "Good Morning");
                }
            } else {
                UnityCall("Main Camera", "ShowNoInternetDialog", "Good Morning");
            }
        }
    }

    public static class HttpPostRequestParticle extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            String jsonStr = sh.makeServiceCall(params[0], "POST", null);
            return jsonStr;
        }

        protected void onPostExecute(String resultt) {
            super.onPostExecute(resultt);

            if (resultt != null) {
                try {
                    JSONObject jsonObject = new JSONObject(resultt);
                    JSONArray result = jsonObject.getJSONArray("data");

                    mDatabaseManager.removeparticlecatagorytable();
                    mDatabaseManager.removeparticletable();
                    mDatabaseManager.removebitparticletable();
                    mDatabaseManager.removesortparticletable();

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject object = result.getJSONObject(i);
                        mDatabaseManager.insertParticlecatagorytable(object.getInt("category_id"), object.getString("category_name"), object.getString("icon_url"));
                        JSONArray themes = object.getJSONArray("particles");
                        for (int j = 0; j < themes.length(); j++) {
                            JSONObject object1 = themes.getJSONObject(j);
                            mDatabaseManager.insertparticle(object1.getInt("id"), object.getInt("category_id"), object1.getString("theme_name").replaceAll("'", "''"), object1.getString("prefab_name"), object1.getString("theme_url"), object1.getString("thumb_url"), object1.getString("particle_url"), object1.getInt("is_lock"));
                        }
                    }

                    JSONArray bits = jsonObject.getJSONArray("bitdata");
                    for (int i = 0; i < bits.length(); i++) {
                        JSONObject object2 = bits.getJSONObject(i);
                        mDatabaseManager.insertbitparticle(object2.getInt("id"), object2.getInt("category_id"), object2.getString("theme_name").replaceAll("'", "''"), object2.getString("prefab_name"), object2.getString("theme_url"), object2.getString("thumb_url"), object2.getString("particle_url"), object2.getInt("is_lock"));
                    }

                    JSONArray resul = jsonObject.getJSONArray("sort");
                    for (int i = 0; i < resul.length(); i++) {
                        JSONObject object = resul.getJSONObject(i);
                        mDatabaseManager.insertSortParticle(object.getInt("id"), object.getInt("particle_id"), object.getString("theme_name"), object.getInt("position"));
                    }

                    JSONArray apps = jsonObject.getJSONArray("apps");
                    for (int i = 0; i < apps.length(); i++) {
                        JSONObject json = apps.getJSONObject(i);
                        Constants.moreAppsModels.add(new MoreAppsModel(json.getString("logo"), json.getString("url"), json.getString("name"), i));
                    }

                    Constants.android = false;
                    if (mDatabaseManager.getGoogleAdStatus() == 1) {
                        UnityPlayer.UnitySendMessage("Main Camera", "LoadPreviewAndroidScreen", "0");
                    } else {
                        UnityPlayer.UnitySendMessage("Main Camera", "LoadPreviewAndroidScreen", "1");
                    }



/*                 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                    if (!prefs.getBoolean("firstTime", false)) {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("firstTime", true);
                        editor.commit();
                        readJson(context);
                    } else {
                        Constants.android = false;
                        if (mDatabaseManager.getGoogleAdStatus() == 1) {
                            UnityPlayer.UnitySendMessage("Main Camera", "LoadPreviewAndroidScreen", "0");
                        } else {
                            UnityPlayer.UnitySendMessage("Main Camera", "LoadPreviewAndroidScreen", "1");
                        }
                    }*/

                } catch (Exception e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
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

        } catch (IOException ex) {
            Log.e(TAG, ex.toString());
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        } finally {
            Constants.android = false;
            if (mDatabaseManager.getGoogleAdStatus() == 1) {
                UnityPlayer.UnitySendMessage("Main Camera", "LoadPreviewAndroidScreen", "0");
            } else {
                UnityPlayer.UnitySendMessage("Main Camera", "LoadPreviewAndroidScreen", "1");
            }
        }
    }

    public static void copyFile(Context context, String filename, int filetype) {
        InputStream in = null;
        OutputStream out = null;
        String newFileName = "";
        try {

            in = context.getAssets().open("asset_bundle/" + filename);
            if (filetype == 0)
                newFileName = ConstantFilePaths.GetParticlePath(context) + filename;
            else
                newFileName = ConstantFilePaths.GetParticleThumbnailPath(context) + filename;

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


    public static void getParticles(Context context) {

        if (mDatabaseManager == null)
            mDatabaseManager = new DatabaseManager(context);

        JSONObject jsonAdd = new JSONObject();
        try {
            JSONArray pdetails = new JSONArray();
            JSONArray odetails = new JSONArray();

            Cursor part = null;
            Cursor down = null;
            Cursor sortpart = null;
            try {
                JSONArray catparts = new JSONArray();
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
                }

                part = mDatabaseManager.featchParticletable();
                if (part.moveToFirst()) {
                    do {
                        JSONObject object = new JSONObject();
                        object.put("UniqueIDNo", part.getInt(part.getColumnIndex("id")));

                        Cursor data = null;
                        data = mDatabaseManager.getdownloadedparticlebyid(part.getInt(part.getColumnIndex("id")));
                        if (data.moveToFirst()) {
                            object.put("BundlePath", data.getString(data.getColumnIndex("particle")));
                            object.put("ImgPath", data.getString(data.getColumnIndex("thumb")));
                            object.put("downloadWebUrl", data.getString(data.getColumnIndex("particle_url")));
                        } else {
                            String BundlePath = part.getString(part.getColumnIndex("particle_url"));
                            String filename = BundlePath.substring(BundlePath.lastIndexOf("/") + 1);
                            object.put("BundlePath", ConstantFilePaths.GetParticlePath(context) + filename);
                            object.put("ImgPath", part.getString(part.getColumnIndex("thumb_url")));
                            object.put("downloadWebUrl", part.getString(part.getColumnIndex("particle_url")));
                        }
                        data.close();

                        object.put("ThemeName", part.getString(part.getColumnIndex("theme_name")));
                        object.put("prefbName", part.getString(part.getColumnIndex("prefab_name")));
                        object.put("NewFlag", "1");

                        pdetails.put(object);
                    } while (part.moveToNext());
                }

                sortpart = mDatabaseManager.featchSortParticle();
                if (sortpart.moveToFirst()) {
                    do {
                        JSONObject object = new JSONObject();
                        object.put("uniqueId", sortpart.getInt(sortpart.getColumnIndex("particle_id")));
                        object.put("priorityIndex", sortpart.getInt(sortpart.getColumnIndex("position")));
                        odetails.put(object);
                    } while (sortpart.moveToNext());
                }

            } finally {
                if (part != null)
                    part.close();

                if (down != null)
                    down.close();

                if (sortpart != null)
                    sortpart.close();
            }

            jsonAdd.put("LyricsTemplates", pdetails);
            jsonAdd.put("orderList", odetails);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        } finally {
            Constants.android = false;
            UnityPlayer.UnitySendMessage("LoadJsonData", "LoadJson", jsonAdd.toString());
        }
    }


    public static void downloadedbitParticle(String json) {

        Cursor data = null;
        Cursor theme = null;
        int uniqueId = 0;
        try {
            uniqueId = Integer.parseInt(json);
            theme = mDatabaseManager.featchbitParticleById(uniqueId);
            if (theme.moveToFirst()) {
                data = mDatabaseManager.getdownloadedbitparticlebyid(uniqueId);
                String BundlePath = theme.getString(theme.getColumnIndex("particle_url"));
                String ImagePath = theme.getString(theme.getColumnIndex("thumb_url"));
                String Particlefilename = BundlePath.substring(BundlePath.lastIndexOf("/") + 1);
                String Imagefilename = ImagePath.substring(ImagePath.lastIndexOf("/") + 1);

                if (data.moveToFirst()) {
                    mDatabaseManager.updatebitParticleThumbnailDownload(uniqueId, GetParticleThumbnailPath() + Imagefilename);
                } else {
                    if (new File(GetParticleThumbnailPath() + Imagefilename).exists()) {
                        mDatabaseManager.insertbitparticledownloads(uniqueId, theme.getInt(theme.getColumnIndex("category_id")), theme.getString(theme.getColumnIndex("theme_name")).replaceAll("'", "''"), theme.getString(theme.getColumnIndex("prefab_name")), GetParticleThumbnailPath() + Imagefilename, GetParticlePath() + Particlefilename, "", theme.getString(theme.getColumnIndex("particle_url")));
                    } else if (new File(GetParticlePath() + Particlefilename).exists()) {
                        mDatabaseManager.insertbitparticledownloads(uniqueId, theme.getInt(theme.getColumnIndex("category_id")), theme.getString(theme.getColumnIndex("theme_name")).replaceAll("'", "''"), theme.getString(theme.getColumnIndex("prefab_name")), theme.getString(theme.getColumnIndex("thumb_url")), GetParticlePath() + Particlefilename, "", "");
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



    public static void getbitParticles(Context context) {

        if (mDatabaseManager == null)
            mDatabaseManager = new DatabaseManager(context);

        JSONObject jsonAdd = new JSONObject();
        try {
            JSONArray pdetails = new JSONArray();
            Cursor part = null;
            try {
                part = mDatabaseManager.featchbitParticletable();
                if (part.moveToFirst()) {
                    do {
                        JSONObject object = new JSONObject();
                        object.put("UniqueIDNo", part.getInt(part.getColumnIndex("id")));

                        Cursor data = null;
                        data = mDatabaseManager.getdownloadedbitparticlebyid(part.getInt(part.getColumnIndex("id")));
                        if (data.moveToFirst()) {
                            object.put("BundlePath", data.getString(data.getColumnIndex("particle")));
                            object.put("ImgPath", data.getString(data.getColumnIndex("thumb")));
                            object.put("downloadWebUrl", data.getString(data.getColumnIndex("particle_url")));
                        } else {
                            String BundlePath = part.getString(part.getColumnIndex("particle_url"));
                            String filename = BundlePath.substring(BundlePath.lastIndexOf("/") + 1);
                            object.put("BundlePath", ConstantFilePaths.GetParticlePath(context) + filename);
                            object.put("ImgPath", part.getString(part.getColumnIndex("thumb_url")));
                            object.put("downloadWebUrl", part.getString(part.getColumnIndex("particle_url")));
                        }
                        data.close();

                        object.put("ThemeName", part.getString(part.getColumnIndex("theme_name")));
                        object.put("prefbName", part.getString(part.getColumnIndex("prefab_name")));
                        object.put("NewFlag", "1");

                        pdetails.put(object);
                    } while (part.moveToNext());
                }

            } finally {
                if (part != null)
                    part.close();

            }

            jsonAdd.put("LyricsTemplates", pdetails);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        } finally {
            Constants.android = false;
            UnityPlayer.UnitySendMessage("LoadJsonData", "LoadJsonForParticle", jsonAdd.toString());
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


}