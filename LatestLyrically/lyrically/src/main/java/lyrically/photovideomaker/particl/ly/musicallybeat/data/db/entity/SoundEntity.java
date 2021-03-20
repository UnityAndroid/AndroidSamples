package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "sound_table")
public class SoundEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String sound_url;
    private String sound_path;

    private String lyrics;

    public SoundEntity(String sound_url, String sound_path, String lyrics) {
        this.sound_url = sound_url;
        this.sound_path = sound_path;
        this.lyrics = lyrics;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSound_url() {
        return sound_url;
    }

    public String getSound_path() {
        return sound_path;
    }

    public String getLyrics() {
        return lyrics;
    }
}
