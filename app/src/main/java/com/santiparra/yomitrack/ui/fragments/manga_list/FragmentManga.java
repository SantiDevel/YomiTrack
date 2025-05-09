package com.santiparra.yomitrack.ui.fragments.manga_list;

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
import com.santiparra.yomitrack.model.adapters.manga_adapter.MangaAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentManga extends Fragment {
    private RecyclerView recyclerView;
    private EditText editTextFilter;
    private TextView textViewTitle;
    private MangaAdapter adapter;

    private final List<AnimeItem> fullMangaList = new ArrayList<>();
    private final List<AnimeItem> filteredMangaList = new ArrayList<>();
    private String currentStatus = "Reading";
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

        initSampleMangaList();

        filteredMangaList.addAll(fullMangaList);
        adapter = new MangaAdapter(getContext(), filteredMangaList);
        recyclerView.setAdapter(adapter);
        adapter.setOnMangaRemoveListener(manga -> fullMangaList.remove(manga));

        editTextFilter.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMangaList(s.toString());
            }
        });

        Map<Integer, String> filterMap = new HashMap<>();
        filterMap.put(R.id.filter_all, "All");
        filterMap.put(R.id.filter_watching, "Reading");
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
                    filterMangaList(editTextFilter.getText().toString());
                    return true;
                }
                return false;
            });
            popup.show();
        });

        setLayoutMode(currentViewMode);

        return view;
    }

    private void filterMangaList(String query) {
        filteredMangaList.clear();
        int count = 0;
        for (AnimeItem manga : fullMangaList) {
            boolean matchesStatus = currentStatus.equals("All") || manga.getStatus().equalsIgnoreCase(currentStatus);
            boolean matchesQuery = manga.getTitle().toLowerCase().contains(query.toLowerCase());
            if (matchesStatus && matchesQuery) {
                filteredMangaList.add(manga);
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

        if (mode == 2) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else if (mode == 1) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        } else if (mode == 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        adapter = new MangaAdapter(getContext(), filteredMangaList);
        adapter.setViewMode(mode);
        adapter.setOnMangaRemoveListener(manga -> fullMangaList.remove(manga));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        int defaultTint = ContextCompat.getColor(requireContext(), R.color.textPrimary);
        int activeTint = ContextCompat.getColor(requireContext(), R.color.activeTint);

        buttonGrid.setColorFilter(mode == 0 ? activeTint : defaultTint);
        buttonLarge.setColorFilter(mode == 1 ? activeTint : defaultTint);
        buttonList.setColorFilter(mode == 2 ? activeTint : defaultTint);
    }

    private void initSampleMangaList() {
        fullMangaList.clear();
        fullMangaList.add(new AnimeItem("Chainsaw Man", "https://cdn.example.com/img1.jpg", 45, 100, 8.5, "Manga", "Reading"));
        fullMangaList.add(new AnimeItem("Berserk", "https://cdn.example.com/img2.jpg", 370, 380, 9.4, "Manga", "Paused"));
        fullMangaList.add(new AnimeItem("One Piece", "https://cdn.example.com/img3.jpg", 1090, 1200, 9.8, "Manga", "Reading"));
        fullMangaList.add(new AnimeItem("Attack on Titan", "https://cdn.example.com/img4.jpg", 139, 139, 9.5, "Manga", "Completed"));
        fullMangaList.add(new AnimeItem("Solo Leveling", "https://cdn.example.com/img5.jpg", 179, 179, 8.9, "Manhwa", "Completed"));
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
            currentStatus = savedInstanceState.getString("currentStatus", "Reading");
            textViewTitle.setText(currentStatus);
            filterMangaList(editTextFilter.getText().toString());
        }
    }
}
