package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao;

import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.CreationEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface CreationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CreationEntity entity);

    @Delete
    void delete(CreationEntity entity);

    @Update
    void update(CreationEntity entity);

    @Query("SELECT * FROM creation_table")
    List<CreationEntity> getAll();

    @Query("SELECT * FROM creation_table where file_path = :data")
    List<CreationEntity> getCreationbyPath(String data);

}
