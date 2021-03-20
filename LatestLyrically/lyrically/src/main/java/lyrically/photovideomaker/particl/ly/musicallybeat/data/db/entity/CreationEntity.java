package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "creation_table")
public class CreationEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public int theme_id;
    public String theme_name;
    public String file_path;

    public CreationEntity( int theme_id, String theme_name, String file_path) {
        this.theme_id = theme_id;
        this.theme_name = theme_name;
        this.file_path = file_path;
    }
}
