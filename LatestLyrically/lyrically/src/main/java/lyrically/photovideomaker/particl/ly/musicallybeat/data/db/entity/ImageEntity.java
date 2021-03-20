package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "image_table")
public class ImageEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String image_path;
    private String cropped_path;

    private int status;
    private int position;

    public ImageEntity(String image_path, String cropped_path, int status, int position) {
        this.image_path = image_path;
        this.cropped_path = cropped_path;
        this.status = status;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage_path() {
        return image_path;
    }

    public String getCropped_path() {
        return cropped_path;
    }

    public int getStatus() {
        return status;
    }

    public int getPosition() {
        return position;
    }
}
