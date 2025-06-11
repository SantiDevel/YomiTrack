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

/**
 * Adaptador para mostrar resultados de búsqueda de mangas desde AniList.
 * Utilizado en el fragmento de búsqueda para permitir seleccionar un manga.
 */
public class MangaSearchAdapter extends RecyclerView.Adapter<MangaSearchAdapter.SearchViewHolder> {

    /** Lista de mangas obtenidos desde la API de AniList. */
    private List<AniListMedia> mangaList;

    /** Listener para manejar clics en los ítems del RecyclerView. */
    private final OnMangaClickListener clickListener;

    /**
     * Interfaz que define el callback cuando se hace clic en un manga.
     */
    public interface OnMangaClickListener {
        /**
         * Método invocado al hacer clic sobre un manga.
         *
         * @param manga objeto de AniList clicado.
         */
        void onClick(AniListMedia manga);
    }

    /**
     * Constructor del adaptador.
     *
     * @param mangaList     lista de resultados de búsqueda.
     * @param clickListener listener para manejar el clic en cada ítem.
     */
    public MangaSearchAdapter(List<AniListMedia> mangaList, OnMangaClickListener clickListener) {
        this.mangaList = mangaList;
        this.clickListener = clickListener;
    }

    /**
     * Reemplaza la lista de mangas actual por una nueva y actualiza el RecyclerView.
     *
     * @param mangaList nueva lista de mangas.
     */
    public void setMangaList(List<AniListMedia> mangaList) {
        this.mangaList = mangaList;
        notifyDataSetChanged();
    }

    /**
     * Infla el layout para un ítem individual del RecyclerView.
     *
     * @param parent    el ViewGroup padre.
     * @param viewType  tipo de vista (no utilizado aquí).
     * @return instancia del ViewHolder.
     */
    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_manga, parent, false);
        return new SearchViewHolder(view);
    }

    /**
     * Asocia los datos del manga con la vista.
     *
     * @param holder   ViewHolder que contiene la vista.
     * @param position posición del ítem en la lista.
     */
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

    /**
     * Devuelve la cantidad total de mangas en la lista.
     *
     * @return tamaño de la lista.
     */
    @Override
    public int getItemCount() {
        return mangaList != null ? mangaList.size() : 0;
    }

    /**
     * ViewHolder que representa cada ítem del RecyclerView.
     */
    static class SearchViewHolder extends RecyclerView.ViewHolder {
        /** Imagen de portada del manga. */
        ImageView imageCover;

        /** Título del manga. */
        TextView title;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView vista inflada del ítem.
         */
        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCover = itemView.findViewById(R.id.imageCover);
            title = itemView.findViewById(R.id.textTitle);
        }
    }
}
