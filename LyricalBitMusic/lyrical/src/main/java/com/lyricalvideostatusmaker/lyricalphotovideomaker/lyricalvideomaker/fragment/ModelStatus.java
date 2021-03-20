package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment;

public class ModelStatus {
    String full_path;
    String path;
    int type;

    public ModelStatus(String str) {
        this.full_path = str;
    }

    public ModelStatus(String str, String str2) {
        this.full_path = str;
        this.path = str2;
    }

    public ModelStatus(String str, String str2, int i) {
        this.full_path = str;
        this.path = str2;
        this.type = i;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getFull_path() {
        return this.full_path;
    }

    public void setFull_path(String str) {
        this.full_path = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }
}
