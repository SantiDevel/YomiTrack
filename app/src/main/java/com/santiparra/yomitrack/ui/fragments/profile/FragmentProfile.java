package com.santiparra.yomitrack.ui.fragments.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.model.UserStatsResponse;
import com.santiparra.yomitrack.utils.ActivityLog;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentProfile extends Fragment {

    private ImageView avatarImage, coverImage;
    private TextView usernameText;
    private EditText editStatus, editBiography;
    private Button buttonPostStatus, buttonSaveBio;
    private LinearLayout animeStatsContainer, mangaStatsContainer, activityContainer;
    private ApiService api;
    private int userId;
    private String username;
    private View view;

    private SharedPreferences bioPrefs;
    private static final String BIO_PREFS = "bio_prefs";
    private static final String BIO_KEY = "bio_text";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initViews();
        setupUserInfo();
        setupListeners();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStats();
        loadActivity();
    }

    private void initViews() {
        avatarImage = view.findViewById(R.id.avatarImage);
        coverImage = view.findViewById(R.id.coverImage);
        usernameText = view.findViewById(R.id.usernameText);
        editStatus = view.findViewById(R.id.editStatus);
        editBiography = view.findViewById(R.id.editBiography);
        buttonPostStatus = view.findViewById(R.id.buttonPostStatus);
        buttonSaveBio = view.findViewById(R.id.buttonSaveBio);
        animeStatsContainer = view.findViewById(R.id.animeStatsContainer);
        mangaStatsContainer = view.findViewById(R.id.mangaStatsContainer);
        activityContainer = view.findViewById(R.id.activityContainer);
    }

    private void setupUserInfo() {
        SharedPreferences prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE);
        userId = prefs.getInt("user_id", -1);
        username = prefs.getString("username", "Usuario");
        usernameText.setText(username);

        bioPrefs = requireContext().getSharedPreferences(BIO_PREFS, Context.MODE_PRIVATE);
        editBiography.setText(bioPrefs.getString(BIO_KEY, ""));

        api = ApiClient.getClient().create(ApiService.class);

        loadStats();
        loadActivity();
    }

    private void setupListeners() {
        buttonPostStatus.setOnClickListener(v -> postStatus());
        buttonSaveBio.setOnClickListener(v -> saveBiography());
    }

    private void loadStats() {
        api.getUserStats(userId).enqueue(new Callback<UserStatsResponse>() {
            @Override
            public void onResponse(Call<UserStatsResponse> call, Response<UserStatsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    populateStats(animeStatsContainer, response.body().getAnimeStats());
                    populateStats(mangaStatsContainer, response.body().getMangaStats());
                } else {
                    Toast.makeText(getContext(), "Error al obtener estadísticas", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserStatsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión al cargar stats", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateStats(LinearLayout container, Map<String, Integer> stats) {
        container.removeAllViews();

        if (stats == null || stats.isEmpty()) {
            addTextToContainer(container, "No hay estadísticas disponibles");
            return;
        }

        int total = stats.values().stream().mapToInt(Integer::intValue).sum();
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            View statView = inflater.inflate(R.layout.item_stat_bar, container, false);
            TextView label = statView.findViewById(R.id.statLabelFull);
            ProgressBar bar = statView.findViewById(R.id.statProgressBar);

            label.setText(String.format(Locale.getDefault(), "%s • %d", entry.getKey(), entry.getValue()));
            bar.setProgress(total > 0 ? (entry.getValue() * 100 / total) : 0);
            bar.setProgressTintList(ColorStateList.valueOf(getColorForStatus(entry.getKey())));

            container.addView(statView);
        }
    }

    private void addTextToContainer(LinearLayout container, String message) {
        TextView noData = new TextView(getContext());
        noData.setText(message);
        noData.setPadding(16, 8, 16, 8);
        container.addView(noData);
    }

    private int getColorForStatus(String status) {
        switch (status.toLowerCase(Locale.ROOT)) {
            case "watching": return requireContext().getColor(R.color.status_watching);
            case "completed": return requireContext().getColor(R.color.status_completed);
            case "paused": return requireContext().getColor(R.color.status_paused);
            case "dropped": return requireContext().getColor(R.color.status_dropped);
            case "planning": return requireContext().getColor(R.color.status_planning);
            default: return requireContext().getColor(R.color.gray);
        }
    }

    private void loadActivity() {
        api.getActivityLog(userId).enqueue(new Callback<List<ActivityLog>>() {
            @Override
            public void onResponse(Call<List<ActivityLog>> call, Response<List<ActivityLog>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    activityContainer.removeAllViews();
                    LayoutInflater inflater = LayoutInflater.from(getContext());

                    for (ActivityLog log : response.body()) {
                        View card = inflater.inflate(R.layout.item_activity_card, activityContainer, false);
                        ((TextView) card.findViewById(R.id.activityUser)).setText(username);
                        ((TextView) card.findViewById(R.id.activityAction)).setText(log.getAction());
                        ((TextView) card.findViewById(R.id.activityTitle)).setText(log.getMediaTitle());
                        ((TextView) card.findViewById(R.id.activityTime)).setText(log.getTimestamp());

                        if (!TextUtils.isEmpty(log.getImageUrl())) {
                            Glide.with(requireContext()).load(log.getImageUrl()).into((ImageView) card.findViewById(R.id.activityCover));
                        }
                        activityContainer.addView(card);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ActivityLog>> call, Throwable t) {
                Toast.makeText(getContext(), "Error al cargar actividad", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postStatus() {
        String status = editStatus.getText().toString().trim();
        if (TextUtils.isEmpty(status)) {
            Toast.makeText(getContext(), "Escribe algo primero", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> post = new HashMap<>();
        post.put("userId", userId);
        post.put("action", "publicó");
        post.put("mediaTitle", status);

        api.postActivity(post).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    editStatus.setText("");
                    loadActivity();
                    Toast.makeText(getContext(), "Publicado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), "Error al publicar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBiography() {
        bioPrefs.edit().putString(BIO_KEY, editBiography.getText().toString().trim()).apply();
        Toast.makeText(getContext(), "Biografía guardada", Toast.LENGTH_SHORT).show();
    }
}
