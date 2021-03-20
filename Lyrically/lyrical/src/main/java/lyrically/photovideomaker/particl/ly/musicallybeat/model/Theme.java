package lyrically.photovideomaker.particl.ly.musicallybeat.model;

public class Theme {
    public int Id, Cat_Id, is_Ad, Theme_Id, counter;
    public String Theme_Name, Thumnail_Big, Thumnail_Small, SoundFile, sound_size, GameobjectName, lyrics;

    public Theme(int id, int cat_Id, int is_Ad, String theme_Name, String thumnail_Big, String thumnail_Small, String soundFile, String sound_size, String gameobjectName, int theme_Id, int counter, String lyrics) {
        Id = id;
        Cat_Id = cat_Id;
        this.is_Ad = is_Ad;
        Theme_Name = theme_Name;
        Thumnail_Big = thumnail_Big;
        Thumnail_Small = thumnail_Small;
        SoundFile = soundFile;
        this.sound_size = sound_size;
        GameobjectName = gameobjectName;
        Theme_Id = theme_Id;
        this.counter = counter;
        this.lyrics = lyrics;
    }
}
