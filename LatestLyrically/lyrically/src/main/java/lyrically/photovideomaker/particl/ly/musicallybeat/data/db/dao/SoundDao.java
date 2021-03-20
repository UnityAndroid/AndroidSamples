package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao;



import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.SoundEntity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface SoundDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(SoundEntity entity);

    @Query("SELECT * FROM sound_table WHERE sound_url = :data")
    SoundEntity check(String data);

    @Query("SELECT sound_path FROM sound_table WHERE sound_url = :data")
    String getfile(String data);
}
