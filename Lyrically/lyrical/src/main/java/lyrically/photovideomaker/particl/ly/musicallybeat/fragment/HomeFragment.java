package lyrically.photovideomaker.particl.ly.musicallybeat.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.tabs.TabLayout;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.adapter.ThemePagerAdapter;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    public TabLayout TlHome;
    public ViewPager VpHome;
    public Context context;
    public ThemePagerAdapter themePagerAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        getIDs(view);
        doWork();
        return view;
    }

    private void getIDs(View view) {
        VpHome = (ViewPager) view.findViewById(R.id.home_viewpager);
        TlHome = (TabLayout) view.findViewById(R.id.home_tab_layout);
    }

    private void setEvents() {

        TlHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                VpHome.setCurrentItem(tab.getPosition());
                Constants.tabpos = tab.getPosition();
                Constants.recpos = 0;

                View view = tab.getCustomView();
                ImageView tabItemAvatar =
                        (ImageView) view.findViewById(R.id.tab_image);
                ImageView tabBorder = view.findViewById(R.id.tab_border_image);
                tabBorder.setVisibility(View.VISIBLE);
              /*  LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Constants.getPixelInteger(context, 40));
                int pad = Constants.getPixelInteger(context, 5);
                lp.setMargins(0, pad * 2, 0, pad * 2);
                tabItemAvatar.setLayoutParams(lp);*/

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ImageView tabItemAvatar =
                        (ImageView) view.findViewById(R.id.tab_image);
              /*  LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Constants.getPixelInteger(context, 40));
                int pad = Constants.getPixelInteger(context, 5);
                lp.setMargins(0, pad * 2, 0, pad * 2);
                tabItemAvatar.setLayoutParams(lp);
                tabItemAvatar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
*/
                ImageView tabBorder = view.findViewById(R.id.tab_border_image);

                tabBorder.setVisibility(View.GONE);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        VpHome.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Constants.tabpos = position;
                Constants.recpos = 0;
                TlHome.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TlHome.getTabAt(Constants.tabpos).select();

    }


    private void doWork() {
        setPager();
    }

    public void setPager() {
        themePagerAdapter = new ThemePagerAdapter(getFragmentManager(), getActivity());
        VpHome.setAdapter(themePagerAdapter);
    }

    public void addPage(String pagename, int CatId, String Image, String Back_img) {
        Bundle bundle = new Bundle();
        bundle.putInt("cat_id", CatId);
        ThemeFragment fragmentChild = new ThemeFragment();
        fragmentChild.setArguments(bundle);
        themePagerAdapter.addFrag(fragmentChild, pagename, Image, Back_img);
        themePagerAdapter.notifyDataSetChanged();

    }

    public void setupTabLayout() {
        for (int i = 0; i < TlHome.getTabCount(); i++) {
            TlHome.getTabAt(i).setCustomView(themePagerAdapter.getTabView(i));
        }
        setEvents();
    }


}
