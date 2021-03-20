package lyrically.photovideomaker.particl.ly.musicallybeat.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.ui.iselect.IselectActivity;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

public class DialogSize extends Dialog {

    Context context;
    ConstraintLayout maincard;

    ConstraintLayout insta, status, youtube;
    ImageView viewInsta, viewStatus, viewYoutube;
    TextView manual, auto;

    String image_size;

    public DialogSize(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_size);
        Objects.requireNonNull(getWindow()).setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        image_size = MyApplication.getInstance().appTheme.image_size;

        preWork();
        getIDs();
        setEvents();
        postWork();


    }

    public void preWork() {

    }

    public void getIDs() {
        maincard = findViewById(R.id.main_card);

        insta = findViewById(R.id.linear_size_instagram);
        status = findViewById(R.id.linear_size_status);
        youtube = findViewById(R.id.linear_size_youtube);

        viewInsta = findViewById(R.id.image_insta);
        viewStatus = findViewById(R.id.image_status);
        viewYoutube = findViewById(R.id.image_youtube);

        manual = findViewById(R.id.btn_manual);
        auto = findViewById(R.id.btn_auto);

    }


    public void setEvents() {
        insta.setOnClickListener(v -> {
            if (image_size.equalsIgnoreCase("A"))
                setInstagram();
        });

        status.setOnClickListener(v -> {
            if (image_size.equalsIgnoreCase("A"))
                setStatus();
        });

        youtube.setOnClickListener(v -> {
            if (image_size.equalsIgnoreCase("A"))
                setYoutube();
        });

        manual.setOnClickListener(v -> {
            ((IselectActivity) context).gotoCrop(0);
            dismiss();
        });

        auto.setOnClickListener(v -> {
            ((IselectActivity) context).gotoCrop(1);
            dismiss();
        });
    }

    public void setInstagram() {
        Glide.with(context).load(R.drawable.video_size_selected_drawable).into(viewInsta);
        Glide.with(context).load(R.drawable.video_size_not_selected).into(viewStatus);
        Glide.with(context).load(R.drawable.video_size_not_selected).into(viewYoutube);
        ((IselectActivity) context).setSize(1, 1, "S");
    }

    public void setStatus() {
        Glide.with(context).load(R.drawable.video_size_not_selected).into(viewInsta);
        Glide.with(context).load(R.drawable.video_size_selected_drawable).into(viewStatus);
        Glide.with(context).load(R.drawable.video_size_not_selected).into(viewYoutube);
        ((IselectActivity) context).setSize(9, 16, "P");
    }

    public void setYoutube() {
        Glide.with(context).load(R.drawable.video_size_not_selected).into(viewInsta);
        Glide.with(context).load(R.drawable.video_size_not_selected).into(viewStatus);
        Glide.with(context).load(R.drawable.video_size_selected_drawable).into(viewYoutube);
        ((IselectActivity) context).setSize(16, 9, "L");
    }


    public void postWork() {
        ViewOutlineProvider mViewOutlineProvider2 = new ViewOutlineProvider() {
            @Override
            public void getOutline(final View view, final Outline outline) {

                int left = 0;
                int top = 0;
                int right = view.getWidth();
                int bottom = view.getHeight();
                float cornerRadiusDP = 10f;
                int cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerRadiusDP, context.getResources().getDisplayMetrics());

                outline.setRoundRect(left, top, right, bottom, cornerRadius);
            }
        };


        maincard.setOutlineProvider(mViewOutlineProvider2);
        maincard.setClipToOutline(true);

        if (image_size.equalsIgnoreCase("A"))
            setInstagram();
        else if (image_size.equalsIgnoreCase("S"))
            setInstagram();
        else if (image_size.equalsIgnoreCase("L"))
            setYoutube();
        else if (image_size.equalsIgnoreCase("P"))
            setStatus();

    }

}
