package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.AppDatabase;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao.ThemeDao;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ThemeEntity;


public class ThemeRepository {

    private ThemeDao dao;

    public ThemeRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        dao = database.themeDao();
    }

    public void insert(ThemeEntity entities) {
        new InsertNoteAsyncTask(dao).execute(entities);
    }

    public ThemeEntity check(int id) {
        return dao.check(id);
    }


    private static class InsertNoteAsyncTask extends AsyncTask<ThemeEntity, Void, Void> {
        private ThemeDao dao;

        private InsertNoteAsyncTask(ThemeDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ThemeEntity... entities) {
            dao.insert(entities[0]);
            return null;
        }
    }


}
