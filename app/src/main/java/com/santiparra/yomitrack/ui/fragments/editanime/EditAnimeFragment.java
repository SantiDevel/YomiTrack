package com.santiparra.yomitrack.ui.fragments.editanime;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.AnimeEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditAnimeFragment extends Fragment {

    private EditText editTextTitle, editTextScore, editTextProgress;
    private Spinner spinnerStatus, spinnerType;
    private Button buttonSave, buttonDelete;
    private AnimeEntity anime;
    private ApiService api;

    public EditAnimeFragment(AnimeEntity anime) {
        this.anime = anime;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_anime, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTitle = view.findViewById(R.id.editTextAnimeTitle);
        editTextScore = view.findViewById(R.id.editTextScore);
        editTextProgress = view.findViewById(R.id.editTextProgress);
        spinnerStatus = view.findViewById(R.id.spinnerStatus);
        spinnerType = view.findViewById(R.id.spinnerType);
        buttonSave = view.findViewById(R.id.buttonSaveAnime);
        buttonDelete = view.findViewById(R.id.buttonDeleteAnime);

        api = ApiClient.getClient().create(ApiService.class);

        // Llenar campos
        fillFields();

        // Spinner datos
        String[] statusArray = getResources().getStringArray(R.array.anime_status_array);
        String[] typeArray = getResources().getStringArray(R.array.anime_type_array);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, statusArray);
        spinnerStatus.setAdapter(statusAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, typeArray);
        spinnerType.setAdapter(typeAdapter);

        for (int i = 0; i < statusArray.length; i++) {
            if (statusArray[i].equalsIgnoreCase(anime.getStatus())) {
                spinnerStatus.setSelection(i);
                break;
            }
        }

        for (int i = 0; i < typeArray.length; i++) {
            if (typeArray[i].equalsIgnoreCase(anime.getType())) {
                spinnerType.setSelection(i);
                break;
            }
        }

        buttonSave.setOnClickListener(v -> saveChanges());

        buttonDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar anime")
                    .setMessage("¿Estás seguro de que quieres eliminar este anime?")
                    .setPositiveButton("Sí", (dialog, which) -> deleteAnime())
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    private void fillFields() {
        editTextTitle.setText(anime.getTitle());
        editTextScore.setText(String.valueOf(anime.getScore()));
        editTextProgress.setText(String.valueOf(anime.getProgress()));
    }

    private void saveChanges() {
        if (anime == null) {
            Toast.makeText(requireContext(), "Error: Anime no cargado", Toast.LENGTH_SHORT).show();
            return;
        }

        String title = editTextTitle.getText().toString().trim();
        String scoreStr = editTextScore.getText().toString().trim();
        String progressStr = editTextProgress.getText().toString().trim();

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "El título no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        int score = 0, progress = 0;
        try {
            score = Integer.parseInt(scoreStr);
            progress = Integer.parseInt(progressStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Score y Progreso deben ser números", Toast.LENGTH_SHORT).show();
            return;
        }

        String status = spinnerStatus.getSelectedItem().toString();
        String type = spinnerType.getSelectedItem().toString();

        anime.setTitle(title);
        anime.setScore(score);
        anime.setProgress(progress);
        anime.setStatus(status);
        anime.setType(type);

        api.updateAnime(anime.getId(), anime).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Anime actualizado", Toast.LENGTH_SHORT).show();
                    requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
                            .edit().putBoolean("refresh_profile", true).apply();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(requireContext(), "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAnime() {
        api.deleteAnime(anime.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Anime eliminado", Toast.LENGTH_SHORT).show();
                    requireContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE)
                            .edit().putBoolean("refresh_profile", true).apply();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(requireContext(), "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
