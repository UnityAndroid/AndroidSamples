package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "particle_table")
public class ParticleEntity {

    @PrimaryKey()
    private int id;

    private int p_id;

    private String theme_name;
    private String prefab_name;

    private String thumb_url;
    private String thumb_path;

    private String particle_url;
    private String particle_path;

    public ParticleEntity(int id, int p_id, String theme_name, String prefab_name, String thumb_url, String thumb_path, String particle_url, String particle_path) {
        this.id = id;
        this.p_id = p_id;
        this.theme_name = theme_name;
        this.prefab_name = prefab_name;
        this.thumb_url = thumb_url;
        this.thumb_path = thumb_path;
        this.particle_url = particle_url;
        this.particle_path = particle_path;
    }

    public int getId() {
        return id;
    }

    public int getP_id() {
        return p_id;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public String getPrefab_name() {
        return prefab_name;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public String getThumb_path() {
        return thumb_path;
    }

    public String getParticle_url() {
        return particle_url;
    }

    public String getParticle_path() {
        return particle_path;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public void setThumb_path(String thumb_path) {
        this.thumb_path = thumb_path;
    }

    public void setParticle_url(String particle_url) {
        this.particle_url = particle_url;
    }

    public void setParticle_path(String particle_path) {
        this.particle_path = particle_path;
    }
}
