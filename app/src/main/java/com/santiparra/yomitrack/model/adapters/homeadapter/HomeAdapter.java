package com.santiparra.yomitrack.ui.adapters; // Ajusta tu paquete si es necesario

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.ItemModel;
import com.santiparra.yomitrack.model.adapters.airing.AiringViewHolder;
import com.santiparra.yomitrack.model.adapters.airing.AnimeViewHolder;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ItemModel> itemList;
    private String sectionTitle; // Para saber si es Airing o no

    private static final int TYPE_AIRING = 0;
    private static final int TYPE_ANIME_MANGA = 1;

    public HomeAdapter(List<ItemModel> itemList, String sectionTitle) {
        this.itemList = itemList;
        this.sectionTitle = sectionTitle;
    }

    @Override
    public int getItemViewType(int position) {
        if (sectionTitle.equalsIgnoreCase("Airing")) {
            return TYPE_AIRING;
        } else {
            return TYPE_ANIME_MANGA;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_AIRING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_anime_horizontal_card, parent, false);
            return new AiringViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_anime_card, parent, false);
            return new AnimeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemModel item = itemList.get(position);

        if (holder instanceof AiringViewHolder) {
            ((AiringViewHolder) holder).bind(item);
        } else if (holder instanceof AnimeViewHolder) {
            ((AnimeViewHolder) holder).bind(item);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
