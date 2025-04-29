package com.santiparra.yomitrack.model.adapters.sectionadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santiparra.yomitrack.model.ItemModel;
import com.santiparra.yomitrack.ui.adapters.HomeAdapter;
import com.santiparra.yomitrack.R;

import java.util.List;
import java.util.Map;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {

    private final List<String> sectionTitles;
    private final Map<String, List<ItemModel>> sectionImages;

    public SectionAdapter(List<String> sectionTitles, Map<String, List<ItemModel>> sectionImages) {
        this.sectionTitles = sectionTitles;
        this.sectionImages = sectionImages;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        String title = sectionTitles.get(position);
        List<ItemModel> items = sectionImages.get(title);

        holder.sectionTitle.setText(title);

        HomeAdapter homeAdapter = new HomeAdapter(items, title);
        holder.sectionRecycler.setLayoutManager(
                new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false)
        );
        holder.sectionRecycler.setAdapter(homeAdapter);
    }

    @Override
    public int getItemCount() {
        return sectionTitles.size();
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTitle;
        RecyclerView sectionRecycler;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.sectionTitle);
            sectionRecycler = itemView.findViewById(R.id.sectionRecycler);
        }
    }
}
