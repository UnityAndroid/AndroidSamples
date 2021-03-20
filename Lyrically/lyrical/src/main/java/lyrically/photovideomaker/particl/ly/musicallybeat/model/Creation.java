package lyrically.photovideomaker.particl.ly.musicallybeat.model;

public class Creation {
    public int themeid, id;
    public String tname, filepath;
    public int is_Ad;

    public Creation(int themeid, int id, String tname, String filepath, int ad) {
        this.themeid = themeid;
        this.id = id;
        this.tname = tname;
        this.filepath = filepath;
        this.is_Ad = ad;
    }
}
