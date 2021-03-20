package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.AppDatabase;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao.ImageDao;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ImageEntity;

import java.util.List;

public class ImageRepository {

    private ImageDao dao;

    public ImageRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        dao = database.imageDao();
    }

    public void insert(ImageEntity entity) {
        new InsertAsyncTask(dao).execute(entity);
    }
    public void update(ImageEntity entity) {
        new UpdateAsyncTask(dao).execute(entity);
    }

    public void delete(ImageEntity entity) {
        new DeleteAsyncTask(dao).execute(entity);
    }

    public void deleteAll() {
        new DeleteAllAsyncTask(dao).execute();
    }

    public List<ImageEntity> getAll() {
        return dao.getAll();
    }
    public List<ImageEntity> getArrangedAll() {
        return dao.getArrangedAll();
    }

    public int getCount() {
        return dao.getCount();
    }

    public ImageEntity check(String image_path) {
        return dao.check(image_path);
    }

    private static class InsertAsyncTask extends AsyncTask<ImageEntity, Void, Void> {
        private ImageDao dao;

        private InsertAsyncTask(ImageDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ImageEntity... entities) {
            dao.insert(entities[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<ImageEntity, Void, Void> {
        private ImageDao dao;

        private UpdateAsyncTask(ImageDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ImageEntity... entities) {
            dao.update(entities[0]);
            return null;
        }
    }


    private static class DeleteAsyncTask extends AsyncTask<ImageEntity, Void, Void> {

        private ImageDao dao;

        private DeleteAsyncTask(ImageDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(ImageEntity... entities) {
            dao.delete(entities[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private ImageDao dao;

        private DeleteAllAsyncTask(ImageDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            return null;
        }
    }
}
