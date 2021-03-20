package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment;

public class VideoModel {
    boolean isSelected;
    long mediaTakenMilli;
    String name;
    String path;

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
