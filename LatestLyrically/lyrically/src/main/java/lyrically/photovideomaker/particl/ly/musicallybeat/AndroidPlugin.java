package lyrically.photovideomaker.particl.ly.musicallybeat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.CreationEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.entity.ParticleEntity;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository.CreationRepository;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.db.repository.ParticleRepository;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.RetrofitClient;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ParticleResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.SoundResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ThemeResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.ImagesFolder;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.SoundStorage;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.Theme.ThemeActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.creation.CreationActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.iarrange.IarrangeActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.iselect.IselectActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.particle.ParticleActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.sound.SoundActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.text.TextActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ConstantUtils;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.StorageUtils;

import com.unity3d.player.UnityPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function3;
import io.reactivex.schedulers.Schedulers;

public class AndroidPlugin {


    public static int HOME_ACTIVITY = 100;
    public static int THEME_ACTIVITY = HOME_ACTIVITY + 1;
    public static int IMAGE_ACTIVITY = THEME_ACTIVITY + 1;
    public static int CROP_ACTIVITY = IMAGE_ACTIVITY + 1;
    public static int ARRANGE_ACTIVITY = CROP_ACTIVITY + 1;
    public static int SOUND_ACTIVITY = ARRANGE_ACTIVITY + 1;
    public static int CREATION_ACTIVITY = SOUND_ACTIVITY + 1;
    public static int VIDEO_ACTIVITY = CREATION_ACTIVITY + 1;
    public static int PARTICLE_ACTIVITY = VIDEO_ACTIVITY + 1;
    public static int TEXT_ACTIVITY = PARTICLE_ACTIVITY + 1;

    public static AndroidPlugin instance;
    public static Context context;

    private Intent Intent;

    public static AndroidPlugin instance() {
        if (instance == null) {
            instance = new AndroidPlugin();
        }
        return instance;
    }

    public static void setContext(Context contex) {
        context = contex;
    }

    public static String getDirectoryDCIM() {
        return ConstantUtils.GetMainVideoPath(context);
    }

    public static String GetParticleThumbnailPath() {
        return ConstantUtils.GetParticleThumbnailPath(context);
    }

    public static String GetParticlePath() {
        return ConstantUtils.GetParticlePath(context);
    }
    public static void CallMain(Context context) {
        Intent intent = new Intent(context, ThemeActivity.class);
        AndroidPlugin.instance().start((UnityPlayerActivity) context, intent, THEME_ACTIVITY);
    }


    public static void CallHome(Context context) {
        Intent intent = new Intent(context, ThemeActivity.class);
        AndroidPlugin.instance().start((UnityPlayerActivity) context, intent, THEME_ACTIVITY);
    }

    public static void CallSongs(Context context) {
        Intent intent = new Intent(context, SoundActivity.class);
        AndroidPlugin.instance().start((UnityPlayerActivity) context, intent, SOUND_ACTIVITY);
    }

    public static void CallGallery(Context context) {
        Intent intent = new Intent(context, IselectActivity.class);
        AndroidPlugin.instance().start((UnityPlayerActivity) context, intent, IMAGE_ACTIVITY);
    }

    public static void ArrangePhotos(Context context) {
        if (MyApplication.getInstance().appTheme.cropImages.size() > 0) {
            Intent intent = new Intent(context, IarrangeActivity.class);
            AndroidPlugin.instance().start((UnityPlayerActivity) context, intent, ARRANGE_ACTIVITY);
        } else {
            Toast.makeText(context, "Select Photos First", Toast.LENGTH_LONG).show();
        }
    }


    public static void editInput(Context context) {
        Intent intent = new Intent(context, TextActivity.class);
        intent.putExtra("text", "text");
        AndroidPlugin.instance().start((UnityPlayerActivity) context, intent, TEXT_ACTIVITY);
    }


    public static void CallParticleStore(String name) {
        Intent intent = new Intent(context, ParticleActivity.class);
        intent.putExtra("category", name);
        AndroidPlugin.instance().start((UnityPlayerActivity) context, intent, PARTICLE_ACTIVITY);
    }

    public static void CallCreation(Context context) {
        Intent intent = new Intent(context, CreationActivity.class);
        AndroidPlugin.instance().start((UnityPlayerActivity) context, intent, CREATION_ACTIVITY);
    }


    public void start(@NonNull Activity activity, Intent intent, int requestCode) {
        activity.startActivityForResult(intent, requestCode);
    }

