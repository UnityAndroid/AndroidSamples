package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;

public class WASavedAdaptor extends Adapter<WASavedAdaptor.MyViewHolder> {

    private final String TAG = "SAVEAdaptor";
    public Context acontext;
    public ArrayList<ModelStatus> arrayList;
    public int count = 4;
    String listenerValue = "";
    public List<Observer> observers;


    public WASavedAdaptor(Context context, ArrayList<ModelStatus> arrayList2) {
        arrayList = arrayList2;
        acontext = context;
        observers = new ArrayList();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wa_saved_pictures, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Glide.with(acontext).load(((ModelStatus) arrayList.get(i)).getFull_path()).into(myViewHolder.imageView);

        if (arrayList.get(i).getFull_path().endsWith(".mp4")) {
            myViewHolder.imgVideoPlay.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.imgVideoPlay.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends ViewHolder {

        public LinearLayout btn_delete;
        public LinearLayout btn_share;
        public ImageView imageView;
        public CardView mCardView;
        public ImageView imgVideoPlay;

        public MyViewHolder(View view) {
            super(view);
            mCardView = (CardView) view.findViewById(R.id.card_view_order_cancel);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            btn_delete = (LinearLayout) view.findViewById(R.id.btn_delete);
            imgVideoPlay = (ImageView) view.findViewById(R.id.imgVideoPlay);

            btn_delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        deleteFile(((ModelStatus) arrayList.get(getAdapterPosition())).getFull_path(), getAdapterPosition());
                    } catch (ArrayIndexOutOfBoundsException unused) {
                        unused.printStackTrace();
                    }
                }
            });

            btn_share = (LinearLayout) view.findViewById(R.id.btn_share);
            btn_share.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelStatus modelStatus = (ModelStatus) arrayList.get(getAdapterPosition());
                    if (modelStatus.getFull_path().endsWith(".jpg")) {
                        shareVia("image/jpg", modelStatus.getFull_path());
                    } else if (modelStatus.getFull_path().endsWith(".mp4")) {
                        shareVia("video/mp4", modelStatus.getFull_path());
                    }
                }
            });


            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelStatus modelStatus = (ModelStatus) arrayList.get(getAdapterPosition());
                    if (modelStatus.getFull_path().endsWith(".jpg")) {
                        Intent intent = new Intent(acontext, ImageViewerActivity.class);
                        intent.putExtra("image", modelStatus.getFull_path());
                        StringBuilder sb = new StringBuilder();
                        sb.append("");
                        sb.append(modelStatus.getType());
                        intent.putExtra("type", sb.toString());
                        intent.putExtra("atype", "0");
                        acontext.startActivity(intent);
                        return;
                    }
                    Intent intent2 = new Intent(acontext, VideoViewerActivity.class);
                    intent2.putExtra("video", modelStatus.getFull_path());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("");
                    sb2.append(modelStatus.getType());
                    intent2.putExtra("type", sb2.toString());
                    intent2.putExtra("atype", "0");
                    acontext.startActivity(intent2);
                }
            });
        }
    }


    public void shareVia(String str, String str2) {
        Uri imageUri = FileProvider.getUriForFile(acontext, acontext.getPackageName() + ".fileprovider", new File(str2));
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(str);
        intent.putExtra("android.intent.extra.STREAM", imageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        acontext.startActivity(Intent.createChooser(intent, "Share via"));
    }

    public void deleteFile(String str, int i) {
        File file = new File(str);
        if (!file.exists()) {
            return;
        }
        if (file.delete()) {
            removeAt(i);
            Toast.makeText(acontext, "Delete Success", Toast.LENGTH_SHORT).show();
            PrintStream printStream = System.out;
            StringBuilder sb = new StringBuilder();
            sb.append("file Deleted :");
            sb.append(str);
            printStream.println(sb.toString());
            return;
        }
        Toast.makeText(acontext, "Delete Failed", Toast.LENGTH_SHORT).show();
        PrintStream printStream2 = System.out;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("file not Deleted :");
        sb2.append(str);
        printStream2.println(sb2.toString());
    }

    public void removeAt(int i) {
        arrayList.remove(i);
        notifyItemRemoved(i);
        notifyItemRangeChanged(i, arrayList.size());
    }

    public void allSharedPreference(int i) {
        Editor edit = acontext.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE).edit();
        edit.putString("ALL", String.valueOf(i));
        edit.apply();
    }
}
