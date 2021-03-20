package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.DatabaseManager;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.MoreAppsModel;

public class MoreAppsActivity extends AppCompatActivity {

    public ImageView back, txtTitle;
    private RecyclerView rvMoreApps;
    public DatabaseManager manager;
    public ArrayList<MoreAppsModel> moreAppsModelArrayList;
    public RecyclerViewAdapter adapter;
    public Typeface typeface;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        setContentView(R.layout.activity_more_apps);
        manager = new DatabaseManager(MoreAppsActivity.this);

        txtTitle = (ImageView) findViewById(R.id.txtTitle);
        back = (ImageView) findViewById(R.id.setting_back);
        rvMoreApps = (RecyclerView) findViewById(R.id.rvMoreApps);

        //LinearLayout creation_banner = (LinearLayout) findViewById(R.id.creation_banner);
        //AdMobLoadAds.getInstance().loadBanner(MoreAppsActivity.this, creation_banner);

        typeface = Typeface.createFromAsset(getAssets(), "fonts/merriweather.ttf");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setMoreApps();
    }

    public void setMoreApps() {
        moreAppsModelArrayList = new ArrayList<>();
        Cursor cursor = manager.fetchAddMore();
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String application = cursor.getString(cursor.getColumnIndex("application"));
                    String app_link = cursor.getString(cursor.getColumnIndex("app_link"));
                    String icon = cursor.getString(cursor.getColumnIndex("icon"));
                    String[] separated = app_link.split("=");
                    Boolean isInstall = isPackageInstalled(separated[1].trim(), getPackageManager());
                    moreAppsModelArrayList.add(new MoreAppsModel(application, separated[1].trim(), icon, isInstall));
                } while (cursor.moveToNext());
            }
        }
        if (cursor != null) {
            cursor.close();
        }

        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(MoreAppsActivity.this, LinearLayoutManager.VERTICAL, false);
        rvMoreApps.setLayoutManager(verticalLayoutManager);
        adapter = new RecyclerViewAdapter(MoreAppsActivity.this, moreAppsModelArrayList);
        rvMoreApps.setAdapter(adapter);
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

        private ArrayList mValues;
        private Activity mContext;
        private MoreAppsModel item;

        public RecyclerViewAdapter(Activity mainActivity, ArrayList arrayList) {
            mValues = arrayList;
            mContext = mainActivity;
        }

        @Override
        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_more_apps, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder Vholder, final int position) {
            item = (MoreAppsModel) mValues.get(position);

            Vholder.textView.setTypeface(typeface, Typeface.NORMAL);

            Vholder.textView.setText(item.getApplication());
            Vholder.textView.setSelected(true);

            Glide.with(mContext).load(item.getIcon()).into(Vholder.imageView);

            if (item.isInstalled()) {
                Vholder.ad_call_to_action.setText("OPEN");
            } else {
                Vholder.ad_call_to_action.setText("INSTALL");
            }

            Vholder.ad_call_to_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item = (MoreAppsModel) mValues.get(position);
                    try {
                        String url = "https://play.google.com/store/apps/details?id=" + item.getApp_link();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });

            Vholder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    item = (MoreAppsModel) mValues.get(position);
                    try {
                        String url = "https://play.google.com/store/apps/details?id=" + item.getApp_link();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        mContext.startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;
            public ImageView imageView;
            public LinearLayout relativeLayout;
            public Button ad_call_to_action;

            public ViewHolder(View view) {
                super(view);
                textView = (TextView) view.findViewById(R.id.txtAppName);
                imageView = (ImageView) view.findViewById(R.id.imgAppIcon);
                relativeLayout = (LinearLayout) view.findViewById(R.id.relativeLayout);
                ad_call_to_action = (Button) view.findViewById(R.id.ad_call_to_action);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
