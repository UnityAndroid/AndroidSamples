package lyrically.photovideomaker.particl.ly.musicallybeat.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.adapter.ThemePagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeParticleFragment extends Fragment {

    public TabLayout tabLayout;
    public ViewPager viewPager;
    public ThemePagerAdapter themePagerAdapter;

    public HomeParticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_particle, container, false);
        getIDs(view);
        setEvents();
        doWork();

        return view;
    }

    private void getIDs(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.particle_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.particle_tab_layout);
    }

    private void setEvents() {
    }

    private void doWork() {
        setPager();
    }

    public void setPager() {
        themePagerAdapter = new ThemePagerAdapter(getFragmentManager(), getActivity());
        viewPager.setAdapter(themePagerAdapter);
    }

    public void addPage(String pagename, int CatId, String Image, String Back_img) {
        Bundle bundle = new Bundle();
        bundle.putInt("cat_id", CatId);
        ParticleFragment fragmentChild = new ParticleFragment();
        fragmentChild.setArguments(bundle);
        themePagerAdapter.addFrag(fragmentChild, pagename, Image, Back_img);
        themePagerAdapter.notifyDataSetChanged();

    }

    public void setupTabLayout() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setCustomView(themePagerAdapter.getTabView(i));
        }
        setEvents();
    }


}
