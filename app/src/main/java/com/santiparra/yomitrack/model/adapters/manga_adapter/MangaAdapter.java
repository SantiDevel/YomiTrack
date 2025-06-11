package com.santiparra.yomitrack.model.adapters.manga_adapter;

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
import com.santiparra.yomitrack.db.entities.MangaEntity;

import java.util.List;

/**
 * Adaptador para mostrar una lista de mangas en un RecyclerView con distintos tipos de vista:
 * normal, compacta y grande. Soporta clics normales y prolongados.
 */
public class MangaAdapter extends RecyclerView.Adapter<MangaAdapter.MangaViewHolder> {

    /** Vista estándar. */
    public static final int VIEW_NORMAL = 0;

    /** Vista compacta. */
    public static final int VIEW_COMPACT = 1;

    /** Vista grande. */
    public static final int VIEW_LARGE = 2;

    /** Lista de mangas a mostrar. */
    private List<MangaEntity> mangaList;

    /** Tipo de vista actual. */
    private int viewType;

    /** Listener para clic corto (edición). */
    private final OnMangaClickListener onEditClick;

    /** Listener para clic prolongado (acciones extendidas). */
    private final OnMangaClickListener onLongClick;

    /**
     * Constructor del adaptador.
     *
     * @param mangaList    lista de mangas a mostrar.
     * @param viewType     tipo de vista deseado.
     * @param onEditClick  callback para clics normales.
     * @param onLongClick  callback para clics prolongados.
     */
    public MangaAdapter(List<MangaEntity> mangaList, int viewType,
                        OnMangaClickListener onEditClick,
                        OnMangaClickListener onLongClick) {
        this.mangaList = mangaList;
        this.viewType = viewType;
        this.onEditClick = onEditClick;
        this.onLongClick = onLongClick;
    }

    @NonNull
    @Override
    public MangaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = R.layout.item_manga;
        if (viewType == VIEW_COMPACT) layout = R.layout.item_manga_compact;
        else if (viewType == VIEW_LARGE) layout = R.layout.item_manga_large;

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new MangaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MangaViewHolder holder, int position) {
        MangaEntity manga = mangaList.get(position);

        String title = manga.getTitle() != null ? manga.getTitle() : "Sin título";
        String status = manga.getStatus() != null ? manga.getStatus() : "";
        String type = manga.getType() != null ? manga.getType() : "";
        String imageUrl = manga.getImageUrl();

        if (holder.textTitle != null) holder.textTitle.setText(title);

        if (holder.textStatus != null) {
            String statusText = status + (type.isEmpty() ? "" : " • " + type);
            holder.textStatus.setText(statusText);
        }

        if (holder.textProgress != null) {
            String progress = manga.getProgress() + " caps";
            holder.textProgress.setText(progress);
        }

        if (holder.textScore != null) {
            holder.textScore.setText("Score: " + manga.getScore());
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
            switch (manga.getStatus()) {
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
            if (onEditClick != null) onEditClick.onClick(manga);
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (onLongClick != null) {
                onLongClick.onClick(manga);
                return true;
            }
            return false;
        });
    }

    /**
     * Devuelve la cantidad de mangas en la lista.
     *
     * @return número total de ítems.
     */
    @Override
    public int getItemCount() {
        return mangaList != null ? mangaList.size() : 0;
    }

    /**
     * Devuelve el tipo de vista para el ítem en la posición dada.
     *
     * @param position posición del ítem.
     * @return tipo de vista.
     */
    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    /**
     * Reemplaza la lista actual por una nueva y actualiza el adaptador.
     *
     * @param newList nueva lista de mangas.
     */
    public void updateList(List<MangaEntity> newList) {
        mangaList.clear();
        mangaList.addAll(newList);
        notifyDataSetChanged();
    }

    /**
     * ViewHolder para representar un ítem de manga en el RecyclerView.
     */
    public static class MangaViewHolder extends RecyclerView.ViewHolder {
        ImageView imageCover;
        TextView textTitle, textStatus, textProgress, textScore, textType;
        View statusDot;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView vista inflada del ítem.
         */
        public MangaViewHolder(@NonNull View itemView) {
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
     * Interfaz para manejar clics sobre ítems de manga.
     */
    public interface OnMangaClickListener {
        /**
         * Se ejecuta cuando se hace clic en un manga.
         *
         * @param manga el ítem clicado.
         */
        void onClick(MangaEntity manga);
    }
}
