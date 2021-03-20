package lyrically.photovideomaker.particl.ly.musicallybeat.ui.Theme;

import android.content.Context;
import android.graphics.Outline;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.ThemeResponse;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ScreenUtils;

import java.util.ArrayList;

public class ThemeFragment extends Fragment {


    Context context;
    MyApplication application;

    public ArrayList<ThemeResponse.Theme> themes = new ArrayList<>();

    RecyclerView recyclerView;
    ThemeAdapter adapter;

    public ThemeFragment() {
    }

    public void setThemes(ArrayList<ThemeResponse.Theme> themes) {
        this.themes = themes;
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
        View view = inflater.inflate(R.layout.fragment_theme, container, false);
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
        recyclerView = view.findViewById(R.id.recycler_view_themes);
    }

    public void setEvents() {

    }

    public void postWork() {

        ViewOutlineProvider mViewOutlineProvider2 = new ViewOutlineProvider() {
            @Override
            public void getOutline(final View view, final Outline outline) {

                int left = 0;
                int top = 0;
                int right = view.getWidth();
                int bottom = view.getHeight();
                float cornerRadiusDP = 1f;
                int cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerRadiusDP, context.getResources().getDisplayMetrics());

                outline.setRoundRect(left, top, right, bottom, cornerRadius);
            }
        };

        int widthint = (ScreenUtils.getScreenWidth(context) - ScreenUtils.convertDPItoINT(context, 60)) / 2;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthint, widthint + (widthint / 2));
        params.setMargins(0, 0, ScreenUtils.convertDPItoINT(context, 20), ScreenUtils.convertDPItoINT(context, 20));

        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setHasFixedSize(true);

        adapter = new ThemeAdapter(context, params, mViewOutlineProvider2);
        recyclerView.setAdapter(adapter);
        adapter.setThemeArrayList(themes);
    }

}