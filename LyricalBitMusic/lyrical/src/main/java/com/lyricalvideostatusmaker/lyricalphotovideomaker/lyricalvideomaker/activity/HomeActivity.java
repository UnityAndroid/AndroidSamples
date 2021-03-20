package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.coolerfall.download.DownloadCallback;
import com.coolerfall.download.DownloadManager;
import com.coolerfall.download.DownloadRequest;
import com.coolerfall.download.Logger;
import com.coolerfall.download.OkHttpDownloader;
import com.coolerfall.download.Priority;
import com.facebook.ads.NativeAdLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.unity3d.player.UnityPlayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.AndroidPlugin;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import okhttp3.OkHttpClient;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model.CategoryModel;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model.SliderItem;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.model.ThemeModel;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.nativetemplates.TemplateView;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdvanceDrawerLayout;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.Constants;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.DatabaseManager;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.GlideImageLoader;

public class HomeActivity extends AppCompatActivity {

    public ImageView Ivdrawer, Ivlogo, Ivlogotext, DIvLogo, DIvLogoText, IvSearch, Ivwhats;
    public LottieAnimationView Ivmoreapps;
    public AdvanceDrawerLayout DlDrawer;
    public ActionBarDrawerToggle toggle;
    public NavigationView navigationView;
    public DatabaseManager manager;
    private String version = "";

    public Typeface typeface, typeface1;
    public RecyclerView recyclerView, recyclerViewCategory;
    public int cat_id;
    public ThemeThumbnailAdapter adapter;
    public CategoryAdapter categoryAdapter;
    public ProgressBar progressBar;
    public boolean end = false;
    public Cursor themeCursor;
    public ArrayList<ThemeModel> songsArrayList = new ArrayList<ThemeModel>();
    public ArrayList<CategoryModel> categoryArrayList = new ArrayList<CategoryModel>();
    public int p = 0;
    public String categoryName;
    private NestedScrollView mScrollView;
    private SliderView sliderView;
    private CardView cardSlider;
    private ArrayList<SliderItem> mSliderItems = new ArrayList<SliderItem>();
    private RelativeLayout rlProgressBar;
    private DownloadManager downloadManager;
    private int downLoadId = 0;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        manager = new DatabaseManager(getApplicationContext());
        Constants.initializeDialog(getApplicationContext(), "Loading Please Wait");
        getIDs();
        setEvents();

        typeface = Typeface.createFromAsset(getAssets(), "fonts/merriweather_bold.ttf");
        typeface1 = Typeface.createFromAsset(getAssets(), "fonts/merriweather.ttf");

        toggle = new ActionBarDrawerToggle(this, DlDrawer, R.string.open, R.string.close);
        DlDrawer.addDrawerListener(toggle);
        toggle.syncState();

        AndroidPlugin.getDirectoryDCIM();

        try {
            AndroidPlugin.getParticles();
        } catch (Exception e) {
            e.printStackTrace();
        }

        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        navigationView.setItemIconTintList(null);
        navigationView.getMenu().add("Ringtone").setTitle("Ringtone").setIcon(R.drawable.ic_ringtone);
        navigationView.getMenu().add("Story Saver").setTitle("Story Saver").setIcon(R.drawable.ic_status_saver);
        navigationView.getMenu().add("My Creation").setTitle("My Creation").setIcon(R.drawable.nav_creation);
        navigationView.getMenu().add("Rate Us").setTitle("Rate Us").setIcon(R.drawable.setting_rate);
        navigationView.getMenu().add("Share App").setTitle("Share App").setIcon(R.drawable.setting_share);
        navigationView.getMenu().add("More Apps").setTitle("More Apps").setIcon(R.drawable.setting_moreapps);
        navigationView.getMenu().add("Feedback").setTitle("Feedback").setIcon(R.drawable.setting_feedback);
        navigationView.getMenu().add("Copyright Policy").setTitle("Copyright Policy").setIcon(R.drawable.setting_copyright);
        navigationView.getMenu().add("Terms of Service").setTitle("Terms of Service").setIcon(R.drawable.setting_terms);
        navigationView.getMenu().add("Privacy Policy").setTitle("Privacy Policy").setIcon(R.drawable.settting_privacy);
        navigationView.getMenu().add("Version").setTitle("Version : " + version).setIcon(R.drawable.setting_version);

