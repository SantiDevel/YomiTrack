package com.santiparra.yomitrack.ui.fragments.addmanga;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.MangaEntity;
import com.santiparra.yomitrack.model.AniListAnime;
import com.santiparra.yomitrack.model.adapters.manga_adapter.MangaSearchAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMangaFragment extends Fragment {

    private EditText searchEditText, scoreEditText, progressEditText;
    private Spinner statusSpinner, typeSpinner;
    private RecyclerView searchResults;
    private MangaSearchAdapter searchAdapter;
    private ApiService api;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_anime, container, false);

        searchEditText = view.findViewById(R.id.editTextSearch);
        scoreEditText = view.findViewById(R.id.editTextScore);
        progressEditText = view.findViewById(R.id.editTextProgress);
        statusSpinner = view.findViewById(R.id.spinnerStatus);
        typeSpinner = view.findViewById(R.id.spinnerType);
        searchResults = view.findViewById(R.id.recyclerSearchResults);
        searchResults.setLayoutManager(new LinearLayoutManager(getContext()));

        api = ApiClient.getClient().create(ApiService.class);
        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("current_user_id", -1);

        setupSpinners();
        setupRecycler();
        setupSearch();

        return view;
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.anime_status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.anime_type_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(typeAdapter);
    }

    private void setupRecycler() {
        searchAdapter = new MangaSearchAdapter(new ArrayList<>(), this::onMangaSelected);
        searchResults.setAdapter(searchAdapter);
    }

    private void setupSearch() {
        searchEditText.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    api.searchMangaAniList(query).enqueue(new Callback<List<AniListAnime>>() {
                        @Override
                        public void onResponse(Call<List<AniListAnime>> call, Response<List<AniListAnime>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                searchAdapter.setMangaList(response.body());
                            } else {
                                Toast.makeText(getContext(), "Sin resultados", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<AniListAnime>> call, Throwable t) {
                            Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return true;
            }
            return false;
        });
    }

    private void onMangaSelected(AniListAnime selected) {
        String status = statusSpinner.getSelectedItem().toString();
        String type = typeSpinner.getSelectedItem().toString();

        int score = 0;
        int progress = 0;
        try {
            score = Integer.parseInt(scoreEditText.getText().toString());
            progress = Integer.parseInt(progressEditText.getText().toString());
        } catch (NumberFormatException ignored) {}

        MangaEntity manga = new MangaEntity();
        manga.setUserId(userId);
        manga.setTitle(selected.getTitle());
        manga.setImageUrl(selected.getImageUrl());
        manga.setStatus(status);
        manga.setType(type);
        manga.setScore(score);
        manga.setProgress(progress);

        api.insertManga(manga).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Manga añadido", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Error al guardar manga", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
