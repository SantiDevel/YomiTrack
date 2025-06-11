package com.santiparra.yomitrack.ui.fragments.editmanga;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.santiparra.yomitrack.R;
import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.db.entities.MangaEntity;
import com.santiparra.yomitrack.model.ApiResponse;
import com.santiparra.yomitrack.ui.fragments.profile.FragmentProfile;

import java.util.HashMap;
import java.util.Map;

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_manga, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_spinner,
                getResources().getStringArray(R.array.manga_status_array));
        statusAdapter.setDropDownViewResource(R.layout.item_spinner);
        spinnerStatus.setAdapter(statusAdapter);

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(requireContext(), R.layout.item_spinner,
                getResources().getStringArray(R.array.manga_type_array));
        typeAdapter.setDropDownViewResource(R.layout.item_spinner);
        spinnerType.setAdapter(typeAdapter);

        setSpinnerSelection(spinnerStatus, manga.getStatus());
        setSpinnerSelection(spinnerType, manga.getType());

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

    private void setSpinnerSelection(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                spinner.setSelection(i);
                break;
            }
        }
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

        int score, progress;
        try {
            score = Integer.parseInt(scoreStr);
            progress = Integer.parseInt(progressStr);
        } catch (NumberFormatException e) {
            Toast.makeText(requireContext(), "Score y Progreso deben ser números", Toast.LENGTH_SHORT).show();
            return;
        }

        manga.setTitle(title);
        manga.setScore(score);
        manga.setProgress(progress);
        manga.setStatus(spinnerStatus.getSelectedItem().toString());
        manga.setType(spinnerType.getSelectedItem().toString());

        api.updateManga(manga.getId(), manga).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!isAdded()) return;

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(requireContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    registrarActividad("update de un manga", manga.getTitle(), manga.getImageUrl());
                    requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
                            .edit().putBoolean("refresh_profile", true).apply();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteManga() {
        registrarActividad("eliminó un manga", manga.getTitle(), manga.getImageUrl());

        api.deleteManga(manga.getId()).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (!isAdded()) return;

                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Manga eliminado", Toast.LENGTH_SHORT).show();
                    requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
                            .edit().putBoolean("refresh_profile", true).apply();

                    Bundle result = new Bundle();
                    result.putBoolean("manga_deleted", true);
                    result.putInt("manga_id", manga.getId());
                    getParentFragmentManager().setFragmentResult("manga_delete_request", result);

                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                if (!isAdded()) return;
                Toast.makeText(requireContext(), "Fallo en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registrarActividad(String action, String titulo, String imagen) {
        int userId = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE).getInt("user_id", -1);
        if (userId == -1) return;

        Map<String, Object> actividad = new HashMap<>();
        actividad.put("userId", userId);
        actividad.put("action", action);
        actividad.put("mediaTitle", titulo);
        actividad.put("imageUrl", imagen);

        api.postActivity(actividad).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.d("ACTIVITY", "Actividad registrada: " + action);
                if (getParentFragment() instanceof FragmentProfile) {
                    ((FragmentProfile) getParentFragment()).loadActivity();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.e("ACTIVITY", "Error al registrar actividad: " + t.getMessage(), t);
            }
        });
    }
}
