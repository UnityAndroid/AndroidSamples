package lyrically.photovideomaker.particl.ly.musicallybeat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;


public class DialogLoader extends Dialog {

    Context context;
    TextView TvMsg;
    String msg;

    ImageView downIcon;

    public DialogLoader(Context context, String msg) {
        super(context);
        this.context = context;
        this.msg = msg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loader);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        preWork();
        getIDs();
        setEvents();
        postWork();

    }

    public void preWork() {
    }

    public void getIDs() {
        TvMsg = findViewById(R.id.wait_text);
        downIcon = findViewById(R.id.download_icon);

    }

    public void setEvents() {
    }

    public void postWork() {
        Glide.with(context).load(R.drawable.loading).into(downIcon);
        TvMsg.setText(msg);
        Animation rotation = AnimationUtils.loadAnimation(context, R.anim.rotate);
        rotation.setFillAfter(true);
        downIcon.startAnimation(rotation);
    }

    public void setText(String message) {
        TvMsg.setText(message);
    }

}
