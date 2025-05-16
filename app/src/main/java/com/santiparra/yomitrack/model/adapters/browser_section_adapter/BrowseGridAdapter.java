package com.santiparra.yomitrack.model.adapters.browser_section_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.ItemModel;

import java.util.List;

public class BrowseGridAdapter extends RecyclerView.Adapter<BrowseGridAdapter.ViewHolder> {

    private final List<ItemModel> items;

    public BrowseGridAdapter(List<ItemModel> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_browse_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemModel item = items.get(position);
        holder.title.setText(item.getTitle());
        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.placeholder_image)
                .into(holder.cover);

        // 👉 Animación
        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.item_animation_fade_scale);
        holder.itemView.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.imageViewCover);
            title = itemView.findViewById(R.id.textViewTitle);
        }
    }
}

