package com.santiparra.yomitrack.model.adapters.manga_adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.AniListMedia;

import java.util.List;

public class MangaSearchAdapter extends RecyclerView.Adapter<MangaSearchAdapter.SearchViewHolder> {

    private List<AniListMedia> mangaList;
    private final OnMangaClickListener clickListener;

    public interface OnMangaClickListener {
        void onClick(AniListMedia manga);
    }

    public MangaSearchAdapter(List<AniListMedia> mangaList, OnMangaClickListener clickListener) {
        this.mangaList = mangaList;
        this.clickListener = clickListener;
    }

    public void setMangaList(List<AniListMedia> mangaList) {
        this.mangaList = mangaList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manga, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        AniListMedia manga = mangaList.get(position);
        holder.title.setText(manga.getTitle());

        Glide.with(holder.itemView.getContext())
                .load(manga.getImageUrl())
                .placeholder(R.drawable.rectangle_placeholder)
                .into(holder.imageCover);

        holder.itemView.setOnClickListener(v -> clickListener.onClick(manga));
    }

    @Override
    public int getItemCount() {
        return mangaList != null ? mangaList.size() : 0;
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
