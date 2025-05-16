// EditAnimeFragment.java optimizado con setupSpinners y safe saveChanges

package com.santiparra.yomitrack.ui.fragments.editanime;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
    private Button buttonSave;
    private AnimeEntity anime;
    private ApiService api;
    private int userId;

    public EditAnimeFragment(AnimeEntity anime) {
        this.anime = anime;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_anime, container, false);

        editTextTitle = view.findViewById(R.id.editTextAnimeTitle);
        editTextScore = view.findViewById(R.id.editTextScore);
        editTextProgress = view.findViewById(R.id.editTextProgress);
        spinnerStatus = view.findViewById(R.id.spinnerStatus);
        spinnerType = view.findViewById(R.id.spinnerType);
        buttonSave = view.findViewById(R.id.buttonSaveAnime);

        api = ApiClient.getClient().create(ApiService.class);
        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("current_user_id", -1);

        if (getArguments() != null && getArguments().containsKey("anime")) {
            anime = (AnimeEntity) getArguments().getSerializable("anime");
            fillFields();
        }

        setupSpinners();

        buttonSave.setOnClickListener(v -> saveChanges());

        return view;
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.anime_status_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerStatus.setAdapter(statusAdapter);

        ArrayAdapter<CharSequence> typeAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.anime_type_array, android.R.layout.simple_spinner_item);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        if (anime != null) {
            String[] statusArray = getResources().getStringArray(R.array.anime_status_array);
            String[] typeArray = getResources().getStringArray(R.array.anime_type_array);

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
        }
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

        String status = spinnerStatus.getSelectedItem() != null ? spinnerStatus.getSelectedItem().toString() : "";
        String type = spinnerType.getSelectedItem() != null ? spinnerType.getSelectedItem().toString() : "";

        if (status.isEmpty() || type.isEmpty()) {
            Toast.makeText(requireContext(), "Debe seleccionar estado y tipo", Toast.LENGTH_SHORT).show();
            return;
        }

        anime.setTitle(title);
        anime.setScore(score);
        anime.setProgress(progress);
        anime.setStatus(status);
        anime.setType(type);

        api.updateAnime(anime.getId(),anime).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(requireContext(), "Anime actualizado", Toast.LENGTH_SHORT).show();
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
}
