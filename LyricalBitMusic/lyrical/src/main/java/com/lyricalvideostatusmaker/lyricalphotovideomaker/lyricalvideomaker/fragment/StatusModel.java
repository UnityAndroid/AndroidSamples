package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment;

public class StatusModel {
    boolean isSelected;
    boolean isVideo;
    long mediaTakenMilli;
    String name;
    String path;

    public boolean isVideo() {
        return this.isVideo;
    }

    public void setVideo(boolean video) {
        this.isVideo = video;
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
