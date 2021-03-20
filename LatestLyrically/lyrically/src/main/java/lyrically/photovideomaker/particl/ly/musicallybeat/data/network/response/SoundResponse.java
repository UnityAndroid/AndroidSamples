package lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class SoundResponse implements Serializable {

    @SerializedName("data")
    public ArrayList<SoundCategory> data = null;

    @SerializedName("flag")
    public boolean flag;
    @SerializedName("message")
    public String message;
    @SerializedName("code")
    public int code;

    public static class SoundCategory implements Serializable {

        @SerializedName("id")
        public int category_id;
        @SerializedName("category_name")
        public String category_name;
        @SerializedName("status")
        public int status;
        @SerializedName("sounds")
        public ArrayList<Sound> sounds;

    }

    public static class Sound implements Serializable {

        @SerializedName("id")
        public int id;
        @SerializedName("category_id")
        public int category_id;
        @SerializedName("sound_name")
        public String sound_name;
        @SerializedName("sound_url")
        public String sound_url;
        @SerializedName("sound_full_url")
        public String sound_full_url;
        @SerializedName("sound_size")
        public String sound_size;
        @SerializedName("status")
        public int status;
        @SerializedName("lyrics")
        public String lyrics;
    }
}
