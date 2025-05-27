package com.santiparra.yomitrack.ui.fragments.browse;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.model.AniListMedia;
import com.santiparra.yomitrack.model.ItemModel;
import com.santiparra.yomitrack.model.adapters.anilist_adapter.AniListSearchAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FragmentBrowse extends Fragment {

    private Spinner spinnerType;
    private EditText editTextSearch;
    private LinearLayout sectionContainer;
    private RecyclerView recyclerViewResults;
    private AniListSearchAdapter searchAdapter;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    private final List<ItemModel> animeList = new ArrayList<>();
    private final List<ItemModel> mangaList = new ArrayList<>();

    public FragmentBrowse() {
        super(R.layout.fragment_browse);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        spinnerType = view.findViewById(R.id.spinnerType);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        sectionContainer = view.findViewById(R.id.sectionContainer);
        recyclerViewResults = view.findViewById(R.id.recyclerViewResults);
        recyclerViewResults.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.item_spinner_large,
                new String[]{"Anime", "Manga"}
        );
        spinnerAdapter.setDropDownViewResource(R.layout.item_spinner_large);
        spinnerType.setAdapter(spinnerAdapter);
        
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(searchRunnable);
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchRunnable = () -> performSearch(s.toString());
                handler.postDelayed(searchRunnable, 500);
            }
        });

        // Aquí continúa tu lógica previa, sin alterarse.
    }

    private int getStatusBarHeight() {
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        return resourceId > 0 ? getResources().getDimensionPixelSize(resourceId) : 0;
    }

    private void performSearch(String query) {
        if (query.trim().isEmpty()) return;

        Object selected = spinnerType.getSelectedItem();
        if (selected == null) return;

        String selectedType = selected.toString();
        String type = selectedType.equalsIgnoreCase("Manga") ? "MANGA" : "ANIME";

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.searchAniList(query, type).enqueue(new retrofit2.Callback<List<AniListMedia>>() {
            @Override
            public void onResponse(Call<List<AniListMedia>> call, Response<List<AniListMedia>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchAdapter = new AniListSearchAdapter(getContext(), response.body(), type);
                    recyclerViewResults.setAdapter(searchAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<AniListMedia>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
