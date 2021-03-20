package lyrically.photovideomaker.particl.ly.musicallybeat.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.ads.NativeAdLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.activity.HomeActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.Theme;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.AdmobAds;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.googleMaster;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ThemeFragment extends Fragment {

    private static final String TAG = ThemeFragment.class.getSimpleName();
    Context context;
    public RecyclerView recyclerView;
    public int cat_id;
    ArrayList<Theme> themeArrayList = new ArrayList<>();
    public ThemeRecycleAdapter adapter;
    DatabaseManager manager;
    ArrayList<AsyncTask> asyntask = new ArrayList<>();
    boolean isAttached = true;

    public ThemeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
        Bundle bundle = getArguments();
        cat_id = bundle.getInt("cat_id");
        manager = new DatabaseManager(context);

        getIDs(view);
        setEvents();
        doWork();
        return view;
    }

    private void getIDs(View view) {
        recyclerView = view.findViewById(R.id.home_recycler_themes);
    }

    private void setEvents() {
    }

    private void doWork() {
        new AsynkSetRecycle().execute();
    }


    public class AsynkSetRecycle extends AsyncTask<String, Void, String> {
        Cursor cursor;
       // StaggeredGridLayoutManager staggeredGridLayoutManager;

        GridLayoutManager gridLayoutManager;
        public AsynkSetRecycle() {
            themeArrayList.clear();
        }

        @Override
        protected String doInBackground(String... params) {
            cursor = manager.featchThemetable(cat_id);
            int position = 0;
            if (cursor.moveToFirst()) {
                do {
                    position++;

                    Theme theme = new Theme(cursor.getInt(cursor.getColumnIndex("Id")), cursor.getInt(cursor.getColumnIndex("Cat_Id")), 0, cursor.getString(cursor.getColumnIndex("Theme_Name")), cursor.getString(cursor.getColumnIndex("Thumnail_Big")), cursor.getString(cursor.getColumnIndex("Thumnail_Small")), cursor.getString(cursor.getColumnIndex("SoundFile")), cursor.getString(cursor.getColumnIndex("sound_size")), cursor.getString(cursor.getColumnIndex("GameobjectName")), cursor.getInt(cursor.getColumnIndex("Theme_Id")), cursor.getInt(cursor.getColumnIndex("counter")), cursor.getString(cursor.getColumnIndex("lyrics")));
                    themeArrayList.add(theme);

                    if (position != 0 && position % 6 == 0) {
                        Theme themee = new Theme(0, 0, 1, "", "", "", "", "", "", 0, 0, "");
                        themeArrayList.add(themee);
                    }
                } while (cursor.moveToNext());

                if (cursor.getCount() <= 5) {
                    if (cursor.getCount() > 1) {
                        Theme theme = new Theme(0, 0, 1, "", "", "", "", "", "", 0, 0, "");
                        themeArrayList.add(2, theme);
                    } else {
                        Theme theme1 = new Theme(0, 0, 2, "", "", "", "", "", "", 0, 0, "");
                        Theme theme = new Theme(0, 0, 1, "", "", "", "", "", "", 0, 0, "");

                        themeArrayList.add(theme1);
                        themeArrayList.add(theme);
                    }
                }
            }

            adapter = new ThemeRecycleAdapter(context, themeArrayList);

            gridLayoutManager = new GridLayoutManager(context, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    try {
                        if (themeArrayList.size() > 6) {
                            int index = position % 7;
                            switch (index) {
                                case 0:
                                    return 1;
                                case 1:
                                    return 1;
                                case 2:
                                    return 1;
                                case 3:
                                    return 1;
                                case 4:
                                    return 1;
                                case 5:
                                    return 1;
                                case 6:
                                    return 2;
                            }
                        } else {
                            switch (position) {
                                case 2:
                                    return 2;
                                default:
                                    return 1;
                            }
                        }
                    } finally {

                    }
                    return 1;
                }
            });
            return null;
        }

        protected void onPostExecute(String resultt) {
            super.onPostExecute(resultt);

            cursor.close();
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);
            recyclerView.getRecycledViewPool().setMaxRecycledViews(1, 50);
           // recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 50);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (AsyncTask task : asyntask) {
            if (task != null)
                task.cancel(true);
        }
    }

    public class ThemeRecycleAdapter extends RecyclerView.Adapter<ThemeRecycleAdapter.Holder> {

        Context context;
        ArrayList<Theme> themeArrayList = new ArrayList<>();
        DatabaseManager manager;

        int AD_TYPE = 1, CONTENT_TYPE = 0, EMPTY_TYPE = 2;

        public ArrayList<AsyncTask> asyntask = new ArrayList<>();

        public ThemeRecycleAdapter(Context context, ArrayList<Theme> arrayList) {
            this.context = context;
            this.themeArrayList = arrayList;
            manager = new DatabaseManager(context);
        }

        public class Holder extends RecyclerView.ViewHolder {
            CardView CvMain, CvMiddle;
            ImageView IvDownload, IvThumb;
            TextView TvName;
            RelativeLayout RlText;
            LottieAnimationView ivprogress;
            CardView adrl;
            FrameLayout frameLayout;
            RelativeLayout ad_text_native;

           /* CardView adrl;
            ImageView img_native;
            TextView textad;

            //  FrameLayout frameLayout;
            NativeAdLayout adLayout;*/

            public Holder(@NonNull View itemView) {
                super(itemView);

                CvMain = itemView.findViewById(R.id.theme_main_card);
                CvMiddle = itemView.findViewById(R.id.theme_middle_card);
                IvDownload = itemView.findViewById(R.id.theme_download_image);
                IvThumb = itemView.findViewById(R.id.theme_thumb_image);
                TvName = itemView.findViewById(R.id.theme_video_name);
                RlText = itemView.findViewById(R.id.theme_name_relative);
                ivprogress = itemView.findViewById(R.id.theme_download_progress);

                ad_text_native = (RelativeLayout) itemView.findViewById(R.id.adtext_native);
                adrl = itemView.findViewById(R.id.ad_rl);
                frameLayout = itemView.findViewById(R.id.fl_adplaceholder);

              /*  adLayout = itemView.findViewById(R.id.native_ad_container);
                img_native = itemView.findViewById(R.id.img_native);
                textad = itemView.findViewById(R.id.text_ad);
                adrl = itemView.findViewById(R.id.ad_rl);*/

            }
        }

       /* @Override
        public void onViewAttachedToWindow(Holder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null) {
                if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                    if (themeArrayList.size() > 6) {
                        int index = holder.getLayoutPosition() % 7;
                        switch (index) {
                            case 0:
                                p.setFullSpan(false);
                                break;
                            case 1:
                                p.setFullSpan(false);
                                break;
                            case 2:
                                p.setFullSpan(false);
                                break;
                            case 3:
                                p.setFullSpan(false);
                                break;
                            case 4:
                                p.setFullSpan(false);
                                break;
                            case 5:
                                p.setFullSpan(false);
                                break;
                            case 6:
                                p.setFullSpan(true);
                                break;
                        }
                    } else {
                        if (holder.getLayoutPosition() == 2) {
                            p.setFullSpan(true);
                        } else {
                            p.setFullSpan(false);
                        }
                    }
                }
            }
            holder.setIsRecyclable(true);
        }
*/


        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = null;
            if (i == AD_TYPE) {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.ad_item_theme, viewGroup, false);
            } else {
                view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_theme, viewGroup, false);
            }

            return new Holder(view);
        }

        @Override
        public int getItemCount() {
            return themeArrayList.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (themeArrayList.get(position).is_Ad == 1)
                return AD_TYPE;
            else if (themeArrayList.get(position).is_Ad == 2)
                return EMPTY_TYPE;
            return CONTENT_TYPE;
        }

        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {
            final Theme theme = themeArrayList.get(position);

            if (theme.is_Ad == AD_TYPE) {

                holder.adrl.setVisibility(View.VISIBLE);
                googleMaster.getInstance().showNative((HomeActivity) context, holder.frameLayout, holder.ad_text_native);


               /* holder.adrl.setVisibility(View.VISIBLE);
                holder.textad.setVisibility(View.VISIBLE);
                holder.img_native.setVisibility(View.VISIBLE);

                //   AdmobAds.getInstance().refreshAd(viewHolder.frameLayout, context);
                AdmobAds.getInstance().loadfbNativeAd(context, holder.adLayout);
*/
            } else if (theme.is_Ad == EMPTY_TYPE) {
                holder.CvMain.setVisibility(View.INVISIBLE);
            } else {

                holder.ivprogress.setVisibility(View.GONE);
                holder.IvDownload.setVisibility(View.VISIBLE);

           /*     switch (position % 4) {
                    case 0:
                        holder.CvMiddle.setCardBackgroundColor(ContextCompat.getColor(context, R.color.theme_back_1));
                        break;
                    case 1:
                        holder.CvMiddle.setCardBackgroundColor(ContextCompat.getColor(context, R.color.theme_back_2));
                        break;
                    case 2:
                        holder.CvMiddle.setCardBackgroundColor(ContextCompat.getColor(context, R.color.theme_back_3));
                        break;
                    case 3:
                        holder.CvMiddle.setCardBackgroundColor(ContextCompat.getColor(context, R.color.theme_back_4));
                        break;
                }*/


                holder.TvName.setText(theme.Theme_Name);
                holder.TvName.setSelected(true);

                GlideImageLoader.SetImageUrl(context, holder.IvThumb, theme.Thumnail_Big, null);

                if (manager.checktheme(theme.Id)) {
                    Cursor download = manager.getdownloads(theme.Id);
                    if (download.moveToFirst()) {
                        if (!manager.checkthemeimageurl(download.getInt(0), theme.Thumnail_Big) || !manager.checkthemesongurl(download.getInt(0), theme.SoundFile)) {
                            GlideImageLoader.SetImageResource(context, holder.IvDownload, R.drawable.home_theme_download, null);
                        } else {
                            GlideImageLoader.SetImageResource(context, holder.IvDownload, R.drawable.home_theme_use, null);
                        }
                    } else {
                        GlideImageLoader.SetImageResource(context, holder.IvDownload, R.drawable.home_theme_download, null);
                    }
                    download.close();
                } else {
                    GlideImageLoader.SetImageResource(context, holder.IvDownload, R.drawable.home_theme_download, null);
                }

                holder.CvMain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (manager.checktheme(theme.Id)) {
                            Cursor download = null;
                            try {
                                download = manager.getdownloads(theme.Id);
                                if (download.moveToFirst()) {
                                    if (!manager.checkthemeimageurl(download.getInt(download.getColumnIndex("themeid")), theme.Thumnail_Big) || !manager.checkthemesongurl(download.getInt(download.getColumnIndex("themeid")), theme.SoundFile)) {
                                        DownloadTheme(theme, holder);
                                    } else {
                                        if (new File(download.getString(1)).exists() && new File(download.getString(2)).exists()) {

                                            manager.setcurrenttheme(download.getInt(0), download.getString(1), download.getString(2), download.getString(3), download.getString(4), theme.Theme_Id);
                                            Constants.recpos = position;

                                            if (Constants.tclickcount == 3) {
                                                Constants.tclickcount = 0;
                                                if (manager.getGoogleAdStatus() == 1) {
                                                    AdmobAds.getInstance().displayInterstitial(context, new AdmobAds.MyCallback() {
                                                        @Override
                                                        public void callbackCall() {
                                                            AndroidPlugin.getTheme();
                                                            ((HomeActivity) context).finish();
                                                        }
                                                    });
                                                } else if (manager.getFacebookAdStatus() == 1) {
                                                    AdmobAds.getInstance().displayfbInterstitial(context, new AdmobAds.MyCallback() {
                                                        @Override
                                                        public void callbackCall() {
                                                            AndroidPlugin.getTheme();
                                                            ((HomeActivity) context).finish();
                                                        }
                                                    });
                                                } else {
                                                    AndroidPlugin.getTheme();
                                                    ((HomeActivity) context).finish();
                                                }
                                            } else {
                                                Constants.tclickcount++;
                                                AndroidPlugin.getTheme();
                                                ((HomeActivity) context).finish();
                                            }

                                        } else {
                                            DownloadTheme(theme, holder);
                                        }
                                    }
                                } else {
                                    DownloadTheme(theme, holder);
                                }
                            } finally {
                                if (download != null)
                                    download.close();
                            }
                        } else {
                            DownloadTheme(theme, holder);
                        }
                    }
                });

              //  setPopinAnimation(holder.itemView);
            }
        }

        private void setPopinAnimation(View viewToAnimate) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
            animation.setDuration(500);
            viewToAnimate.startAnimation(animation);
        }

        public void DownloadTheme(final Theme theme, final Holder holder) {

            holder.ivprogress.setVisibility(View.VISIBLE);
            holder.IvDownload.setVisibility(View.GONE);

            asyntask.add(new DownloadThemeTask(theme, holder).execute());


      /*  DialogDownloadTheme dialogDownloadTheme = new DialogDownloadTheme(context, theme.Id, theme.SoundFile, theme.Thumnail_Big, theme.Theme_Name, theme.GameobjectName, theme.Theme_Id, theme.lyrics);
        dialogDownloadTheme.setCancelable(false);
        dialogDownloadTheme.show();

        dialogDownloadTheme.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (manager.checktheme(theme.Id)) {
                    GlideImageLoader.SetImageResource(context, holder.IvDownload, R.drawable.home_theme_use, null);
                } else {
                    GlideImageLoader.SetImageResource(context, holder.IvDownload, R.drawable.home_theme_download, null);
                }
            }
        });
*/
        }


        private class DownloadThemeTask extends AsyncTask<String, Integer, String> {

            Holder viewHolder;
            Theme theme;

            String soundfile, imagefile, filename;


            public DownloadThemeTask(final Theme theme, final Holder viewHolder) {
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
                if (!manager.checksongurl(theme.SoundFile)) {
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
                        Log.e(TAG, e.toString());
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

            }

            @Override
            protected void onPostExecute(final String result) {

                if (!result.equalsIgnoreCase("-1")) {

                    SaveImage(context,theme,viewHolder);

                 /*   if (HomeActivity.isAttached) {
                        Glide.with(context).asBitmap().load(theme.Thumnail_Big).into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                            }
                        });

                    } else {
                        Log.e(TAG, "Download Theme Failed");
                        //  AndroidPlugin.showToast(context, "Donwload Theme Failed");
                    }*/
                }
            }

            private void SaveImage(final Context context, final Theme theme, final Holder viewHolder) {

                soundfile = AndroidPlugin.GetSoundPath() + filename + ".mp3";
                imagefile = AndroidPlugin.GetImagePath() + filename + ".png";

                class SaveThisImage extends AsyncTask<Void, Void, Void> {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... arg0) {
                        try {
                            FileOutputStream fos = null;
                            try {
                                fos = new FileOutputStream(imagefile);
                                Bitmap bitmap = Picasso.get().load(theme.Thumnail_Big).get();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                                intent.setData(Uri.fromFile(new File(imagefile)));
                                context.sendBroadcast(intent);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        super.onPostExecute(result);


                        if (!manager.checksongurl(theme.SoundFile)) {
                            manager.insertdownloads(theme.Id, soundfile, imagefile, theme.Theme_Name.replaceAll("'", "''"), theme.GameobjectName, theme.Thumnail_Big, theme.SoundFile, theme.lyrics);
                            Cursor cursor = null;
                            try {
                                cursor = manager.featchSongByUrl(theme.SoundFile);
                                if (cursor.moveToFirst()) {
                                    manager.insertsongdownloads(cursor.getInt(cursor.getColumnIndex("id")), soundfile, theme.SoundFile, cursor.getInt(cursor.getColumnIndex("Cat_Id")), theme.lyrics);
                                }
                            } finally {
                                if (cursor != null)
                                    cursor.close();
                            }
                        } else {
                            Cursor cursor = null;
                            try {
                                cursor = manager.getdownloadedsoundfromurl(theme.SoundFile);
                                if (cursor.moveToFirst()) {
                                    manager.insertdownloads(theme.Id, cursor.getString(cursor.getColumnIndex("sound")), imagefile, theme.Theme_Name.replaceAll("'", "''"), theme.GameobjectName, theme.Thumnail_Big, theme.SoundFile, theme.lyrics);
                                } else {
                                    manager.insertdownloads(theme.Id, soundfile, imagefile, theme.Theme_Name.replaceAll("'", "''"), theme.GameobjectName, theme.Thumnail_Big, theme.SoundFile, theme.lyrics);
                                }
                            } finally {
                                if (cursor != null)
                                    cursor.close();
                            }
                        }

                        Cursor download = null;
                        try {
                            download = manager.getdownloads(theme.Id);
                            if (download.moveToFirst()) {
                                try {

                                    JSONObject jsonAdd = new JSONObject();
                                    jsonAdd.put("themeid", String.valueOf(download.getInt(0)));
                                    jsonAdd.put("sound", download.getString(1));
                                    jsonAdd.put("image", download.getString(2));

                                    manager.setcurrenttheme(theme.Id, soundfile, imagefile, theme.Theme_Name, theme.GameobjectName, theme.Theme_Id);

                                } catch (JSONException e) {
                                    Log.e(TAG, e.toString());
                                }
                            }
                        } finally {
                            if (download != null)
                                download.close();
                        }

                        viewHolder.ivprogress.setVisibility(View.GONE);
                        viewHolder.IvDownload.setVisibility(View.VISIBLE);

                        if (manager.checktheme(theme.Id)) {
                            GlideImageLoader.SetImageResource(context, viewHolder.IvDownload, R.drawable.home_theme_use, null);
                        } else {
                            GlideImageLoader.SetImageResource(context, viewHolder.IvDownload, R.drawable.home_theme_download, null);
                        }

                    }
                }
                SaveThisImage shareimg = new SaveThisImage();
                shareimg.execute();
            }

        }
    }


}
