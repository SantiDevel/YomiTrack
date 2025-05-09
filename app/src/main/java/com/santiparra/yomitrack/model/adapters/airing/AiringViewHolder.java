package com.santiparra.yomitrack.model.adapters.airing;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.ItemModel;

public class AiringViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView titleTextView;
    public TextView progressTextView;

    public AiringViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.mediaImage);
        titleTextView = itemView.findViewById(R.id.titleTextView);
        progressTextView = itemView.findViewById(R.id.progressTextView);
    }

    public void bind(ItemModel item) {
        titleTextView.setText(item.getTitle());
        progressTextView.setText("Progress: " + item.getProgress());

        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(itemView.getContext())
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.error_image)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.placeholder_image);
        }
    }
}
