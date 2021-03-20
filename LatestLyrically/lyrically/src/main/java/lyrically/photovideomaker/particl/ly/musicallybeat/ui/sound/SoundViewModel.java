package lyrically.photovideomaker.particl.ly.musicallybeat.ui.sound;

import android.app.Application;


import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.SoundEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository.SoundRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class SoundViewModel extends AndroidViewModel {

    private SoundRepository repository;

    public SoundViewModel(@NonNull Application application) {
        super(application);
        repository = new SoundRepository(application);
    }

    public void insert(SoundEntity entity) {
        repository.insert(entity);
    }

    public String GetMusicFileByUrl(String sound_url) {
        return repository.getfile(sound_url);
    }

    public SoundEntity checkSound(String sound_url) {
        return repository.check(sound_url);
    }
}
