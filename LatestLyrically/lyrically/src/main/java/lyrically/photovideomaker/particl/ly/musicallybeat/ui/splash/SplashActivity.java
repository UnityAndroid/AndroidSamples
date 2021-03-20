package lyrically.photovideomaker.particl.ly.musicallybeat.ui.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ParticleEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository.ParticleRepository;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ParticleResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ConstantUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AndroidPlugin.setContext(this);
        AndroidPlugin.LuanchApp(this);
    }

    public void checkparticle() {

        ParticleRepository repository = new ParticleRepository(MyApplication.getInstance());
        ParticleEntity particleEntity = repository.check(177);

        ConstantUtils.showErrorLog(featchParticleById(177).prefab_name + " check");
    }

    public ParticleResponse.Particle featchParticleById(int id) {
        for (int i = 0; i < MyApplication.getInstance().particleResponse.data.size(); i++) {
            List<ParticleResponse.Particle> particles = fetchparticle(i, id);
            if (particles.size() > 0) {
                return particles.get(0);
            }
        }
        return null;
    }

    public List<ParticleResponse.Particle> fetchparticle(int position, int id) {
        return Observable.fromIterable(MyApplication.getInstance().particleResponse.data.get(position).particles)
                .subscribeOn(Schedulers.computation())
                .map(object -> object)
                .filter(object -> {
                    if (object.id == id)
                        return true;
                    else
                        return false;
                })
                .toList()
                .blockingGet();
    }


}