package lyrically.photovideomaker.particl.ly.musicallybeat.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {
    private static PrefUtils yourPreference;
    private SharedPreferences sharedPreferences;

    public static PrefUtils getInstance(Context context) {
        if (yourPreference == null) {
            yourPreference = new PrefUtils(context);
        }
        return yourPreference;
    }

    private PrefUtils(Context context) {
        sharedPreferences = context.getSharedPreferences("PrefUtils",Context.MODE_PRIVATE);
    }

    public void saveStringData(String key,String value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putString(key, value);
        prefsEditor.apply();
    }

    public String getStringData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getString(key, "");
        }
        return "";
    }

    public void saveIntData(String key,int value) {
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor .putInt(key, value);
        prefsEditor.apply();
    }

    public int getIntData(String key) {
        if (sharedPreferences!= null) {
            return sharedPreferences.getInt(key,0);
        }
        return -1;
    }
}