        DlDrawer.setViewScale(Gravity.START, 0.9f);
        DlDrawer.setRadius(Gravity.START, 35);
        DlDrawer.setViewElevation(Gravity.START, 20);

    }

    private void getIDs() {
        DlDrawer = findViewById(R.id.home_drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        Ivdrawer = findViewById(R.id.home_drawer_button);
        Ivlogo = findViewById(R.id.home_logo);
        Ivlogotext = findViewById(R.id.home_logo_text);
        Ivmoreapps = findViewById(R.id.home_settings_button);
        Ivwhats = findViewById(R.id.home_whats_button);
        IvSearch = findViewById(R.id.home_search_button);

        View headerLayout = navigationView.getHeaderView(0);
        DIvLogo = headerLayout.findViewById(R.id.drawer_logo);
        DIvLogoText = headerLayout.findViewById(R.id.drawer_logo_text);

        recyclerViewCategory = findViewById(R.id.category_recycler);
        recyclerView = findViewById(R.id.category_recycler_themes);
        progressBar = findViewById(R.id.progressBar);
        mScrollView = findViewById(R.id.mScrollView);

        recyclerViewCategory.setFocusable(false);
        recyclerViewCategory.setNestedScrollingEnabled(false);

        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

        sliderView = findViewById(R.id.imageSlider);
        cardSlider = findViewById(R.id.cardSlider);
        rlProgressBar = findViewById(R.id.rlProgressBar);

        try {
            loadSlider();
            setTabView();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                View view = (View) mScrollView.getChildAt(mScrollView.getChildCount() - 1);

                int diff = (view.getBottom() - (mScrollView.getHeight() + mScrollView.getScrollY()));

                if (diff == 0) {
                    if (!end) {
                        end = true;
                        showProgressView();
                        new HttpPostRequestSMoreVideos().execute(Constants. + "moreVideos/formate/json/");
                    }
                }
            }
        });
    }

    private void setEvents() {

        Ivdrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DlDrawer.openDrawer(GravityCompat.START);
            }
        });

        IvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ThemeSearchActivity.class);
                startActivityForResult(intent, 102);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Ivmoreapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, MoreAppsActivity.class));
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        Ivwhats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdMobLoadAds.getInstance().displayInterstitial(HomeActivity.this, new AdMobLoadAds.MyCallback() {
                    @Override
                    public void callbackCall() {
                        startActivity(new Intent(HomeActivity.this, WhatsAppStatusActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    }
                });
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getTitle().toString()) {
                    case "My Creation":
                        DlDrawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(HomeActivity.this, CreationActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case "Story Saver":
                        DlDrawer.closeDrawer(GravityCompat.START);
                        AdMobLoadAds.getInstance().displayInterstitial(HomeActivity.this, new AdMobLoadAds.MyCallback() {
                            @Override
                            public void callbackCall() {
                                startActivity(new Intent(HomeActivity.this, WhatsAppStatusActivity.class));
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                            }
                        });
                        break;
                    case "Ringtone":
                        DlDrawer.closeDrawer(GravityCompat.START);
                        startActivity(new Intent(HomeActivity.this, RingtoneActivity.class));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        break;
                    case "Rate Us":
                        DlDrawer.closeDrawer(GravityCompat.START);
                        rateUs();
                        break;
                    case "Share App":
                        DlDrawer.closeDrawer(GravityCompat.START);
                        shareapp();
                        break;
                    case "More Apps":
                        DlDrawer.closeDrawer(GravityCompat.START);
                        goToUrl("https://play.google.com/store/apps/developer?id=Birthday+Lyrical+Photo+Video+Maker+Editor+Free+App");
                        break;
                    case "Feedback":
                        DlDrawer.closeDrawer(GravityCompat.START);
                        feedback();
                        break;
                    case "Copyright Policy":
                        DlDrawer.closeDrawer(GravityCompat.START);
                        gotourl("https://birthdayvideostatus.xyz/LyricalBitMusic/copyright_policy.html");
                        break;
                    case "Terms of Service":
                        DlDrawer.closeDrawer(GravityCompat.START);
                        gotourl("https://birthdayvideostatus.xyz/LyricalBitMusic/terms_of_service.html");
                        break;
                    case "Privacy Policy":
                        DlDrawer.closeDrawer(GravityCompat.START);
                        gotourl("https://birthdayvideostatus.xyz/LyricalBitMusic/privacy_policy.html");
                        break;
                }
                return false;
            }
        });
    }

    public void gotourl(String url) {
        try {
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(rateIntent);
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    public void feedback() {
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;

            Intent send = new Intent(Intent.ACTION_SENDTO);

            String uriText = "mailto:" + Uri.encode("photovideoeffect@gmail.com") +
                    "?subject=" + Uri.encode("App: " + getString(R.string.app_name)) +
                    "&body=" + Uri.encode(String.valueOf(Html.fromHtml("Give Your Valuable Feedback \n\n\n" + "" +
                    "\n Version : " + version + "\n Android OS : " + Build.VERSION.RELEASE +
                    "\n Model : " + Build.MODEL + "\n Manufacturer : " + Build.MANUFACTURER +
                    "\n Language: " + Locale.getDefault().getDisplayLanguage() +
                    "\n TimeZone: " + TimeZone.getDefault().getID())));

            Uri uri = Uri.parse(uriText.replaceAll("\\n", "\n"));

            send.setData(uri);
            startActivity(send);
        } catch (Exception e) {
            Log.e("error", e.toString());
        }

    }

    public void goToUrl(String url) {
        try {
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(rateIntent);
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    public void shareapp() {

        String path = AndroidPlugin.GetImagePath() + "LyricalBitBanner.jpg";
        if (!new File(path).exists()) {
            AndroidPlugin.loadimage(HomeActivity.this);
        }

        try {
            Uri uri = FileProvider.getUriForFile(HomeActivity.this, getPackageName() + ".fileprovider", new File(path));
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpg");
            String name = "App: Lyrical Bit Music : Photo Video Status Maker";
            intent.putExtra(Intent.EXTRA_SUBJECT, name);
            String text = "App: Lyrical Bit Music : Photo Video Status Maker" +
                    "\n\nhttps://play.google.com/store/apps/details?id=" + getPackageName() + "\n";
            intent.putExtra(Intent.EXTRA_TEXT, text);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "choose one"));
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    public void rateUs() {
        try {
            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
            startActivity(rateIntent);
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    public void loadSlider() {
        mSliderItems = new ArrayList<SliderItem>();
        Cursor cursor = manager.fetchSlider();
        if (cursor.moveToFirst()) {
            do {
                SliderItem sliderItem = new SliderItem();
                sliderItem.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                sliderItem.setType(cursor.getString(cursor.getColumnIndex("type")));
                sliderItem.setImageUrl(cursor.getString(cursor.getColumnIndex("icon")));
                sliderItem.setName(cursor.getString(cursor.getColumnIndex("name")));
                mSliderItems.add(sliderItem);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }

        if (mSliderItems.size() == 0) {
            cardSlider.setVisibility(View.GONE);
        } else {
            cardSlider.setVisibility(View.VISIBLE);
            SliderAdapter adapter = new SliderAdapter(mSliderItems);
            sliderView.setSliderAdapter(adapter);
            sliderView.setIndicatorAnimation(IndicatorAnimationType.SWAP);
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            sliderView.setIndicatorSelectedColor(Color.WHITE);
            sliderView.setIndicatorUnselectedColor(Color.GRAY);
            sliderView.setScrollTimeInSec(3);
            sliderView.setAutoCycle(true);
            sliderView.startAutoCycle();
        }
    }

    public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {


        private ArrayList<SliderItem> mSliderItems = new ArrayList<>();

        public SliderAdapter(ArrayList<SliderItem> sliderItems) {
            this.mSliderItems = sliderItems;
        }

        @Override
        public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
            return new SliderAdapterVH(inflate);
        }

        @Override
        public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

            SliderItem sliderItem = mSliderItems.get(position);

            Glide.with(viewHolder.itemView).load(sliderItem.getImageUrl()).fitCenter().thumbnail(0.05f)
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ic_p1)).into(viewHolder.imageViewBackground);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSliderItems.get(position).getType().equalsIgnoreCase("Yes")) {

                        String name = (mSliderItems.get(position).getName());

                        for (p = 0; p < categoryArrayList.size(); p++) {
                            if (categoryArrayList.get(p).getName().equalsIgnoreCase(name)) {
                                categoryArrayList.get(p).setTrue(true);
                                categoryName = categoryArrayList.get(p).Name;
                                cat_id = categoryArrayList.get(p).Cat_Id;
                                AndroidPlugin.tabpos = p;
                            } else {
                                categoryArrayList.get(p).setTrue(false);
                            }
                        }

                        categoryAdapter.notifyItemChanged(cat_id);
                        setRecycleData(cat_id);
                        recyclerViewCategory.scrollToPosition(AndroidPlugin.tabpos);
                        recyclerView.scrollToPosition(0);
                        categoryAdapter.notifyDataSetChanged();

                    } else if (mSliderItems.get(position).getType().equalsIgnoreCase("No")) {

                        String url = mSliderItems.get(position).getName().trim();
                        Uri uri = Uri.parse("market://details?id=" + url);
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + url)));
                        }
                    } else if (mSliderItems.get(position).getType().equalsIgnoreCase("Gamezop")) {
                        String url = mSliderItems.get(position).getName().trim();
                        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                        builder.setToolbarColor(ContextCompat.getColor(HomeActivity.this, R.color.black));
                        builder.setShareState(CustomTabsIntent.SHARE_STATE_DEFAULT);
                        CustomTabsIntent customTabsIntent = builder.build();
                        customTabsIntent.intent.setPackage("com.android.chrome");
                        customTabsIntent.launchUrl(HomeActivity.this, Uri.parse(url));
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    } else if (mSliderItems.get(position).getType().equalsIgnoreCase("Songs")) {
                        AdMobLoadAds.getInstance().displayInterstitial(HomeActivity.this, new AdMobLoadAds.MyCallback() {
                            @Override
                            public void callbackCall() {
                                try {
                                    int audioId = Integer.parseInt(mSliderItems.get(position).getName().trim());
                                    ThemeModel themeModel = new ThemeModel();
                                    Cursor theme = null;
                                    try {
                                        theme = manager.getthemebyid(audioId);
                                        if (theme.moveToFirst()) {
                                            themeModel = new ThemeModel(theme.getInt(theme.getColumnIndex("Id")),
                                                    theme.getInt(theme.getColumnIndex("Cat_Id")), theme.getString(theme.getColumnIndex("Theme_Name")),
                                                    theme.getString(theme.getColumnIndex("Thumnail_Big")), theme.getString(theme.getColumnIndex("Thumnail_Small")),
                                                    theme.getString(theme.getColumnIndex("SoundFile")), theme.getString(theme.getColumnIndex("sound_size")),
                                                    theme.getString(theme.getColumnIndex("GameobjectName")), theme.getInt(theme.getColumnIndex("Theme_Id")),
                                                    theme.getString(theme.getColumnIndex("lyrics")), 1);

                                            if (manager.checktheme(themeModel.getId())) {
                                                Cursor download = manager.getdownloads(themeModel.getId());
                                                if (download.moveToFirst()) {
                                                    if (!manager.checkthemeimageurl(download.getInt(0), themeModel.getThumnail_Big())
                                                            || !manager.checkthemesongurl(download.getInt(0), themeModel.getSoundFile())) {

                                                        rlProgressBar.setVisibility(View.VISIBLE);
                                                        new DownloadThemeTask(themeModel).execute();
                                                    } else {

                                                        manager.setcurrenttheme(download.getInt(0), download.getString(1), download.getString(2),
                                                                download.getString(3).replaceAll("'", "''"), download.getString(4), themeModel.getTheme_Id());

                                                        AndroidPlugin.getTheme();
                                                        finish();
                                                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                                    }
                                                } else {
                                                    rlProgressBar.setVisibility(View.VISIBLE);
                                                    new DownloadThemeTask(themeModel).execute();
                                                }
                                                download.close();
                                            } else {
                                                rlProgressBar.setVisibility(View.VISIBLE);
                                                new DownloadThemeTask(themeModel).execute();
                                            }
                                        } else {

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        if (theme != null)
                                            theme.close();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else {
                        try {
                            String url = mSliderItems.get(position).getName().trim();
                            Intent rateIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(rateIntent);
                        } catch (Exception e) {
                            Log.e("error", e.toString());
                        }
                    }
                }
            });
        }

        @Override
        public int getCount() {
            return mSliderItems.size();
        }

        class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

            View itemView;
            ImageView imageViewBackground;

            public SliderAdapterVH(View itemView) {
                super(itemView);
                imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
                this.itemView = itemView;
            }
        }

    }

    private class DownloadThemeTask extends AsyncTask<String, Integer, String> {

        ThemeModel theme;
        String soundfile, imagefile, filename;

        public DownloadThemeTask(final ThemeModel theme) {
            this.theme = theme;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            filename = String.valueOf(System.currentTimeMillis());
        }


        @Override
        protected String doInBackground(String... sUrl) {
            if (!manager.checkthemesongurl(theme.Id, theme.SoundFile, theme.Thumnail_Big)) {
                InputStream input = null;
                OutputStream output = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(theme.SoundFile);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("Accept-Encoding", "identity");
                    connection.connect();

                    if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                        return "Server returned HTTP " + connection.getResponseCode()
                                + " " + connection.getResponseMessage();
                    }

                    int fileLength = connection.getContentLength();

                    soundfile = AndroidPlugin.GetSoundPath() + filename + ".mp3";
                    imagefile = AndroidPlugin.GetImagePath() + filename + ".png";

                    input = connection.getInputStream();
                    output = new FileOutputStream(soundfile);


                    byte data[] = new byte[4096];
                    long total = 0;
                    int count;
                    while ((count = input.read(data)) != -1) {
                        if (isCancelled()) {
                            input.close();
                            return null;
                        }
                        total += count;
                        publishProgress((int) (total * 100 / fileLength));
                        output.write(data, 0, count);
                    }
                } catch (Exception e) {
                    return "-1";
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException ignored) {
                    }
                    if (connection != null)
                        connection.disconnect();
                }
            } else {
                publishProgress(100);
            }
            return String.valueOf(theme.Id);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //txt_progress.setText("" + values[0]);
        }

        @Override
        protected void onPostExecute(final String result) {
            if (!result.equalsIgnoreCase("-1")) {
                soundfile = AndroidPlugin.GetSoundPath() + filename + ".mp3";
                imagefile = AndroidPlugin.GetImagePath() + filename + ".png";

                Glide.with(HomeActivity.this).asBitmap().load(theme.Thumnail_Big).into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {

                            Bitmap bitmap = Bitmap.createScaledBitmap(resource, 720, 1080, false);
                            FileOutputStream out = new FileOutputStream(new File(imagefile));
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                            out.close();

                            manager.insertdownloads(theme.Id, soundfile, imagefile, theme.Theme_Name.replaceAll("'", "''"), theme.GameobjectName, theme.Thumnail_Big, theme.SoundFile, theme.lyrics.replaceAll("'", "''"));

                            manager.setcurrenttheme(theme.Id, soundfile, imagefile,
                                    theme.Theme_Name.replaceAll("'", "''"),
                                    theme.GameobjectName, theme.Theme_Id);

                            new HttpPostRequestCountSongs(theme.Id).execute(Constants. + "download/formate/json/");

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
            } else {
                rlProgressBar.setVisibility(View.GONE);
                AndroidPlugin.showToast(HomeActivity.this, "Download Theme Failed");
            }
        }
    }

    public class HttpPostRequestCountSongs extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;
        private int audioId = 0;

        public HttpPostRequestCountSongs(final int audioId) {
            this.audioId = audioId;
        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                connection.setRequestProperty("Authorization", "Basic V0JpdE1hc3RlcjpNYXN0ZXJAV0JpdA==");

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("audio_id", String.valueOf(audioId));
                String query = builder.build().getEncodedQuery();
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(String resultt) {
            super.onPostExecute(resultt);
            rlProgressBar.setVisibility(View.GONE);
            AndroidPlugin.getTheme();
            finish();
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
    }

    public void setTabView() {
        categoryArrayList.clear();
        Cursor cats = manager.featchCatagarytable();
        if (cats.moveToFirst()) {
            do {
                categoryArrayList.add(new CategoryModel(cats.getInt(cats.getColumnIndex("Id")),
                        cats.getString(cats.getColumnIndex("Cat_Name")),
                        cats.getString(cats.getColumnIndex("Cat_img")), false));
            } while (cats.moveToNext());
        }
        if (cats != null) {
            cats.close();
        }

        categoryAdapter = new CategoryAdapter(categoryArrayList);
        LinearLayoutManager manager = new LinearLayoutManager(HomeActivity.this);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        recyclerViewCategory.setLayoutManager(manager);
        recyclerViewCategory.setAdapter(categoryAdapter);
        categoryArrayList.get(AndroidPlugin.tabpos).setTrue(true);
        cat_id = categoryArrayList.get(AndroidPlugin.tabpos).getCat_Id();

        recyclerViewCategory.scrollToPosition(AndroidPlugin.tabpos);
        setRecycle(cat_id);
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

        public ArrayList<CategoryModel> categoryList;

        public CategoryAdapter(ArrayList<CategoryModel> categoryArrayList) {
            this.categoryList = categoryArrayList;
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView tabItemName;
            ImageView tabItemAvatar;
            RelativeLayout border;

            public Holder(@NonNull View itemView) {
                super(itemView);
                tabItemName = (TextView) itemView.findViewById(R.id.tab_name);
                tabItemAvatar = (ImageView) itemView.findViewById(R.id.tab_image);
                border = (RelativeLayout) itemView.findViewById(R.id.music_tab_indicator);
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Holder(LayoutInflater.from(HomeActivity.this).inflate(R.layout.item_category, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int i) {
            holder.tabItemName.setTypeface(typeface, Typeface.BOLD);

            holder.tabItemName.setText(categoryList.get(i).Name);
            holder.tabItemName.setSelected(true);
            //holder.tabItemName.setTextColor(HomeActivity.this.getResources().getColor(R.color.black));

            Glide.with(HomeActivity.this).load(categoryList.get(i).Thumbnail).apply(new RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)).into(holder.tabItemAvatar);

            if (categoryList.get(i).isTrue) {
                holder.border.setVisibility(View.VISIBLE);
            } else {
                holder.border.setVisibility(View.GONE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryName = categoryList.get(i).Name;

                    cat_id = categoryList.get(i).Cat_Id;
                    for (p = 0; p < categoryList.size(); p++) {
                        if (p == i) {
                            categoryList.get(p).setTrue(true);
                        } else {
                            categoryList.get(p).setTrue(false);
                        }
                    }
                    AndroidPlugin.tabpos = i;
                    notifyItemChanged(cat_id);
                    setRecycleData(cat_id);
                    recyclerView.scrollToPosition(0);
                    end = false;
                    notifyDataSetChanged();
                }
            });
        }

        @Override
        public int getItemCount() {
            return categoryList.size();
        }
    }

    public void setRecycle(int cId) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm);
        songsArrayList = new ArrayList<ThemeModel>();

        themeCursor = manager.featchThemetable(cId);
        p = 0;
        if (themeCursor.moveToFirst()) {
            do {
                songsArrayList.add(new ThemeModel(themeCursor.getInt(themeCursor.getColumnIndex("Id")),
                        themeCursor.getInt(themeCursor.getColumnIndex("Cat_Id")), themeCursor.getString(themeCursor.getColumnIndex("Theme_Name")),
                        themeCursor.getString(themeCursor.getColumnIndex("Thumnail_Big")), themeCursor.getString(themeCursor.getColumnIndex("Thumnail_Small")),
                        themeCursor.getString(themeCursor.getColumnIndex("SoundFile")), themeCursor.getString(themeCursor.getColumnIndex("sound_size")),
                        themeCursor.getString(themeCursor.getColumnIndex("GameobjectName")), themeCursor.getInt(themeCursor.getColumnIndex("Theme_Id")),
                        themeCursor.getString(themeCursor.getColumnIndex("lyrics")), 1));

                if (!AndroidPlugin.adsNative.equalsIgnoreCase("None")) {
                    if (p == 3) {
                        ThemeModel themeModel = new ThemeModel();
                        themeModel.setViewType(2);
                        songsArrayList.add(themeModel);
                    }
                }
                p++;
            } while (themeCursor.moveToNext());
        }

        if (themeCursor != null) {
            themeCursor.close();
        }

        adapter = new ThemeThumbnailAdapter(songsArrayList, margin);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(HomeActivity.this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int itemViewType = adapter.getItemViewType(position);
                if (itemViewType == 1) {
                    return 1;
                } else {
                    return 2;
                }
            }
        });

        recyclerView.setAdapter(adapter);

        if (AndroidPlugin.recpos != 0 && AndroidPlugin.recpos != 1 && AndroidPlugin.recpos != 2 && AndroidPlugin.recpos != 3) {
            mScrollView.post(new Runnable() {
                @Override
                public void run() {
                    final float y = recyclerView.getY() + recyclerView.getChildAt(AndroidPlugin.recpos).getY();
                    mScrollView.smoothScrollTo(0, (int) y);
                }
            });
        }

    }

    public void setRecycleData(int cId) {
        songsArrayList.clear();
        themeCursor = manager.featchThemetable(cId);
        p = 0;
        if (themeCursor.moveToFirst()) {
            do {
                songsArrayList.add(new ThemeModel(themeCursor.getInt(themeCursor.getColumnIndex("Id")),
                        themeCursor.getInt(themeCursor.getColumnIndex("Cat_Id")), themeCursor.getString(themeCursor.getColumnIndex("Theme_Name")),
                        themeCursor.getString(themeCursor.getColumnIndex("Thumnail_Big")), themeCursor.getString(themeCursor.getColumnIndex("Thumnail_Small")),
                        themeCursor.getString(themeCursor.getColumnIndex("SoundFile")), themeCursor.getString(themeCursor.getColumnIndex("sound_size")),
                        themeCursor.getString(themeCursor.getColumnIndex("GameobjectName")), themeCursor.getInt(themeCursor.getColumnIndex("Theme_Id")),
                        themeCursor.getString(themeCursor.getColumnIndex("lyrics")), 1));

                if (!AndroidPlugin.adsNative.equalsIgnoreCase("None")) {
                    if (p == 3) {
                        ThemeModel themeModel = new ThemeModel();
                        themeModel.setViewType(2);
                        songsArrayList.add(themeModel);
                    }
                }
                p++;
            } while (themeCursor.moveToNext());
        }

        if (themeCursor != null) {
            themeCursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    void showProgressView() {
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideProgressView() {
        progressBar.setVisibility(View.GONE);
    }

    public class ThemeThumbnailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public boolean on_attach = true;
        public Cursor cursor;
        public int margin;
        public DatabaseManager manager;
        public ArrayList<ThemeModel> arrayList;
        public ThemeModel themeModel;
        public ArrayList<AsyncTask> asyntask = new ArrayList<AsyncTask>();
        public ArrayList<AsyncTask> asyntaskCount = new ArrayList<AsyncTask>();

        public ThemeThumbnailAdapter(ArrayList<ThemeModel> songsArrayList, int margin) {
            manager = new DatabaseManager(HomeActivity.this);
            this.margin = margin;
            this.arrayList = songsArrayList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == 1) {
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_theme_new, parent, false));
            } else {
                return new NativeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ad_theme, parent, false));
            }
        }

        @Override
        public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    on_attach = false;
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });
            super.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holderPrent, final int position) {
            themeModel = (ThemeModel) arrayList.get(position);
            int itemViewType = getItemViewType(position);
            if (getItemViewType(position) == 1) {
                final ViewHolder viewHolder = (ViewHolder) holderPrent;

                viewHolder.CardMain.startAnimation(AnimationUtils.loadAnimation(HomeActivity.this, R.anim.card_animation));

                viewHolder.txt_progress.setTypeface(typeface, Typeface.BOLD);
                viewHolder.themename.setTypeface(typeface, Typeface.BOLD);

                switch (position % 6) {
                    case 0:
                        viewHolder.CardMiddle.setCardBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.home_cat_1));
                        //viewHolder.RlText.setCardBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.home_cat_1));
                        break;
                    case 1:
                        viewHolder.CardMiddle.setCardBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.home_cat_2));
                        //viewHolder.RlText.setCardBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.home_cat_2));
                        break;
                    case 2:
                        viewHolder.CardMiddle.setCardBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.home_cat_3));
                        //viewHolder.RlText.setCardBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.home_cat_3));
                        break;
                    case 3:
                        viewHolder.CardMiddle.setCardBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.home_cat_4));
                        //viewHolder.RlText.setCardBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.home_cat_4));
                        break;
                    case 4:
                        viewHolder.CardMiddle.setCardBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.home_cat_5));
                        //viewHolder.RlText.setCardBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.home_cat_5));
                        break;
                    case 5:
                        viewHolder.CardMiddle.setCardBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.home_cat_6));
                        //viewHolder.RlText.setCardBackgroundColor(ContextCompat.getColor(HomeActivity.this, R.color.home_cat_6));
                        break;
                }

                viewHolder.themename.setSelected(true);
                viewHolder.themename.setText(themeModel.getTheme_Name());

                Glide.with(HomeActivity.this).load(themeModel.getThumnail_Small()).apply(new RequestOptions().override(180, 320)).into(viewHolder.ivthumb);

                if (manager.checktheme(themeModel.Id)) {
                    Cursor download = manager.getdownloads(themeModel.Id);
                    if (download.moveToFirst()) {
                        if (!manager.checkthemeimageurl(download.getInt(0), themeModel.getThumnail_Big()) || !manager.checkthemesongurl(download.getInt(0), themeModel.getSoundFile())) {
                            GlideImageLoader.SetImageResource(HomeActivity.this, viewHolder.ivdownload, R.drawable.theme_download_icon, null);
                        } else {
                            GlideImageLoader.SetImageResource(HomeActivity.this, viewHolder.ivdownload, R.drawable.theme_use_icon, null);
                        }
                    } else {
                        GlideImageLoader.SetImageResource(HomeActivity.this, viewHolder.ivdownload, R.drawable.theme_download_icon, null);
                    }
                    download.close();
                } else {
                    GlideImageLoader.SetImageResource(HomeActivity.this, viewHolder.ivdownload, R.drawable.theme_download_icon, null);
                }


                /*viewHolder.CardMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeModel = (ThemeModel) arrayList.get(position);
                        if (manager.checktheme(themeModel.getId())) {
                            Cursor download = manager.getdownloads(themeModel.getId());
                            if (download.moveToFirst()) {
                                if (!manager.checkthemeimageurl(download.getInt(0), themeModel.getThumnail_Big())
                                        || !manager.checkthemesongurl(download.getInt(0), themeModel.getSoundFile())) {

                                    DownloadTheme(themeModel, viewHolder);

                                } else {

                                    manager.setcurrenttheme(download.getInt(0), download.getString(1), download.getString(2),
                                            download.getString(3).replaceAll("'", "''"), download.getString(4), themeModel.getTheme_Id());

                                    if (AndroidPlugin.isAdsShow) {
                                        AdMobLoadAds.getInstance().displayInterstitial(HomeActivity.this, new AdMobLoadAds.MyCallback() {
                                            @Override
                                            public void callbackCall() {
                                                AndroidPlugin.isAdsShow = false;
                                                AndroidPlugin.recpos = position;
                                                AndroidPlugin.getTheme();
                                                ((HomeActivity) HomeActivity.this).finish();
                                                ((HomeActivity) HomeActivity.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            }
                                        });
                                    } else {
                                        AndroidPlugin.isAdsShow = true;
                                        AndroidPlugin.recpos = position;
                                        AndroidPlugin.getTheme();
                                        ((HomeActivity) HomeActivity.this).finish();
                                        ((HomeActivity) HomeActivity.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }
                                }
                            } else {
                                DownloadTheme(themeModel, viewHolder);
                            }
                            download.close();
                        } else {
                            DownloadTheme(themeModel, viewHolder);
                        }
                    }
                });*/

                viewHolder.CardMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeModel = (ThemeModel) arrayList.get(position);
                        if (manager.checktheme(themeModel.getId())) {
                            Cursor download = manager.getdownloads(themeModel.getId());
                            if (download.moveToFirst()) {
                                if (!manager.checkthemeimageurl(download.getInt(0), themeModel.getThumnail_Big())
                                        || !manager.checkthemesongurl(download.getInt(0), themeModel.getSoundFile())) {

                                    DownloadTheme(themeModel, viewHolder);

                                } else {

                                    manager.setcurrenttheme(download.getInt(0), download.getString(1), download.getString(2),
                                            download.getString(3).replaceAll("'", "''"), download.getString(4), themeModel.getTheme_Id());

                                    if (AndroidPlugin.isAdsShow) {
                                        AdMobLoadAds.getInstance().displayInterstitial(HomeActivity.this, new AdMobLoadAds.MyCallback() {
                                            @Override
                                            public void callbackCall() {
                                                AndroidPlugin.isAdsShow = false;
                                                AndroidPlugin.recpos = position;
                                                AndroidPlugin.getTheme();
                                                ((HomeActivity) HomeActivity.this).finish();
                                                ((HomeActivity) HomeActivity.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                            }
                                        });
                                    } else {
                                        AndroidPlugin.isAdsShow = true;
                                        AndroidPlugin.recpos = position;
                                        AndroidPlugin.getTheme();
                                        ((HomeActivity) HomeActivity.this).finish();
                                        ((HomeActivity) HomeActivity.this).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                    }
                                }
                            } else {
                                DownloadTheme(themeModel, viewHolder);
                            }
                            download.close();
                        } else {
                            DownloadTheme(themeModel, viewHolder);
                        }
                    }
                });

            } else if (getItemViewType(position) == 2) {
                final NativeViewHolder viewHolder = (NativeViewHolder) holderPrent;

                viewHolder.CardAd.setVisibility(View.VISIBLE);

                if (AndroidPlugin.adsNative.equalsIgnoreCase("Google")) {

                    viewHolder.native_ad_container.setVisibility(View.GONE);

                    AdLoader adLoader = new AdLoader.Builder(HomeActivity.this, AndroidPlugin.adMob_native_id)
                            .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                                @Override
                                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                                    viewHolder.native_admob_container.setVisibility(View.VISIBLE);
                                    viewHolder.native_ad_container.setVisibility(View.GONE);

                                    UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                                            .inflate(R.layout.ad_unified_big, null);
                                    populateUnifiedNativeAdView(unifiedNativeAd, adView);
                                    viewHolder.native_admob_container.removeAllViews();
                                    viewHolder.native_admob_container.addView(adView);
                                }
                            })
                            .withAdListener(new AdListener() {
                                @Override
                                public void onAdFailedToLoad(LoadAdError loadAdError) {
                                    super.onAdFailedToLoad(loadAdError);
                                }
                            })
                            .build();

                    adLoader.loadAd(new AdRequest.Builder().build());

                } else if (AndroidPlugin.adsNative.equalsIgnoreCase("Facebook")) {

                    AdMobLoadAds.getInstance().loadNativeAds(HomeActivity.this, viewHolder.native_ad_container);
                    viewHolder.native_ad_container.setVisibility(View.VISIBLE);

                } else if (AndroidPlugin.adsNative.equalsIgnoreCase("Both")) {

                    if (AdMobLoadAds.adsNativeType) {
                        AdMobLoadAds.adsNativeType = false;

                        viewHolder.native_ad_container.setVisibility(View.GONE);

                        AdLoader adLoader = new AdLoader.Builder(HomeActivity.this, AndroidPlugin.adMob_native_id)
                                .forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                                    @Override
                                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                                        viewHolder.native_admob_container.setVisibility(View.VISIBLE);
                                        viewHolder.native_ad_container.setVisibility(View.GONE);

                                        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                                                .inflate(R.layout.ad_unified_big, null);
                                        populateUnifiedNativeAdView(unifiedNativeAd, adView);
                                        viewHolder.native_admob_container.removeAllViews();
                                        viewHolder.native_admob_container.addView(adView);
                                    }
                                })
                                .withAdListener(new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                                        super.onAdFailedToLoad(loadAdError);
                                    }
                                })
                                .build();

                        adLoader.loadAd(new AdRequest.Builder().build());

                    } else {
                        AdMobLoadAds.adsNativeType = true;
                        AdMobLoadAds.getInstance().loadNativeAds(HomeActivity.this, viewHolder.native_ad_container);
                        viewHolder.native_ad_container.setVisibility(View.VISIBLE);
                    }
                } else {
                    viewHolder.native_ad_container.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return arrayList.get(position).viewType;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private CardView CardMain, CardMiddle;
            private ImageView ivdownload, ivthumb;
            private TextView themename, txt_progress;
            private ProgressBar ivprogress;
            private RelativeLayout llDownload;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ivdownload = itemView.findViewById(R.id.theme_download_image);
                ivthumb = itemView.findViewById(R.id.theme_thumb_image);
                themename = itemView.findViewById(R.id.theme_video_name);
                CardMain = itemView.findViewById(R.id.theme_root_card);
                CardMiddle = itemView.findViewById(R.id.theme_middle_card);
                ivprogress = itemView.findViewById(R.id.imageProgress);
                txt_progress = itemView.findViewById(R.id.txt_progress);
                llDownload = itemView.findViewById(R.id.llDownload);
            }
        }

        public class NativeViewHolder extends RecyclerView.ViewHolder {

            private CardView CardAd;
            private NativeAdLayout native_ad_container;
            private FrameLayout native_admob_container;

            public NativeViewHolder(@NonNull View itemView) {
                super(itemView);
                CardAd = itemView.findViewById(R.id.theme_ad_relative);

                native_admob_container = itemView.findViewById(R.id.native_admob_container);
                native_ad_container = itemView.findViewById(R.id.native_ad_container);
            }
        }

        public void DownloadTheme(final ThemeModel theme, final ViewHolder holder) {

            if (downLoadId == 0) {
                downLoadId = 1;
                holder.llDownload.setVisibility(View.VISIBLE);
                holder.ivdownload.setVisibility(View.GONE);
                final String soundfile, imagefile, filename;
                filename = String.valueOf(System.currentTimeMillis());
                soundfile = AndroidPlugin.GetSoundPath() + filename + ".mp3";
                imagefile = AndroidPlugin.GetImagePath() + filename + ".png";

                OkHttpClient client = new OkHttpClient.Builder().build();
                downloadManager = new DownloadManager.Builder().context(HomeActivity.this).downloader(OkHttpDownloader.create(client))
                        .threadPoolSize(3).logger(new Logger() {
                            @Override
                            public void log(String message) {

                            }
                        })
                        .build();

                DownloadRequest request = new DownloadRequest.Builder().url(theme.SoundFile).retryTime(5)
                        .retryInterval(2, TimeUnit.SECONDS).progressInterval(1, TimeUnit.SECONDS).priority(Priority.HIGH)
                        .allowedNetworkTypes(DownloadRequest.NETWORK_ALL).destinationFilePath(soundfile)
                        .downloadCallback(new DownloadCallback() {
                            @Override
                            public void onStart(int downloadId, long totalBytes) {

                            }

                            @Override
                            public void onRetry(int downloadId) {

                            }

                            @Override
                            public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
                                int progress = (int) (bytesWritten * 100f / totalBytes);
                            }

                            @Override
                            public void onSuccess(int downloadId, String filePath) {

                                DownloadRequest request1 = new DownloadRequest.Builder().url(theme.Thumnail_Big).retryTime(5)
                                        .retryInterval(2, TimeUnit.SECONDS).progressInterval(1, TimeUnit.SECONDS).priority(Priority.HIGH)
                                        .allowedNetworkTypes(DownloadRequest.NETWORK_ALL).destinationFilePath(imagefile)
                                        .downloadCallback(new DownloadCallback() {
                                            @Override
                                            public void onStart(int downloadId, long totalBytes) {

                                            }

                                            @Override
                                            public void onRetry(int downloadId) {

                                            }

                                            @Override
                                            public void onProgress(int downloadId, long bytesWritten, long totalBytes) {
                                                int progress = (int) (bytesWritten * 100f / totalBytes);
                                            }

                                            @Override
                                            public void onSuccess(int downloadId, String filePath) {
                                                downLoadId = 0;
                                                manager.insertdownloads(theme.Id, soundfile, imagefile, theme.Theme_Name.replaceAll("'", "''"), theme.GameobjectName, theme.Thumnail_Big, theme.SoundFile, theme.lyrics.replaceAll("'", "''"));

                                                holder.llDownload.setVisibility(View.GONE);
                                                holder.ivdownload.setVisibility(View.VISIBLE);

                                                if (manager.checktheme(theme.Id)) {
                                                    Glide.with(HomeActivity.this).load(R.drawable.theme_use_icon).apply(new RequestOptions().fitCenter()).into(holder.ivdownload);
                                                } else {
                                                    Glide.with(HomeActivity.this).load(R.drawable.theme_download_icon).apply(new RequestOptions().fitCenter()).into(holder.ivdownload);
                                                }

                                                asyntaskCount.add(new HttpPostRequestCountSongs(theme.Id).execute(Constants. + "download/formate/json/"));
                                            }

                                            @Override
                                            public void onFailure(int downloadId, int statusCode, String errMsg) {

                                            }
                                        })
                                        .build();

                                downloadManager.add(request1);
                            }

                            @Override
                            public void onFailure(int downloadId, int statusCode, String errMsg) {
                                downLoadId = 0;
                                holder.llDownload.setVisibility(View.GONE);
                                holder.ivdownload.setVisibility(View.VISIBLE);
                                Glide.with(HomeActivity.this).load(R.drawable.theme_download_icon).apply(new RequestOptions().fitCenter()).into(holder.ivdownload);
                                AndroidPlugin.showToast(HomeActivity.this, "Download Theme Failed");
                            }
                        })
                        .build();

                downloadManager.add(request);
            } else {
                AndroidPlugin.showToast(HomeActivity.this, "Please Wait Until,\nAnother Theme Download Finished!!");
            }

            /*if (asyntask.size() == 0) {
                holder.llDownload.setVisibility(View.VISIBLE);
                holder.ivdownload.setVisibility(View.GONE);
                asyntask.add(new DownloadThemeTask(theme, holder).execute());
            } else {
                AndroidPlugin.showToast(HomeActivity.this, "Please Wait Until,\nAnother Theme Download Finished!!");
            }*/
        }

        private class DownloadThemeTask extends AsyncTask<String, Integer, String> {

            ViewHolder viewHolder;
            ThemeModel theme;
            String soundfile, imagefile, filename;

            public DownloadThemeTask(final ThemeModel theme, final ViewHolder viewHolder) {
                this.theme = theme;
                this.viewHolder = viewHolder;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                filename = String.valueOf(System.currentTimeMillis());
            }


            @Override
            protected String doInBackground(String... sUrl) {
                if (!manager.checkthemesongurl(theme.Id, theme.SoundFile, theme.Thumnail_Big)) {
                    InputStream input = null;
                    OutputStream output = null;
                    HttpURLConnection connection = null;
                    try {
                        URL url = new URL(theme.SoundFile);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestProperty("Accept-Encoding", "identity");
                        connection.connect();

                        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            return "Server returned HTTP " + connection.getResponseCode()
                                    + " " + connection.getResponseMessage();
                        }

                        int fileLength = connection.getContentLength();

                        soundfile = AndroidPlugin.GetSoundPath() + filename + ".mp3";
                        imagefile = AndroidPlugin.GetImagePath() + filename + ".png";

                        input = connection.getInputStream();
                        output = new FileOutputStream(soundfile);


                        byte data[] = new byte[4096];
                        long total = 0;
                        int count;
                        while ((count = input.read(data)) != -1) {
                            if (isCancelled()) {
                                input.close();
                                return null;
                            }
                            total += count;
                            publishProgress((int) (total * 100 / fileLength));
                            output.write(data, 0, count);
                        }
                    } catch (Exception e) {
                        return "-1";
                    } finally {
                        try {
                            if (output != null)
                                output.close();
                            if (input != null)
                                input.close();
                        } catch (IOException ignored) {
                        }
                        if (connection != null)
                            connection.disconnect();
                    }
                } else {
                    publishProgress(100);
                }
                return String.valueOf(theme.Id);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                viewHolder.txt_progress.setText("" + values[0]);
            }

            @Override
            protected void onPostExecute(final String result) {
                if (!result.equalsIgnoreCase("-1")) {
                    soundfile = AndroidPlugin.GetSoundPath() + filename + ".mp3";
                    imagefile = AndroidPlugin.GetImagePath() + filename + ".png";

                    Glide.with(HomeActivity.this).asBitmap().load(theme.Thumnail_Big).into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            try {

                                Bitmap bitmap = Bitmap.createScaledBitmap(resource, 720, 1080, false);
                                FileOutputStream out = new FileOutputStream(new File(imagefile));
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
                                out.close();

                                manager.insertdownloads(theme.Id, soundfile, imagefile, theme.Theme_Name.replaceAll("'", "''"), theme.GameobjectName, theme.Thumnail_Big, theme.SoundFile, theme.lyrics.replaceAll("'", "''"));

                                Cursor download = null;
                                try {
                                    download = manager.getdownloads(Integer.parseInt(result));
                                    if (download.moveToFirst()) {
                                        try {
                                            JSONObject jsonAdd = new JSONObject();
                                            jsonAdd.put("themeid", String.valueOf(download.getInt(0)));
                                            jsonAdd.put("sound", download.getString(1));
                                            jsonAdd.put("image", download.getString(2));

                                            manager.setcurrenttheme(theme.Id, soundfile, imagefile,
                                                    theme.Theme_Name.replaceAll("'", "''"),
                                                    theme.GameobjectName, theme.Theme_Id);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } finally {
                                    if (download != null)
                                        download.close();
                                }

                                viewHolder.llDownload.setVisibility(View.GONE);
                                viewHolder.ivdownload.setVisibility(View.VISIBLE);

                                if (manager.checktheme(theme.Id)) {
                                    Glide.with(HomeActivity.this).load(R.drawable.theme_use_icon).apply(new RequestOptions().fitCenter()).into(viewHolder.ivdownload);
                                } else {
                                    Glide.with(HomeActivity.this).load(R.drawable.theme_download_icon).apply(new RequestOptions().fitCenter()).into(viewHolder.ivdownload);
                                }

                                asyntaskCount.add(new HttpPostRequestCountSongs(theme.Id).execute(Constants. + "download/formate/json/"));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });

                } else {
                    viewHolder.llDownload.setVisibility(View.GONE);
                    viewHolder.ivdownload.setVisibility(View.VISIBLE);
                    Glide.with(HomeActivity.this).load(R.drawable.theme_download_icon).apply(new RequestOptions().fitCenter()).into(viewHolder.ivdownload);
                    AndroidPlugin.showToast(HomeActivity.this, "Download Theme Failed");
                }
                asyntask.clear();
            }
        }

        public class HttpPostRequestCountSongs extends AsyncTask<String, Void, String> {
            public static final String REQUEST_METHOD = "POST";
            public static final int READ_TIMEOUT = 15000;
            public static final int CONNECTION_TIMEOUT = 15000;
            private int audioId = 0;

            public HttpPostRequestCountSongs(final int audioId) {
                this.audioId = audioId;
            }

            @Override
            protected String doInBackground(String... params) {
                String stringUrl = params[0];
                String result;
                String inputLine;
                try {
                    //Create a URL object holding our url
                    URL myUrl = new URL(stringUrl);
                    //Create a connection
                    HttpURLConnection connection = (HttpURLConnection)
                            myUrl.openConnection();
                    connection.setRequestProperty("Authorization", "Basic V0JpdE1hc3RlcjpNYXN0ZXJAV0JpdA==");

                    connection.setRequestMethod(REQUEST_METHOD);
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.setConnectTimeout(CONNECTION_TIMEOUT);


                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("audio_id", String.valueOf(audioId));
                    String query = builder.build().getEncodedQuery();
                    OutputStream os = connection.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();

                    connection.connect();
                    //Create a new InputStreamReader
                    InputStreamReader streamReader = new
                            InputStreamReader(connection.getInputStream());
                    //Create a new buffered reader and String Builder
                    BufferedReader reader = new BufferedReader(streamReader);
                    StringBuilder stringBuilder = new StringBuilder();
                    //Check if the line we are reading is not null
                    while ((inputLine = reader.readLine()) != null) {
                        stringBuilder.append(inputLine);
                    }
                    //Close our InputStream and Buffered reader
                    reader.close();
                    streamReader.close();
                    //Set our result equal to our stringBuilder
                    result = stringBuilder.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    result = null;
                }
                return result;
            }

            @Override
            protected void onPostExecute(String resultt) {
                super.onPostExecute(resultt);
            }
        }

    }

    public class HttpPostRequestSMoreVideos extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "POST";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        public HttpPostRequestSMoreVideos() {

        }

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                connection.setRequestProperty("Authorization", "Basic V0JpdE1hc3RlcjpNYXN0ZXJAV0JpdA==");

                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("cat_id", String.valueOf(cat_id))
                        .appendQueryParameter("offset", String.valueOf(manager.getpage(cat_id)));
                String query = builder.build().getEncodedQuery();
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();

                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();

                Log.e("error", e.toString());
                result = null;
            }
            return result;
        }

        @Override
        protected void onPostExecute(final String resultt) {
            super.onPostExecute(resultt);
            hideProgressView();
            if (resultt != null) {
                try {
                    int oldSize = songsArrayList.size();
                    JSONObject object = new JSONObject(resultt);

                    if (object.getString("status").equalsIgnoreCase("1")) {

                        JSONArray themes = object.getJSONArray("videos");
                        for (int j = 0; j < themes.length(); j++) {
                            JSONObject object1 = themes.getJSONObject(j);

                            manager.insertthemetable(object1.getInt("Id"), object1.getInt("Cat_Id"), "0",
                                    object1.getString("Theme_Name").replaceAll("'", "''"),
                                    object1.getString("Thumbnail_Big"), object1.getString("Thumbnail_Small"),
                                    object1.getString("SoundFile"), object1.getString("SoundSize"),
                                    object1.getString("GameobjectName"), "0", object1.getString("Status"),
                                    "0", object1.getInt("Theme_Id"), 2,
                                    object1.getString("lyrics").replaceAll("'", "''"));

                            songsArrayList.add(new ThemeModel(object1.getInt("Id"),
                                    cat_id, object1.getString("Theme_Name").replaceAll("'", "''"),
                                    object1.getString("Thumbnail_Big"), object1.getString("Thumbnail_Small"),
                                    object1.getString("SoundFile"), object1.getString("SoundSize"),
                                    object1.getString("GameobjectName"), object1.getInt("Theme_Id"),
                                    object1.getString("lyrics").replaceAll("'", "''"), 1));

                            /*if (!AndroidPlugin.adsNative.equalsIgnoreCase("None")) {
                                if (j % 4 == 3) {
                                    ThemeModel themeModel = new ThemeModel();
                                    themeModel.setViewType(2);
                                    songsArrayList.add(themeModel);
                                }
                            }*/
                        }

                        adapter.notifyItemMoved(oldSize, songsArrayList.size());
                        manager.increpage(cat_id);
                        end = false;
                    } else {
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            try {
                String message = data.getStringExtra("MESSAGE");
                if (message.equalsIgnoreCase("YES")) {
                    finish();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (DlDrawer.isDrawerOpen(GravityCompat.START)) {
            DlDrawer.closeDrawers();
            return;
        }

        AndroidPlugin.isAdsLastShow = true;
        AdMobLoadAds.getInstance().displayInterstitial(HomeActivity.this, new AdMobLoadAds.MyCallback() {
            @Override
            public void callbackCall() {
                showDialog();
            }
        });
    }

    public void showDialog() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(HomeActivity.this);
        View sheetView = getLayoutInflater().inflate(R.layout.dialog_exit, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCanceledOnTouchOutside(false);
        ((View) sheetView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
        mBottomSheetDialog.show();

        LinearLayout llExit = (LinearLayout) sheetView.findViewById(R.id.llExit);
        Button btnExit = (Button) sheetView.findViewById(R.id.btnExit);

        llExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.cancel();
                UnityPlayer.UnitySendMessage("Category", "CloseGame", "Good Morning");
                finishAffinity();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.cancel();
                UnityPlayer.UnitySendMessage("Category", "CloseGame", "Good Morning");
                finishAffinity();
            }
        });

    }

    public AlertDialog showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
                UnityPlayer.UnitySendMessage("Category", "CloseGame", "Good Morning");
                finishAffinity();
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setNeutralButton((CharSequence) "Rate Us", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    startActivity(goToMarket);
                } catch (Exception e) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        builder.setTitle(getResources().getString(R.string.app_name));
        builder.setMessage("Are you sure you want to exit?");
        builder.setIcon(R.drawable.ic_launcher);

        AlertDialog create = builder.create();
        View inflate = getLayoutInflater().inflate(R.layout.ad_exit_view, null);
        if (AndroidPlugin.adsNative.equalsIgnoreCase("Google")) {
            refreshAd(inflate);
        } else if (AndroidPlugin.adsNative.equalsIgnoreCase("Facebook")) {
            NativeAdLayout native_ad_container = inflate.findViewById(R.id.native_ad_container);
            AdMobLoadAds.getInstance().loadNativeAds(HomeActivity.this, native_ad_container);
        }
        create.setView(inflate);
        create.requestWindowFeature(1);

        return create;
    }

    private void refreshAd(final View dialogView) {
        AdLoader.Builder builder = new AdLoader.Builder(this, AndroidPlugin.adMob_native_id);
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                FrameLayout frameLayout = dialogView.findViewById(R.id.flAdPlaceHolder);
                UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater().inflate(R.layout.ad_unified_big, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(true)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {

            }
        }).build();

        adLoader.loadAd(new AdRequest.Builder().build());

    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd nativeAd, UnifiedNativeAdView adView) {
        // Set the media view. Media content will be automatically populated in the media view once
        // adView.setNativeAd() is called.
        com.google.android.gms.ads.formats.MediaView mediaView = adView.findViewById(R.id.ad_media);

        mediaView.setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            @Override
            public void onChildViewAdded(View parent, View child) {
                if (child instanceof ImageView) {
                    ImageView imageView = (ImageView) child;
                    imageView.setAdjustViewBounds(true);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }
            }

            @Override
            public void onChildViewRemoved(View parent, View child) {
            }
        });
        adView.setMediaView(mediaView);

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));

        // The headline is guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad. The SDK will populate the adView's MediaView
        // with the media content from this native ad.
        adView.setNativeAd(nativeAd);

        // Get the video controller for the ad. One will always be provided, even if the ad doesn't
        // have a video asset.
        VideoController vc = nativeAd.getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            // Create a new VideoLifecycleCallbacks object and pass it to the VideoController. The
            // VideoController will call methods on this object when events occur in the video
            // lifecycle.
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    // Publishers should allow native ads to complete video playback before
                    // refreshing or replacing them with another ad in the same UI location.
                    super.onVideoEnd();
                }
            });
        } else {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlideImageLoader.endGlideProcess(getApplicationContext());
        if (downloadManager != null) {
            downloadManager.release();
        }
    }
}
