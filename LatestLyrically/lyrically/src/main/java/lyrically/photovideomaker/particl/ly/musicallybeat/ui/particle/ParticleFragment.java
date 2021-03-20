package lyrically.photovideomaker.particl.ly.musicallybeat.ui.particle;

import android.content.Context;
import android.graphics.Outline;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ParticleResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ConstantUtils;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ScreenUtils;

import java.util.ArrayList;


public class ParticleFragment extends Fragment {

    Context context;
    MyApplication application;

    private ArrayList<ParticleResponse.Particle> particleArrayList = new ArrayList<>();
    private ParticleResponse.ParticleCategory category;
    RecyclerView recyclerViewParticles;
    ParticleAdapter particleAdapter;

    public ParticleFragment() {
    }

    public void setParticles(ArrayList<ParticleResponse.Particle> particleArrayList, ParticleResponse.ParticleCategory category) {
        this.particleArrayList = particleArrayList;
        this.category = category;
        ConstantUtils.showErrorLog(category.category_id+"1 catname");

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_particle, container, false);
        preWork();
        getIDs(view);
        setEvents();
        postWork();
        return view;
    }

    public void preWork() {
        application = MyApplication.getInstance();
    }

    public void getIDs(View view) {
        recyclerViewParticles = view.findViewById(R.id.recycler_view_particles);
    }

    public void setEvents() {

    }

    public void postWork() {

        ViewOutlineProvider mViewOutlineProvider2 = new ViewOutlineProvider() {
            @Override
            public void getOutline(final View view, final Outline outline) {

                int left = 0;
                int top = 0;
                int right = view.getWidth();
                int bottom = view.getHeight();
                float cornerRadiusDP = 5f;
                int cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerRadiusDP, context.getResources().getDisplayMetrics());

                outline.setRoundRect(left, top, right, bottom, cornerRadius);
            }
        };

        int widthint = (ScreenUtils.getScreenWidth(context) - ScreenUtils.convertDPItoINT(context, 40)) / 3;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthint, widthint);
        params.setMargins(0, ScreenUtils.convertDPItoINT(context, 10), 0, ScreenUtils.convertDPItoINT(context, 10));

        recyclerViewParticles.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerViewParticles.setHasFixedSize(true);

        particleAdapter = new ParticleAdapter(context, params, mViewOutlineProvider2, category);
        recyclerViewParticles.setAdapter(particleAdapter);
        particleAdapter.setParticleArrayList(particleArrayList);
    }

}