package lyrically.photovideomaker.particl.ly.musicallybeat.ui.sound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.SoundResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.SoundStorage;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ConstantUtils;
import wseemann.media.FFmpegMediaMetadataRetriever;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import lyrically.photovideomaker.particl.ly.musicallybeat.dialog.DialogLoader;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.seekbar.OnRangeChangedListener;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.seekbar.RangeSeekBar;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.trimmer.CheapSoundFile;
import lyrically.photovideomaker.particl.ly.musicallybeat.extras.trimmer.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class SoundActivity extends AppCompatActivity {

    Context context;
    MyApplication application;
    ImageView backImageView;
    ImageView imageViewBack;
    TextView textViewTitle1;

    public ArrayList<SoundResponse.SoundCategory> soundCategoryList = new ArrayList<>();
    ViewPager viewPagerSounds;
    RecyclerView recyclerViewCategorys;
    SoundCategoryAdapter soundCategoryAdapter;
    SoundPagerAdapter soundPagerAdapter;

    public ArrayList<SoundStorage> soundStorages = new ArrayList<>();
    ViewPager viewPagerStorages;
    RecyclerView recyclerViewStorageCategorys;
    SoundStorageCategoryAdapter categoryAdapter;
    SoundStoragePagerAdapter storagePagerAdapter;

    public MediaPlayer mediaPlayer;
    public String current_sound = "", current_title = "";
    ConstraintLayout constraintSoundOnline, constraintSoundStorage;
    boolean isOnline = false, isFirst1 = true, isFirst2 = true;

    boolean isSeek = false;
    TextView textViewName, timeText1, timeText2;
    RangeSeekBar rangeSeekBar;
    public int max_value;
    public boolean is_left_touch = false, tm_check = false;
    public CountDownTimer tm;
    public int tm_timer = 0;

    public DialogLoader dialogLoader;
    ConstraintLayout nodata;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound);
        preWork();
        getIDs();
        setEvents();
        postWork();
    }

    public void preWork() {
        application = MyApplication.getInstance();
        context = this;
        initializeDialog(context, "Loading Please Wait");
    }

    public void getIDs() {
        imageViewBack = findViewById(R.id.tool_left_icon);
        textViewTitle1 = findViewById(R.id.tool_title);

        nodata = findViewById(R.id.no_data);
        recyclerViewCategorys = findViewById(R.id.recycler_view_categorys);
        viewPagerSounds = findViewById(R.id.sound_viewpager);

        constraintSoundOnline = findViewById(R.id.sound_online);
        constraintSoundStorage = findViewById(R.id.sound_storage);

        recyclerViewStorageCategorys = findViewById(R.id.recycler_view_storage_categorys);
        viewPagerStorages = findViewById(R.id.sound_storage_viewpager);

        timeText1 = findViewById(R.id.storage_time_text);
        timeText2 = findViewById(R.id.storage_time_text2);
        textViewName = findViewById(R.id.storage_sound_name);
        backImageView = findViewById(R.id.background);
        rangeSeekBar = findViewById(R.id.sb_range_2);

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void SelectMusic(String sound_path, String sound_title, String lyrics) {

        application.appTheme.sound_name = sound_title;
        application.appTheme.sound_path = sound_path;
        application.appTheme.lyrics = lyrics;

        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "YES");
        intent.putExtra("final_sound_path", sound_path);

        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void SelectStorageMusic(String sound_path, String sound_title) {

        application.appTheme.sound_name = sound_title;
        application.appTheme.sound_path = sound_path;
        application.appTheme.lyrics = "";

        if (sound_path.equalsIgnoreCase(current_sound)) {
            trimaudio();
        } else {
            setCutterseekbar(sound_path);
        }
    }

    public void setEvents() {
        imageViewBack.setOnClickListener(view -> onBackPressed());

        //  textViewTitle1.setOnClickListener(view -> showMyMusic());

        //  textViewTitle2.setOnClickListener(view -> showOnline());

        rangeSeekBar.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {
                if (max_value > 30) {
                    if (isFromUser) {
                        if (is_left_touch) {
                            if ((leftValue + 30) <= max_value) {
                                rangeSeekBar.setProgress(leftValue, leftValue + 30);
                                //   textViewTime.setText(getDurationString((int) leftValue) + " - " + getDurationString((int) rightValue));
                            } else if ((leftValue + 30) > max_value) {
                                rangeSeekBar.setProgress(max_value - 30, max_value);
                            }
                        } else {
                            if ((rightValue - 30) >= 0) {
                                rangeSeekBar.setProgress(rightValue - 30, rightValue);
                                //  textViewTime.setText(getDurationString((int) leftValue) + " - " + getDurationString((int) rightValue));

                            } else if ((rightValue - 30) < 30) {
                                rangeSeekBar.setProgress(0, 30);
                            }
                        }
                    }
                } else {
                    if (isFromUser) {
                        rangeSeekBar.setProgress(0, max_value);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {
                is_left_touch = isLeft;
            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {
                if (max_value > 30) {
                    if (mediaPlayer != null) {
                        mediaPlayer.seekTo((int) rangeSeekBar.leftSB.getProgress() * 1000);
                        if (!mediaPlayer.isPlaying()) {
                            mediaPlayer.start();
                            refreshStorage();
                        }
                        tm_timer = 30000;
                        pausemediaplayer(tm_timer);

                    }
                }
                //     textViewTime.setText(getDurationString((int) rangeSeekBar.leftSB.getProgress()) + " - " + getDurationString((int) rangeSeekBar.rightSB.getProgress()));

            }
        });


    }

    public void postWork() {
        Glide.with(context).load(R.drawable.all_background).into(backImageView);

        textViewTitle1.setText(getResources().getString(R.string.title_sound));
        rangeSeekBar.getLeftSeekBar().setThumbDrawableId(R.drawable.song_slider_left);
        rangeSeekBar.getRightSeekBar().setThumbDrawableId(R.drawable.song_slider_right);

        rangeSeekBar.setRange(0f, 100f);
        rangeSeekBar.setProgress(0f, 30f);
        rangeSeekBar.setEnabled(false);

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setLooping(true);
        }

        showLoader();
        setData();

    }


    public void setData() {

        constraintSoundOnline.setVisibility(View.GONE);
        constraintSoundStorage.setVisibility(View.GONE);

        setOnline();
    }


    public void showMyMusic() {
        isOnline = false;
        constraintSoundOnline.setVisibility(View.GONE);
        constraintSoundStorage.setVisibility(View.VISIBLE);

        //  textViewTitle1.setTextColor(Color.parseColor("#000000"));
        //   textViewTitle2.setTextColor(Color.parseColor("#4d000000"));

        if (isFirst1) {
            isFirst1 = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoader();
                }
            }, 200);
        }
    }

    public void showOnline() {
        isOnline = true;
        constraintSoundStorage.setVisibility(View.GONE);
        constraintSoundOnline.setVisibility(View.VISIBLE);

        if (isFirst1) {
            isFirst1 = false;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideLoader();
                }
            }, 200);
        }

        //  textViewTitle1.setTextColor(Color.parseColor("#4d000000"));
        // textViewTitle2.setTextColor(Color.parseColor("#000000"));

       /* if (isFirst1) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    showMyMusic();
                }
            }, 100);
        }*/
    }


    public void setOnline() {
        for (int i = 0; i < application.getSoundResponse().data.size(); i++) {
            if (application.getSoundResponse().data.get(i).sounds.size() > 0) {
                soundCategoryList.add(application.getSoundResponse().data.get(i));
            }
        }

        recyclerViewCategorys.setHasFixedSize(true);
        recyclerViewCategorys.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        SoundCategoryAdapter.callback callback = position -> viewPagerSounds.setCurrentItem(position);

        soundCategoryAdapter = new SoundCategoryAdapter(context, soundCategoryList, callback);
        recyclerViewCategorys.setAdapter(soundCategoryAdapter);

        soundPagerAdapter = new SoundPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerSounds.setAdapter(soundPagerAdapter);
        viewPagerSounds.setOffscreenPageLimit(3);

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                soundCategoryAdapter.setSelectedIndex(position);
                soundCategoryAdapter.notifyDataSetChanged();
                recyclerViewCategorys.scrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        viewPagerSounds.addOnPageChangeListener(pageChangeListener);
        showOnline();
    }


    public void setMyMusic() {

        soundStorages = application.soundArrayList;

        recyclerViewStorageCategorys.setHasFixedSize(true);
        recyclerViewStorageCategorys.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        SoundStorageCategoryAdapter.callback callback = position -> viewPagerStorages.setCurrentItem(position);

        categoryAdapter = new SoundStorageCategoryAdapter(context, soundStorages, callback);
        recyclerViewStorageCategorys.setAdapter(categoryAdapter);

        if (categoryAdapter.getItemCount() > 0)
            nodata.setVisibility(View.GONE);

        storagePagerAdapter = new SoundStoragePagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerStorages.setAdapter(storagePagerAdapter);
        viewPagerStorages.setOffscreenPageLimit(3);

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                categoryAdapter.setSelectedIndex(position);
                categoryAdapter.notifyDataSetChanged();
                recyclerViewStorageCategorys.scrollToPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };

        viewPagerStorages.addOnPageChangeListener(pageChangeListener);
        showOnline();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null)
            mediaPlayer.release();

        tm_check = false;
        if (tm != null) {
            tm.cancel();
        }
    }

    public void checkMusicIcon(String path, ImageView imageView, TextView textView) {
        if (current_sound.equalsIgnoreCase(path)) {
            textView.setBackgroundResource(R.drawable.catsound_back_selected);
            if (mediaPlayer.isPlaying()) {
                imageView.setBackgroundResource(R.drawable.sound_pause_background);
                Glide.with(SoundActivity.this).load(R.drawable.sound_pause_icon).into(imageView);
            } else {
                textView.setBackgroundResource(R.drawable.catsound_back_normal);
                imageView.setBackgroundResource(R.drawable.sound_play_background);
                Glide.with(SoundActivity.this).load(R.drawable.sound_play_icon).into(imageView);
            }
        } else {
            textView.setBackgroundResource(R.drawable.catsound_back_normal);
            imageView.setBackgroundResource(R.drawable.sound_play_background);
            Glide.with(SoundActivity.this).load(R.drawable.sound_play_icon).into(imageView);
        }
    }

    public void playPauseSound(String path, String title) {
        if (current_sound.equalsIgnoreCase(path)) {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
                refreshOnline();
                //  refreshStorage();
            } else {
                playSound(path, title);
            }
        } else {
            playSound(path, title);
        }
    }

    public void playSound(String path, String position) {

        current_sound = path;
        current_title = position;
        try {
            if (!isOnline)
                setCutterseekbar("");

            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setLooping(true);
            } else {
                mediaPlayer.reset();
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaPlayer.setAudioAttributes(new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build());
            } else {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            }

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                    refreshOnline();
                    //  refreshStorage();
                }
            });

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    refreshOnline();
                    // refreshStorage();
                }
            });

            mediaPlayer.setDataSource("file://" + path);
            mediaPlayer.prepare();

        } catch (Exception e) {
            ConstantUtils.showErrorLog(e.toString());
            Toast.makeText(this, "Error Playing Song", Toast.LENGTH_SHORT).show();
        }
    }


    public void refreshStorage() {
        // viewPager2StorageAdapter.notifyDataSetChanged();
        storagePagerAdapter.notifyDataSetChanged();

    }

    public void refreshOnline() {
        soundPagerAdapter.notifyDataSetChanged();
    }

    public void setCutterseekbar(String position) {
        max_value = getDuration(new File(current_sound));
        rangeSeekBar.setRange(0f, (float) max_value);
        textViewName.setText(current_title);
        textViewName.setSelected(true);
        rangeSeekBar.setEnabled(true);

        if (max_value > 30) {
            rangeSeekBar.setProgress(0, 30);
            timeText1.setText(getDurationString((int) rangeSeekBar.getMinProgress()));
            timeText2.setText(getDurationString(30));
        } else {
            rangeSeekBar.setProgress(0, max_value);
            timeText1.setText(getDurationString((int) rangeSeekBar.getMinProgress()));
            timeText2.setText(getDurationString(max_value));
        }

        if (!position.equalsIgnoreCase("")) {
            trimaudio();
        }
    }

    private static int getDuration(File file) {
        if (file.exists()) {
            try {
                FFmpegMediaMetadataRetriever retriever = new FFmpegMediaMetadataRetriever();
                retriever.setDataSource(file.getAbsolutePath());
                String durationStr = retriever.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION);
                return (int) TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(durationStr));
            } catch (Exception e) {
                Log.e(Application.class.getName(), e.toString());
                return 0;
            }
        } else return 0;
    }

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
        tm_check = true;

        tm = new CountDownTimer(time, 1000) {
            public void onTick(long millisUntilFinished) {
                tm_timer = 30000 - (int) millisUntilFinished;
            }

            public void onFinish() {
                tm_check = false;
                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        refreshStorage();
                    }
                }
            }
        }.start();
    }

    public void trimaudio() {

        try {

            final String outputpath = ConstantUtils.GetSoundPath(context) + "cutter.mp3";
            File file = new File(outputpath);

            CheapSoundFile cheapSoundFile = CheapSoundFile.create(application.appTheme.sound_path, new CheapSoundFile.ProgressListener() {
                @Override
                public boolean reportProgress(double fractionComplete) {

                    if (fractionComplete == 1000) {

                        application.appTheme.sound_path = outputpath;

                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE", "YES");
                        intent.putExtra("final_sound_path", outputpath);

                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }
                    return true;
                }
            });

            int mSampleRate = cheapSoundFile.getSampleRate();
            int mSamplesPerFrame = cheapSoundFile.getSamplesPerFrame();
            int startFrame = Util.secondsToFrames(rangeSeekBar.leftSB.getProgress(), mSampleRate, mSamplesPerFrame);
            int endFrame = Util.secondsToFrames(rangeSeekBar.rightSB.getProgress(), mSampleRate, mSamplesPerFrame);
            cheapSoundFile.WriteFile(file, startFrame, endFrame - startFrame);

        } catch (Exception e) {
            Log.e(Application.class.getName(), e.toString());
        }
    }


    public class SoundPagerAdapter extends FragmentPagerAdapter {

        SoundPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            SoundFragment resto = new SoundFragment();
            resto.setSounds(soundCategoryList.get(position).sounds);
            return resto;
        }

        @Override
        public int getCount() {
            return soundCategoryList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }
    }


    public class SoundStoragePagerAdapter extends FragmentPagerAdapter {

        SoundStoragePagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            SoundStorageFragment resto = new SoundStorageFragment();
            resto.setSounds(soundStorages.get(position).sound_files);
            return resto;
        }

        @Override
        public int getCount() {
            return soundStorages.size();
        }

        @Override
        public int getItemPosition(Object object) {
            // POSITION_NONE makes it possible to reload the PagerAdapter
            return POSITION_NONE;
        }
    }


}