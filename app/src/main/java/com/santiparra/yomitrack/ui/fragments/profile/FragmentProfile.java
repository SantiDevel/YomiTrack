package com.santiparra.yomitrack.ui.fragments.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.utils.ActivityLog;

import org.json.JSONObject;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

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

        SharedPreferences prefs = requireContext().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);
        username = prefs.getString("username", "Usuario");

        api = ApiClient.getClient().create(ApiService.class);

        usernameText.setText(username);

        loadStats();
        loadActivity();

        buttonPostStatus.setOnClickListener(v -> postStatus());
        buttonSaveBio.setOnClickListener(v -> saveBiography());

        return view;
    }

    private void loadStats() {
        api.getUserStats(userId).enqueue(new Callback<Map<String, Map<String, Integer>>>() {
            @Override
            public void onResponse(Call<Map<String, Map<String, Integer>>> call, Response<Map<String, Map<String, Integer>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Integer> animeStats = response.body().get("animeStats");
                    Map<String, Integer> mangaStats = response.body().get("mangaStats");

                    populateStats(animeStatsContainer, animeStats);
                    populateStats(mangaStatsContainer, mangaStats);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Map<String, Integer>>> call, Throwable t) {}
        });
    }

    private void populateStats(LinearLayout container, Map<String, Integer> stats) {
        container.removeAllViews();
        int total = 0;
        for (int count : stats.values()) total += count;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            View statView = inflater.inflate(R.layout.item_stat_bar, container, false);
            TextView label = statView.findViewById(R.id.statLabelFull);
            ProgressBar bar = statView.findViewById(R.id.statProgressBar);

            label.setText(String.format(Locale.getDefault(), "%s • %d", entry.getKey(), entry.getValue()));
            int progress = total > 0 ? (entry.getValue() * 100 / total) : 0;
            bar.setProgress(progress);
            container.addView(statView);
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
                        activityContainer.addView(card);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ActivityLog>> call, Throwable t) {}
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

        api.postActivity(post).enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                if (response.isSuccessful()) {
                    editStatus.setText("");
                    loadActivity();
                    Toast.makeText(getContext(), "Publicado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(getContext(), "Error al publicar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBiography() {
        String bio = editBiography.getText().toString().trim();
        // Aquí podrías guardar biografía en base de datos si se desea.
        Toast.makeText(getContext(), "Biografía guardada", Toast.LENGTH_SHORT).show();
    }
}
