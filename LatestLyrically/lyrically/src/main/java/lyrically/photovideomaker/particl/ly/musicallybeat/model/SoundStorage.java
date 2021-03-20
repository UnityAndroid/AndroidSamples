package lyrically.photovideomaker.particl.ly.musicallybeat.model;

import java.io.Serializable;
import java.util.ArrayList;

public class SoundStorage implements Serializable {


    public String folder_name;
    public ArrayList<SoundFile> sound_files;

    public SoundStorage() {
    }

    public String getFolder_name() {
        return folder_name;
    }

    public void setFolder_name(String folder_name) {
        this.folder_name = folder_name;
    }

    public ArrayList<SoundFile> getSound_files() {
        return sound_files;
    }

    public void setSound_files(ArrayList<SoundFile> sound_files) {
        this.sound_files = sound_files;
    }
}