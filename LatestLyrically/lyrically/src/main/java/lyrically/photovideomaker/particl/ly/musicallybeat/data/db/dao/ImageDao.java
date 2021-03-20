package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao;


import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ImageEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ImageEntity entity);

    @Delete
    void delete(ImageEntity entity);

    @Update
    void update(ImageEntity entity);

    @Query("DELETE FROM image_table")
    void deleteAll();

    @Query("SELECT * FROM image_table WHERE image_path = :data")
    ImageEntity check(String data);

    @Query("SELECT * FROM image_table")
    List<ImageEntity> getAll();

    @Query("SELECT * FROM image_table ORDER BY position asc")
    List<ImageEntity> getArrangedAll();

    @Query("SELECT COUNT(*) FROM image_table")
    int getCount();
}
