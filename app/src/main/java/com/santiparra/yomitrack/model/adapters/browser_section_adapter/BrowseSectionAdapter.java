package com.santiparra.yomitrack.model.adapters.browser_section_adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.BrowseSection;
import com.santiparra.yomitrack.model.adapters.homeadapter.HomeAdapter;

import java.util.List;

public class BrowseSectionAdapter extends RecyclerView.Adapter<BrowseSectionAdapter.BrowseViewHolder> {

    private final List<BrowseSection> sectionList;

    public BrowseSectionAdapter(List<BrowseSection> sectionList) {
        this.sectionList = sectionList;
    }

    @NonNull
    @Override
    public BrowseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new BrowseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrowseViewHolder holder, int position) {
        BrowseSection section = sectionList.get(position);
        holder.sectionTitle.setText(section.getTitle());

        HomeAdapter adapter = new HomeAdapter(section.getItems(), section.getTitle());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
        holder.recyclerView.setAdapter(adapter);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    static class BrowseViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTitle;
        RecyclerView recyclerView;

        public BrowseViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitle = itemView.findViewById(R.id.sectionTitle);
            recyclerView = itemView.findViewById(R.id.sectionRecyclerView);
        }
    }
}
