package lyrically.photovideomaker.particl.ly.musicallybeat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

import java.util.ArrayList;

public class ThemePagerAdapter extends FragmentStatePagerAdapter {
    public ArrayList<Fragment> mFragmentList = new ArrayList<>();
    public ArrayList<String> mFragmentTitleList = new ArrayList<>();

    public ArrayList<String> mFragmentbacksList = new ArrayList<>();
    public ArrayList<String> mFragmentIconList = new ArrayList<>();
    public Context context;

    public ThemePagerAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title, String Image, String Back_img) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        mFragmentIconList.add(Image);
        mFragmentbacksList.add(Back_img);
    }

    public View getTabView(final int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_tab, null);
        TextView tabItemName = (TextView) view.findViewById(R.id.tab_name);
        ImageView tabItemAvatar =
                (ImageView) view.findViewById(R.id.tab_image);
        ImageView tabBorder = view.findViewById(R.id.tab_border_image);

        if (Constants.tabpos == position) {

            tabBorder.setVisibility(View.VISIBLE);
         /*   LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Constants.getPixelInteger(context, 40));
            int pad = Constants.getPixelInteger(context, 5);
            lp.setMargins(0, pad * 2, 0, pad * 2);
            tabItemAvatar.setLayoutParams(lp);*/
        } else {
            tabBorder.setVisibility(View.GONE);

        }

        tabItemName.setText(mFragmentTitleList.get(position));
        tabItemName.setTextColor(context.getResources().getColor(android.R.color.background_light));
        GlideImageLoader.SetImageUrl(context, tabItemAvatar, mFragmentIconList.get(position), null);
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }


}
