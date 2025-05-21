package com.santiparra.yomitrack.ui.fragments.editmanga;

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
import com.santiparra.yomitrack.db.entities.MangaEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMangaFragment extends Fragment {

    private EditText editTextTitle, editTextScore, editTextProgress;
    private Spinner spinnerStatus, spinnerType;
    private Button buttonSave, buttonDelete;
    private MangaEntity manga;
    private ApiService api;

    public EditMangaFragment(MangaEntity manga) {
        this.manga = manga;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_manga, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editTextTitle = view.findViewById(R.id.editTextMangaTitle);
        editTextScore = view.findViewById(R.id.editTextScore);
        editTextProgress = view.findViewById(R.id.editTextProgress);
        spinnerStatus = view.findViewById(R.id.spinnerStatus);
        spinnerType = view.findViewById(R.id.spinnerType);
        buttonSave = view.findViewById(R.id.buttonSaveManga);
        buttonDelete = view.findViewById(R.id.buttonDeleteManga);

        api = ApiClient.getClient().create(ApiService.class);

        fillFields();

        String[] statusArray = getResources().getStringArray(R.array.manga_status_array);
        String[] typeArray = getResources().getStringArray(R.array.manga_type_array);

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, statusArray);
        spinnerStatus.setAdapter(statusAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, typeArray);
        spinnerType.setAdapter(typeAdapter);

        for (int i = 0; i < statusArray.length; i++) {
            if (statusArray[i].equalsIgnoreCase(manga.getStatus())) {
                spinnerStatus.setSelection(i);
                break;
            }
        }

        for (int i = 0; i < typeArray.length; i++) {
            if (typeArray[i].equalsIgnoreCase(manga.getType())) {
                spinnerType.setSelection(i);
                break;
            }
        }

        buttonSave.setOnClickListener(v -> saveChanges());

        buttonDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar manga")
                    .setMessage("¿Estás seguro de que quieres eliminar este manga?")
                    .setPositiveButton("Sí", (dialog, which) -> deleteManga())
                    .setNegativeButton("Cancelar", null)
                    .show();
        });
    }

    private void fillFields() {
        editTextTitle.setText(manga.getTitle());
        editTextScore.setText(String.valueOf(manga.getScore()));
        editTextProgress.setText(String.valueOf(manga.getProgress()));
    }

    private void saveChanges() {
        if (manga == null) {
            Toast.makeText(requireContext(), "Error: Manga no cargado", Toast.LENGTH_SHORT).show();
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

        manga.setTitle(title);
        manga.setScore(score);
        manga.setProgress(progress);
        manga.setStatus(status);
        manga.setType(type);

        api.updateManga(manga.getId(), manga).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Manga actualizado", Toast.LENGTH_SHORT).show();
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

    private void deleteManga() {
        api.deleteManga(manga.getId()).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Manga eliminado", Toast.LENGTH_SHORT).show();
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
