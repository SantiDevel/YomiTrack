package com.santiparra.yomitrack.ui.fragments.anime_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.AnimeEntity;
import com.santiparra.yomitrack.model.adapters.anime_adapter.AnimeAdapter;
import com.santiparra.yomitrack.ui.fragments.addanime.AddAnimeFragment;
import com.santiparra.yomitrack.ui.fragments.editanime.EditAnimeFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAnime extends Fragment {

    private EditText editSearch;
    private RecyclerView recyclerView;
    private AnimeAdapter adapter;
    private ApiService api;
    private int userId = 1;

    private ImageButton btnViewCompact, btnViewNormal, btnViewLarge;
    private int currentViewType = AnimeAdapter.VIEW_NORMAL;
    private List<AnimeEntity> animeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewAnime);
        btnViewCompact = view.findViewById(R.id.btnViewCompact);
        btnViewNormal = view.findViewById(R.id.btnViewNormal);
        btnViewLarge = view.findViewById(R.id.btnViewLarge);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddAnime);

        editSearch = view.findViewById(R.id.editSearch);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAnimeList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        api = ApiClient.getClient().create(ApiService.class);
        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("current_user_id", -1);

        // Botones de vista
        btnViewCompact.setOnClickListener(v -> setViewType(AnimeAdapter.VIEW_COMPACT));
        btnViewNormal.setOnClickListener(v -> setViewType(AnimeAdapter.VIEW_NORMAL));
        btnViewLarge.setOnClickListener(v -> setViewType(AnimeAdapter.VIEW_LARGE));

        fabAdd.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new AddAnimeFragment())
                .addToBackStack(null)
                .commit());

        fetchAnimeList();
    }

    private void filterAnimeList(String query) {
        List<AnimeEntity> filtered = new ArrayList<>();
        for (AnimeEntity anime : animeList) {
            if (anime.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(anime);
            }
        }
        adapter.updateList(filtered);
    }

    private void fetchAnimeList() {
        api.getAnimeByUser(userId).enqueue(new Callback<List<AnimeEntity>>() {
            @Override
            public void onResponse(@NonNull Call<List<AnimeEntity>> call,
                                   @NonNull Response<List<AnimeEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    animeList = response.body();
                    setViewType(currentViewType);
                } else {
                    Toast.makeText(getContext(), "Error al cargar animes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<AnimeEntity>> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setViewType(int viewType) {
        currentViewType = viewType;

        if (viewType == AnimeAdapter.VIEW_LARGE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }

        adapter = new AnimeAdapter(animeList, viewType,
                this::showEditDialog,
                this::deleteAnime);
        recyclerView.setAdapter(adapter);
    }

    private void showEditDialog(AnimeEntity anime) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new EditAnimeFragment(anime))
                .addToBackStack(null)
                .commit();
    }

    private void deleteAnime(AnimeEntity anime) {
        api.deleteAnime(anime.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Anime eliminado", Toast.LENGTH_SHORT).show();
                    fetchAnimeList();
                } else {
                    Toast.makeText(getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}