package com.santiparra.yomitrack.model.adapters.sectionadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.ItemModel;
import com.santiparra.yomitrack.model.adapters.homeadapter.HomeAdapter;

import java.util.List;
import java.util.Map;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {

    private final List<String> sectionTitles;
    private final Map<String, List<ItemModel>> sectionItems;

    public SectionAdapter(List<String> sectionTitles, Map<String, List<ItemModel>> sectionItems) {
        this.sectionTitles = sectionTitles;
        this.sectionItems = sectionItems;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        String sectionTitle = sectionTitles.get(position);
        holder.title.setText(sectionTitle);

        List<ItemModel> fullList = sectionItems.get(sectionTitle);

        HomeAdapter adapter = new HomeAdapter(fullList, sectionTitle);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return sectionTitles.size();
    }

    static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        RecyclerView recyclerView;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.sectionTitle);
            recyclerView = itemView.findViewById(R.id.sectionRecyclerView);
        }
    }
}
