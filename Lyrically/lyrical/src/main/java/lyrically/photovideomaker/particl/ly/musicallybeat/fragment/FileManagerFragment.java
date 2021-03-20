package lyrically.photovideomaker.particl.ly.musicallybeat.fragment;


import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lyrically.photovideomaker.particl.ly.musicallybeat.AndroidPlugin;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.activity.SongSelectActivity;
import lyrically.photovideomaker.particl.ly.musicallybeat.audiotrimmer.CheapSoundFile;
import lyrically.photovideomaker.particl.ly.musicallybeat.audiotrimmer.Util;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.SdSound;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.Constants;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.DatabaseManager;
import lyrically.photovideomaker.particl.ly.musicallybeat.other.GlideImageLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.seekbar.OnRangeChangedListener;
import lyrically.photovideomaker.particl.ly.musicallybeat.seekbar.RangeSeekBar;
import com.unity3d.player.UnityPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


/**
 * A simple {@link Fragment} subclass.
 */
public class FileManagerFragment extends Fragment {

    private static final String TAG = FileManagerFragment.class.getSimpleName();

    public DatabaseManager manager;
    public Context context;
    public TextView nomp3, timetext;
    public int maxvalue;
    RecyclerView recyclerView;
    public RelativeLayout cutterrl;
    public RangeSeekBar seekbar;
    public String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
            + " AND " + MediaStore.Audio.Media.MIME_TYPE + " LIKE 'audio/mpeg'"
            + " AND " + MediaStore.Audio.Media.DURATION + " > 5000";

    public String[] projection = {
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.MIME_TYPE
    };

    public ArrayList<SdSound> soundArrayList = new ArrayList<>();

    public String current_sound = "";
    public MediaPlayer mediaPlayer;
    AlbumAdapter albumAdapter;


    public CountDownTimer tm;
    public int tmtimer = 0;

    public boolean islefttouch = false, tmcheck = false;

    public FileManagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        context = getActivity();
        View view = inflater.inflate(R.layout.fragment_file_manager, container, false);
        manager = new DatabaseManager(context);
        getIDs(view);
        setEvents();
        doWork();

