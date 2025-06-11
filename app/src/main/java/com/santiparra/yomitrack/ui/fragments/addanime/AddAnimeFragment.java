package com.santiparra.yomitrack.ui.fragments.addanime;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

import com.google.gson.JsonObject;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.AnimeEntity;
import com.santiparra.yomitrack.model.AniListMedia;
import com.santiparra.yomitrack.model.ApiResponse;
import com.santiparra.yomitrack.model.adapters.anime_adapter.AnimeSearchAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAnimeFragment extends Fragment {

    private EditText searchEditText, scoreEditText, progressEditText;
    private Spinner statusSpinner, typeSpinner;
    private RecyclerView searchResults;
    private AnimeSearchAdapter searchAdapter;
    private ApiService api;
    private int userId;
    private String selectedImageUrl = "";

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
        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        setupSpinners();
        setupRecycler();
        setupSearch();

        return view;
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.anime_status_array, R.layout.item_spinner);
        statusAdapter.setDropDownViewResource(R.layout.item_spinner);
        statusSpinner.setAdapter(statusAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.anime_type_array, R.layout.item_spinner);
        typeAdapter.setDropDownViewResource(R.layout.item_spinner);
        typeSpinner.setAdapter(typeAdapter);
    }

    private void setupRecycler() {
        searchAdapter = new AnimeSearchAdapter(new ArrayList<>(), this::onAnimeSelected);
        searchResults.setAdapter(searchAdapter);
    }

    private void setupSearch() {
        searchEditText.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String query = searchEditText.getText().toString().trim();
                if (!query.isEmpty()) {
                    api.searchAniList(query, "ANIME").enqueue(new Callback<List<AniListMedia>>() {
                        @Override
                        public void onResponse(Call<List<AniListMedia>> call, Response<List<AniListMedia>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                searchAdapter.setAnimeList(response.body());
                            } else {
                                Toast.makeText(getContext(), "Sin resultados", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<List<AniListMedia>> call, Throwable t) {
                            Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return true;
            }
            return false;
        });
    }

    private void onAnimeSelected(AniListMedia selected) {
        String status = statusSpinner.getSelectedItem().toString();
        String type = typeSpinner.getSelectedItem().toString();

        int score = parseIntOrZero(scoreEditText.getText().toString());
        int progress = parseIntOrZero(progressEditText.getText().toString());

        AnimeEntity anime = new AnimeEntity();
        anime.setUserId(userId);
        anime.setTitle(selected.getTitle());

        selectedImageUrl = (selected.getImageUrl() == null || selected.getImageUrl().isEmpty())
                ? "android.resource://" + requireContext().getPackageName() + "/" + R.drawable.sample_cover
                : selected.getImageUrl();

        anime.setImageUrl(selectedImageUrl);
        anime.setStatus(status);
        anime.setType(type);
        anime.setScore(score);
        anime.setProgress(progress);

        api.insertAnime(anime).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    registrarActividad(anime.getTitle());
                    notificarAñadido();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Error al guardar anime", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registrarActividad(String titulo) {
        Map<String, Object> actividad = new HashMap<>();
        actividad.put("userId", userId);
        actividad.put("action", "añadió un anime");
        actividad.put("mediaTitle", titulo);
        actividad.put("imageUrl", selectedImageUrl);

        api.postActivity(actividad).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("ACTIVITY_POST", "Código de respuesta: " + response.code());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ACTIVITY_POST", "Error al registrar actividad: " + t.getMessage(), t);
                if (isAdded()) {
                    Toast.makeText(getContext(), "Error al registrar actividad", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void notificarAñadido() {
        Bundle result = new Bundle();
        result.putBoolean("anime_added", true);
        getParentFragmentManager().setFragmentResult("anime_add_request", result);
    }

    private int parseIntOrZero(String value) {
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
