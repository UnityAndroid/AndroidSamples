package lyrically.photovideomaker.particl.ly.musicallybeat.ui.particle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ParticleResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogLoader;

import java.util.ArrayList;

public class ParticleActivity extends AppCompatActivity {

    Context context;
    MyApplication application;

    ImageView imageViewBack;
    TextView textViewTitle, textViewDone;

    boolean isdownload = false;
    public ArrayList<ParticleResponse.ParticleCategory> particleCategories = new ArrayList<>();
    ViewPager viewPager;
    RecyclerView recyclerViewCategorys;
    ParticleCategoryAdapter particleCategoryAdapter;
    ParticlePagerAdapter particlePagerAdapter;

    ConstraintLayout nodata;
    public DialogLoader dialogLoader;

    public void initializeDialog(Context context, String msg) {
        dialogLoader = new DialogLoader(context, msg);
    }

    public void showLoader() {
        dialogLoader.show();
    }

    public void hideLoader() {
        if (dialogLoader != null)
            if (dialogLoader.isShowing())
                dialogLoader.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_particle);
        preWork();
        getIDs();
        setEvents();
        postWork();
    }

    public void preWork() {
        application = MyApplication.getInstance();
        context = this;
        initializeDialog(context, "Loading Please Wait");
    }

    public void getIDs() {
        imageViewBack = findViewById(R.id.tool_left_icon);
        textViewTitle = findViewById(R.id.tool_title);
        textViewDone = findViewById(R.id.tool_right_icon);

        nodata = findViewById(R.id.no_data);
        recyclerViewCategorys = findViewById(R.id.recycler_view_categorys);
        viewPager = findViewById(R.id.particle_viewpager);

    }

    public void setEvents() {
        imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    public void postWork() {

        textViewTitle.setText(context.getResources().getString(R.string.title_home));
        textViewDone.setVisibility(View.GONE);

        for (int i = 0; i < application.getParticleResponse().data.size(); i++) {
            if (application.getParticleResponse().data.get(i).particles.size() > 0) {
                particleCategories.add(application.getParticleResponse().data.get(i));
            }
        }

        recyclerViewCategorys.setHasFixedSize(true);
        recyclerViewCategorys.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        ParticleCategoryAdapter.callback callback = position -> viewPager.setCurrentItem(position);

        particleCategoryAdapter = new ParticleCategoryAdapter(context, particleCategories, callback);
        recyclerViewCategorys.setAdapter(particleCategoryAdapter);

        particlePagerAdapter = new ParticlePagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(particlePagerAdapter);
        viewPager.setOffscreenPageLimit(10);

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                particleCategoryAdapter.setSelectedIndex(position);
                particleCategoryAdapter.notifyDataSetChanged();
                recyclerViewCategorys.scrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        viewPager.addOnPageChangeListener(pageChangeListener);
    }

    public void setDownload() {
        isdownload = true;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        if (isdownload)
            intent.putExtra("MESSAGE", "YES");
        else
            intent.putExtra("MESSAGE", "NO");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public class ParticlePagerAdapter extends FragmentPagerAdapter {

        ParticlePagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            ParticleFragment resto = new ParticleFragment();
            resto.setParticles(particleCategories.get(position).particles,particleCategories.get(position));
            return resto;
        }

        @Override
        public int getCount() {
            return particleCategories.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
}