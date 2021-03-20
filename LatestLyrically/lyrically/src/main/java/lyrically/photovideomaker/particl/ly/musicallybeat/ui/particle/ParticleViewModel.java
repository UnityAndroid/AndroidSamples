package lyrically.photovideomaker.particl.ly.musicallybeat.ui.particle;

import android.app.Application;

import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ParticleEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository.ParticleRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class ParticleViewModel extends AndroidViewModel {

    private ParticleRepository repository;

    public ParticleViewModel(@NonNull Application application) {
        super(application);
        repository = new ParticleRepository(application);
    }

    public void insert(ParticleEntity entity) {
        repository.insert(entity);
    }

    public String getParticleFile(int id) {
        return repository.getparticlefile(id);
    }

    public ParticleEntity checkParticle(int id) {
        return repository.check(id);
    }
}

