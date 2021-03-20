package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "theme_table")
public class ThemeEntity {

    @PrimaryKey()
    private int id;

    private String theme_name;
    private String gameobjectName;

    private String thumb_url;
    private String thumb_path;

    private String sound_url;
    private String sound_path;
    private String lyrics;

    public ThemeEntity(int id, String theme_name, String gameobjectName, String thumb_url, String thumb_path, String sound_url, String sound_path, String lyrics) {
        this.id = id;
        this.theme_name = theme_name;
        this.gameobjectName = gameobjectName;
        this.thumb_url = thumb_url;
        this.thumb_path = thumb_path;
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

    public String getTheme_name() {
        return theme_name;
    }

    public String getGameobjectName() {
        return gameobjectName;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public String getThumb_path() {
        return thumb_path;
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
