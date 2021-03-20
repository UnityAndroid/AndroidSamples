package lyrically.photovideomaker.particl.ly.musicallybeat.ui.text;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import lyrically.photovideomaker.particl.ly.musicallybeat.MyApplication;
import lyrically.photovideomaker.particl.ly.musicallybeat.R;

import java.util.ArrayList;

public class TextActivity extends AppCompatActivity {

    Context context;
    MyApplication application;

    ImageView imageViewBack;
    TextView textViewTitle, textViewDone;

    LinearLayout linearLayout;
    ArrayList<TextView> textViews = new ArrayList<>();
    String data = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        preWork();
        getIDs();
        setEvents();
        postWork();
    }

    public void preWork() {
        if (getIntent() != null && getIntent().hasExtra("text"))
            data = getIntent().getStringExtra("text");


        application = MyApplication.getInstance();
        context = this;
    }

    public void getIDs() {
        imageViewBack = findViewById(R.id.tool_left_icon);
        textViewTitle = findViewById(R.id.tool_title);
        textViewDone = findViewById(R.id.tool_right_icon);

        linearLayout = findViewById(R.id.text_linear);

        textViewTitle.setText(context.getResources().getString(R.string.title_textedit));
    }

    public void setEvents() {
        imageViewBack.setOnClickListener(v -> {
            onBackPressed();
        });

        textViewDone.setOnClickListener(v -> {
            sendData();
        });
    }

    public void postWork() {
        createTextview(data);
    }

    public void createTextview(String data) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_text_editor, null, false);
        TextView textView = view.findViewById(R.id.text_name);
        textView.setText(data);
        textViews.add(textView);
        linearLayout.addView(view);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    public void sendData() {
        String text = "";

        for (int i = 0; i < textViews.size(); i++) {
            text = text + textViews.get(i).getText().toString();
        }

        Intent intent = new Intent();
        intent.putExtra("MESSAGE", "YES");
        intent.putExtra("final_text", text);

        setResult(Activity.RESULT_OK, intent);
        finish();

    }
}