    public static void UnityCall(String ObjectName, String Methodname, String Data) {
        UnityPlayer.UnitySendMessage(ObjectName, Methodname, Data);
    }

    public static void LuanchApp(Context context) {
        fetchSounds(context);
    }

    public static void fetchSounds(Context context) {
        Observable<ThemeResponse> themeResponseObservable = RetrofitClient.getInstance().getApi().getThemes(context.getPackageName());
        Observable<SoundResponse> soundResponseObservable = RetrofitClient.getInstance().getApi().getSounds(context.getPackageName());
        Observable<ParticleResponse> particleResponseObservable = RetrofitClient.getInstance().getApi().getParticles(context.getPackageName());

        Observable.zip(themeResponseObservable.subscribeOn(Schedulers.io()), soundResponseObservable.subscribeOn(Schedulers.io()), particleResponseObservable.subscribeOn(Schedulers.io()), new Function3<ThemeResponse, SoundResponse, ParticleResponse, List<String>>() {
            @Override
            public ArrayList<String> apply(ThemeResponse type1, SoundResponse type2, ParticleResponse type3) {
                ArrayList<String> list = new ArrayList<String>();
                MyApplication.getInstance().setThemeResponse(type1);
                MyApplication.getInstance().setSoundResponse(type2);
                MyApplication.getInstance().setParticleResponse(type3);
                return list;
            }
        }).subscribe(new Observer<List<String>>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(List<String> strings) {
            }

            @Override
            public void onError(Throwable e) {
                ConstantUtils.showErrorLog(e.toString());
                UnityCall("LaunchApp", "ShowNoInternetDialog", "");
            }

            @Override
            public void onComplete() {
                SearchStorage(context);
            }
        });

    }

    @SuppressLint("CheckResult")
    public static void SearchStorage(Context context) {
        Observable<ArrayList<ImagesFolder>> observable = Observable.just(Objects.requireNonNull(StorageUtils.SearchStorageAsync(context)));
        Observable<ArrayList<SoundStorage>> observable2 = Observable.just(Objects.requireNonNull(StorageUtils.searchSoundAsync(context)));

        Observable.zip(observable.subscribeOn(Schedulers.io()), observable2.subscribeOn(Schedulers.io()), (BiFunction<ArrayList<ImagesFolder>, ArrayList<SoundStorage>, Object>) (imagesFolders, soundStorages) -> 1).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Object o) {
            }

            @Override
            public void onError(Throwable e) {
                ConstantUtils.showErrorLog(e.toString());
                UnityCall("LaunchApp", "ShowNoInternetDialog", "");
            }

            @Override
            public void onComplete() {
                doWork();
            }
        });

    }

    public static void doWork() {
        ConstantUtils.showErrorLog("done");
        UnityCall("Main Camera", "LoadPreviewAndroidScreen", "Good Morning");
    }

    public static void getTheme() {

        JSONObject object = new JSONObject();
        try {
            object.put("TemplateId", MyApplication.getInstance().appTheme.GameobjectName);
            object.put("ThemeId", MyApplication.getInstance().appTheme.Theme_Id);
            object.put("sound", MyApplication.getInstance().appTheme.sound_path);
            object.put("image", MyApplication.getInstance().appTheme.image_file);
            object.put("lyricsContent", MyApplication.getInstance().appTheme.lyrics);
        } catch (JSONException e) {
            ConstantUtils.showErrorLog(e.toString());
        } finally {
            UnityCall("CategoryManagement", "OnLoadUserData", object.toString());
        }
    }


    public static void downloadedParticle(String json) {

        MyApplication application = MyApplication.getInstance();
        int uniqueId = 0;
        ParticleRepository repository = new ParticleRepository(application);

        try {

            uniqueId = Integer.parseInt(json);

            ParticleResponse.Particle particle = featchParticleById(uniqueId);

            String Particlefilename = particle.particle_url.substring(particle.particle_url.lastIndexOf("/") + 1);
            String Imagefilename = particle.thumb_url.substring(particle.thumb_url.lastIndexOf("/") + 1);

            ParticleEntity particleEntity = repository.check(uniqueId);
            ParticleEntity entity = new ParticleEntity(
                    uniqueId,
                    particle.category_id,
                    particle.theme_name,
                    particle.prefab_name,
                    "", "", "", ""
            );

            if (particleEntity == null) {
                if (new File(Imagefilename).exists()) {

                    entity.setThumb_url("");
                    entity.setThumb_path(ConstantUtils.GetParticleThumbnailPath(context) + Imagefilename);
                    entity.setParticle_url(particle.particle_url);
                    entity.setParticle_path(ConstantUtils.GetParticlePath(context) + Particlefilename);
                } else {

                    entity.setThumb_url(particle.thumb_url);
                    entity.setThumb_path(particle.thumb_url);
                    entity.setParticle_url("");
                    entity.setParticle_path(ConstantUtils.GetParticlePath(context) + Particlefilename);
                }
            } else {


                entity.setThumb_url(particleEntity.getThumb_url());
                entity.setThumb_path(ConstantUtils.GetParticleThumbnailPath(context) + Imagefilename);
                entity.setParticle_path(particleEntity.getParticle_path());
                entity.setParticle_url("");
            }

            repository.insert(entity);

        } finally {

        }
    }

    public static ParticleResponse.Particle featchParticleById(int id) {
        for (int i = 0; i < MyApplication.getInstance().particleResponse.data.size(); i++) {
            List<ParticleResponse.Particle> particles = fetchparticle(i, id);
            if (particles.size() > 0) {
                return particles.get(0);
            }
        }
        return null;
    }

    public static List<ParticleResponse.Particle> fetchparticle(int position, int id) {
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

    public static void getParticles(Context context) {

        MyApplication application = MyApplication.getInstance();
        JSONObject jsonAdd = new JSONObject();

        ParticleRepository repository = new ParticleRepository(application);

        JSONArray partdetails = new JSONArray();
        JSONArray sortdetails = new JSONArray();

        try {

            for (ParticleResponse.ParticleCategory category : application.particleResponse.data) {
                for (ParticleResponse.Particle particle : category.particles) {

                    JSONObject object = new JSONObject();
                    object.put("UniqueIDNo", particle.id);

                    ParticleEntity particleEntity = repository.check(particle.id);
                    if (particleEntity != null) {
                        object.put("BundlePath", particleEntity.getParticle_path());
                        object.put("ImgPath", particleEntity.getThumb_path());
                        object.put("downloadWebUrl", particleEntity.getParticle_url());
                    } else {
                        String Particlefilename = particle.particle_url.substring(particle.particle_url.lastIndexOf("/") + 1);
                        object.put("BundlePath", ConstantUtils.GetParticlePath(context) + Particlefilename);
                        object.put("ImgPath", particle.thumb_url);
                        object.put("downloadWebUrl", particle.particle_url);
                    }

                    object.put("ThemeName", particle.theme_name);
                    object.put("prefbName", particle.prefab_name);
                    object.put("NewFlag", "1");
                    partdetails.put(object);
                }
            }

            for (ParticleResponse.SortParticle category : application.particleResponse.sort) {
                JSONObject object = new JSONObject();
                object.put("uniqueId", category.particle_id);
                object.put("priorityIndex", category.position);
                sortdetails.put(object);
            }

            jsonAdd.put("LyricsTemplates", partdetails);
            jsonAdd.put("orderList", sortdetails);

        } catch (JSONException e) {
            ConstantUtils.showErrorLog(e.toString());
        } finally {
            UnityPlayer.UnitySendMessage("LoadJsonData", "LoadJson", jsonAdd.toString());
        }
    }


    public static void getLyricsTheme() {

        JSONObject object = new JSONObject();
        try {
            object.put("TemplateId", MyApplication.getInstance().appTheme.GameobjectName);
            object.put("ThemeId", MyApplication.getInstance().appTheme.Theme_Id);
            object.put("sound", MyApplication.getInstance().appTheme.sound_path);
            object.put("image", MyApplication.getInstance().appTheme.image_file);
            object.put("lyricsContent", MyApplication.getInstance().appTheme.lyrics);

            ConstantUtils.showErrorLog(object.toString());

        } catch (JSONException e) {
            ConstantUtils.showErrorLog(e.toString());
        } finally {
            UnityCall("CategoryManagement", "OnLoadUserData", object.toString());
        }
    }


    public static ParticleResponse.Particle fetchbitparticle(int id) {
        return Observable.fromIterable(MyApplication.getInstance().particleResponse.bitdata)
                .subscribeOn(Schedulers.computation())
                .map(object -> object)
                .filter(object -> {
                    if (object.id == id)
                        return true;
                    else
                        return false;
                })
                .toList()
                .blockingGet().get(0);
    }


    public static void downloadedbitParticle(String json) {

        MyApplication application = MyApplication.getInstance();
        int uniqueId = 0;
        try {

            ParticleRepository repository = new ParticleRepository(application);
            uniqueId = Integer.parseInt(json);

            ParticleResponse.Particle particle = fetchbitparticle(uniqueId);

            String Particlefilename = particle.particle_url.substring(particle.particle_url.lastIndexOf("/") + 1);
            String Imagefilename = particle.thumb_url.substring(particle.thumb_url.lastIndexOf("/") + 1);

            ParticleEntity particleEntity = repository.check(uniqueId);
            ParticleEntity entity = new ParticleEntity(
                    uniqueId,
                    particle.category_id,
                    particle.theme_name,
                    particle.prefab_name,
                    "", "", "", ""
            );

            if (particleEntity == null) {
                if (new File(Imagefilename).exists()) {
                    entity.setThumb_url("");
                    entity.setThumb_path(ConstantUtils.GetParticleThumbnailPath(context) + Imagefilename);
                    entity.setParticle_url(particle.particle_url);
                    entity.setParticle_path(ConstantUtils.GetParticlePath(context) + Particlefilename);
                } else {
                    entity.setThumb_url(particle.thumb_url);
                    entity.setThumb_path(particle.thumb_url);
                    entity.setParticle_url("");
                    entity.setParticle_path(ConstantUtils.GetParticlePath(context) + Particlefilename);
                }
            } else {
                entity.setThumb_url(particleEntity.getThumb_url());
                entity.setThumb_path(ConstantUtils.GetParticleThumbnailPath(context) + Imagefilename);
                entity.setParticle_path(particleEntity.getParticle_path());
                entity.setParticle_url("");
            }

            repository.insert(entity);

        } finally {

        }
    }


    public static void getbitParticles(Context context) {

        MyApplication application = MyApplication.getInstance();
        JSONObject jsonAdd = new JSONObject();

        ParticleRepository repository = new ParticleRepository(application);

        JSONArray partdetails = new JSONArray();
        JSONArray sortdetails = new JSONArray();

        try {
            for (ParticleResponse.Particle particle : application.particleResponse.bitdata) {

                JSONObject object = new JSONObject();
                object.put("UniqueIDNo", particle.id);

                ParticleEntity particleEntity = repository.check(particle.id);
                if (particleEntity != null) {
                    object.put("BundlePath", particleEntity.getParticle_path());
                    object.put("ImgPath", particleEntity.getThumb_path());
                    object.put("downloadWebUrl", particleEntity.getParticle_url());
                } else {
                    String Particlefilename = particle.particle_url.substring(particle.particle_url.lastIndexOf("/") + 1);
                    object.put("BundlePath", Particlefilename);
                    object.put("ImgPath", particle.thumb_url);
                    object.put("downloadWebUrl", particle.particle_url);
                }

                object.put("ThemeName", particle.theme_name);
                object.put("prefbName", particle.prefab_name);
                object.put("NewFlag", "1");
                partdetails.put(object);
            }

            jsonAdd.put("LyricsTemplates", partdetails);
            jsonAdd.put("orderList", sortdetails);

        } catch (JSONException e) {
            ConstantUtils.showErrorLog(e.toString());
        } finally {
            ConstantUtils.showErrorLog(jsonAdd.toString());
            UnityPlayer.UnitySendMessage("LoadJsonData", "LoadJsonForParticle", jsonAdd.toString());
        }
    }

    public static void videoComplete(String filepath) {
        RefereshVideoList(context, filepath);
        CreationRepository repository = new CreationRepository(MyApplication.getInstance());
        CreationEntity entity = new CreationEntity(0, MyApplication.getInstance().appTheme.sound_name, filepath);
        repository.insert(entity);
        UnityPlayer.UnitySendMessage("DIALOG", "OnStopCapturing", "ads");
    }

    public static void RefereshVideoList(Context context, String str) {
        try {
            MediaScannerConnection.scanFile(context, new String[]{str}, new String[]{"video/mp4"}, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void playerscreen(String[] values) {
        Intent intent = new Intent(context, ThemeActivity.class);
        intent.putExtra("filepath", values[0]);
        AndroidPlugin.instance().start((UnityPlayerActivity) context, intent, THEME_ACTIVITY);
    }


}
