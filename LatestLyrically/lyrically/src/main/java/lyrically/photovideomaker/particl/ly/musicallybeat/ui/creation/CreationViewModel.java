package lyrically.photovideomaker.particl.ly.musicallybeat.ui.creation;

import android.app.Application;

import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.CreationEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository.CreationRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class CreationViewModel extends AndroidViewModel {

    private CreationRepository repository;

    public CreationViewModel(@NonNull Application application) {
        super(application);
        repository = new CreationRepository(application);
    }

    public void deleteCreation(CreationEntity entity) {
        repository.delete(entity);
    }

    public List<CreationEntity> getAllCreations() {
        return repository.getAll();
    }

    public List<CreationEntity> getAllCreationbyFilepath(String filepath) {
        return repository.getCreationbyFilepath(filepath);
    }

}
