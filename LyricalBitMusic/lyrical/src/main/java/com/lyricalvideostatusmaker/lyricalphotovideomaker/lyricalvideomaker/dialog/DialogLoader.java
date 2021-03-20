package com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import com.lyricalvideostatusmaker.lyricalphotovideomaker.lyricalvideomaker.R;

public class DialogLoader extends Dialog {

    Context context;
    LottieAnimationView downIcon;
    TextView TvMsg;
    String msg;

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

        TvMsg = findViewById(R.id.wait_text);
        downIcon = findViewById(R.id.download_icon);

        TvMsg.setText(msg);

    }

}
