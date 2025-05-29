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

import com.google.gson.JsonObject;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.MangaEntity;
import com.santiparra.yomitrack.model.AniListMedia;
import com.santiparra.yomitrack.model.ApiResponse;
import com.santiparra.yomitrack.model.adapters.manga_adapter.MangaSearchAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String selectedImageUrl = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_manga, container, false);

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
                requireContext(),
                R.array.manga_status_array,
                R.layout.item_spinner
        );
        statusAdapter.setDropDownViewResource(R.layout.item_spinner);
        statusSpinner.setAdapter(statusAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.manga_type_array,
                R.layout.item_spinner
        );
        typeAdapter.setDropDownViewResource(R.layout.item_spinner);
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
                    api.searchAniList(query, "MANGA").enqueue(new Callback<List<AniListMedia>>() {
                        @Override
                        public void onResponse(Call<List<AniListMedia>> call, Response<List<AniListMedia>> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                searchAdapter.setMangaList(response.body());
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

    private void onMangaSelected(AniListMedia selected) {
        String status = statusSpinner.getSelectedItem().toString();
        String type = typeSpinner.getSelectedItem().toString();

        int score = 0;
        int progress = 0;
        try {
            score = Integer.parseInt(scoreEditText.getText().toString());
            progress = Integer.parseInt(progressEditText.getText().toString());
        } catch (NumberFormatException ignored) {
        }

        MangaEntity manga = new MangaEntity();
        manga.setUserId(userId);
        manga.setTitle(selected.getTitle());

        if (selected.getImageUrl() == null || selected.getImageUrl().isEmpty()) {
            selectedImageUrl = "android.resource://" + requireContext().getPackageName() + "/" + R.drawable.sample_cover;
        } else {
            selectedImageUrl = selected.getImageUrl();
        }

        manga.setImageUrl(selectedImageUrl);
        manga.setStatus(status);
        manga.setType(type);
        manga.setScore(score);
        manga.setProgress(progress);

        api.insertManga(manga).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    registrarActividad(manga.getTitle());
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Error al guardar manga", Toast.LENGTH_SHORT).show();
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
        actividad.put("action", "añadió un manga");
        actividad.put("mediaTitle", titulo);
        actividad.put("imageUrl", selectedImageUrl);

        api.postActivity(actividad).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (!response.isSuccessful()) {
                    // Puedes logear el error si lo deseas
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "Error al registrar actividad", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
