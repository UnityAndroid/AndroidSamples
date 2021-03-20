package lyrically.photovideomaker.particl.ly.musicallybeat.model;

public class CropImage {

    public int id;
    public String imagepath;
    public String croppath;
    public int status;
    public int position;

    public CropImage(int id, String imagepath, String croppath, int status, int position) {
        this.id = id;
        this.imagepath = imagepath;
        this.croppath = croppath;
        this.status = status;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getCroppath() {
        return croppath;
    }

    public void setCroppath(String croppath) {
        this.croppath = croppath;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
