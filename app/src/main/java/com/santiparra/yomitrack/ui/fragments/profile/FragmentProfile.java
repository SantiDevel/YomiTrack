package com.santiparra.yomitrack.ui.fragments.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.AnimeEntity;
import com.santiparra.yomitrack.db.entities.MangaEntity;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends Fragment {

    private TextView textUsername;
    private LinearLayout animeStatsContainer, mangaStatsContainer;
    private ApiService api;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textUsername = view.findViewById(R.id.usernameText);
        animeStatsContainer = view.findViewById(R.id.animeStatsContainer);
        mangaStatsContainer = view.findViewById(R.id.mangaStatsContainer);
        api = ApiClient.getClient().create(ApiService.class);

        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("current_user_id", -1);

        if (userId == -1) {
            textUsername.setText("Invitado");
            Toast.makeText(getContext(), "Estadísticas no disponibles en modo invitado", Toast.LENGTH_SHORT).show();
            return view;
        }

        textUsername.setText("Usuario #" + userId);

        loadStats();

        return view;
    }

    private void loadStats() {
        animeStatsContainer.removeAllViews();
        mangaStatsContainer.removeAllViews();

        api.getAnimeByUser(userId).enqueue(new Callback<List<AnimeEntity>>() {
            @Override
            public void onResponse(Call<List<AnimeEntity>> call, Response<List<AnimeEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showStats(animeStatsContainer, "Anime", countByStatus(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<AnimeEntity>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar anime", Toast.LENGTH_SHORT).show();
            }
        });

        api.getMangaByUser(userId).enqueue(new Callback<List<MangaEntity>>() {
            @Override
            public void onResponse(Call<List<MangaEntity>> call, Response<List<MangaEntity>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    showStats(mangaStatsContainer, "Manga", countByStatus(response.body()));
                }
            }

            @Override
            public void onFailure(Call<List<MangaEntity>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar manga", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Map<String, Integer> countByStatus(List<?> items) {
        Map<String, Integer> counts = new HashMap<>();
        for (Object item : items) {
            String status = "";
            if (item instanceof AnimeEntity) {
                status = ((AnimeEntity) item).getStatus();
            } else if (item instanceof MangaEntity) {
                status = ((MangaEntity) item).getStatus();
            }
            counts.put(status, counts.getOrDefault(status, 0) + 1);
        }
        return counts;
    }

    private void showStats(LinearLayout container, String category, Map<String, Integer> data) {
        TextView title = new TextView(getContext());
        title.setText(category.toUpperCase());
        title.setTextSize(18);
        title.setPadding(0, 24, 0, 12);
        container.addView(title);

        int total = 0;
        for (int count : data.values()) total += count;

        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            String status = entry.getKey();
            int count = entry.getValue();
            int percent = (int) ((count / (float) total) * 100);

            TextView label = new TextView(getContext());
            label.setText(status + ": " + count + " (" + percent + "%)");
            label.setTextSize(16);
            container.addView(label);

            ProgressBar progress = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
            progress.setMax(100);
            progress.setProgress(percent);
            progress.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            container.addView(progress);
        }
    }
}
