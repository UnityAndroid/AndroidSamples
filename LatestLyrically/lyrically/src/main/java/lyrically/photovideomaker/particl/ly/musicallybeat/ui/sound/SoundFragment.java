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
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.SoundResponse;

import java.util.ArrayList;
import java.util.List;

public class SoundFragment extends Fragment {

    Context context;
    MyApplication application;

    private List<SoundResponse.Sound> soundArrayList = new ArrayList<>();

    RecyclerView recyclerViewSounds;
    SoundAdapter soundAdapter;



    public SoundFragment() {
    }

    public void setSounds(List<SoundResponse.Sound> soundArrayList) {
        this.soundArrayList = soundArrayList;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_sound, container, false);
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

        soundAdapter = new SoundAdapter(context);
        recyclerViewSounds.setAdapter(soundAdapter);
        soundAdapter.setSoundArrayList(soundArrayList);
    }

}