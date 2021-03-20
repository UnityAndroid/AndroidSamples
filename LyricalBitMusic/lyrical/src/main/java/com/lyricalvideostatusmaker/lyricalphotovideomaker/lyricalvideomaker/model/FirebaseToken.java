package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model;

public class FirebaseToken {
    public String android_id, gcm_id;

    public FirebaseToken(String android_id, String gcm_id) {
        this.android_id = android_id;
        this.gcm_id = gcm_id;
    }
}
