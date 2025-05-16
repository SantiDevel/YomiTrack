// EditMangaFragment.java conectado a fragment_edit_manga.xml y funcional

package com.santiparra.yomitrack.ui.fragments.editmanga;

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
import com.santiparra.yomitrack.db.entities.MangaEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMangaFragment extends Fragment {

    private EditText editTextTitle, editTextScore, editTextProgress;
    private Spinner spinnerStatus, spinnerType;
    private Button buttonSave;
    private MangaEntity manga;
    private ApiService api;
    private int userId;

    public EditMangaFragment(MangaEntity manga) {
        this.manga = manga;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_manga, container, false);

        editTextTitle = view.findViewById(R.id.editTextMangaTitle);
        editTextScore = view.findViewById(R.id.editTextScore);
        editTextProgress = view.findViewById(R.id.editTextProgress);
        spinnerStatus = view.findViewById(R.id.spinnerStatus);
        spinnerType = view.findViewById(R.id.spinnerType);
        buttonSave = view.findViewById(R.id.buttonSaveManga);

        api = ApiClient.getClient().create(ApiService.class);
        SharedPreferences prefs = requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        userId = prefs.getInt("current_user_id", -1);

        setupSpinners();

        if (getArguments() != null && getArguments().containsKey("manga")) {
            manga = (MangaEntity) getArguments().getSerializable("manga");
            fillFields();
        }

        buttonSave.setOnClickListener(v -> updateManga());

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
    }

    private void fillFields() {
        editTextTitle.setText(manga.getTitle());
        editTextScore.setText(String.valueOf(manga.getScore()));
        editTextProgress.setText(String.valueOf(manga.getProgress()));

        String[] statusArray = getResources().getStringArray(R.array.anime_status_array);
        for (int i = 0; i < statusArray.length; i++) {
            if (statusArray[i].equalsIgnoreCase(manga.getStatus())) {
                spinnerStatus.setSelection(i);
                break;
            }
        }

        String[] typeArray = getResources().getStringArray(R.array.anime_type_array);
        for (int i = 0; i < typeArray.length; i++) {
            if (typeArray[i].equalsIgnoreCase(manga.getType())) {
                spinnerType.setSelection(i);
                break;
            }
        }
    }

    private void updateManga() {
        String title = editTextTitle.getText().toString().trim();
        int score = Integer.parseInt(editTextScore.getText().toString());
        int progress = Integer.parseInt(editTextProgress.getText().toString());
        String status = spinnerStatus.getSelectedItem().toString();
        String type = spinnerType.getSelectedItem().toString();

        manga.setTitle(title);
        manga.setScore(score);
        manga.setProgress(progress);
        manga.setStatus(status);
        manga.setType(type);

        api.updateManga(manga.getId(),manga).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Manga actualizado", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Toast.makeText(getContext(), "Error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
