package lyrically.photovideomaker.particl.ly.musicallybeat.ui.sound;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.SoundFile;

import java.util.ArrayList;
import java.util.List;

public class SoundStorageFragment extends Fragment {

    Context context;
    MyApplication application;

    private List<SoundFile> soundArrayList = new ArrayList<>();

    RecyclerView recyclerViewSounds;
    SoundStorageAdapter soundAdapter;

    public SoundStorageFragment() {
    }

    public void setSounds(List<SoundFile> soundArrayList) {
        this.soundArrayList = soundArrayList;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_sound_storage, container, false);
        preWork();
        getIDs(view);
        setEvents();
        postWork();
        return view;
    }

    public void preWork() {
        application = MyApplication.getInstance();
    }

    public void getIDs(View view) {
        recyclerViewSounds = view.findViewById(R.id.recycler_view_sounds);
    }

    public void setEvents() {

    }

    public void postWork() {

        recyclerViewSounds.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewSounds.setHasFixedSize(true);

        soundAdapter = new SoundStorageAdapter(context);
        recyclerViewSounds.setAdapter(soundAdapter);
        soundAdapter.setSoundArrayList(soundArrayList);
    }
}