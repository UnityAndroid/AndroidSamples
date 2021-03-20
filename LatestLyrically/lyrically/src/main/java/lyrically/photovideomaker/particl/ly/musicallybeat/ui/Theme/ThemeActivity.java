package lyrically.photovideomaker.particl.ly.musicallybeat.ui.Theme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ThemeResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.AppTheme;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.creation.CreationActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.PrefUtils;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ScreenUtils;

import java.util.ArrayList;

public class ThemeActivity extends AppCompatActivity {

    ImageView backImageView;
    Context context;
    MyApplication application;

    ImageView creation;
    boolean isFirst = false;
    ImageView imageViewBack;
    TextView textViewTitle, textViewDone;

    public ArrayList<ThemeResponse.ThemeCategory> themeCategories = new ArrayList<>();
    ViewPager viewPagerthemes;
    RecyclerView recyclerViewCategorys;
    ThemeCategoryAdapter themeCategoryAdapter;
    ThemePagerAdapter themePagerAdapter;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        context = this;
        if (getIntent().hasExtra("filepath")) {
            startActivityForResult(new Intent(context, CreationActivity.class).putExtra("filepath", getIntent().getStringExtra("filepath")), AndroidPlugin.CREATION_ACTIVITY);
        }
        preWork();
        getIDs();
        setEvents();
        postWork();
    }

    public void preWork() {
        application = MyApplication.getInstance();
        application.appTheme = new AppTheme();
        MyApplication.getInstance().appTheme.sound_path = PrefUtils.getInstance(context).getStringData("sound_path");
        MyApplication.getInstance().appTheme.sound_name = PrefUtils.getInstance(context).getStringData("sound_name");
        if (application.isFirst) {
            application.isFirst = false;
            AndroidPlugin.getParticles(context);
        }
        initializeDialog(context, "Loading Please Wait");
        showLoader();
    }

    public void getIDs() {
        imageViewBack = findViewById(R.id.tool_left_icon);
        textViewTitle = findViewById(R.id.tool_title);
        textViewDone = findViewById(R.id.tool_right_icon);
        backImageView = findViewById(R.id.background);
        nodata = findViewById(R.id.no_data);
        recyclerViewCategorys = findViewById(R.id.recycler_view_categorys);
        viewPagerthemes = findViewById(R.id.theme_viewpager);
        creation = findViewById(R.id.creatiom);
    }

    public void setEvents() {

        creation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(context, CreationActivity.class), AndroidPlugin.CREATION_ACTIVITY);
            }
        });

    }

    public void postWork() {
        textViewTitle.setText(context.getResources().getString(R.string.title_home));
        textViewDone.setVisibility(View.GONE);
        imageViewBack.setVisibility(View.GONE);
        Glide.with(context).load(R.drawable.all_background).into(backImageView);
        for (int i = 0; i < application.getThemeResponse().data.size(); i++) {
            if (application.getThemeResponse().data.get(i).videos.size() > 0) {
                themeCategories.add(application.getThemeResponse().data.get(i));
            }
        }

        recyclerViewCategorys.setHasFixedSize(true);
        recyclerViewCategorys.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        ThemeCategoryAdapter.callback callback = position -> viewPagerthemes.setCurrentItem(position);

        themeCategoryAdapter = new ThemeCategoryAdapter(context, themeCategories, callback);
        recyclerViewCategorys.setAdapter(themeCategoryAdapter);

        setPager();
    }

    public void SelectTheme() {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "YES");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void setPager() {

        int widthint = (ScreenUtils.getScreenWidth(context) - ScreenUtils.convertDPItoINT(context, 40)) / 3;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthint, widthint);
        params.setMargins(0, ScreenUtils.convertDPItoINT(context, 10), 0, ScreenUtils.convertDPItoINT(context, 10));

        /*ViewPager2Adapter adapter = new ViewPager2Adapter(context, params);
        viewPagerthemes.setAdapter(adapter);
        viewPagerthemes.setOffscreenPageLimit(10);

        viewPagerthemes.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                themeCategoryAdapter.setSelectedIndex(position);
                themeCategoryAdapter.notifyDataSetChanged();
                recyclerViewCategorys.scrollToPosition(position);
            }
        });
*/
        themePagerAdapter = new ThemePagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerthemes.setAdapter(themePagerAdapter);
        viewPagerthemes.setOffscreenPageLimit(10);
        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                themeCategoryAdapter.setSelectedIndex(position);
                themeCategoryAdapter.notifyDataSetChanged();
                recyclerViewCategorys.scrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };

        viewPagerthemes.addOnPageChangeListener(pageChangeListener);
        hideLoader();
    }


    public class ThemePagerAdapter extends FragmentPagerAdapter {

        public ArrayList<Fragment> mFragmentList = new ArrayList<>();

        ThemePagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            ThemeFragment resto = new ThemeFragment();
            resto.setThemes(themeCategories.get(position).videos);
            return resto;
        }

        @Override
        public int getCount() {
            return themeCategories.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewHolder> {

        private LayoutInflater mInflater;
        FrameLayout.LayoutParams params;

        ViewPager2Adapter(Context context, FrameLayout.LayoutParams params) {
            this.mInflater = LayoutInflater.from(context);
            this.params = params;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.fragment_sound, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
            holder.recyclerView.setHasFixedSize(true);

            ThemeAdapter adapter = new ThemeAdapter(context, params, mViewOutlineProvider2);
            holder.recyclerView.setAdapter(adapter);
            adapter.setThemeArrayList(themeCategories.get(position).videos);
        }

        @Override
        public int getItemCount() {
            return themeCategories.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            RecyclerView recyclerView;

            ViewHolder(View itemView) {
                super(itemView);
                recyclerView = itemView.findViewById(R.id.recycler_view_themes);
            }
        }

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == AndroidPlugin.IMAGE_ACTIVITY || requestCode == AndroidPlugin.THEME_ACTIVITY) {
                String message = data.getStringExtra("MESSAGE");
                int count = data.getIntExtra("IMAGECOUNT", 1);
                assert message != null;
                if (message.equalsIgnoreCase("YES")) {
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", "YES");
                    intent.putExtra("IMAGECOUNT", count);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        }
    }
}