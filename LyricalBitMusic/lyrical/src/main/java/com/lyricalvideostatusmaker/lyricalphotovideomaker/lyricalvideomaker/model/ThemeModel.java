package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model;

import java.util.ArrayList;

public class ThemeModel {

    public int Id, Cat_Id, Theme_Id;
    public String Theme_Name, Thumnail_Big, Thumnail_Small, SoundFile, sound_size, GameobjectName, lyrics;
    public int viewType;
    public ArrayList<CategoryModel> categoryArrayList = new ArrayList<CategoryModel>();
    private ArrayList<SliderItem> mSliderItems = new ArrayList<SliderItem>();

    public ThemeModel() {

    }

    public ThemeModel(int Id, int Cat_Id, String Theme_Name, String Thumnail_Big, String Thumnail_Small, String SoundFile,
                      String sound_size, String GameobjectName, int Theme_Id, String lyrics, int viewType) {
        this.Id = Id;
        this.Cat_Id = Cat_Id;
        this.Theme_Name = Theme_Name;
        this.Thumnail_Big = Thumnail_Big;
        this.Thumnail_Small = Thumnail_Small;
        this.SoundFile = SoundFile;
        this.sound_size = sound_size;
        this.GameobjectName = GameobjectName;
        this.Theme_Id = Theme_Id;
        this.lyrics = lyrics;
        this.viewType = viewType;
    }

    public String getSoundFile() {
        return SoundFile;
    }

    public String getGameobjectName() {
        return GameobjectName;
    }

    public int getTheme_Id() {
        return Theme_Id;
    }

    public int getId() {
        return Id;
    }

    public int getCat_Id() {
        return Cat_Id;
    }

    public String getSound_size() {
        return sound_size;
    }

    public String getTheme_Name() {
        return Theme_Name;
    }

    public String getThumnail_Big() {
        return Thumnail_Big;
    }

    public String getThumnail_Small() {
        return Thumnail_Small;
    }

    public void setSound_size(String sound_size) {
        this.sound_size = sound_size;
    }

    public void setId(int id) {
        Id = id;
    }

    public void setCat_Id(int cat_Id) {
        Cat_Id = cat_Id;
    }

    public void setGameobjectName(String gameobjectName) {
        GameobjectName = gameobjectName;
    }

    public void setSoundFile(String soundFile) {
        SoundFile = soundFile;
    }

    public void setTheme_Id(int theme_Id) {
        Theme_Id = theme_Id;
    }

    public void setTheme_Name(String theme_Name) {
        Theme_Name = theme_Name;
    }

    public void setThumnail_Big(String thumnail_Big) {
        Thumnail_Big = thumnail_Big;
    }

    public void setThumnail_Small(String thumnail_Small) {
        Thumnail_Small = thumnail_Small;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public ArrayList<CategoryModel> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<CategoryModel> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    public ArrayList<SliderItem> getmSliderItems() {
        return mSliderItems;
    }

    public void setmSliderItems(ArrayList<SliderItem> mSliderItems) {
        this.mSliderItems = mSliderItems;
    }
}
