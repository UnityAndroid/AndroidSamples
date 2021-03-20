package lyrically.photovideomaker.particl.ly.musicallybeat.ui.Theme;

import android.app.Application;


import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.SoundEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ThemeEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository.SoundRepository;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository.ThemeRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class ThemeViewModel extends AndroidViewModel {

    private ThemeRepository repository;
    private SoundRepository soundRepository;

    public ThemeViewModel(@NonNull Application application) {
        super(application);
        repository = new ThemeRepository(application);
        soundRepository = new SoundRepository(application);
    }

    public void insert(ThemeEntity entity) {
        repository.insert(entity);
    }

    public void insertSound(SoundEntity entity) {
        soundRepository.insert(entity);
    }

    public ThemeEntity checkTheme(int id) {
        return repository.check(id);
    }

    public SoundEntity checkSound(String sound_url) {
        return soundRepository.check(sound_url);
    }


}
