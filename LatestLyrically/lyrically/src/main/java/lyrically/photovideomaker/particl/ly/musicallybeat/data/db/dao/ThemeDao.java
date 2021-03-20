package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao;



import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ThemeEntity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ThemeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ThemeEntity entity);

    @Query("SELECT * FROM theme_table WHERE id = :data ORDER BY id DESC")
    ThemeEntity check(int data);
}
