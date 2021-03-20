package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model;

public class CategoryModel {

    public int Cat_Id;
    public String Name, Thumbnail;
    public boolean isTrue;

    public CategoryModel(int cat_Id, String category_name, String thumbnail, boolean isTrue) {
        Cat_Id = cat_Id;
        Name = category_name;
        Thumbnail = thumbnail;
        this.isTrue = isTrue;
    }

    public int getCat_Id() {
        return Cat_Id;
    }

    public String getName() {
        return Name;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setCat_Id(int cat_Id) {
        Cat_Id = cat_Id;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }

    public void setTrue(boolean aTrue) {
        isTrue = aTrue;
    }

    public boolean isTrue() {
        return isTrue;
    }
}
