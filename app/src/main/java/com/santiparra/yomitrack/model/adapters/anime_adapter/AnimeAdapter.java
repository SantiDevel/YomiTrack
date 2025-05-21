package com.santiparra.yomitrack.model.adapters.anime_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.db.entities.AnimeEntity;

import java.util.ArrayList;
import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    public static final int VIEW_NORMAL = 0;
    public static final int VIEW_COMPACT = 1;
    public static final int VIEW_LARGE = 2;

    private List<AnimeEntity> animeList;
    private int viewType;
    private final OnAnimeClickListener onEditClick;
    private final OnAnimeClickListener onLongClick;

    public AnimeAdapter(List<AnimeEntity> animeList, int viewType,
                        OnAnimeClickListener onEditClick,
                        OnAnimeClickListener onLongClick) {
        this.animeList = animeList != null ? animeList : new ArrayList<>();
        this.viewType = viewType;
        this.onEditClick = onEditClick;
        this.onLongClick = onLongClick;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    public void updateList(List<AnimeEntity> newList) {
        this.animeList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.item_anime;
        if (viewType == VIEW_COMPACT) layout = R.layout.item_anime_compact;
        else if (viewType == VIEW_LARGE) layout = R.layout.item_anime_large;

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        AnimeEntity anime = animeList.get(position);

        String title = anime.getTitle() != null ? anime.getTitle() : "Sin título";
        String status = anime.getStatus() != null ? anime.getStatus() : "";
        String type = anime.getType() != null ? anime.getType() : "";
        String imageUrl = anime.getImageUrl();

        if (holder.textTitle != null) holder.textTitle.setText(title);

        if (holder.textStatus != null) {
            String statusText = status + (type.isEmpty() ? "" : " • " + type);
            holder.textStatus.setText(statusText);
        }

        if (holder.textProgress != null) {
            String progress = anime.getProgress() + " eps";
            holder.textProgress.setText(progress);
        }

        if (holder.textScore != null) {
            holder.textScore.setText("Score: " + anime.getScore());
        }

        if (holder.textType != null) {
            holder.textType.setText("Tipo: " + type);
        }

        if (holder.imageCover != null && imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.rectangle_placeholder)
                    .into(holder.imageCover);
        } else if (holder.imageCover != null) {
            holder.imageCover.setImageResource(R.drawable.rectangle_placeholder);
        }

        if (holder.statusDot != null) {
            int colorResId;
            switch (anime.getStatus()) {
                case "Completed":
                    colorResId = R.color.status_completed;
                    break;
                case "Watching":
                    colorResId = R.color.status_watching;
                    break;
                case "Paused":
                    colorResId = R.color.status_paused;
                    break;
                case "Dropped":
                    colorResId = R.color.status_dropped;
                    break;
                case "Planning":
                default:
                    colorResId = R.color.status_planning;
                    break;
            }
            holder.statusDot.setBackgroundTintList(
                    ContextCompat.getColorStateList(holder.itemView.getContext(), colorResId)
            );
        }

        holder.itemView.setOnClickListener(v -> {
            if (onEditClick != null) onEditClick.onClick(anime);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onLongClick != null) {
                onLongClick.onClick(anime);
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return animeList != null ? animeList.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCover;
        TextView textTitle, textStatus, textProgress, textScore, textType;
        View statusDot;

        public AnimeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCover = itemView.findViewById(R.id.imageCover);
            textTitle = itemView.findViewById(R.id.textTitle);
            textStatus = itemView.findViewById(R.id.textStatus);
            textProgress = itemView.findViewById(R.id.textProgress);
            textScore = itemView.findViewById(R.id.textScore);
            textType = itemView.findViewById(R.id.textType);
            statusDot = itemView.findViewById(R.id.statusDot);
        }
    }

    public interface OnAnimeClickListener {
        void onClick(AnimeEntity anime);
    }
}