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

/**
 * Adaptador para el RecyclerView del fragmento de inicio (Home).
 * Muestra tarjetas con imagen, título y progreso de animes o mangas recientes.
 */
public class HomeCardAdapter extends RecyclerView.Adapter<HomeCardAdapter.ViewHolder> {

    /**
     * Interfaz para manejar clics en los ítems del RecyclerView.
     */
    public interface OnItemClickListener {
        /**
         * Se llama cuando el usuario hace clic sobre un ítem.
         *
         * @param item el ítem seleccionado.
         */
        void onItemClick(ItemModel item);
    }

    /** Lista de ítems a mostrar (animes o mangas). */
    private final List<ItemModel> itemList;

    /** Listener que maneja clics en los ítems. */
    private final OnItemClickListener listener;

    /**
     * Constructor del adaptador.
     *
     * @param itemList lista de ítems a mostrar.
     * @param listener manejador de eventos de clic.
     */
    public HomeCardAdapter(List<ItemModel> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    /**
     * Infla la vista para un ítem del RecyclerView.
     *
     * @param parent   vista contenedora.
     * @param viewType tipo de vista (no usado aquí).
     * @return instancia de ViewHolder.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_card, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Asocia los datos del ítem con su vista.
     *
     * @param holder   ViewHolder con las vistas del ítem.
     * @param position posición del ítem en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemModel item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.progress.setText(item.getProgress());

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(R.drawable.rectangle_placeholder)
                .into(holder.cover);

        holder.card.setOnClickListener(v -> listener.onItemClick(item));
    }

    /**
     * Devuelve la cantidad de ítems en la lista.
     *
     * @return número total de ítems.
     */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /**
     * ViewHolder que representa cada tarjeta (ítem) en el RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /** Texto que muestra el título del ítem. */
        TextView title;

        /** Texto que muestra el progreso del ítem. */
        TextView progress;

        /** Imagen de portada del ítem. */
        ImageView cover;

        /** Tarjeta contenedora del ítem. */
        CardView card;

        /**
         * Constructor del ViewHolder.
         *
         * @param itemView vista del ítem inflada desde el layout.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.itemTitle);
            progress = itemView.findViewById(R.id.itemProgress);
            cover = itemView.findViewById(R.id.itemImage);
            card = itemView.findViewById(R.id.cardItem);
        }
    }
}
