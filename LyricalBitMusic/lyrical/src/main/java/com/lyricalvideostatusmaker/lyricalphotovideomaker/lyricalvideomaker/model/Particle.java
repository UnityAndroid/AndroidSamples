package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model;

public class Particle {
    public int id, category_id, is_Ad;
    public String theme_name, prefab_name, thumb_url, particle_url, theme_url;

    public Particle(int id, int category_id, String theme_name, String prefab_name, String thumb_url, String particle_url, String theme_url, int is_Ad) {
        this.id = id;
        this.category_id = category_id;
        this.theme_name = theme_name;
        this.prefab_name = prefab_name;
        this.thumb_url = thumb_url;
        this.particle_url = particle_url;
        this.theme_url = theme_url;
        this.is_Ad = is_Ad;
    }
}
