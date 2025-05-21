package com.santiparra.yomitrack.ui.fragments.register;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.santiparra.yomitrack.api.ApiClient;
import com.santiparra.yomitrack.api.ApiService;
import com.santiparra.yomitrack.databinding.FragmentRegisterBinding;
import com.santiparra.yomitrack.db.entities.UserEntity;
import com.santiparra.yomitrack.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

    public RegisterFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        binding.buttonRegister.setOnClickListener(v -> registerUser());

        return binding.getRoot();
    }

    private void registerUser() {
        String username = binding.editTextUsernameRegister.getText().toString().trim();
        String password = binding.editTextPasswordRegister.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showToast("Todos los campos son obligatorios");
            return;
        }

        UserEntity user = new UserEntity(username, password);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.registerUser(user).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    showToast("Registro exitoso");
                    NavController navController = Navigation.findNavController(binding.getRoot());
                    navController.popBackStack(); // Volver al LoginFragment
                } else {
                    String errorMsg = (response.body() != null && response.body().getMessage() != null)
                            ? response.body().getMessage()
                            : "Error desconocido al registrar";
                    showToast(errorMsg);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                showToast("Fallo de red: " + t.getMessage());
            }
        });
    }

    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
