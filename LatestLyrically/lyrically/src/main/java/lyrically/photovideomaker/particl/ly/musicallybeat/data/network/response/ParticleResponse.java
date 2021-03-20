package lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ParticleResponse implements Serializable {

    @SerializedName("data")
    public ArrayList<ParticleCategory> data = null;

    @SerializedName("bitdata")
    public ArrayList<Particle> bitdata = null;

    @SerializedName("sort")
    public ArrayList<SortParticle> sort = null;

    @SerializedName("flag")
    public boolean flag;

    @SerializedName("message")
    public String message;

    @SerializedName("code")
    public int code;

    public static class ParticleCategory implements Serializable {

        @SerializedName("category_id")
        public int category_id;
        @SerializedName("category_name")
        public String category_name;
        @SerializedName("icon_url")
        public String icon_url;
        @SerializedName("position")
        public int position;
        @SerializedName("particles")
        public ArrayList<Particle> particles;

    }

    public static class Particle implements Serializable {
        @SerializedName("id")
        public int id;
        @SerializedName("category_id")
        public int category_id;
        @SerializedName("theme_name")
        public String theme_name;
        @SerializedName("prefab_name")
        public String prefab_name;
        @SerializedName("theme_url")
        public String theme_url;
        @SerializedName("thumb_url")
        public String thumb_url;
        @SerializedName("particle_url")
        public String particle_url;
    }

    public static class SortParticle implements Serializable {
        @SerializedName("id")
        public int id;
        @SerializedName("particle_id")
        public int particle_id;
        @SerializedName("theme_name")
        public String theme_name;
        @SerializedName("position")
        public int position;
    }
}
