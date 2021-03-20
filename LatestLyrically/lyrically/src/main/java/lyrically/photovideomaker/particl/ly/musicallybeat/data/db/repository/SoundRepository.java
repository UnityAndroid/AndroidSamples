package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.AppDatabase;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao.SoundDao;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.SoundEntity;


public class SoundRepository {

    private SoundDao dao;

    public SoundRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        dao = database.soundDao();
    }

    public void insert(SoundEntity entity) {
        new InsertNoteAsyncTask(dao).execute(entity);
    }

    public SoundEntity check(String sound_url) {
        return dao.check(sound_url);
    }

    public String getfile(String sound_url) {
        return dao.getfile(sound_url);
    }

    private static class InsertNoteAsyncTask extends AsyncTask<SoundEntity, Void, Void> {
        private SoundDao dao;

        private InsertNoteAsyncTask(SoundDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(SoundEntity... entities) {
            dao.insert(entities[0]);
            return null;
        }
    }


}
