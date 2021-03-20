package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;

public class WAVideosFragment extends Fragment implements OnRefreshListener {

    private ArrayList<ModelStatus> data;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rv;
    private TextView textView;
    private LinearLayout adContainer;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_wa_status_videos, viewGroup, false);

        adContainer = (LinearLayout) inflate.findViewById(R.id.adBanner);

        rv = (RecyclerView) inflate.findViewById(R.id.rv_status);
        mSwipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.contentView);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        textView = (TextView) inflate.findViewById(R.id.textView);
        rv.setHasFixedSize(true);


        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        adContainer.removeAllViews();
        AdMobLoadAds.getInstance().loadBanner(getActivity(), adContainer);

        loadData();
    }

    public void loadData() {
        this.data = new ArrayList<>();
        File file = new File(Config.WhatsAppDirectoryPath);
        if (file.exists()) {
            final File[] listFiles = file.listFiles();
            final String[] strArr = {""};

            new AsyncTask<Void, Void, Void>() {
                @Override
                public Void doInBackground(Void... voidArr) {
                    for (int i = 0; i < listFiles.length; i++) {
                        if (listFiles[i].getName().endsWith(".mp4")) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(Config.WhatsAppDirectoryPath);
                            sb3.append(listFiles[i].getName());
                            strArr[0] = sb3.toString();
                            data.add(new ModelStatus(strArr[0], listFiles[i].getName().substring(0, listFiles[i].getName().length() - 4), 0));
                        }
                    }
                    return null;
                }

                @Override
                public void onPostExecute(Void voidR) {
                    super.onPostExecute(voidR);
                    if (data.toArray().length == 0) {
                        textView.setVisibility(View.VISIBLE);
                        textView.setText("☹ No Status Available \n Check Out some Status & come back again...");
                    } else {
                        Collections.reverse(data);
                        rv.setAdapter(new WAVideosAdaptor(getActivity(), data));
                        rv.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    }
                }
            }.execute(new Void[0]);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText("☹ No Status Available \n Check Out some Status & come back again...");
            Snackbar.make(getActivity().findViewById(android.R.id.content), (CharSequence) "WhatsApp Not Installed", Snackbar.LENGTH_SHORT).show();
        }
        refreshItems();
    }


    @Override
    public void onRefresh() {
        loadData();
    }

    public void refreshItems() {
        onItemsLoadComplete();
    }

    public void onItemsLoadComplete() {
        this.mSwipeRefreshLayout.setRefreshing(false);
    }
}
