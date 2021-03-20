package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment.WAPictureFragment;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment.WASaveFragment;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment.WAVideosFragment;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.NonSwipeableViewPager;

public class WhatsAppStatusActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private NonSwipeableViewPager viewPager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_status);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setTitle("Status Saver");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (NonSwipeableViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.status_saver, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (itemId == R.id.action_help) {
            startActivity(new Intent(WhatsAppStatusActivity.this, WhatsAppHelpActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager2) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(WhatsAppStatusActivity.this, getSupportFragmentManager());
        viewPagerAdapter.addFragment(new WAPictureFragment(), "Picture");
        viewPagerAdapter.addFragment(new WAVideosFragment(), "Videos");
        viewPagerAdapter.addFragment(new WASaveFragment(), "Saved");
        viewPager2.setAdapter(viewPagerAdapter);
        viewPager2.setCurrentItem(0);
        viewPager2.setOffscreenPageLimit(0);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private Context mContext;
        private final List<Fragment> mFragmentList = new ArrayList();
        private final List<String> mFragmentTitleList = new ArrayList();

        public ViewPagerAdapter(Context context, FragmentManager fragmentManager) {
            super(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.mContext = context;
        }

        @Override
        public Fragment getItem(int i) {
            return (Fragment) mFragmentList.get(i);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String str) {
            this.mFragmentList.add(fragment);
            this.mFragmentTitleList.add(str);
        }

        @Override
        public CharSequence getPageTitle(int i) {
            return (CharSequence) this.mFragmentTitleList.get(i);
        }
    }
}
