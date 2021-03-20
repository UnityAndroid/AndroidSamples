package lyrically.photovideomaker.particl.ly.musicallybeat.model;

public class MoreAppsModel {

    String iconurl, appurl, appname;
    int position;

    public MoreAppsModel(String iconurl, String appurl, String appname, int position) {
        this.iconurl = iconurl;
        this.appurl = appurl;
        this.appname = appname;
        this.position = position;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public String getAppurl() {
        return appurl;
    }

    public void setAppurl(String appurl) {
        this.appurl = appurl;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
