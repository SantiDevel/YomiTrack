package com.santiparra.yomitrack.model.adapters.homeadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.ItemModel;

import java.util.List;

public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ItemModel item);
    }

    private final List<ItemModel> itemList;
    private final OnItemClickListener listener;

    public HomeCardAdapter(List<ItemModel> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemModel item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.progress.setText(item.getProgress());
        Glide.with(holder.itemView.getContext()).load(item.getImageUrl()).into(holder.cover);

        holder.card.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, progress;
        ImageView cover;
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            progress = itemView.findViewById(R.id.itemProgress);
            cover = itemView.findViewById(R.id.itemImage);
            card = itemView.findViewById(R.id.cardItem);
        }
    }
}
