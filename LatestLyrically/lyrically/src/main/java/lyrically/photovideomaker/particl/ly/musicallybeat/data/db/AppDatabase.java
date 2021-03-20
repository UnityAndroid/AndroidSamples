package lyrically.photovideomaker.particl.ly.musicallybeat.data.db;

import android.content.Context;


import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao.CreationDao;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao.ImageDao;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao.ParticleDao;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao.SoundDao;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao.ThemeDao;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.CreationEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ImageEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ParticleEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.SoundEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ThemeEntity;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {ThemeEntity.class,SoundEntity.class, ImageEntity.class, CreationEntity.class, ParticleEntity.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase instance;

    public abstract ThemeDao themeDao();
    public abstract SoundDao soundDao();
    public abstract ImageDao imageDao();
    public abstract CreationDao creationDao();
    public abstract ParticleDao particleDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "app_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
