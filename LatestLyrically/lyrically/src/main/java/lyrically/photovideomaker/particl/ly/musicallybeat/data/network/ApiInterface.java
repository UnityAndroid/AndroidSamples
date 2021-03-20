package lyrically.photovideomaker.particl.ly.musicallybeat.data.network;


import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ParticleResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.SoundResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ThemeResponse;


import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("VideoCatgoryList.php")
    Observable<ThemeResponse> getThemes(@Field("package_name") String package_name);

    @FormUrlEncoded
    @POST("SoundCategoryList.php")
    Observable<SoundResponse> getSounds(@Field("package_name") String package_name);

    @FormUrlEncoded
    @POST("ParticleCategoryList.php")
    Observable<ParticleResponse> getParticles(@Field("package_name") String package_name);

}
