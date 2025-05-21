package com.santiparra.yomitrack.ui.fragments.manga_list;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.MangaEntity;
import com.santiparra.yomitrack.model.MangaPageResponse;
import com.santiparra.yomitrack.model.ApiResponse;
import com.santiparra.yomitrack.model.adapters.manga_adapter.MangaAdapter;
import com.santiparra.yomitrack.ui.fragments.addmanga.AddMangaFragment;
import com.santiparra.yomitrack.ui.fragments.editmanga.EditMangaFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentManga extends Fragment {

    private EditText editSearch;
    private RecyclerView recyclerView;
    private MangaAdapter adapter;
    private ApiService api;
    private int userId = 1;

    private ImageButton btnViewCompact, btnViewNormal, btnViewLarge;
    private int currentViewType = MangaAdapter.VIEW_NORMAL;
    private final List<MangaEntity> mangaList = new ArrayList<>();

    private boolean isLoading = false;
    private int currentPage = 1;
    private final int PAGE_SIZE = 20;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerViewManga);
        btnViewCompact = view.findViewById(R.id.btnViewCompact);
        btnViewNormal = view.findViewById(R.id.btnViewNormal);
        btnViewLarge = view.findViewById(R.id.btnViewLarge);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddManga);
        editSearch = view.findViewById(R.id.editSearch);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMangaList(s.toString());
            }
        });

        api = ApiClient.getClient().create(ApiService.class);
        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(getContext(), "Error: sesión no iniciada", Toast.LENGTH_SHORT).show();
            return;
        }

        fabAdd.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new AddMangaFragment())
                .addToBackStack(null)
                .commit());

        btnViewCompact.setOnClickListener(v -> setViewType(MangaAdapter.VIEW_COMPACT));
        btnViewNormal.setOnClickListener(v -> setViewType(MangaAdapter.VIEW_NORMAL));
        btnViewLarge.setOnClickListener(v -> setViewType(MangaAdapter.VIEW_LARGE));

        setViewType(currentViewType);
        loadMoreMangas(currentPage);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && (firstVisibleItemPosition + visibleItemCount) >= totalItemCount - 4) {
                    currentPage++;
                    loadMoreMangas(currentPage);
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(view, (v, insets) -> {
            int bottomInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom;
            recyclerView.setPadding(
                    recyclerView.getPaddingLeft(),
                    recyclerView.getPaddingTop(),
                    recyclerView.getPaddingRight(),
                    bottomInset + 95
            );
            return insets;
        });
    }

    private void filterMangaList(String query) {
        List<MangaEntity> filtered = new ArrayList<>();
        for (MangaEntity manga : mangaList) {
            if (manga.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(manga);
            }
        }
        adapter.updateList(filtered);
    }

    private void setViewType(int viewType) {
        currentViewType = viewType;

        if (viewType == MangaAdapter.VIEW_LARGE) {
            recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        }

        adapter = new MangaAdapter(mangaList, viewType,
                this::showEditDialog,
                this::deleteManga);
        recyclerView.setAdapter(adapter);
    }

    private void showEditDialog(MangaEntity manga) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new EditMangaFragment(manga))
                .addToBackStack(null)
                .commit();
    }

    private void deleteManga(MangaEntity manga) {
        api.deleteManga(manga.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!isAdded()) return;

                if (response.isSuccessful()) {
                    String msg = response.body() != null ? response.body().getMessage() : "Manga eliminado";
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                    currentPage = 1;
                    mangaList.clear();
                    loadMoreMangas(currentPage);
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API_RESPONSE", "Fallo al eliminar manga: " + t.getMessage(), t);
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMoreMangas(int page) {
        isLoading = true;
        api.getMangas(userId, page, PAGE_SIZE).enqueue(new Callback<MangaPageResponse>() {
            @Override
            public void onResponse(Call<MangaPageResponse> call, Response<MangaPageResponse> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    List<MangaEntity> nuevos = response.body().getData();
                    mangaList.addAll(nuevos);
                    adapter.notifyItemRangeInserted(mangaList.size() - nuevos.size(), nuevos.size());
                    isLoading = response.body().isHasNextPage();
                } else {
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<MangaPageResponse> call, Throwable t) {
                Log.e("API_RESPONSE", "Error al cargar mangas: " + t.getMessage(), t);
                isLoading = false;
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error al cargar más mangas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
