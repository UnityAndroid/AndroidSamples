package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.AppDatabase;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao.ParticleDao;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ParticleEntity;

import java.util.List;

public class ParticleRepository {

    private ParticleDao dao;

    public ParticleRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        dao = database.particleDao();
    }

    public void insert(ParticleEntity entity) {
        new InsertNoteAsyncTask(dao).execute(entity);
    }

    public ParticleEntity check(int id) {
        return dao.check(id);
    }

    public String getparticlefile(int id) {
        return dao.getparticlefile(id);
    }

    public List<ParticleEntity> getAll() {
        return dao.getAll();
    }

    public List<ParticleEntity> getAllbyCatId(int id) {
        return dao.getAllbyCatId(id);
    }

    public String getthumbfile(int id) {
        return dao.getthumbfile(id);
    }

    private static class InsertNoteAsyncTask extends AsyncTask<ParticleEntity, Void, Void> {
        private ParticleDao dao;

        private InsertNoteAsyncTask(ParticleDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ParticleEntity... entities) {
            dao.insert(entities[0]);
            return null;
        }
    }


}
