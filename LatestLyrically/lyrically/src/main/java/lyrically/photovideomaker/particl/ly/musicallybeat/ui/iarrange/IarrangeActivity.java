package lyrically.photovideomaker.particl.ly.musicallybeat.ui.iarrange;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.model.CropImage;
import lyrically.photovideomaker.particl.ly.musicallybeat.util.ScreenUtils;

public class IarrangeActivity extends AppCompatActivity {

    ImageView backImageView;
    Context context;
    MyApplication application;

    ImageView imageViewBack;
    TextView textViewTitle, textViewDone;

    RecyclerView recyclerViewCrops;
    ArrangeAdapter sImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iarrange);
        preWork();
        getIDs();
        setEvents();
        postWork();
    }

    public void preWork() {
        application = MyApplication.getInstance();
        context = this;
    }

    public void getIDs() {
        imageViewBack = findViewById(R.id.tool_left_icon);
        textViewTitle = findViewById(R.id.tool_title);
        textViewDone = findViewById(R.id.tool_right_icon);
        backImageView = findViewById(R.id.background);

        recyclerViewCrops = findViewById(R.id.recycler_view_arrange);

        textViewTitle.setText(context.getResources().getString(R.string.title_aselect));
    }

    public void setEvents() {
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        textViewDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doneCrop();
            }
        });
    }

    public void postWork() {

        Glide.with(context).load(R.drawable.all_background).into(backImageView);
        ViewOutlineProvider mViewOutlineProvider2 = new ViewOutlineProvider() {
            @Override
            public void getOutline(final View view, final Outline outline) {

                int left = 0;
                int top = 0;
                int right = view.getWidth();
                int bottom = view.getHeight();
                float cornerRadiusDP = 5f;
                int cornerRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerRadiusDP, context.getResources().getDisplayMetrics());

                outline.setRoundRect(left, top, right, bottom, cornerRadius);
            }
        };

        int widthint = (ScreenUtils.getScreenWidth(context) - ScreenUtils.convertDPItoINT(context, 40)) / 3;
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthint, widthint);
        params.setMargins(0, ScreenUtils.convertDPItoINT(context, 10), 0, ScreenUtils.convertDPItoINT(context, 10));

        recyclerViewCrops.setLayoutManager(new GridLayoutManager(context, 3));
        recyclerViewCrops.setHasFixedSize(true);

        sImageAdapter = new ArrangeAdapter(context, mViewOutlineProvider2, params);
        recyclerViewCrops.setAdapter(sImageAdapter);

        ItemTouchHelper.Callback callback = new ItemMoveCallback(sImageAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerViewCrops);
    }

    public void doneCrop() {
        application.getCropImages().clear();
        String final_image_path = "";

        for (int i = 0; i < application.tempImages.size(); i++) {
            CropImage image = application.tempImages.get(i);
            image.setPosition(i);
            final_image_path = final_image_path.concat(image.getCroppath());
            final_image_path = final_image_path.concat("?");
            application.getCropImages().add(image);
        }
        final_image_path = final_image_path.substring(0, final_image_path.length() - 1);
        application.getTempImages().clear();

        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "YES");
        intent.putExtra("final_image_path", final_image_path);

        setResult(Activity.RESULT_OK, intent);
        finish();

    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
}