package com.santiparra.yomitrack.ui.fragments.anime_list;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.model.AnimeItem;
import com.santiparra.yomitrack.model.adapters.anime_adapter.AnimeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentAnime extends Fragment {
    private RecyclerView recyclerView;
    private EditText editTextFilter;
    private TextView textViewTitle;
    private AnimeAdapter adapter;

    private final List<AnimeItem> fullAnimeList = new ArrayList<>();
    private final List<AnimeItem> filteredAnimeList = new ArrayList<>();
    private String currentStatus = "Watching";
    private int currentViewMode = 0;

    private ImageButton buttonGrid, buttonLarge, buttonList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alist, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAnimeList);
        editTextFilter = view.findViewById(R.id.editTextFilter);
        textViewTitle = view.findViewById(R.id.textViewWatching);
        ImageView buttonFilterMenu = view.findViewById(R.id.buttonFilterMenu);
        buttonGrid = view.findViewById(R.id.buttonViewGrid);
        buttonLarge = view.findViewById(R.id.buttonViewLarge);
        buttonList = view.findViewById(R.id.buttonViewList);

        buttonGrid.setOnClickListener(v -> setLayoutMode(0));
        buttonLarge.setOnClickListener(v -> setLayoutMode(1));
        buttonList.setOnClickListener(v -> setLayoutMode(2));

        initSampleAnimeList();

        filteredAnimeList.addAll(fullAnimeList);
        adapter = new AnimeAdapter(getContext(), filteredAnimeList);
        recyclerView.setAdapter(adapter);
        adapter.setOnAnimeRemoveListener(anime -> fullAnimeList.remove(anime));

        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAnimeList(s.toString());
            }
        });

        Map<Integer, String> filterMap = new HashMap<>();
        filterMap.put(R.id.filter_all, "All");
        filterMap.put(R.id.filter_watching, "Watching");
        filterMap.put(R.id.filter_planning, "Planning");
        filterMap.put(R.id.filter_paused, "Paused");
        filterMap.put(R.id.filter_dropped, "Dropped");
        filterMap.put(R.id.filter_completed, "Completed");

        buttonFilterMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(requireContext(), buttonFilterMenu);
            popup.inflate(R.menu.filter_list_menu);
            popup.setOnMenuItemClickListener(item -> {
                String selectedStatus = filterMap.get(item.getItemId());
                if (selectedStatus != null) {
                    currentStatus = selectedStatus;
                    textViewTitle.setText(currentStatus);
                    filterAnimeList(editTextFilter.getText().toString());
                    return true;
                }
                return false;
            });
            popup.show();
        });

        setLayoutMode(currentViewMode);

        return view;
    }

    private void filterAnimeList(String query) {
        filteredAnimeList.clear();
        int count = 0;
        for (AnimeItem anime : fullAnimeList) {
            boolean matchesStatus = currentStatus.equals("All") || anime.getStatus().equalsIgnoreCase(currentStatus);
            boolean matchesQuery = anime.getTitle().toLowerCase().contains(query.toLowerCase());
            if (matchesStatus && matchesQuery) {
                filteredAnimeList.add(anime);
                count++;
                if (count >= 10) {
                    Toast.makeText(requireContext(), "Mostrando los primeros 10 resultados", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void setLayoutMode(int mode) {
        currentViewMode = mode;

        if (mode == 2) { // Compacta: solo texto
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else if (mode == 1) { // Imagen grande + info
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        } else if (mode == 0) { // Lista tipo AniList
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        adapter = new AnimeAdapter(getContext(), filteredAnimeList);
        adapter.setViewMode(mode);
        adapter.setOnAnimeRemoveListener(anime -> fullAnimeList.remove(anime));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        int defaultTint = ContextCompat.getColor(requireContext(), R.color.textPrimary);
        int activeTint = ContextCompat.getColor(requireContext(), R.color.activeTint);

        buttonGrid.setColorFilter(mode == 0 ? activeTint : defaultTint);
        buttonLarge.setColorFilter(mode == 1 ? activeTint : defaultTint);
        buttonList.setColorFilter(mode == 2 ? activeTint : defaultTint);
    }

    private void initSampleAnimeList() {
        fullAnimeList.clear();
        fullAnimeList.add(new AnimeItem("Aharen-san wa Hakarenai Season 2", "https://cdn.example.com/img1.jpg", 5, 12, 8.5, "TV", "Watching"));
        fullAnimeList.add(new AnimeItem("Anne Shirley", "https://cdn.example.com/img2.jpg", 5, 24, 7.9, "OVA", "Planning"));
        fullAnimeList.add(new AnimeItem("Ballpark de Tsukamaete!", "https://cdn.example.com/img3.jpg", 5, 12, 9.2, "Movie", "Paused"));
        fullAnimeList.add(new AnimeItem("Bleach: TYBW", "https://cdn.example.com/img4.jpg", 8, 13, 9.0, "TV", "Completed"));
        fullAnimeList.add(new AnimeItem("Chainsaw Man", "https://cdn.example.com/img5.jpg", 1, 12, 8.8, "TV", "Dropped"));
        fullAnimeList.add(new AnimeItem("Code Geass", "https://cdn.example.com/img6.jpg", 25, 25, 9.5, "TV", "Completed"));
        fullAnimeList.add(new AnimeItem("Death Note", "https://cdn.example.com/img7.jpg", 37, 37, 9.6, "TV", "Completed"));
        fullAnimeList.add(new AnimeItem("Erased", "https://cdn.example.com/img8.jpg", 12, 12, 9.0, "TV", "Watching"));
        fullAnimeList.add(new AnimeItem("Fate/Zero", "https://cdn.example.com/img9.jpg", 13, 13, 9.1, "TV", "Planning"));
        fullAnimeList.add(new AnimeItem("Gintama", "https://cdn.example.com/img10.jpg", 201, 201, 9.7, "TV", "Paused"));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentStatus", currentStatus);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            currentStatus = savedInstanceState.getString("currentStatus", "Watching");
            textViewTitle.setText(currentStatus);
            filterAnimeList(editTextFilter.getText().toString());
        }
    }
}
