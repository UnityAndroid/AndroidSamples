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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;
import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.other.AdMobLoadAds;

public class WAVideosAdaptor extends Adapter<WAVideosAdaptor.MyViewHolder> {

    private final String TAG = "VIDEOAdaptor";
    public Context acontext;
    public ArrayList<ModelStatus> arrayList;
    public int count = 4;
    String listenerValue = "";
    public List<Observer> observers;


    public WAVideosAdaptor(Context context, ArrayList<ModelStatus> arrayList2) {
        this.arrayList = arrayList2;
        this.acontext = context;
        this.observers = new ArrayList();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wa_videos, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        Glide.with(this.acontext).load(((ModelStatus) this.arrayList.get(i)).getFull_path()).into(myViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    public class MyViewHolder extends ViewHolder {
        public LinearLayout btn_download;
        public LinearLayout btn_share;
        public ImageView imageView;
        public ImageView img_btn_download;
        public ImageView img_btn_share;
        public CardView mCardView;

        public MyViewHolder(View view) {
            super(view);
            mCardView = (CardView) view.findViewById(R.id.card_view_order_cancel);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            btn_download = (LinearLayout) view.findViewById(R.id.btn_download);

            btn_download.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    ModelStatus modelStatus = (ModelStatus) arrayList.get(getAdapterPosition());
                    String str = "";
                    if (modelStatus.getType() == 0) {
                        str = Config.WhatsAppSaveStatus;
                    } else if (modelStatus.getType() == 1) {
                        str = Config.GBWhatsAppSaveStatus;
                    } else if (modelStatus.getType() == 2) {
                        str = Config.WhatsAppBusinessSaveStatus;
                    }
                    if (modelStatus.getFull_path().endsWith(".jpg")) {
                        copyFileOrDirectory(modelStatus.getFull_path(), str);
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(modelStatus.getPath());
                        sb.append(".jpg");
                        acontext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(sb.toString()))));
                    } else if (modelStatus.getFull_path().endsWith(".mp4")) {
                        copyFileOrDirectory(modelStatus.getFull_path(), str);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(str);
                        sb2.append(modelStatus.getPath());
                        sb2.append(".mp4");
                        acontext.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(new File(sb2.toString()))));
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
                    Intent intent = new Intent(acontext, VideoViewerActivity.class);
                    intent.putExtra("video", modelStatus.getFull_path());
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(modelStatus.getType());
                    intent.putExtra("type", sb.toString());
                    intent.putExtra("atype", "1");
                    acontext.startActivity(intent);

                }
            });
        }
    }

    public void copyFileOrDirectory(String str, String str2) {
        try {
            File file = new File(str);
            File file2 = new File(str2, file.getName());
            if (file.isDirectory()) {
                for (String file3 : file.list()) {
                    copyFileOrDirectory(new File(file, file3).getPath(), file2.getPath());
                }
                return;
            }
            copyFile(file, file2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void copyFile(File file, File file2) throws IOException {
        if (!file2.getParentFile().exists()) {
            file2.getParentFile().mkdirs();
        }
        if (!file2.exists()) {
            file2.createNewFile();
        }
        try {
            FileChannel fileChannel2 = new FileInputStream(file).getChannel();
            FileChannel fileChannel = new FileOutputStream(file2).getChannel();
            fileChannel.transferFrom(fileChannel2, 0, fileChannel2.size());
            Toast.makeText(acontext, "Video Saved", Toast.LENGTH_SHORT).show();
            if (fileChannel2 != null) {
                fileChannel2.close();
            }
            if (fileChannel != null) {
                fileChannel.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareVia(String str, String str2) {
        Uri imageUri = FileProvider.getUriForFile(acontext, acontext.getPackageName() + ".fileprovider", new File(str2));
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(str);
        intent.putExtra("android.intent.extra.STREAM", imageUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        this.acontext.startActivity(Intent.createChooser(intent, "Share via"));
    }

    public void allSharedPreference(int i) {
        Editor edit = this.acontext.getSharedPreferences("PREFRENCE", Context.MODE_PRIVATE).edit();
        edit.putString("ALL", String.valueOf(i));
        edit.apply();
    }
}
