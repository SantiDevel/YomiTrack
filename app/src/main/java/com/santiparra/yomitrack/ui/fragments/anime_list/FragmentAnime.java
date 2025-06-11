package com.santiparra.yomitrack.ui.fragments.anime_list;

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
import android.widget.TextView;
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
import com.santiparra.yomitrack.db.entities.AnimeEntity;
import com.santiparra.yomitrack.model.AnimePageResponse;
import com.santiparra.yomitrack.model.ApiResponse;
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
    private int userId;

    private ImageButton btnViewCompact, btnViewNormal, btnViewLarge;
    private int currentViewType = AnimeAdapter.VIEW_NORMAL;
    private final List<AnimeEntity> animeList = new ArrayList<>();

    private boolean isLoading = false;
    private int currentPage = 1;
    private final int PAGE_SIZE = 20;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupRecyclerView();
        setupViewButtons();
        setupSearchListener();
        setupFab(view);
        setupInsets(view);
        setupResultListener();

        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);

        if (userId == -1) {
            Toast.makeText(getContext(), "Error: sesión no iniciada", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = prefs.getString("username", "Usuario");
        TextView textViewUsername = view.findViewById(R.id.textViewUsername);
        textViewUsername.setText(username);

        loadMoreAnimes(currentPage);
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewAnime);
        btnViewCompact = view.findViewById(R.id.btnViewCompact);
        btnViewNormal = view.findViewById(R.id.btnViewNormal);
        btnViewLarge = view.findViewById(R.id.btnViewLarge);
        editSearch = view.findViewById(R.id.editSearch);
        api = ApiClient.getClient().create(ApiService.class);
    }

    private void setupRecyclerView() {
        adapter = new AnimeAdapter(animeList, currentViewType, this::showEditDialog, this::deleteAnime);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

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
                    loadMoreAnimes(currentPage);
                }
            }
        });
    }

    private void setupResultListener() {
        getParentFragmentManager().setFragmentResultListener("anime_add_request", this, (requestKey, bundle) -> {
            if (bundle.getBoolean("anime_added", false)) {
                currentPage = 1;
                animeList.clear();
                adapter.updateList(new ArrayList<>());
                loadMoreAnimes(currentPage);
            }
        });

        getParentFragmentManager().setFragmentResultListener("anime_delete_request", this, (requestKey, bundle) -> {
            int deletedId = bundle.getInt("anime_id", -1);
            if (deletedId != -1) {
                for (int i = 0; i < animeList.size(); i++) {
                    if (animeList.get(i).getId() == deletedId) {
                        animeList.remove(i);
                        adapter.updateList(animeList);
                        break;
                    }
                }
            }
        });
    }

    private void setupViewButtons() {
        btnViewCompact.setOnClickListener(v -> setViewType(AnimeAdapter.VIEW_COMPACT));
        btnViewNormal.setOnClickListener(v -> setViewType(AnimeAdapter.VIEW_NORMAL));
        btnViewLarge.setOnClickListener(v -> setViewType(AnimeAdapter.VIEW_LARGE));
    }

    private void setupSearchListener() {
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterAnimeList(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });
    }

    private void setupFab(View view) {
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddAnime);
        fabAdd.setOnClickListener(v -> requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, new AddAnimeFragment())
                .addToBackStack(null)
                .commit());
    }

    private void setupInsets(View view) {
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

    private void filterAnimeList(String query) {
        List<AnimeEntity> filtered = new ArrayList<>();
        for (AnimeEntity anime : animeList) {
            if (anime.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(anime);
            }
        }
        adapter.updateList(filtered);
    }

    private void setViewType(int viewType) {
        currentViewType = viewType;
        recyclerView.setLayoutManager(viewType == AnimeAdapter.VIEW_LARGE ?
                new GridLayoutManager(requireContext(), 2) :
                new LinearLayoutManager(requireContext()));
        adapter = new AnimeAdapter(animeList, viewType, this::showEditDialog, this::deleteAnime);
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
        api.deleteAnime(anime.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    animeList.remove(anime);
                    adapter.updateList(animeList);
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("API_RESPONSE", "Fallo al eliminar: " + t.getMessage(), t);
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadMoreAnimes(int page) {
        isLoading = true;

        api.getAnimes(userId, page, PAGE_SIZE).enqueue(new Callback<AnimePageResponse>() {
            @Override
            public void onResponse(Call<AnimePageResponse> call, Response<AnimePageResponse> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    List<AnimeEntity> nuevos = response.body().getData();
                    if (page == 1) {
                        animeList.clear();
                        animeList.addAll(nuevos);
                        adapter.updateList(animeList);
                    } else {
                        int start = animeList.size();
                        animeList.addAll(nuevos);
                        adapter.notifyItemRangeInserted(start, nuevos.size());
                    }
                    isLoading = response.body().isHasNextPage();
                } else {
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<AnimePageResponse> call, Throwable t) {
                Log.e("API_RESPONSE", "Error cargando más animes: " + t.getMessage(), t);
                isLoading = false;
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error al cargar más animes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
