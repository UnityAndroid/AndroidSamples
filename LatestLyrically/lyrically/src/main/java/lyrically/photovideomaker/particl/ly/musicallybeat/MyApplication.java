package lyrically.photovideomaker.particl.ly.musicallybeat;

import android.app.Application;

import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ParticleResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.SoundResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ThemeResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.CropImage;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.ImagesFolder;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.SoundStorage;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.AppTheme;

import java.util.ArrayList;

public class MyApplication extends Application {

    private static MyApplication sInstance;
    public AppTheme appTheme = new AppTheme();
    public boolean isFirst = true;
    public ThemeResponse themeResponse;
    public SoundResponse soundResponse;
    public ParticleResponse particleResponse;

    public ArrayList<ImagesFolder> imageFolders = new ArrayList<>();
    public ArrayList<SoundStorage> soundArrayList = new ArrayList<>();
    public ArrayList<CropImage> tempImages = new ArrayList<>();

    public static synchronized MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

    }

    public ThemeResponse getThemeResponse() {
        return themeResponse;
    }

    public void setThemeResponse(ThemeResponse themeResponse) {
        this.themeResponse = themeResponse;
    }

    public SoundResponse getSoundResponse() {
        return soundResponse;
    }

    public void setSoundResponse(SoundResponse soundResponse) {
        this.soundResponse = soundResponse;
    }

    public ArrayList<CropImage> getCropImages() {
        return appTheme.cropImages;
    }

    public ArrayList<CropImage> getTempImages() {
        return tempImages;
    }

    public ParticleResponse getParticleResponse() {
        return particleResponse;
    }

    public void setParticleResponse(ParticleResponse particleResponse) {
        this.particleResponse = particleResponse;
    }

    public void setSound_name(String sound_name) {
        appTheme.sound_name = sound_name;
    }

    public void setSound_path(String sound_path) {
        appTheme.sound_path = sound_path;
    }
}
