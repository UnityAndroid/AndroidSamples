package lyrically.photovideomaker.particl.ly.musicallybeat.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.activity.ParticleActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogDownloadParticle;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.Particle;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParticleFragment extends Fragment {

    private static final String TAG = ParticleFragment.class.getSimpleName();
    public RecyclerView recyclerView;
    public Context context;
    DatabaseManager manager;
    public int cat_id, margin, width;
    ArrayList<Particle> particleArrayList = new ArrayList<>();
    public ParticleThumbnailAdapter adapter;

    public ParticleFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_particle, container, false);
        Bundle bundle = getArguments();
        context = getActivity();
        manager = new DatabaseManager(context);
        cat_id = bundle.getInt("cat_id");
        getIDs(view);
        setEvents();
        doWork();

        return view;
    }

    private void getIDs(View view) {
        recyclerView = view.findViewById(R.id.particle_recycle);
    }

    private void setEvents() {
    }

    private void doWork() {
        new AsynkSetRecycler().execute();
    }

    public class AsynkSetRecycler extends AsyncTask<String, Void, String> {
        DisplayMetrics dm;
        StaggeredGridLayoutManager staggeredGridLayoutManager;
        Cursor cursor;

        public AsynkSetRecycler() {
            particleArrayList.clear();
            dm = context.getResources().getDisplayMetrics();
            margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm);
            width = dm.widthPixels - (margin * 30);
        }

        @Override
        protected String doInBackground(String... params) {
            cursor = manager.featchParticletable(cat_id);
            int position = 2;
            if (cursor.moveToFirst()) {
                do {
                    if (position == 2) {
                        position = 0;
                        Particle particle = new Particle(cursor.getInt(cursor.getColumnIndex("id")), cursor.getInt(cursor.getColumnIndex("category_id")), cursor.getString(cursor.getColumnIndex("theme_name")), cursor.getString(cursor.getColumnIndex("prefab_name")), cursor.getString(cursor.getColumnIndex("thumb_url")), cursor.getString(cursor.getColumnIndex("particle_url")), cursor.getString(cursor.getColumnIndex("theme_url")), 1);
                        particleArrayList.add(particle);
                    } else {
                        position++;
                        Particle particle = new Particle(cursor.getInt(cursor.getColumnIndex("id")), cursor.getInt(cursor.getColumnIndex("category_id")), cursor.getString(cursor.getColumnIndex("theme_name")), cursor.getString(cursor.getColumnIndex("prefab_name")), cursor.getString(cursor.getColumnIndex("thumb_url")), cursor.getString(cursor.getColumnIndex("particle_url")), cursor.getString(cursor.getColumnIndex("theme_url")), 0);
                        particleArrayList.add(particle);
                    }
                } while (cursor.moveToNext());
            }
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            adapter = new ParticleThumbnailAdapter();
            return null;
        }

        protected void onPostExecute(String resultt) {
            super.onPostExecute(resultt);
            cursor.close();
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            recyclerView.setAdapter(adapter);
        }

    }


    public class ParticleThumbnailAdapter extends RecyclerView.Adapter<ParticleThumbnailAdapter.Holder> {

        public ParticleThumbnailAdapter() {
        }

        public class Holder extends RecyclerView.ViewHolder {

            CardView CardMiddle;
            RelativeLayout CardText;
            ImageView ivdownload, ivthumb;
            TextView themename;

            public Holder(@NonNull View itemView) {
                super(itemView);
                CardText = itemView.findViewById(R.id.theme_name_relative);
                ivdownload = itemView.findViewById(R.id.theme_download_image);
                ivthumb = itemView.findViewById(R.id.theme_thumb_image);
                themename = itemView.findViewById(R.id.theme_video_name);
                CardMiddle = itemView.findViewById(R.id.theme_middle_card);
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(
                    R.layout.item_particle, viewGroup, false);
            Holder holder = new Holder(v);
            return holder;
        }

        @Override
        public int getItemCount() {
            return particleArrayList.size();
        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int i) {
            final Particle particle = particleArrayList.get(i);

            GlideImageLoader.SetImageUrl(context, holder.ivthumb, particle.theme_url, null);
            GlideImageLoader.SetImageResource(context, holder.ivdownload, R.drawable.home_theme_download, null);

            holder.themename.setText(particle.theme_name);
            if (manager.checkparticle(particle.id)) {
                Cursor download = null;
                try {
                    download = manager.getparticledownloads(particle.id);
                    if (download.moveToFirst()) {
                        if (!manager.checktparticlethumburl(download.getInt(0), particle.thumb_url) || !manager.checkparticleurl(download.getInt(0), particle.particle_url)) {
                            holder.ivdownload.setVisibility(View.VISIBLE);
                        } else {
                            holder.ivdownload.setVisibility(View.GONE);
                        }
                    } else {
                        holder.ivdownload.setVisibility(View.VISIBLE);
                    }
                } finally {
                    if (download != null)
                        download.close();
                }
            } else {
                holder.ivdownload.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (manager.checkparticle(particle.id)) {
                        Cursor download = null;
                        try {
                            download = manager.getparticledownloads(particle.id);
                            if (download.moveToFirst()) {
                                if (!manager.checktparticlethumburl(download.getInt(0), particle.thumb_url) || !manager.checkparticleurl(download.getInt(0), particle.particle_url)) {
                                    DownloadTheme(particle, holder);
                                } else {
                                    ((ParticleActivity) context).finish();
                                }
                            } else {
                                DownloadTheme(particle, holder);
                            }
                        } finally {
                            if (download != null)
                                download.close();
                        }
                    } else {
                        DownloadTheme(particle, holder);
                    }
                }
            });
        }

        public void DownloadTheme(final Particle theme, final Holder holder) {

            DialogDownloadParticle dialogDownloadTheme = new DialogDownloadParticle(context, theme.id, theme.category_id, theme.theme_name, theme.prefab_name, theme.thumb_url, theme.particle_url);
            dialogDownloadTheme.setCancelable(false);
            dialogDownloadTheme.show();

            dialogDownloadTheme.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (manager.checkparticle(theme.id)) {
                        holder.ivdownload.setVisibility(View.GONE);
                        getParticles(theme);
                    } else {
                        holder.ivdownload.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }


    public void getParticles(Particle particle) {

        try {
            JSONObject jsonAdd = new JSONObject();
            JSONArray pdetails = new JSONArray();
            Cursor part = null;
            try {

                part = manager.getdownloadedparticlebyid(particle.id);
                if (part.moveToFirst()) {

                    JSONArray catparts = new JSONArray();
                    do {
                        JSONObject object = new JSONObject();
                        object.put("UniqueIDNo", part.getInt(part.getColumnIndex("id")));
                        object.put("BundlePath", part.getString(part.getColumnIndex("particle")));
                        object.put("ImgPath", part.getString(part.getColumnIndex("thumb")));
                        object.put("ThemeName", part.getString(part.getColumnIndex("theme_name")));
                        object.put("prefbName", part.getString(part.getColumnIndex("prefab_name")));
                        object.put("NewFlag", "1");
                        object.put("isLocked", part.getInt(part.getColumnIndex("is_lock")));
                        object.put("downloadWebUrl", "");
                        catparts.put(object);

                    } while (part.moveToNext());

                    pdetails.put(catparts);
                }
            } finally {
                if (part != null)
                    part.close();
            }

            jsonAdd.put("ParticalDetails", pdetails);
            Constants.android = false;
            AndroidPlugin.UnityCall("LoadJsonData", "LoadJson", jsonAdd.toString());

        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        }
    }

}