        return view;
    }

    private void getIDs(View view) {
        recyclerView = view.findViewById(R.id.song_recycler);
        cutterrl = view.findViewById(R.id.cutter_rl);
        seekbar = view.findViewById(R.id.sb_range_2);
        timetext = view.findViewById(R.id.time_text);
        nomp3 = view.findViewById(R.id.mp3_no_text);

    }

    private void setEvents() {

        seekbar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                if (maxvalue > 30) {
                    if (isFromUser) {
                        if (islefttouch) {
                            if ((leftValue + 30) <= maxvalue) {
                                seekbar.setProgress(leftValue, leftValue + 30);
                                timetext.setText(getDurationString((int) leftValue) + " - " + getDurationString((int) rightValue));
                            } else if ((leftValue + 30) > maxvalue) {
                                seekbar.setProgress(maxvalue - 30, maxvalue);
                            }
                        } else {
                            if ((rightValue - 30) >= 0) {
                                seekbar.setProgress(rightValue - 30, rightValue);
                                timetext.setText(getDurationString((int) leftValue) + " - " + getDurationString((int) rightValue));

                            } else if ((rightValue - 30) < 30) {
                                seekbar.setProgress(0, 30);
                            }
                        }/*
                    else if (leftValue > maxvalue - 30)
                        if () {
                            seekbar.setProgress(maxvalue - 30, maxvalue);
                        }*/
                    }
                } else {
                    if (isFromUser) {
                        seekbar.setProgress(0, maxvalue);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                islefttouch = isLeft;
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                if (maxvalue > 30) {
                    if (mediaPlayer != null) {
                        mediaPlayer.seekTo((int) seekbar.leftSB.getProgress() * 1000);
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            albumAdapter.notifyDataSetChanged();
                        }
                        tmtimer = 30000;
                        pausemediaplayer(tmtimer);

                    }
                }
                timetext.setText(getDurationString((int) seekbar.leftSB.getProgress()) + " - " + getDurationString((int) seekbar.rightSB.getProgress()));

            }
        });


    }

    private void doWork() {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(((SongSelectActivity) context), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        } else {
            new GetBuckets().execute();
        }
    }

    private class GetBuckets extends AsyncTask<String, Integer, String> {
        Cursor cursor;

        public GetBuckets() {
            cursor = ((SongSelectActivity) context).getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    null,
                    null);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... sUrl) {

            try {
                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)) != null) {
                            if (cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)).endsWith(".mp3")) {
                                SdSound sdSound = new SdSound(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME)), cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)), cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                                soundArrayList.add(sdSound);
                            }
                        }
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }


            return "hello";
        }

        @Override
        protected void onPostExecute(final String result) {

            if (cursor != null)
                cursor.close();

            if (soundArrayList.size() > 0) {
                nomp3.setVisibility(View.GONE);
                current_sound = soundArrayList.get(0).path;
                cutterrl.setVisibility(View.VISIBLE);
            } else {
                cutterrl.setVisibility(View.GONE);
                nomp3.setVisibility(View.VISIBLE);
            }
            setAlbums();
        }

    }

    public void setAlbums() {
        albumAdapter = new AlbumAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(albumAdapter);

        if (soundArrayList.size() > 0) {
            current_sound = soundArrayList.get(0).path;
            setCutterseekbar();
        }
    }


    public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.Holder> {
        private boolean on_attach = true;
        long DURATION = 500;

        public AlbumAdapter() {
        }

        public class Holder extends RecyclerView.ViewHolder {
            TextView title;
            ImageView use, play;
            LinearLayout item_back;

            public Holder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.song_name);
                use = itemView.findViewById(R.id.download_icon);
                item_back = itemView.findViewById(R.id.song_item_linear);
                play = itemView.findViewById(R.id.play_icon);
            }
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new Holder(LayoutInflater.from(context).inflate(R.layout.item_song, viewGroup, false));
        }

        private String millisecondsToTime(long milliseconds) {
            long minutes = (milliseconds / 1000) / 60;
            long seconds = (milliseconds / 1000) % 60;
            String secondsStr = Long.toString(seconds);
            String secs;
            if (secondsStr.length() >= 2) {
                secs = secondsStr.substring(0, 2);
            } else {
                secs = "0" + secondsStr;
            }

            return minutes + ":" + secs;
        }


        @Override
        public void onBindViewHolder(@NonNull Holder holder, final int i) {

            holder.title.setText(soundArrayList.get(i).title);

            if (current_sound.equalsIgnoreCase(soundArrayList.get(i).path)) {
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        GlideImageLoader.SetImageResource(context, holder.play, R.drawable.song_pause_icon, null);
                    } else {
                        GlideImageLoader.SetImageResource(context, holder.play, R.drawable.song_play_icon, null);
                    }
                } else {
                    GlideImageLoader.SetImageResource(context, holder.play, R.drawable.song_play_icon, null);
                }
            } else {
                GlideImageLoader.SetImageResource(context, holder.play, R.drawable.song_play_icon, null);
            }

            GlideImageLoader.SetImageResource(context, holder.use, R.drawable.song_use_button, null);


            holder.play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current_sound.equalsIgnoreCase(soundArrayList.get(i).path)) {
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                            } else {
                                mediaPlayer.start();
                            }
                        } else {
                            playsong(current_sound);
                        }
                    } else {
                        playsong(current_sound);

                    }
                    notifyDataSetChanged();
                }
            });

            holder.title.setEllipsize(TextUtils.TruncateAt.MARQUEE);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (current_sound.equalsIgnoreCase(soundArrayList.get(i).path)) {
                        if (mediaPlayer != null) {
                            if (mediaPlayer.isPlaying()) {
                                mediaPlayer.pause();
                            } else {
                                mediaPlayer.start();
                            }
                        } else {
                            playsong(current_sound);
                        }
                    } else {
                        playsong(current_sound);
                    }

                    notifyDataSetChanged();
                }
            });
            holder.use.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = i;
                    trimaudio(position);
                    // getImagesFromBuckets(position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return soundArrayList.size();
        }
    }

    public void playsong(String path) {
        current_sound = path;

        setCutterseekbar();
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setLooping(true);
            }

            mediaPlayer.reset();

            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            if ((int) seekbar.leftSB.getProgress() > 0) {
                mediaPlayer.seekTo((int) seekbar.leftSB.getProgress() * 1000);
            }
            mediaPlayer.start();
            if (maxvalue > 30) {
                pausemediaplayer(30000);
            }
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    albumAdapter.notifyDataSetChanged();
                }
            });


        } catch (Exception e) {
            Log.e(TAG, e.toString());

            AndroidPlugin.showToast(context,"Error Playing Song");
        }
    }

    public void setCutterseekbar() {
      //  maxvalue = getDuration(new File(current_sound));
        seekbar.setRange(0f, (float) maxvalue);

        if (maxvalue > 30) {
            seekbar.setProgress(0, 30);
            timetext.setText(getDurationString((int) seekbar.getMinProgress()) + " - " + getDurationString(30));
        } else {
            seekbar.setProgress(0, maxvalue);
            timetext.setText(getDurationString((int) seekbar.getMinProgress()) + " - " + getDurationString(maxvalue));
        }
    }

    /*private static int getDuration(File file) {
        if (file.exists()) {
            try {
                FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
                retriever.setDataSource(file.getAbsolutePath());
                String durationStr = retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
                return (int) TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(durationStr));
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                return 0;
            }
        } else return 0;
    }
*/
    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;

        return twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }

    private String twoDigitString(int number) {

        if (number == 0) {
            return "00";
        }
        if (number / 10 == 0) {
            return "0" + number;
        }
        return String.valueOf(number);
    }

    public void pausemediaplayer(int time) {
        if (tm != null) {
            tm.cancel();
        }
        tmcheck = true;

        tm = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                tmtimer = 30000 - (int) millisUntilFinished;
            }

            public void onFinish() {
                tmcheck = false;
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        albumAdapter.notifyDataSetChanged();
                    }
                }
            }
        }.start();
    }

    public void trimaudio(final int position) {

        try {
            final String outputpath = AndroidPlugin.GetSoundPath() + String.valueOf(System.currentTimeMillis()) + ".mp3";
            File file = new File(outputpath);

            CheapSoundFile cheapSoundFile = CheapSoundFile.create(soundArrayList.get(position).path, new CheapSoundFile.ProgressListener() {
                @Override
                public boolean reportProgress(double fractionComplete) {
                    if (fractionComplete == 1000) {
                        Constants.songname = soundArrayList.get(position).title;
                        Constants.android = false;
                        UnityPlayer.UnitySendMessage("SelectMusic", "GetSelectedMusicPath", outputpath);
                        ((SongSelectActivity) context).onBackPressed();
                        ((SongSelectActivity) context).finish();
                    }
                    return true;
                }
            });

            int mSampleRate = cheapSoundFile.getSampleRate();
            int mSamplesPerFrame = cheapSoundFile.getSamplesPerFrame();
            int startFrame = Util.secondsToFrames(seekbar.leftSB.getProgress(), mSampleRate, mSamplesPerFrame);
            int endFrame = Util.secondsToFrames(seekbar.rightSB.getProgress(), mSampleRate, mSamplesPerFrame);
            cheapSoundFile.WriteFile(file, startFrame, endFrame - startFrame);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.release();

        tmcheck = false;
        if (tm != null) {
            tm.cancel();
        }
    }

    @Override
    public void onPause() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                albumAdapter.notifyDataSetChanged();
            }
        }

        if (tm != null) {
            tm.cancel();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tmcheck) {
            pausemediaplayer(tmtimer);
        }
    }
}
