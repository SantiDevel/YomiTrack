package com.santiparra.yomitrack.model.adapters.anime_adapter;

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

/**
 * Adaptador de RecyclerView para mostrar una lista de animes con soporte para múltiples tipos de vista:
 * normal, compacta y grande. Permite manejar clics normales y prolongados sobre los ítems.
 */
public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    /** Vista normal por defecto. */
    public static final int VIEW_NORMAL = 0;

    /** Vista compacta. */
    public static final int VIEW_COMPACT = 1;

    /** Vista ampliada. */
    public static final int VIEW_LARGE = 2;

    /** Lista de animes a mostrar. */
    private List<AnimeEntity> animeList;

    /** Tipo de vista actual. */
    private int viewType;

    /** Listener para clics normales (edición). */
    private final OnAnimeClickListener onEditClick;

    /** Listener para clics prolongados (acciones extendidas). */
    private final OnAnimeClickListener onLongClick;

    /**
     * Constructor del adaptador.
     *
     * @param animeList    lista de animes.
     * @param viewType     tipo de vista a usar (normal, compacta, grande).
     * @param onEditClick  callback para clics normales.
     * @param onLongClick  callback para clics prolongados.
     */
    public AnimeAdapter(List<AnimeEntity> animeList, int viewType,
                        OnAnimeClickListener onEditClick,
                        OnAnimeClickListener onLongClick) {
        this.animeList = animeList != null ? animeList : new ArrayList<>();
        this.viewType = viewType;
        this.onEditClick = onEditClick;
        this.onLongClick = onLongClick;
    }

    /**
     * Cambia el tipo de vista del adaptador.
     *
     * @param viewType nuevo tipo de vista.
     */
    public void setViewType(int viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    /**
     * Actualiza la lista de animes mostrada.
     *
     * @param newList nueva lista de animes.
     */
    public void updateList(List<AnimeEntity> newList) {
        this.animeList = new ArrayList<>(newList); // o .clear() + .addAll()
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

    /**
     * ViewHolder que representa un ítem individual del RecyclerView de animes.
     */
    public static class AnimeViewHolder extends RecyclerView.ViewHolder {

        /** Imagen de portada del anime. */
        ImageView imageCover;

        /** Título del anime. */
        TextView textTitle;

        /** Texto que muestra el estado y tipo del anime. */
        TextView textStatus;

        /** Texto que muestra el progreso (episodios vistos). */
        TextView textProgress;

        /** Texto que muestra la puntuación. */
        TextView textScore;

        /** Texto que muestra el tipo de anime. */
        TextView textType;

        /** Punto de color que indica el estado visualmente. */
        View statusDot;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView vista inflada del ítem.
         */
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

    /**
     * Interfaz para manejar clics sobre un anime.
     */
    public interface OnAnimeClickListener {
        /**
         * Método invocado al hacer clic en un ítem de anime.
         *
         * @param anime objeto de anime clicado.
         */
        void onClick(AnimeEntity anime);
    }
}
