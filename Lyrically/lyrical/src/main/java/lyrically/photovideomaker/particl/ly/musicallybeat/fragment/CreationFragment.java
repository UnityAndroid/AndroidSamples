package lyrically.photovideomaker.particl.ly.musicallybeat.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.ads.NativeAdLayout;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.activity.CreationActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.activity.HomeActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.activity.PlayerActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.Creation;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.AdmobAds;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.googleMaster;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreationFragment extends Fragment {

    public RecyclerView recyclerView;
    public CreationAdapter adapter;
    public TextView TvNoCreations;
    public DatabaseManager manager;

    int AD_TYPE = 1, CONTENT_TYPE = 0, EMPTY_TYPE = 2;
    Context context;
    ArrayList<Creation> creations = new ArrayList<>();


    public CreationFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_creation, container, false);
        context = getActivity();
        manager = new DatabaseManager(getContext());

        getIDs(view);
        setEvents();

        checkcreationfiles();
        //  checkcreation();

        doWork();
        return view;
    }

    public void checkcreation() {
        if (creations.size() > 0) {
            TvNoCreations.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            TvNoCreations.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void checkcreationfiles() {
        Cursor songs = null;
        try {
            songs = manager.getcreation();
            if (songs.moveToFirst()) {
                do {
                    if (new File(songs.getString(songs.getColumnIndex("filepath"))).exists()) {
                    } else {
                        manager.deletecreation(songs.getString(songs.getColumnIndex("filepath")));
                    }
                } while (songs.moveToNext());
            }
        } finally {
            if (songs != null)
                songs.close();
        }
    }


    private void getIDs(View view) {
        recyclerView = view.findViewById(R.id.creation_recycler);
        TvNoCreations = view.findViewById(R.id.creation_no_text);
    }

    private void setEvents() {
    }

    private void doWork() {
        new AsynkSetRecycler().execute();
    }

    public class AsynkSetRecycler extends AsyncTask<String, Void, String> {
        DisplayMetrics dm;
        int margin;
      //  StaggeredGridLayoutManager staggeredGridLayoutManager;
        Cursor cursor;
        GridLayoutManager gridLayoutManager;

        public AsynkSetRecycler() {
            creations.clear();
            dm = context.getResources().getDisplayMetrics();
            margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm);
        }

        @Override
        protected String doInBackground(String... params) {

            cursor = manager.getcreation();
            int position = 0;
            if (cursor.moveToFirst()) {

                do {
                    position++;

                    Creation creation = new Creation(cursor.getInt(0), cursor.getInt(3), cursor.getString(1), cursor.getString(2), 0);
                    creations.add(creation);

                    if (position != 0 && position % 6 == 0) {
                        Creation creation1 = new Creation(0, 0, "", "", 1);
                        creations.add(creation1);
                    }

                } while (cursor.moveToNext());

                if (cursor.getCount() <= 5) {
                    if (cursor.getCount() > 1) {
                        Creation creation = new Creation(0, 0, "", "", 1);
                        creations.add(2, creation);

                    } else {
                        Creation creation1 = new Creation(0, 0, "", "", 2);
                        Creation creation = new Creation(0, 0, "", "", 1);

                        creations.add(creation1);
                        creations.add(creation);
                    }
                }
            }

            adapter = new CreationAdapter(context, margin);
            gridLayoutManager = new GridLayoutManager(context, 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    try {
                        if (creations.size() > 6) {
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
          //  recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 50);
            checkcreation();
        }

    }

    public class CreationAdapter extends RecyclerView.Adapter<CreationAdapter.Holder> {
        public boolean on_attach = true;
        public int margin;
        public Context context;

        public CreationAdapter(Context context, int margin) {
            this.margin = margin;
            this.context = context;
        }

        public class Holder extends RecyclerView.ViewHolder {

            CardView CvMiddle;
            ImageView IvDownload, IvThumb;
            TextView TvName;
            CardView RlText;
            LinearLayout CvMain;
            ImageView delete, share;


            CardView adrl;

            FrameLayout frameLayout;
            RelativeLayout ad_text_native;


         /*   CardView adrl;
            ImageView img_native;
            TextView textad;
            NativeAdLayout adLayout;
*/
            public Holder(@NonNull View itemView) {
                super(itemView);
                CvMain = itemView.findViewById(R.id.theme_main_card);
                CvMiddle = itemView.findViewById(R.id.theme_middle_card);
                IvDownload = itemView.findViewById(R.id.theme_download_image);
                IvThumb = itemView.findViewById(R.id.theme_thumb_image);
                TvName = itemView.findViewById(R.id.theme_video_name);
                RlText = itemView.findViewById(R.id.theme_name_relative);

                delete = itemView.findViewById(R.id.creation_delete);
                share = itemView.findViewById(R.id.creation_share);

                ad_text_native = (RelativeLayout) itemView.findViewById(R.id.adtext_native);
                adrl = itemView.findViewById(R.id.ad_rl);
                frameLayout = itemView.findViewById(R.id.fl_adplaceholder);



               /* adrl = itemView.findViewById(R.id.ad_rl);
                adLayout = itemView.findViewById(R.id.native_ad_container);
                img_native = itemView.findViewById(R.id.img_native);
                textad = itemView.findViewById(R.id.text_ad);
*/

            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            if (viewType == AD_TYPE) {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.ad_item_theme, parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_creation, parent, false);
            }
            return new Holder(view);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (creations.get(position).is_Ad == 1)
                return AD_TYPE;
            else if (creations.get(position).is_Ad == 2)
                return EMPTY_TYPE;
            return CONTENT_TYPE;
        }
  /*      @Override
        public void onViewAttachedToWindow(Holder holder) {
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null) {
                if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                    if (creations.size() > 6) {
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
        @Override
        public void onBindViewHolder(@NonNull final Holder holder, final int position) {

            if (creations.get(position).is_Ad == AD_TYPE) {

                holder.adrl.setVisibility(View.VISIBLE);
                googleMaster.getInstance().showNative((HomeActivity) context, holder.frameLayout, holder.ad_text_native);

              /*  holder.adrl.setVisibility(View.VISIBLE);
                holder.textad.setVisibility(View.VISIBLE);
                holder.img_native.setVisibility(View.VISIBLE);

                AdmobAds.getInstance().loadfbNativeAd(context, holder.adLayout);
*/
            } else if (creations.get(position).is_Ad == EMPTY_TYPE) {
                holder.CvMain.setVisibility(View.INVISIBLE);
            } else {


                GlideImageLoader.SetImageResource(context, holder.IvDownload, R.drawable.creation_play, null);
                GlideImageLoader.SetImageUrl(context, holder.IvThumb, creations.get(position).filepath, null);

                holder.TvName.setText(creations.get(position).tname);
                holder.TvName.setSelected(true);

                holder.IvDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (new File(creations.get(position).filepath).exists()) {
                            Intent intent = new Intent(context, PlayerActivity.class);
                            intent.putExtra("filepath", creations.get(position).filepath);
                            intent.putExtra("creation", 0);
                            context.startActivity(intent);
                            ((CreationActivity) context).finish();
                        } else {
                            creations.remove(position);
                            refresh();
                        }

                    }
                });

                holder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        shareVideoMore(creations.get(position).filepath);
                    }
                });

                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(context)
                                .setTitle("Delete Creation")
                                .setMessage("Are you sure you want to delete this creation?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (new File(creations.get(position).filepath).exists()) {
                                            File file = new File(creations.get(position).filepath);
                                            file.delete();
                                        }
                                        manager.deletecreation(creations.get(position).filepath);
                                        creations.remove(position);
                                        refresh();
                                    }
                                })
                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

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

        public void shareVideoMore(String filepath) {

            Uri uri = FileProvider.getUriForFile(context, Constants.cpack + ".fileprovider", new File(filepath));
            String app_name = context.getResources().getString(R.string.app_name);
            Intent videoshare = new Intent(Intent.ACTION_SEND);
            videoshare.setType("video/*");
            String name = "App:" + app_name;
            videoshare.putExtra(Intent.EXTRA_SUBJECT, name);
            String text = app_name + "  : Lyrical Video Maker\n" +
                    "https://play.google.com/store/apps/details?id=" + Constants.cpack + "\n";
            videoshare.putExtra(Intent.EXTRA_TEXT, text);
            videoshare.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            videoshare.putExtra(Intent.EXTRA_STREAM, uri);

            startActivity(Intent.createChooser(videoshare, "Share Video"));

        }


        public void refresh() {
            checkcreation();
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return creations.size();
        }
    }


}
