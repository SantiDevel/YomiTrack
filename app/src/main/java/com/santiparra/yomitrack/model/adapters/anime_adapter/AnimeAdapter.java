package com.santiparra.yomitrack.model.adapters.anime_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.AnimeItem;

import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    private final List<AnimeItem> animeList;
    private final Context context;
    private int viewMode = 0;

    public interface OnAnimeRemoveListener {
        void onAnimeRemoved(AnimeItem anime);
    }

    private OnAnimeRemoveListener removeListener;

    public void setOnAnimeRemoveListener(OnAnimeRemoveListener listener) {
        this.removeListener = listener;
    }

    public void setViewMode(int mode) {
        this.viewMode = mode;
    }

    public AnimeAdapter(Context context, List<AnimeItem> animeList) {
        this.context = context;
        this.animeList = animeList;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewMode) {
            case 1:
                layoutId = R.layout.item_anime_large; // solo imagen con score y episodios
                break;
            case 2:
                layoutId = R.layout.item_anime_compact; // solo texto
                break;
            default:
                layoutId = R.layout.item_anime; // imagen + texto
                break;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        AnimeItem anime = animeList.get(position);

        if (holder.title != null)
            holder.title.setText(anime.getTitle());

        if (holder.progress != null)
            holder.progress.setText("Progress: " + anime.getWatchedEpisodes() + "/" + anime.getTotalEpisodes());

        if (holder.score != null)
            holder.score.setText(String.valueOf(anime.getScore()));

        if (holder.type != null)
            holder.type.setText(anime.getType());

        if (holder.cover != null) {
            Glide.with(context)
                    .load(anime.getImageUrl())
                    .placeholder(R.drawable.sample_anime_cover)
                    .into(holder.cover);
        }

        if (holder.buttonOptions != null) {
            holder.buttonOptions.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(context, holder.buttonOptions);
                popup.inflate(R.menu.anime_item_menu);
                popup.setOnMenuItemClickListener(item -> {
                    int id = item.getItemId();
                    if (id == R.id.action_edit) {
                        Toast.makeText(context, "Edit: " + anime.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (id == R.id.action_remove) {
                        int pos = holder.getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            AnimeItem removed = animeList.remove(pos);
                            notifyItemRemoved(pos);
                            if (removeListener != null) {
                                removeListener.onAnimeRemoved(removed);
                            }
                            Toast.makeText(context, "Removed: " + removed.getTitle(), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                    return false;
                });
                popup.show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    static class AnimeViewHolder extends RecyclerView.ViewHolder {
        TextView title, progress, score, type;
        ImageView cover, buttonOptions;

        AnimeViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            progress = itemView.findViewById(R.id.textViewProgress);
            score = itemView.findViewById(R.id.textViewScore);
            type = itemView.findViewById(R.id.textViewType);
            cover = itemView.findViewById(R.id.imageViewCover);
            buttonOptions = itemView.findViewById(R.id.buttonOptions);
        }
    }
}
