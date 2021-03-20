package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment;

public class ImageModel {
    boolean isSelected;
    boolean isdownloaded;
    long mediaTakenMilli;
    String name;
    String path;

    public boolean isIsdownloaded() {
        return this.isdownloaded;
    }

    public void setIsdownloaded(boolean isdownloaded2) {
        this.isdownloaded = isdownloaded2;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path2) {
        this.path = path2;
    }

    public long getMediaTakenMilli() {
        return this.mediaTakenMilli;
    }

    public void setMediaTakenMilli(long mediaTakenMilli2) {
        this.mediaTakenMilli = mediaTakenMilli2;
    }
}
