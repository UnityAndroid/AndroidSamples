package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other;

public class MoreAppsModel {

    String application, app_link, icon;
    boolean isInstalled;

    public MoreAppsModel(String application, String app_link, String icon, boolean isInstalled) {
        this.application = application;
        this.app_link = app_link;
        this.icon = icon;
        this.isInstalled = isInstalled;
    }

    public String getApp_link() {
        return app_link;
    }

    public String getApplication() {
        return application;
    }

    public String getIcon() {
        return icon;
    }

    public void setApp_link(String app_link) {
        this.app_link = app_link;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }

}
