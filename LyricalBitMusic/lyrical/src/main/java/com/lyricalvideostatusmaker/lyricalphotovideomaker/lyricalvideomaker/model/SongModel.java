package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model;

public class SongModel {

    int id, Cat_Id;
    String sound_name, sound_url, sound_full_url, sound_size, lyrics;

    public SongModel(int id, int Cat_Id, String sound_name, String sound_url, String sound_full_url, String sound_size, String lyrics) {
        this.id = id;
        this.Cat_Id = Cat_Id;
        this.sound_name = sound_name;
        this.sound_url = sound_url;
        this.sound_full_url = sound_full_url;
        this.sound_size = sound_size;
        this.lyrics = lyrics;
    }

    public int getId() {
        return id;
    }

    public int getCat_Id() {
        return Cat_Id;
    }

    public String getSound_full_url() {
        return sound_full_url;
    }

    public String getSound_name() {
        return sound_name;
    }

    public String getSound_size() {
        return sound_size;
    }

    public String getSound_url() {
        return sound_url;
    }

    public void setCat_Id(int cat_Id) {
        Cat_Id = cat_Id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSound_full_url(String sound_full_url) {
        this.sound_full_url = sound_full_url;
    }

    public void setSound_name(String sound_name) {
        this.sound_name = sound_name;
    }

    public void setSound_size(String sound_size) {
        this.sound_size = sound_size;
    }

    public void setSound_url(String sound_url) {
        this.sound_url = sound_url;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }
}
