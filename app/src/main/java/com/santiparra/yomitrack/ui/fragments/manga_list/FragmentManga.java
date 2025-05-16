package com.santiparra.yomitrack.ui.fragments.manga_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.MangaEntity;
import com.santiparra.yomitrack.model.adapters.manga_adapter.MangaAdapter;
import com.santiparra.yomitrack.ui.fragments.addmanga.AddMangaFragment;
import com.santiparra.yomitrack.ui.fragments.editmanga.EditMangaFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentManga extends Fragment {

    private RecyclerView recyclerView;
    private MangaAdapter adapter;
    private ApiService api;
    private int currentViewType = MangaAdapter.VIEW_NORMAL;
    private int userId = 1; // Deberías usar SharedPreferences si tienes login

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mlist, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewManga);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ImageView changeViewButton = view.findViewById(R.id.buttonChangeViewType);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddManga);

        api = ApiClient.getClient().create(ApiService.class);

        changeViewButton.setOnClickListener(v -> {
            currentViewType = (currentViewType + 1) % 3;
            if (adapter != null) adapter.setViewType(currentViewType);
        });

        fabAdd.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new AddMangaFragment())
                .addToBackStack(null)
                .commit());

        loadMangaList();

        return view;
    }

    private void loadMangaList() {
        api.getMangaByUser(userId).enqueue(new Callback<List<MangaEntity>>() {
            @Override
            public void onResponse(Call<List<MangaEntity>> call, Response<List<MangaEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new MangaAdapter(response.body(), currentViewType,
                            FragmentManga.this::showEditDialog,
                            FragmentManga.this::deleteManga);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(getContext(), "Error al cargar la lista", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<MangaEntity>> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showEditDialog(MangaEntity manga) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new EditMangaFragment(manga))
                .addToBackStack(null)
                .commit();
    }

    private void deleteManga(MangaEntity manga) {
        api.deleteManga(manga.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Manga eliminado", Toast.LENGTH_SHORT).show();
                    loadMangaList();
                } else {
                    Toast.makeText(getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
