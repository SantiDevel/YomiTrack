package com.santiparra.yomitrack.model.adapters.manga_adapter;

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

public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {

    private final List<AnimeItem> mangaList;
    private final Context context;
    private int viewMode = 0;

    public interface OnMangaRemoveListener {
        void onMangaRemoved(AnimeItem manga);
    }

    private OnMangaRemoveListener removeListener;

    public void setOnMangaRemoveListener(OnMangaRemoveListener listener) {
        this.removeListener = listener;
    }

    public void setViewMode(int mode) {
        this.viewMode = mode;
    }

    public MangaAdapter(Context context, List<AnimeItem> mangaList) {
        this.context = context;
        this.mangaList = mangaList;
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId;
        switch (viewMode) {
            case 1:
                layoutId = R.layout.item_anime_large;
                break;
            case 2:
                layoutId = R.layout.item_anime_compact;
                break;
            default:
                layoutId = R.layout.item_anime;
                break;
        }
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        AnimeItem manga = mangaList.get(position);

        if (holder.title != null)
            holder.title.setText(manga.getTitle());

        if (holder.progress != null)
            holder.progress.setText("Progress: " + manga.getWatchedEpisodes() + "/" + manga.getTotalEpisodes());

        if (holder.score != null)
            holder.score.setText(String.valueOf(manga.getScore()));

        if (holder.type != null)
            holder.type.setText(manga.getType());

        if (holder.cover != null) {
            Glide.with(context)
                    .load(manga.getImageUrl())
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
                        Toast.makeText(context, "Edit: " + manga.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (id == R.id.action_remove) {
                        int pos = holder.getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            AnimeItem removed = mangaList.remove(pos);
                            notifyItemRemoved(pos);
                            if (removeListener != null) {
                                removeListener.onMangaRemoved(removed);
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
        return mangaList.size();
    }

    static class MangaViewHolder extends RecyclerView.ViewHolder {
        TextView title, progress, score, type;
        ImageView cover, buttonOptions;

        MangaViewHolder(View itemView) {
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

