package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.AppDatabase;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao.CreationDao;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.CreationEntity;

import java.util.List;

public class CreationRepository {

    private CreationDao dao;

    public CreationRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        dao = database.creationDao();
    }

    public void insert(CreationEntity entity) {
        new InsertNoteAsyncTask(dao).execute(entity);
    }

    public void delete(CreationEntity entity) {
        new DeleteNoteAsyncTask(dao).execute(entity);
    }

    public List<CreationEntity> getAll() {
        return dao.getAll();
    }

    public List<CreationEntity> getCreationbyFilepath(String filepath) {
        return dao.getCreationbyPath(filepath);
    }

    private static class InsertNoteAsyncTask extends AsyncTask<CreationEntity, Void, Void> {
        private CreationDao dao;

        private InsertNoteAsyncTask(CreationDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(CreationEntity... entities) {
            dao.insert(entities[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<CreationEntity, Void, Void> {
        private CreationDao dao;

        private DeleteNoteAsyncTask(CreationDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(CreationEntity... entities) {
            dao.delete(entities[0]);
            return null;
        }
    }


}
