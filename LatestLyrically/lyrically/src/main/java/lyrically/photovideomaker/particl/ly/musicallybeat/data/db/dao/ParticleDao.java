package lyrically.photovideomaker.particl.ly.musicallybeat.data.db.dao;

import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ParticleEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ParticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ParticleEntity entity);

    @Query("SELECT * FROM particle_table WHERE id = :data")
    ParticleEntity check(int data);

    @Query("SELECT particle_path FROM particle_table WHERE id = :data")
    String getparticlefile(int data);

    @Query("SELECT thumb_path FROM particle_table WHERE id = :data")
    String getthumbfile(int data);

    @Query("SELECT * FROM particle_table")
    List<ParticleEntity> getAll();

    @Query("SELECT * FROM particle_table where p_id=:data")
    List<ParticleEntity> getAllbyCatId(int data);

}
