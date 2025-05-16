package com.santiparra.yomitrack.model.adapters.anime_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.AniListAnime;

import java.util.List;

public class AnimeSearchAdapter extends RecyclerView.Adapter<AnimeSearchAdapter.SearchViewHolder> {

    private List<AniListAnime> animeList;
    private final OnAnimeClickListener clickListener;

    public interface OnAnimeClickListener {
        void onClick(AniListAnime anime);
    }

    public AnimeSearchAdapter(List<AniListAnime> animeList, OnAnimeClickListener clickListener) {
        this.animeList = animeList;
        this.clickListener = clickListener;
    }

    public void setAnimeList(List<AniListAnime> animeList) {
        this.animeList = animeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anime, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        AniListAnime anime = animeList.get(position);
        holder.title.setText(anime.getTitle());

        Glide.with(holder.itemView.getContext())
                .load(anime.getImageUrl())
                .placeholder(R.drawable.rectangle_placeholder)
                .into(holder.imageCover);

        holder.itemView.setOnClickListener(v -> clickListener.onClick(anime));
    }

    @Override
    public int getItemCount() {
        return animeList != null ? animeList.size() : 0;
    }

    static class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCover;
        TextView title;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCover = itemView.findViewById(R.id.imageCover);
            title = itemView.findViewById(R.id.textTitle);
        }
    }
}
