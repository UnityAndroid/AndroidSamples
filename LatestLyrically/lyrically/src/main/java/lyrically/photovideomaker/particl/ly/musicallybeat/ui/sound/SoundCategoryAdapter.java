package lyrically.photovideomaker.particl.ly.musicallybeat.ui.sound;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import lyrically.photovideomaker.particl.ly.musicallybeat.R;
import lyrically.photovideomaker.particl.ly.musicallybeat.data.network.response.SoundResponse;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SoundCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SoundResponse.SoundCategory> soundCategoryList = new ArrayList<>();
    Context context;

    private int selectedIndex;
    private callback listener;

    public SoundCategoryAdapter(Context context, List<SoundResponse.SoundCategory> folders, callback listener) {
        this.soundCategoryList = folders;
        this.context = context;
        this.listener = listener;
        selectedIndex = 0;
    }

    public static class ViewHolder1 extends RecyclerView.ViewHolder {

        private TextView textViewCatName;
        RelativeLayout line;

        public ViewHolder1(@NonNull View itemView) {
            super(itemView);
            textViewCatName = itemView.findViewById(R.id.catsound_text_name);
            line = itemView.findViewById(R.id.linerl);
        }
    }

    public void setSelectedIndex(int position) {
        selectedIndex = position;
    }

    public interface callback {
        void onTitleClicked(int position);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sound_category, parent, false);
        return new ViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        ViewHolder1 viewHolder1 = (ViewHolder1) holder;

        SoundResponse.SoundCategory folder = soundCategoryList.get(position);
        viewHolder1.textViewCatName.setText(folder.category_name);

        if (selectedIndex == position) {
            viewHolder1.textViewCatName.setTextColor(Color.parseColor("#FF742B"));
            viewHolder1.line.setVisibility(View.VISIBLE);
        } else {
            viewHolder1.textViewCatName.setTextColor(Color.parseColor("#FFFFFF"));
            viewHolder1.line.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedIndex = position;
                listener.onTitleClicked(position);
                //  ((SoundActivity) context).refreshData(holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return soundCategoryList.size();
    }
}

