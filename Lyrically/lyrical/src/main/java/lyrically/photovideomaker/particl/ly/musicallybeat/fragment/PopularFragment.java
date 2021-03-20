package lyrically.photovideomaker.particl.ly.musicallybeat.fragment;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.activity.SongSelectActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogDownloadSong;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.SoundAlbum;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.AdmobAds;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PopularFragment extends Fragment {

    private static final String TAG = PopularFragment.class.getSimpleName();

    public Context context;

    public RecyclerView SongsRecycler, AlbumsRecycler;
    public DatabaseManager manager;

    AlbumAdapter albumAdapter;
    public SongsAdapter songsAdapter;

    public ArrayList<SoundAlbum> albums = new ArrayList<>();

    public int album_position = 0, song_position = 0;

    public Cursor songs;
    public String current_sound = "", final_sound = "";

    public MediaPlayer mediaPlayer;

    public PopularFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_popular, container, false);
        manager = new DatabaseManager(context);

        getIDs(view);
        setEvents();
        doWork();
        return view;
    }

    public void playsong(String path) {
        current_sound = path;

        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setLooping(true);
            }

            mediaPlayer.reset();

            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    albumAdapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            AndroidPlugin.showToast(context, "Error Playing Song");
        }
    }

    private void getIDs(View view) {
        SongsRecycler = view.findViewById(R.id.songs_popular_recycle);
        AlbumsRecycler = view.findViewById(R.id.songs_popular_category);
    }

    private void setEvents() {
    }

    private void doWork() {
        setTabView();
    }

    public void setTabView() {
        albums.clear();
        Cursor cats = null;
        try {
            cats = manager.featchSongCatagarytable();
            if (cats.moveToFirst()) {
                do {
                    Cursor curso = manager.featchSongtable(cats.getInt(0));
                    if (curso.moveToFirst()) {
                        SoundAlbum album = new SoundAlbum(cats.getInt(cats.getColumnIndex("Id")), cats.getString(cats.getColumnIndex("Cat_Name")));
                        albums.add(album);
                    }
                    curso.close();
                } while (cats.moveToNext());
            }
        } finally {
            if (cats != null)
                cats.close();
        }
        setrecycle();
    }

    public void setrecycle() {
        albumAdapter = new AlbumAdapter();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(RecyclerView.HORIZONTAL);
        AlbumsRecycler.setLayoutManager(manager);
        AlbumsRecycler.setAdapter(albumAdapter);

        songsAdapter = new SongsAdapter();
        SongsRecycler.setLayoutManager(new LinearLayoutManager(context));
        SongsRecycler.setAdapter(songsAdapter);
        SongsRecycler.setHasFixedSize(true);
        SongsRecycler.getRecycledViewPool().setMaxRecycledViews(1, 20);

        songsAdapter.switchsongs();

    }


    public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Holder> {

        public AlbumAdapter() {
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView album_name;
            ImageView album_back;

            public Holder(@NonNull View itemView) {
                super(itemView);
                album_name = itemView.findViewById(R.id.album_name);
                album_back = itemView.findViewById(R.id.album_background);
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Holder(LayoutInflater.from(context).inflate(R.layout.item_image_album, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int position) {
            SoundAlbum album = albums.get(position);
            holder.album_name.setText(album.name);
            holder.album_name.setSelected(true);

            if (album_position == position) {
                holder.album_back.setBackgroundResource(R.drawable.album_select_back);
                holder.album_name.setTextColor(Color.parseColor("#000000"));
            } else {
                holder.album_back.setBackgroundResource(R.drawable.album_normal_back);
                holder.album_name.setTextColor(Color.parseColor("#000000"));
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != album_position) {
                        notifyItemChanged(album_position);
                        album_position = position;
                        notifyItemChanged(album_position);
                        songsAdapter.switchsongs();
                        SongsRecycler.scrollToPosition(0);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return albums.size();
        }
    }

    public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.Holder> {
        private boolean on_attach = true;

        public SongsAdapter() {
            manager.removeanims();
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView title,tsize;
            ImageView use, play, download;
            LinearLayout item_back;

            public Holder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.song_name);
                download = itemView.findViewById(R.id.download_icon);
                play = itemView.findViewById(R.id.play_icon);
                item_back = itemView.findViewById(R.id.song_item_linear);
                use = itemView.findViewById(R.id.use_icon);
                tsize=itemView.findViewById(R.id.song_size);
            }
        }


        public void switchsongs() {
            if (songs != null)
                songs.close();
            songs = manager.featchSongtable(albums.get(album_position).id);
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new SongsAdapter.Holder(LayoutInflater.from(context).inflate(R.layout.item_song, viewGroup, false));
        }

        @Override
        public int getItemCount() {
            return songs.getCount();
        }

        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int i) {
            songs.moveToPosition(i);

            holder.title.setText(songs.getString(songs.getColumnIndex("sound_name")));
            holder.title.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.tsize.setText(songs.getString(songs.getColumnIndex("sound_size")));

            if (manager.checksongurl(songs.getString(songs.getColumnIndex("sound_url")))) {
                holder.download.setVisibility(View.GONE);
                holder.use.setVisibility(View.VISIBLE);
                GlideImageLoader.SetImageResource(context, holder.use, R.drawable.song_use_button, null);
            } else {
                holder.download.setVisibility(View.VISIBLE);
                holder.use.setVisibility(View.GONE);
                GlideImageLoader.SetImageResource(context, holder.download, R.drawable.song_download, null);
            }

            final Cursor download = manager.getdownloadedsoundfromurl(songs.getString(songs.getColumnIndex("sound_url")));
            if (download.moveToFirst()) {
                if (current_sound.equalsIgnoreCase(download.getString(download.getColumnIndex("sound")))) {
                    if (mediaPlayer != null) {
                        if (mediaPlayer.isPlaying()) {
                            GlideImageLoader.SetImageResource(context, holder.play, R.drawable.song_pause_icon, null);
                            GlideImageLoader.SetImageResource(context, holder.use, R.drawable.song_use_playing, null);
                        } else {
                            GlideImageLoader.SetImageResource(context, holder.play, R.drawable.song_play_icon, null);
                        }
                    } else {
                        GlideImageLoader.SetImageResource(context, holder.play, R.drawable.song_play_icon, null);
                    }
                } else {
                    GlideImageLoader.SetImageResource(context, holder.play, R.drawable.song_play_icon, null);
                }
            } else {
                GlideImageLoader.SetImageResource(context, holder.play, R.drawable.song_play_icon, null);
            }
            download.close();

            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    songs.moveToPosition(i);
                    if (manager.checksongurl(songs.getString(songs.getColumnIndex("sound_url")))) {
                        Cursor download = null;
                        try {
                            download = manager.getdownloadedsoundfromurl(songs.getString(songs.getColumnIndex("sound_url")));
                            if (download.moveToFirst()) {
                                if (new File(download.getString(1)).exists()) {
                                    if (current_sound.equalsIgnoreCase(download.getString(download.getColumnIndex("sound")))) {
                                        if (mediaPlayer != null) {
                                            if (mediaPlayer.isPlaying()) {
                                                mediaPlayer.pause();
                                            } else {
                                                mediaPlayer.start();
                                            }
                                        } else {
                                            playsong(download.getString(download.getColumnIndex("sound")));
                                        }
                                    } else {
                                        playsong(download.getString(download.getColumnIndex("sound")));
                                    }
                                } else {
                                    downloadsong(i);
                                }
                            } else {
                                downloadsong(i);
                            }
                        } finally {
                            download.close();
                        }
                        notifyDataSetChanged();
                    } else {
                        downloadsong(i);
                    }

                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    songs.moveToPosition(i);
                    if (manager.checksongurl(songs.getString(songs.getColumnIndex("sound_url")))) {
                        Cursor download = null;
                        try {
                            download = manager.getdownloadedsoundfromurl(songs.getString(songs.getColumnIndex("sound_url")));
                            if (download.moveToFirst()) {
                                if (new File(download.getString(1)).exists()) {
                                    if (current_sound.equalsIgnoreCase(download.getString(download.getColumnIndex("sound")))) {
                                        if (mediaPlayer != null) {
                                            if (mediaPlayer.isPlaying()) {
                                                mediaPlayer.pause();
                                            } else {
                                                mediaPlayer.start();
                                            }
                                        } else {
                                            playsong(download.getString(download.getColumnIndex("sound")));
                                        }
                                    } else {
                                        playsong(download.getString(download.getColumnIndex("sound")));
                                    }
                                } else {
                                    downloadsong(i);
                                }
                            } else {
                                downloadsong(i);
                            }
                        } finally {
                            download.close();
                        }
                        notifyDataSetChanged();
                    } else {
                        downloadsong(i);
                    }

                }
            });

            holder.use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    songs.moveToPosition(i);
                    if (manager.checksongurl(songs.getString(songs.getColumnIndex("sound_url")))) {
                        Cursor download = null;
                        try {
                            download = manager.getdownloadedsoundfromurl(songs.getString(songs.getColumnIndex("sound_url")));
                            if (download.moveToFirst()) {
                                final_sound = download.getString(download.getColumnIndex("sound"));
                                Constants.songname = songs.getString(songs.getColumnIndex("sound_name"));

                                final JSONObject jsonAdd = new JSONObject();
                                jsonAdd.put("soundpath", final_sound);
                                jsonAdd.put("lyrics", songs.getString(songs.getColumnIndex("lyrics")));

                                if (manager.getGoogleAdStatus() == 1) {
                                   AdmobAds.getInstance().displayInterstitial(context, new AdmobAds.MyCallback() {
                                        @Override
                                        public void callbackCall() {
                                            AndroidPlugin.UnityCall("SelectMusic", "GetSelectedMusic", jsonAdd.toString());

                                            ((SongSelectActivity) context).onBackPressed();
                                            ((SongSelectActivity) context).finish();
                                        }
                                    });
                                } else if (manager.getFacebookAdStatus() == 1) {
                                    AdmobAds.getInstance().displayfbInterstitial(context, new AdmobAds.MyCallback() {
                                        @Override
                                        public void callbackCall() {
                                           AndroidPlugin.UnityCall("SelectMusic", "GetSelectedMusic", jsonAdd.toString());

                                            ((SongSelectActivity) context).onBackPressed();
                                            ((SongSelectActivity) context).finish();
                                        }
                                    });
                                } else {
                                    AndroidPlugin.UnityCall("SelectMusic", "GetSelectedMusic", jsonAdd.toString());
                                    ((SongSelectActivity) context).onBackPressed();
                                    ((SongSelectActivity) context).finish();
                                }


                            }
                        } catch (Exception e) {

                        } finally {
                            if (download != null)
                                download.close();
                        }

                    } else {
                        DialogDownloadSong downloadDialog = new DialogDownloadSong(context, songs.getInt(songs.getColumnIndex("id")), songs.getString(songs.getColumnIndex("sound_full_url")), albums.get(album_position).id, songs.getString(songs.getColumnIndex("lyrics")));
                        downloadDialog.setCancelable(false);
                        downloadDialog.show();

                        downloadDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                notifyDataSetChanged();
                            }
                        });
                    }

                }
            });

            if (!manager.checkanims(i)) {
                setAnimation(holder.itemView, i);
            }
        }

        private void setAnimation(View itemView, int i) {
            manager.insertanims(i);
            if (!on_attach) {
                i = -1;
            }
            boolean isNotFirstItem = i == -1;
            i++;
            itemView.setAlpha(0.f);
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator animator = ObjectAnimator.ofFloat(itemView, "alpha", 0.f, 0.5f, 1.0f);
            ObjectAnimator.ofFloat(itemView, "alpha", 0.f).start();
            animator.setDuration(500);
            animatorSet.play(animator);
            animator.start();
        }

        private void setPopinAnimation(View viewToAnimate) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.pop_in);
            animation.setDuration(500);
            viewToAnimate.startAnimation(animation);
        }


        public void downloadsong(int position) {
            songs.moveToPosition(position);
            DialogDownloadSong downloadDialog = new DialogDownloadSong(context, songs.getInt(songs.getColumnIndex("id")), songs.getString(songs.getColumnIndex("sound_full_url")), albums.get(album_position).id, songs.getString(songs.getColumnIndex("lyrics")));
            downloadDialog.setCancelable(false);
            downloadDialog.show();

            downloadDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Cursor download = manager.getdownloadedsoundfromurl(songs.getString(songs.getColumnIndex("sound_url")));
                    if (download.moveToFirst()) {
                        if (current_sound.equalsIgnoreCase(download.getString(download.getColumnIndex("sound")))) {
                            if (mediaPlayer != null) {
                                if (mediaPlayer.isPlaying()) {
                                    mediaPlayer.pause();
                                } else {
                                    mediaPlayer.start();
                                }
                            } else {
                                playsong(download.getString(download.getColumnIndex("sound")));
                            }
                        } else {
                            playsong(download.getString(download.getColumnIndex("sound")));
                        }
                    }
                    download.close();
                    notifyDataSetChanged();
                }
            });
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

    @Override
    public void onPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                songsAdapter.notifyDataSetChanged();
            }
        }
        super.onPause();
    }


}
