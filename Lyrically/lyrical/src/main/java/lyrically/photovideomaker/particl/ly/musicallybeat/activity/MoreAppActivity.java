package lyrically.photovideomaker.particl.ly.musicallybeat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

public class MoreAppActivity extends AppCompatActivity {

    ImageView mainbackground, back, bottomback;
    Context context;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    MoreAppsAdapter moreAppsAdapter;
    TextView cancel, exit;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more__apps);
        context = this;
        getIDs();
        setEvents();
        doWork();
    }

    public void getIDs() {
        mainbackground = findViewById(R.id.app_background);
        back = findViewById(R.id.app_back_button);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        cancel = findViewById(R.id.button_cancel);
        exit = findViewById(R.id.button_exit);
        bottomback = findViewById(R.id.app_botttom_back);
    }

    private void setEvents() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
            }
        });
    }

    private void doWork() {
        GlideImageLoader.SetImageResource(context, mainbackground, R.drawable.apps_background, null);
        GlideImageLoader.SetImageResource(this, back, R.drawable.all_back_button, null);
        GlideImageLoader.SetImageResource(this, bottomback, R.drawable.app_bottom_back, null);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        moreAppsAdapter = new MoreAppsAdapter(this);
        recyclerView.setAdapter(moreAppsAdapter);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MoreAppActivity.this, MainActivity.class));
        finish();

    }


    public class MoreAppsAdapter extends
            RecyclerView.Adapter<MoreAppsAdapter.ViewHolder> {

        Context con;

        public MoreAppsAdapter(Context con) {
            super();
            this.con = con;
        }

        @Override
        public MoreAppsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.more_apps_list_item, parent, false);

            MoreAppsAdapter.ViewHolder holder = new MoreAppsAdapter.ViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MoreAppsAdapter.ViewHolder holder,
                                     final int position) {

            switch (Constants.moreAppsModels.get(position).getPosition() % 4) {
                case 0:
                    GlideImageLoader.SetImageResource(context, holder.backg, R.drawable.app_background_1, null);
                    break;
                case 1:
                    GlideImageLoader.SetImageResource(context, holder.backg, R.drawable.app_background_2, null);
                    break;
                case 2:
                    GlideImageLoader.SetImageResource(context, holder.backg, R.drawable.app_background_3, null);
                    break;
                case 3:
                    GlideImageLoader.SetImageResource(context, holder.backg, R.drawable.app_background_4, null);
                    break;

            }
            Glide.with(con).load(Constants.moreAppsModels.get(position).getIconurl()).into(holder.icon);
            holder.appname.setText(Constants.moreAppsModels.get(position).getAppname());

            holder.install.setOnClickListener(new View.OnClickListener() {

                @SuppressLint("WrongConstant")
                public void onClick(View arg0) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(Constants.moreAppsModels.get(position).getAppurl()));
                    con.startActivity(intent);
                }
            });

        }

        public int getItemCount() {
            return Constants.moreAppsModels.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView icon, backg;
            TextView appname;
            CardView install;

            public ViewHolder(View itemView) {
                super(itemView);

                install = itemView.findViewById(R.id.install_button);
                icon = itemView.findViewById(R.id.img_icon);
                appname = itemView.findViewById(R.id.appname_text);
                backg = itemView.findViewById(R.id.app_background);
            }
        }
    }
}
