package lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ThemeResponse implements Serializable {

    @SerializedName("data")
    public ArrayList<ThemeCategory> data = null;

    @SerializedName("flag")
    public boolean flag;
    @SerializedName("message")
    public String message;
    @SerializedName("code")
    public int code;

    public static class ThemeCategory implements Serializable {

        @SerializedName("Cat_Id")
        public int Cat_Id;
        @SerializedName("Category_Name")
        public String Category_Name;
        @SerializedName("Icon")
        public String Icon;
        @SerializedName("status")
        public int status;
        @SerializedName("background")
        public String background;
        @SerializedName("videos")
        public ArrayList<Theme> videos;

    }

    public static class Theme implements Serializable {

        @SerializedName("Id")
        public int Id;
        @SerializedName("Cat_Id")
        public int Cat_Id;
        @SerializedName("Theme_Id")
        public int Theme_Id;
        @SerializedName("Theme_Name")
        public String Theme_Name;
        @SerializedName("Thumnail_Big")
        public String Thumnail_Big;
        @SerializedName("Thumnail_Small")
        public String Thumnail_Small;
        @SerializedName("sound_name")
        public String sound_name;
        @SerializedName("SoundFile")
        public String SoundFile;
        @SerializedName("sound_size")
        public String sound_size;
        @SerializedName("GameobjectName")
        public String GameobjectName;
        @SerializedName("Is_Preimum")
        public int Is_Preimum;
        @SerializedName("Status")
        public int Status;
        @SerializedName("lyrics")
        public String lyrics;
    }
}
